package com.att.libs.executor.graph.release.policies;

/**
 * ReleasePolicy interface provides methods to decide
 * releasing a branch from execution
 * 
 * @author aq728y
 *
 */
public interface ReleasePolicy<O> {

	/**
	 * Returns true if the given object can be released from execution
	 * @return release
	 */
	 boolean canRelease(O object);
	 
	 /**
	  * Indicates if this release policy suggests that the object being
	  * executed can be executed repeatedly
	  * If the object is repeated it will be executed atleast once
	  * @return repeatable
	  */
	 boolean isRepeatable();
}
