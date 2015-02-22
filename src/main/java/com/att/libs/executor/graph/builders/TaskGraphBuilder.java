package com.att.libs.executor.graph.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.libs.executor.exceptions.BuilderException;
import com.att.libs.executor.graph.Graph;
import com.att.libs.executor.graph.Node;
import com.att.libs.executor.graph.impl.TaskGraph;
import com.att.libs.executor.graph.impl.TaskNode;
import com.att.libs.executor.graph.release.policies.ExpressionBasedNodeReleasePolicy;
import com.att.libs.executor.graph.release.policies.ReleasePolicy;
import com.att.libs.executor.tasks.ExecutableTask;
import com.att.libs.executor.utils.Constants;
import com.att.libs.executor.utils.IdGenerator;
import com.att.libs.executor.watchers.ConsoleReporter;
import com.att.libs.executor.watchers.GraphWatcher;

/**
 * TaskGraphBuilder provides builder implementation for a {@link TaskGraph}
 * with {@link TaskNode} nodes
 * 
 * @author aq728y
 *
 */
public class TaskGraphBuilder extends BuilderProcessor<TaskGraph, TaskGraphBuilder>{
	
	private static final Logger log = LoggerFactory.getLogger(TaskGraphBuilder.class);
	
	private String name;
	private TaskNode node;
	private List<TaskNode> nodes;
	private int index;
	private int order;
	private boolean inSeq;
	private boolean allowFailures;
	private ReleasePolicy<Node> releasePolicy;
	private List<GraphWatcher<? extends Graph<? extends Node>, ? extends Node>> watchers;
	
	public TaskGraphBuilder(){
		nodes = new ArrayList<TaskNode>();
		watchers = new ArrayList<GraphWatcher<? extends Graph<? extends Node>, ? extends Node>>();
		order = 0;
		index = 0;
	}
	
	@Override
	protected TaskGraph performBuild() throws BuilderException {
		log.debug("building graph");
		try{
			//create task graph
			TaskGraph graph = new TaskGraph();
			graph.setId(IdGenerator.generate());
			graph.setName(name);
			graph.setAllowFailures(allowFailures);
			graph.setNodes(nodes);
			//status reporters, add default if not specified
			if(watchers.isEmpty()){
				watchers.add(new ConsoleReporter());
			}
			graph.setWatchers(watchers);
			return graph;
		}catch(Exception e){
			throw new BuilderException("error in building graph", e);
		}
	}
	
	/**
	 * Sets the graph name
	 * @param name
	 * @return TaskGraphBuilder
	 */
	public TaskGraphBuilder name(String name){
		if(node != null){
			throw new IllegalStateException("name can only be specified on a graph, not a node");
		}
		this.name = name;
		return this;
	}
	
	/**
	 * Indicates if graph allows skipping the failed nodes
	 * @param allowFailure
	 * @return TaskGraphBuilder
	 */
	public TaskGraphBuilder allowFailure(boolean allowFailure){
		if(node != null){
			throw new IllegalStateException("allow failure can only be specified on a graph, not a node");
		}
		this.allowFailures = allowFailure;
		return this;
	}
	
	/**
	 * Adds a watcher
	 * @param watcher
	 * @return TaskGraphBuilder
	 */
	public TaskGraphBuilder watcher(GraphWatcher<? extends Graph<? extends Node>, ? extends Node> watcher){
		if(node != null){
			throw new IllegalStateException("watcher can only be specified on a graph, not a node");
		}
		this.watchers.add(watcher);
		return this;
	}
	
	/**
	 * Starts nodes for this graph
	 * @return TaskGraphBuilder
	 */
	public TaskGraphBuilder nodes(){
		order++;
		return this;
	}
	
	/**
	 * Starts sequential odes
	 * @return TaskGraphBuilder
	 */
	public TaskGraphBuilder seq(){
		inSeq = true;
		order++;
		//reset release policy
		releasePolicy = null;
		return this;
	}
	
