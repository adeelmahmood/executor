package com.att.libs.executor.graph.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.att.libs.executor.graph.AbstractGraph;
import com.att.libs.executor.graph.Node.NodeStatus;

/**
 * TaskGraph provides graph implementation for 
 * {@link TaskNode} nodes with an indexed graph
 * 
 * @author aq728y
 *
 */
public class TaskGraph extends AbstractGraph<TaskNode>{

	public TaskGraph(){
		nodes = new ArrayList<TaskNode>();
	}
	
	/**
	 * Returns next node to process. IndexedGraph relies on
	 * internal orderering of nodes based on an index number.
	 * This methods returns the next unprocessed node based on
	 * index number
	 */
	@Override
	public TaskNode getNextNode() {
		List<TaskNode> graphNodes = new ArrayList<TaskNode>(getNodes());
		//sort by processing order number
		Collections.sort(graphNodes, new Comparator<TaskNode>(){
			public int compare(TaskNode n1, TaskNode n2) {
				return -1*(n2.getOrder() - n1.getOrder());
			}
		});
		//find the first unprocessed node
		for(TaskNode node : graphNodes){
			if(node.getStatus() == NodeStatus.NOT_PROCESSED){
				return node;
			}
		}
		return null;
	}

	@Override
	public TaskNode getNode(TaskNode node) {
		List<TaskNode> graphNodes = new ArrayList<TaskNode>(nodes);
		return graphNodes.get(graphNodes.indexOf(node));
	}
}