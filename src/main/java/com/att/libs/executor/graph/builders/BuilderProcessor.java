package com.att.libs.executor.graph.builders;

import com.att.libs.executor.exceptions.BuilderException;

/**
 * BuilderProcessor provides base classes with a 
 * reasonable implementation to extend from. It also adds life
 * cycle events and init and configure methods
 * 
 * @author aq728y
 *
 * @param <O> Object to build
 * @param <B> Builder object
 */
public abstract class BuilderProcessor<O, B extends AbstractBuilder<O>> extends AbstractBuilder<O> implements Builder<O>{
	
	@SuppressWarnings("unused")
	private BuildState buildState = BuildState.NOT_BUILT;
	
	@Override
	protected final O doBuild() throws BuilderException{
		synchronized(this){
			buildState = BuildState.INITIALIZING;
			init();
			
			buildState = BuildState.CONFIGURING;
			configure();
			
			buildState = BuildState.BUILDING;
			O result = performBuild();
			buildState = BuildState.BUILT;
			return result;
		}
	}
	
	protected void init(){ }
	
	protected void configure(){ }
	
	/**
	 * Subclasses must implement this method to perform the build operation
	 * @return built object
	 * @throws Exception
	 */
	protected abstract O performBuild() throws BuilderException;

	private static enum BuildState {
		NOT_BUILT,
		INITIALIZING,
		CONFIGURING,
		BUILDING,
		BUILT
	}
}