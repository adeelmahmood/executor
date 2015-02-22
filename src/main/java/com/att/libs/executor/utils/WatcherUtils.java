package com.att.libs.executor.utils;

import java.util.ArrayList;
import java.util.List;

import com.att.libs.executor.graph.AbstractGraph;
import com.att.libs.executor.graph.Graph;
import com.att.libs.executor.graph.Node;
import com.att.libs.executor.watchers.GraphWatcher;

/**
 * WatcherUtils provides utility methods for Watcher objects
 * @author aq728y
 *
 * @param <G> Graph
 * @param <N> Node
 */
public class WatcherUtils {

	/**
	 * Returns a list of watchers for given graph
	 * handles the casting of generic graph type of given graph type
	 * TODO : there has to be a better way to avoid casting
	 * @param graph
	 * @return list of watchers
	 */
	@SuppressWarnings("unchecked")
	public static <G extends Graph<N>, N extends Node> List<GraphWatcher<G, N>> getWatchers(G graph){
		List<GraphWatcher<G, N>> watchers = new ArrayList<GraphWatcher<G, N>>();
		if(graph instanceof AbstractGraph){
			List<GraphWatcher<? extends Graph<? extends Node>, ? extends Node>> graphWatchers = ((AbstractGraph<N>) graph).getWatchers();
			//iterate over the watchers list and convert to concrete graph and node type
			for(GraphWatcher<? extends Graph<? extends Node>, ? extends Node> watcher : graphWatchers){
				//cast
				watchers.add((GraphWatcher<G, N>) watcher);
			}
		}
		return watchers;
	}
}
