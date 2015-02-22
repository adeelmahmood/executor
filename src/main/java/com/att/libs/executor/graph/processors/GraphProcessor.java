package com.att.libs.executor.graph.processors;

import com.att.libs.executor.exceptions.GraphProcessingException;
import com.att.libs.executor.graph.Graph;
import com.att.libs.executor.graph.Node;

/**
 * GraphProcessor interface provides methods for
 * processing a graph
 * 
 * @author aq728y
 *
 * @param <G> graph to process
 */
public interface GraphProcessor<G extends Graph<N>, N extends Node> {

	/**
	 * Processes given graph
	 * @param graph
	 * @throws GraphProcessingException 
	 */
	void process(G graph) throws GraphProcessingException;
}