	/**
	 * Starts parallel nodes
	 * @return TaskGraphBuilder
	 */
	public TaskGraphBuilder par(){
		inSeq = false;
		//reset release strategy
		releasePolicy = null;
		return this;
	}
	
	/**
	 * Sets up a release policy based on given evaluation 
	 * expression to continue processing the nodes until the release
	 * expession returns true
	 * 
	 * @param releaseExpression release evaluation expression
	 * @return TaskGraphBuilder
	 */
	public TaskGraphBuilder repeatUntil(String releaseExpression){
		//change release policy so that all incoming nodes can use it
		releasePolicy = new ExpressionBasedNodeReleasePolicy<Node>(releaseExpression);
		return this;
	}
	
	/**
	 * Adds a node to graph
	 * @param name name of node
	 * @param task task class 
	 * @return TaskGraphBuilder
	 */
	public TaskGraphBuilder node(String name, Class<? extends ExecutableTask> task){
		//create new node
		node = new TaskNode(name);
		node.setTask(task);
		node.setOrder(order);
		node.setIndex(index++);
		if(releasePolicy != null){
			node.setReleasePolicy(releasePolicy);
		}
		return this;
	}
	
	/**
	 * Adds a pre step to node
	 * @param pre
	 * @return TaskGraphBuilder
	 */
	public TaskGraphBuilder pre(Class<? extends ExecutableTask> pre){
		//make sure task is valid
		if(node == null){
			throw new IllegalStateException("node must be specified to add a pre action");
		}
		node.setPre(pre);
		return this;
	}
	
	/**
	 * Adds a post step to node
	 * @param post
	 * @return TaskGraphBuilder
	 */
	public TaskGraphBuilder post(Class<? extends ExecutableTask> post){
		//make sure task is valid
		if(node == null){
			throw new IllegalStateException("node must be specified to add a post action");
		}
		node.setPost(post);
		return this;
	}
	
	/**
	 * Adds node data
	 * @param data
	 * @return TaskGraphBuilder
	 */
	public TaskGraphBuilder data(Map<String, Object> data){
		//make sure task is valid
		if(node == null){
			throw new IllegalStateException("node must be specified to add data");
		}
		//create a new map to hold given data
		Map<String, Object> d = new HashMap<String, Object>();
		for(Entry<String, Object> de : data.entrySet()){
			d.put(de.getKey(), de.getValue());
		}
		node.setData(d);
		return this;
	}
	
	/**
	 * Adds node data
	 * @param data passed in as key value pairs string
	 * e.g. "k1:v1,k2:v2"
	 * @return TaskGraphBuilder
	 */
	public TaskGraphBuilder dataAs(String data){
		//make sure task is valid
		if(node == null){
			throw new IllegalStateException("node must be specified to add data");
		}
		//create a new map to hold given data
		Map<String, Object> d = new HashMap<String, Object>();
		try{
			//split the values
			for(String val : data.split(Constants.DATA_VALUES_SEP)){
				String[] kv = val.split(Constants.DATA_KEYS_SEP);
				//add to map
				d.put(kv[0], kv[1]);
			}
		}catch(Exception e){
			String msg = String.format("Node data must be specified in the form \"k1%sv1%sk2%sv2\"", 
						Constants.DATA_KEYS_SEP, Constants.DATA_VALUES_SEP, Constants.DATA_KEYS_SEP);
			throw new IllegalArgumentException(msg, e);
		}
		node.setData(d);
		return this;
	}
	
	/**
	 * Completes a node
	 * @return TaskGraphBuilder
	 */
	public TaskGraphBuilder end(){
		//make sure task is valid
		if(node == null){
			throw new IllegalStateException("node must be specified for end action");
		}
		//complete the node
		nodes.add(node);
		//adjust order
		if(inSeq){
			order++;
		}
		//reset node
		node = null;
		return this;
	}
}