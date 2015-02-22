package com.att.libs.executor.graph;

import java.util.List;

import com.att.libs.executor.watchers.GraphWatcher;

/**
 * AbstractGraph provides a common implementation for {@link Graph}
 * classes
 * 
 * @author aq728y
 *
 * @param <N> node type
 */
public abstract class AbstractGraph<N extends Node> extends Graph<N>{

	private boolean allowFailures;
	
	private List<GraphWatcher<? extends Graph<? extends Node>, ? extends Node>> watchers;
	
	/**
	 * Adds a node to nodes list
	 */
	@Override
	public void addNode(N node) {
		nodes.add(node);
	}

	/**
	 * Removes a node from nodes list
	 */
	@Override
	public boolean removeNode(N node) {
		return nodes.remove(node);
	}
	
	public List<GraphWatcher<? extends Graph<? extends Node>, ? extends Node>> getWatchers() {
		return watchers;
	}
	public void setWatchers(List<GraphWatcher<? extends Graph<? extends Node>, ? extends Node>> watchers) {
		this.watchers = watchers;
	}

	public boolean isAllowFailures() {
		return allowFailures;
	}

	public void setAllowFailures(boolean allowFailures) {
		this.allowFailures = allowFailures;
	}
}