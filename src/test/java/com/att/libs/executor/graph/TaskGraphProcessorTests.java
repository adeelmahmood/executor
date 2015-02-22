package com.att.libs.executor.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.libs.executor.exceptions.GraphProcessingException;
import com.att.libs.executor.graph.Graph.GraphStatus;
import com.att.libs.executor.graph.Node.NodeStatus;
import com.att.libs.executor.graph.builders.TaskGraphBuilder;
import com.att.libs.executor.graph.impl.TaskGraph;
import com.att.libs.executor.graph.impl.TaskNode;
import com.att.libs.executor.graph.processors.GraphProcessor;
import com.att.libs.executor.graph.processors.TaskGraphProcessor;
import com.att.libs.executor.tasks.ExecutableTask;
import com.att.libs.executor.watchers.ConsoleReporter;

/**
 * TaskGraphProcessorTests
 * 
 * @author aq728y
 *
 */
public class TaskGraphProcessorTests {

	private GraphProcessor<TaskGraph, TaskNode> indexedTaskGraphProcessor;
	
	@Test
	public void testGraphProcessorWithoutErrors() throws Exception{
		indexedTaskGraphProcessor = new TaskGraphProcessor();
		
		//test graph without errors
		TaskGraph graph = getGraphWithoutErrors();
		//make sure graph is created
		assertThat(graph, not(nullValue()));
		
		//process the graph
		indexedTaskGraphProcessor.process(graph);
		
		//check graph status
		assertThat(graph.getStatus(), equalTo(GraphStatus.PROCESSED));
		List<Node> nodes = new ArrayList<Node>(graph.getNodes());
		assertThat(nodes.get(0).getStatus(), equalTo(NodeStatus.PROCESSED));
		assertThat(nodes.get(1).getStatus(), equalTo(NodeStatus.PROCESSED));
		assertThat(nodes.get(2).getStatus(), equalTo(NodeStatus.PROCESSED));
		assertThat(nodes.get(3).getStatus(), equalTo(NodeStatus.PROCESSED));
	}
	
	@Test
	public void testGraphProcessorWithErrors() throws Exception{
		indexedTaskGraphProcessor = new TaskGraphProcessor();
		
		//test graph without errors
		TaskGraph graph = getGraphWithErrors();
		//make sure graph is created
		assertThat(graph, not(nullValue()));
		
		//process the graph
		try{
			indexedTaskGraphProcessor.process(graph);
		}catch(GraphProcessingException e){
			//ignore the error but the graph instance is updated with failed status
		}
		
		//check graph status
		assertThat(graph.getStatus(), equalTo(GraphStatus.FAILED));
	}
	
	@Test
	public void testGraphProcessorWithErrorsButAllowFailures() throws Exception{
		indexedTaskGraphProcessor = new TaskGraphProcessor();
		
		//test graph without errors
		TaskGraph graph = getGraphWithErrors();
		//make sure graph is created
		assertThat(graph, not(nullValue()));
		graph.setAllowFailures(true);
		
		//process the graph
		indexedTaskGraphProcessor.process(graph);
		
		//check graph status
		assertThat(graph.getStatus(), equalTo(GraphStatus.PROCESSED));
		List<Node> nodes = new ArrayList<Node>(graph.getNodes());
		assertThat(nodes.get(0).getStatus(), equalTo(NodeStatus.PROCESSED));
		assertThat(nodes.get(1).getStatus(), equalTo(NodeStatus.FAILED));
		assertThat(nodes.get(2).getStatus(), equalTo(NodeStatus.FAILED));
		assertThat(nodes.get(3).getStatus(), equalTo(NodeStatus.PROCESSED));
	}
	
	private TaskGraph getGraphWithErrors() throws Exception{
		
		Map<String, Object> data1 = new HashMap<String, Object>();
		data1.put("key", "Key1-1");
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("key", "Key1-2");
		Map<String, Object> data3 = new HashMap<String, Object>();
		data3.put("key", "Key2-3");
		Map<String, Object> data4 = new HashMap<String, Object>();
		data4.put("key", "Key3-4");

		ConsoleReporter rep = new ConsoleReporter();
		rep.setShowStats(true);
		
		//build graph
		return new TaskGraphBuilder().
				name("graph with nodes").
				//allowFailure(true).
				watcher(rep).
				nodes().
					par().
						node("Task 1-1", TestTaskWithoutErrors.class).
							data(data1).
							pre(TestTaskWithoutErrors.class).
							post(TestTaskWithoutErrors.class).
							end().
						node("Task 1-2", TestTaskWithErrors.class).
							data(data2).
							pre(TestTaskWithoutErrors.class).
							post(TestTaskWithoutErrors.class).
							end().
					seq().
						node("Task 2-3", TestTaskWithErrors.class).
							data(data3).
							pre(TestTaskWithoutErrors.class).
							post(TestTaskWithoutErrors.class).
							end().
						node("Task 3-4", TestTaskWithoutErrors.class).
							data(data4).
							pre(TestTaskWithoutErrors.class).
							post(TestTaskWithoutErrors.class).
							end().
				build();
	}
	
	private TaskGraph getGraphWithoutErrors() throws Exception{
		
		Map<String, Object> data1 = new HashMap<String, Object>();
		data1.put("key", "Key1-1");
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("key", "Key1-2");
		Map<String, Object> data3 = new HashMap<String, Object>();
		data3.put("key", "Key2-3");
		Map<String, Object> data4 = new HashMap<String, Object>();
		data4.put("key", "Key3-4");
		
		ConsoleReporter rep = new ConsoleReporter();
		rep.setShowStats(true);
		//build graph
		return new TaskGraphBuilder().
				name("graph with nodes").
				watcher(rep).
				nodes().
					par().
						node("Task 1-1", TestTaskWithoutErrors.class).
							data(data1).
							pre(TestTaskWithoutErrors.class).
							post(TestTaskWithoutErrors.class).
							end().
						node("Task 1-2", TestTaskWithoutErrors.class).
							data(data2).
							pre(TestTaskWithoutErrors.class).
							post(TestTaskWithoutErrors.class).
							end().
					seq().
						node("Task 2-3", TestTaskWithoutErrors.class).
							data(data3).
							pre(TestTaskWithoutErrors.class).
							post(TestTaskWithoutErrors.class).
							end().
						node("Task 3-4", TestTaskWithoutErrors.class).
							data(data4).
							pre(TestTaskWithoutErrors.class).
							post(TestTaskWithoutErrors.class).
							end().
				build();
	}
	
	public static class TestTaskWithErrors extends ExecutableTask{
		private static final Logger log = LoggerFactory.getLogger(TestTaskWithErrors.class);
		
		String key;
		
		@Override
		public void init(Map<String, Object> data) {
			key = data.get("key").toString();
			log.debug("Task init with key " + key);
		}

		@Override
		public void execute() throws Exception {
			log.debug("Task " + Thread.currentThread().getName() + " execute with key " + key);
			throw new RuntimeException("Intentional Error");
		}
	}
	
	public static class TestTaskWithoutErrors extends ExecutableTask{
		private static final Logger log = LoggerFactory.getLogger(TestTaskWithoutErrors.class);
		
		String key;
		
		@Override
		public void init(Map<String, Object> data) {
			key = data.get("key").toString();
			log.debug("Task init with key " + key);
		}

		@Override
		public void execute() throws Exception {
			log.debug("Task " + Thread.currentThread().getName() + " execute with key " + key);
			Thread.sleep(500);
		}
	}
}