package com.att.libs.executor.watchers;

import com.att.libs.executor.graph.Graph;
import com.att.libs.executor.graph.Node;

/**
 * GraphWatcher watches for events on graph and nodes
 * 
 * @author aq728y
 *
 * @param <G> Graph
 * @param <N> Node
 */
public interface GraphWatcher<G extends Graph<N>, N extends Node> {

	/**
	 * Fires before graph processing starts
	 * @param graph
	 */
	void beforeGraphProcessing(G graph);

	/**
	 * Fires when graph processing has started
	 * @param graph
	 */
	void processingGraph(G graph);
	
	/**
	 * Fires when graph processing has completed
	 * @param graph
	 */
	void graphProcessingCompleted(G graph);
	
	/**
	 * Fires before node processing starts
	 * @param graph
	 * @param node
	 */
	void beforeNodeProcessing(G graph, N node);
	
	/**
	 * Fires when node processing has started
	 * @param graph
	 * @param node
	 */
	void processingNode(G graph, N node);
	
	/**
	 * Fires when node processing has completed
	 * @param graph
	 * @param node
	 */
	void nodeProcessingCompleted(G graph, N node);
	
	/**
	 * Fires if graph processing has failed
	 * @param graph
	 * @param e
	 */
	void graphProcessingFailed(G graph, Throwable e);
	
	/**
	 * Fires if node processing has failed
	 * @param graph
	 * @param node
	 * @param e
	 */
	void nodeProcessingFailed(G graph, N node, Throwable e);
}