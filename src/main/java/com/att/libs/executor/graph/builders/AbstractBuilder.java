package com.att.libs.executor.graph.builders;

import java.util.concurrent.atomic.AtomicBoolean;

import com.att.libs.executor.exceptions.BuilderException;

/**
 * AbstractBuilder wraps build functionality in atomicboolean 
 * to limit building process to only once
 * 
 * @author aq728y
 *
 * @param <O>
 */
public abstract class AbstractBuilder<O> implements Builder<O> {

	private AtomicBoolean building = new AtomicBoolean();
	
	private O object;
	
	public final O build() throws BuilderException{
		if(building.compareAndSet(false, true)){
			object = doBuild();
			return object;
		}
		throw new IllegalStateException("Object already built, build method must be called only once. Use getObject instead");
	}

	/**
	 * Subclasses should implement this method to perform the build
	 * @return
	 */
	protected abstract O doBuild() throws BuilderException;
	
	public final O getObject(){
		if(!building.get()){
			throw new IllegalStateException("Object not built yet, call build method first");
		}
		return object;
	}
}