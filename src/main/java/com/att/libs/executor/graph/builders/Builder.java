package com.att.libs.executor.graph.builders;

import com.att.libs.executor.exceptions.BuilderException;

/**
 * IBuilder interface provides methods to build objects
 * @author aq728y
 *
 * @param <O> Object to build
 */
public interface Builder<O> {

	/**
	 * Builds an object
	 * @return built object
	 * @throws BuilderException
	 */
	O build() throws BuilderException;
}
