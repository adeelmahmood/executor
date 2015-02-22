package com.att.libs.executor.exceptions;

import com.att.libs.executor.graph.Graph;
import com.att.libs.executor.graph.Node;

public class GraphProcessingException extends Exception {

	private static final long serialVersionUID = -3896058298356051851L;

	private Graph<? extends Node> graph;
	
	public GraphProcessingException(String msg) {
		super(msg);
	}
	
	public GraphProcessingException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public GraphProcessingException(String msg, Throwable e, Graph<? extends Node> graph) {
		super(msg, e);
		setGraph(graph);
	}

	public Graph<? extends Node> getGraph() {
		return graph;
	}

	public void setGraph(Graph<? extends Node> graph) {
		this.graph = graph;
	}
}
