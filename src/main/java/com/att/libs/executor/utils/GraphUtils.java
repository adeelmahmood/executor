package com.att.libs.executor.utils;

import java.util.ArrayList;
import java.util.List;

import com.att.libs.executor.graph.Graph;
import com.att.libs.executor.graph.Node;
import com.att.libs.executor.graph.Node.NodeStatus;

/**
 * GraphUtils
 * 
 * @author aq728y
 *
 */
public class GraphUtils {

	/**
	 * Returns the number of nodes matching given status in the graph
	 * @param graph
	 * @param node
	 * @return number of nodes
	 */
	public static <G extends Graph<N>, N extends Node> int countNodesByStatus(G graph, NodeStatus status){
		int count = 0;
		if(graph.getNodes() != null && graph.getNodes() instanceof ArrayList){
			//convert collection to array list
			List<Node> nodes = new ArrayList<Node>(graph.getNodes());
			for(Node node : nodes){
				if(node.getStatus() == status){
					count++;
				}
			}
		}
		return count;
	}
}
