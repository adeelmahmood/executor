package com.att.libs.executor.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.att.libs.executor.graph.builders.TaskGraphBuilder;
import com.att.libs.executor.graph.impl.TaskGraph;
import com.att.libs.executor.graph.impl.TaskNode;
import com.att.libs.executor.graph.processors.TaskGraphProcessor;
import com.att.libs.executor.graph.release.policies.IndexBasedNodeReleasePolicy;
import com.att.libs.executor.tasks.ExecutableTask;
import com.att.libs.executor.watchers.ConsoleReporter;

public class ReleaseStrategyTests {

	TaskGraphProcessor indexedTaskGraphProcessor;
	
	@Test
	public void testNodeIndexBasedReleaseStrategy() {
		int order = 1;
		TaskNode n1 = new TaskNode();
		n1.setOrder(0);
		TaskNode n2 = new TaskNode();
		n2.setOrder(1);
		TaskNode n3 = new TaskNode();
		n3.setOrder(2);
		TaskNode n4 = new TaskNode();
		n4.setOrder(3);
		
		assertFalse(new IndexBasedNodeReleasePolicy<Node>(order).canRelease(n1));
		assertFalse(new IndexBasedNodeReleasePolicy<Node>(order).canRelease(n2));
		assertTrue(new IndexBasedNodeReleasePolicy<Node>(order).canRelease(n3));
		assertTrue(new IndexBasedNodeReleasePolicy<Node>(order).canRelease(n4));
	}
	
	@Test
	public void testGraphProcessorOrderingOfNodesExecution() throws Exception{
		indexedTaskGraphProcessor = new TaskGraphProcessor();
		
		//test graph without errors
		TaskGraph graph = getGraph();
		//make sure graph is created
		assertThat(graph, not(nullValue()));
		
		//process the graph
		indexedTaskGraphProcessor.process(graph);
	}
	
	private TaskGraph getGraph() throws Exception{
		return new TaskGraphBuilder().
				name("graph with nodes").
				watcher(new ConsoleReporter()).
				nodes().
					par().
						node("Task 1-1", TestTask.class).end().
						node("Task 2-1", TestTask.class).end().
					seq().
						node("Task 3-2", TestTask.class).end().
						node("Task 4-3", TestTask.class).end().
				build();
	}


	public static class TestTask extends ExecutableTask{
		@Override
		public void init(Map<String, Object> data) {
			data.put("test", "test");
		}

		@Override
		public void execute() throws Exception {
			System.out.println(Thread.currentThread().getName() + " is executing ...");
			Thread.sleep(500);
		}
	}
}