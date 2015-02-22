package com.att.libs.executor.graph.processors;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.libs.executor.exceptions.GraphProcessingException;
import com.att.libs.executor.exceptions.NodeProcessingException;
import com.att.libs.executor.graph.Node;
import com.att.libs.executor.graph.impl.TaskGraph;
import com.att.libs.executor.graph.impl.TaskNode;
import com.att.libs.executor.graph.release.policies.IndexBasedNodeReleasePolicy;
import com.att.libs.executor.tasks.ExecutableTask;
import com.att.libs.executor.tasks.TaskContext;

/**
 * GraphProcessor processes a graph with {@link TaskNode} nodes
 * 
 * @author aq728y
 *
 */
public class TaskGraphProcessor extends AbstractGraphProcessor<TaskGraph, TaskNode> {

	private static final Logger log = LoggerFactory.getLogger(TaskGraphProcessor.class);
	
	private int batch = 0;
	
	/*
	 * (non-Javadoc)
	 * @see com.att.utils.executor.processors.GraphProcessor#process(com.att.utils.executor.graph.Graph)
	 */
	public void doProcess(final TaskGraph graph) throws GraphProcessingException {
		//process the graph
		for(int order=1;;order++){
			//get the next node to process
			TaskNode node = graph.getNextNode();
			//graph has been fully processed
			if(node == null){
				break;
			}
			
			//reset batch counter
			batch = 0;	
			//process all nodes with same index
			while(node != null && !new IndexBasedNodeReleasePolicy<Node>(order).canRelease(node)){
				//indicate node is about to be processed
				beforeNodeProcessing(graph, node);
				
				final TaskNode fNode = node;
				//create an async task to process the node
				executorService.submit(new Callable<TaskNode>(){
					public TaskNode call() throws NodeProcessingException {				
						return processNode(graph, fNode);
					}
				});
				batch++;
				
				//get the next node
				node = graph.getNextNode();
			}
			
			//now wait until all tasks in queue are completed
			for(int i=0; i<batch; i++){
				try {
					//wait for task to complete
					executorService.take().get();
				}
				catch (Exception e) {
					//check if graph allows node failures
					if(!graph.isAllowFailures() || !(e.getCause() instanceof NodeProcessingException)){
						//indicate graph processing has failed
						graphProcessingFailed(graph, e);
						throw new GraphProcessingException("graph processing failed", e);
					}
				}
			}
		}
	}
	
	/**
	 * Processes given node
	 * @param graph
	 * @param node
	 * @return
	 * @throws NodeProcessingException 
	 */
	private TaskNode processNode(TaskGraph graph, TaskNode node) throws NodeProcessingException{
		//start processing the node
		processingNode(graph, node);
		
		//get the classes for the task
		Class<? extends ExecutableTask> task = node.getTask();
		Class<? extends ExecutableTask> pre = node.getPre();
		Class<? extends ExecutableTask> post = node.getPost();
		
		//get node data
		Map<String, Object> data = node.getData();
		if(data == null){
			data = new HashMap<String, Object>();
		}
		
		//set node data as task context
		TaskContext.set(data);
		
		try {
			//check if pre step specified
			if(pre != null){
				pre.newInstance().call();
			}
			
			//invoke the task
			task.newInstance().call();
			
			//check if post step specified
			if(post != null){
				post.newInstance().call();
			}
			
			//indicate node has been processed
			nodeProcessingCompleted(graph, node);			
		} 
		catch (Exception e) {
			log.error("error in processing node " + node.getName(), e);
			nodeProcessingFailed(graph, graph.getNode(node), e);
			throw new NodeProcessingException("error in processing node " + node.getName(), e);
		}
		finally{
			//clear task data
			TaskContext.unset();
		}
		
		//check if node requires repeated processing
		boolean repeat = node.getReleasePolicy() != null 
								&& node.getReleasePolicy().isRepeatable() 
								&& !node.getReleasePolicy().canRelease(node);
		
		//either return or repeat this node
		return repeat ? processNode(graph, node) : node;
	}
}