package com.att.libs.executor.graph.processors;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.att.libs.executor.exceptions.GraphProcessingException;
import com.att.libs.executor.graph.Graph;
import com.att.libs.executor.graph.Graph.GraphStatus;
import com.att.libs.executor.graph.Node;
import com.att.libs.executor.graph.Node.NodeStatus;
import com.att.libs.executor.utils.WatcherUtils;
import com.att.libs.executor.watchers.GraphWatcher;

/**
 * AbstractGraphProcessor provides base functionality
 * for processing graphs
 * 
 * Watcher event methods are implemented here for subclasses
 * but can be overridden. If overriden, use super.method at the 
 * end of your implementation
 * 
 * @author aq728y
 *
 */
public abstract class AbstractGraphProcessor<G extends Graph<N>, N extends Node> implements GraphProcessor<G, N>{

	private AtomicBoolean processing = new AtomicBoolean();
	
	protected ExecutorService executor;
	protected ExecutorCompletionService<N> executorService;
	
	/*
	 * (non-Javadoc)
	 * @see com.att.utils.executor.processors.GraphProcessor#process(com.att.utils.executor.graph.Graph)
	 */
	public final void process(G graph) throws GraphProcessingException {
		if(processing.compareAndSet(false, true)){
			//run initializers
			init(graph);
			
			//indicate graph processing has started
			processingGraph(graph);
			
			try{
				//process the graph
				doProcess(graph);

				//finalize processing
				complete(graph);
			}
			catch(GraphProcessingException e){
				failed(graph);
				throw new GraphProcessingException("failed in processing graph", e, graph);
			}
		}
		else{
			throw new IllegalStateException("Graph is already being processed");
		}
	}

	protected final void init(G graph){
		//initialize the thread pool
		executor = Executors.newCachedThreadPool();
		//initialize the thread pool service
		executorService = new ExecutorCompletionService<N>(executor);
		
		//indicate graph processing is about to start
		beforeGraphProcessing(graph);
	}

	/**
	 * Subclasses must implement this method to process the graph
	 * @param graph
	 * @throws GraphProcessingException
	 */
	protected abstract void doProcess(G graph) throws GraphProcessingException;
	
	protected final void complete(G graph){
		//indicate graph processing has completed
		graphProcessingCompleted(graph);
		
		//shut down executor service
		executor.shutdown();
		//indicate processing has completed
		processing.set(false);
	}
	
	protected final void failed(G graph){
		//shut down executor service
		executor.shutdown();
		//indicate processing has completed
		processing.set(false);
	}
	
	/**
	 * Should be fired by subclasses when graph processing
	 * is about to start
	 * @param graph
	 */
	protected void beforeGraphProcessing(G graph){
		//update graph status
		graph.setStatus(GraphStatus.INITIALIZING);
		//send graph processing started event to watchers
		//TODO: shouldnt have to pull these every time
		for(GraphWatcher<G,N> watcher : WatcherUtils.getWatchers(graph)){
			watcher.beforeGraphProcessing(graph);
		}
	}
	
	/**
	 * Should be fired by subclasses when graph processing
	 * has started
	 * @param graph
	 */
	protected void processingGraph(G graph){
		//update graph status
		graph.setStatus(GraphStatus.PROCESSING);
		//send graph processing started event to watchers
		for(GraphWatcher<G,N> watcher : WatcherUtils.getWatchers(graph)){
			watcher.processingGraph(graph);
		}
	}
	
	/**
	 * Should be fired by subclasses when graph processing
	 * has completed 
	 * @param graph
	 */
	protected void graphProcessingCompleted(G graph){
		//update graph status
		graph.setStatus(GraphStatus.PROCESSED);
		//send graph processing started event to watchers
		for(GraphWatcher<G,N> watcher : WatcherUtils.getWatchers(graph)){
			watcher.graphProcessingCompleted(graph);
		}
	}
	
	/**
	 * Should be fired by subclasses when node processing
	 * is about to start
	 * @param graph
	 * @param node
	 */
	protected void beforeNodeProcessing(G graph, N node){
		//update node status
		node.setStatus(NodeStatus.INITIALIZING);
		//send graph processing started event to watchers
		for(GraphWatcher<G,N> watcher : WatcherUtils.getWatchers(graph)){
			watcher.beforeNodeProcessing(graph, node);
		}
	}
	
	/**
	 * Should be fired by subclasses when node processing
	 * has started
	 * @param graph
	 * @param node
	 */
	protected void processingNode(G graph, N node){
		//update node status
		node.setStatus(NodeStatus.PROCESSING);
		//send graph processing started event to watchers
		for(GraphWatcher<G,N> watcher : WatcherUtils.getWatchers(graph)){
			watcher.processingNode(graph, node);
		}
	}
	
	/**
	 * Should be fired by subclasses when node processing
	 * has completed
	 * @param graph
	 * @param node
	 */
	protected void nodeProcessingCompleted(G graph, N node){
		//update node status
		node.setStatus(NodeStatus.PROCESSED);
		//send graph processing started event to watchers
		for(GraphWatcher<G,N> watcher : WatcherUtils.getWatchers(graph)){
			watcher.nodeProcessingCompleted(graph, node);
		}
	}
	
	/**
	 * Should be fired by subclasses when node processing fails
	 * @param graph
	 * @param node
	 * @param e
	 */
	protected void nodeProcessingFailed(G graph, N node, Throwable e){
		//update node status
		node.setStatus(NodeStatus.FAILED);
		//send graph processing started event to watchers
		for(GraphWatcher<G,N> watcher : WatcherUtils.getWatchers(graph)){
			watcher.nodeProcessingFailed(graph, node, e);
		}
	}
	
	/**
	 * Should be fired by subclasses when graph processing fails
	 * @param graph
	 * @param e
	 */
	protected void graphProcessingFailed(G graph, Throwable e){
		//update graph status
		graph.setStatus(GraphStatus.FAILED);
		//send graph processing started event to watchers
		for(GraphWatcher<G,N> watcher : WatcherUtils.getWatchers(graph)){
			watcher.graphProcessingFailed(graph, e);
		}
	}
}