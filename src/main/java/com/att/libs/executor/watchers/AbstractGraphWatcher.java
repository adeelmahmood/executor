package com.att.libs.executor.watchers;

import com.att.libs.executor.graph.Graph;
import com.att.libs.executor.graph.Node;

/**
 * AbstractGraphWatcher provides no-op methods for 
 * subclasses
 * @author aq728y
 *
 * @param <G> Graph 
 * @param <N> Node
 */
public abstract class AbstractGraphWatcher<G extends Graph<N>, N extends Node> implements GraphWatcher<G, N>{

	/*
	 * (non-Javadoc)
	 * @see com.att.utils.executor.status.watchers.GraphWatcher#beforeGraphProcessing(com.att.utils.executor.graph.Graph)
	 */
	public void beforeGraphProcessing(G graph) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.att.utils.executor.status.watchers.GraphWatcher#processingGraph(com.att.utils.executor.graph.Graph)
	 */
	public void processingGraph(G graph) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.att.utils.executor.status.watchers.GraphWatcher#graphProcessingCompleted(com.att.utils.executor.graph.Graph)
	 */
	public void graphProcessingCompleted(G graph) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.att.utils.executor.status.watchers.GraphWatcher#beforeNodeProcessing(com.att.utils.executor.graph.Graph, com.att.utils.executor.graph.Node)
	 */
	public void beforeNodeProcessing(G graph, N node) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.att.utils.executor.status.watchers.GraphWatcher#processingNode(com.att.utils.executor.graph.Graph, com.att.utils.executor.graph.Node)
	 */
	public void processingNode(G graph, N node) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.att.utils.executor.status.watchers.GraphWatcher#nodeProcessingCompleted(com.att.utils.executor.graph.Graph, com.att.utils.executor.graph.Node)
	 */
	public void nodeProcessingCompleted(G graph, N node) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.att.utils.executor.status.watchers.GraphWatcher#onGraphProcessingFailure(com.att.utils.executor.graph.Graph, com.att.utils.executor.graph.Node, java.lang.Throwable)
	 */
	public void graphProcessingFailed(G graph, Throwable e) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.att.utils.executor.status.watchers.GraphWatcher#onNodeProcessingFailure(com.att.utils.executor.graph.Graph, com.att.utils.executor.graph.Node, java.lang.Throwable)
	 */
	public void nodeProcessingFailed(G graph, N node, Throwable e) {
	}
}