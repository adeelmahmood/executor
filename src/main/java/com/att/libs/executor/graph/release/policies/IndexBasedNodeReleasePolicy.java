package com.att.libs.executor.graph.release.policies;

import com.att.libs.executor.graph.Node;
import com.att.libs.executor.graph.impl.TaskNode;

/**
 * NodeIndexBasedReleasePolicy releases given node once the
 * given index is greater than node's index
 * 
 * @author aq728y
 *
 */
public class IndexBasedNodeReleasePolicy<N extends Node> implements ReleasePolicy<N>{

	private final int order;
	
	public IndexBasedNodeReleasePolicy(int order) {
		this.order = order;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.att.libs.executor.graph.strategy.ReleaseStrategy#canRelease(java.lang.Object)
	 */
	public boolean canRelease(N node) {
		//compare node order with current context order
		if(node != null && node instanceof TaskNode){
			return ((TaskNode) node).getOrder() > order;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.att.libs.executor.graph.strategy.ReleaseStrategy#isRepeatable()
	 */
	public boolean isRepeatable() {
		return false;
	}
}
