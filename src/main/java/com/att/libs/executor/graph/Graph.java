package com.att.libs.executor.graph;

import java.util.Collection;

import com.att.libs.executor.utils.JsonUtils;

/**
 * Graph class provides skeleton for a Graph
 * Subclasses should implement functions to provide
 * concrete implementation of a graph
 * 
 * @author aq728y
 *
 */
public abstract class Graph<N extends Node> {

	private String id;
	protected String name;
	
	protected Collection<N> nodes;

	protected GraphStatus status = GraphStatus.NOT_PROCESSED;
	
	/**
	 * Adds given node to graph
	 * @param node
	 */
	protected abstract void addNode(N node);
	
	/**
	 * Removes requested node from graph
	 * @param node
	 * @return true if removed successfully
	 */
	protected abstract boolean removeNode(N node);
	
	/**
	 * Gets the next node in graph to process or null 
	 * This means graph internally should keep
	 * track of which nodes have been processed
	 * @return next node to process
	 */
	protected abstract N getNextNode();
	
	/**
	 * Returns the node in the graph that matches
	 * the given node
	 * @param node
	 * @return matched node
	 */
	protected abstract N getNode(N node);
	
	@Override
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("[Graph " + name + ", status " + status.getStatus() + ", Id " + id + "]\n");
		for(N node : nodes){
			buffer.append(node.toString());
		}
		return buffer.toString();
	}
	
	/**
	 * Returns a json string representation of graph
	 * @return json
	 */
	public String toJson(){
		return JsonUtils.getGson().toJson(this);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<N> getNodes() {
		return nodes;
	}

	public void setNodes(Collection<N> nodes) {
		this.nodes = nodes;
	}

	public GraphStatus getStatus() {
		return status;
	}

	public void setStatus(GraphStatus status) {
		this.status = status;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static enum GraphStatus {

		NOT_PROCESSED("Not Processed"),
		
		INITIALIZING("Initializing"),
		
		PROCESSING("Processing"),
		
		FAILED("Failed"),
		
		PROCESSED("Processed");
		
		private String status;
		
		GraphStatus(String status){
			this.status = status;
		}
		
		public String getStatus(){
			return status;
		}
	}

}