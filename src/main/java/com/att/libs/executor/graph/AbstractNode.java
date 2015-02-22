package com.att.libs.executor.graph;

import com.att.libs.executor.graph.release.policies.ReleasePolicy;

/**
 * AbstractNode 
 * 
 * @author aq728y
 *
 */
public abstract class AbstractNode extends Node{
	
	private ReleasePolicy<Node> releasePolicy;

	public ReleasePolicy<Node> getReleasePolicy() {
		return releasePolicy;
	}

	public void setReleasePolicy(ReleasePolicy<Node> releasePolicy) {
		this.releasePolicy = releasePolicy;
	}
}