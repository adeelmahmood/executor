package com.att.libs.executor.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.att.libs.executor.graph.builders.TaskGraphBuilder;
import com.att.libs.executor.graph.impl.TaskGraph;
import com.att.libs.executor.graph.processors.TaskGraphProcessor;
import com.att.libs.executor.tasks.ExecutableTask;
import com.att.libs.executor.watchers.ConsoleReporter;

public class TaskGraphExecutionOrderTests {
	
	static AtomicInteger counter = new AtomicInteger(0);
	TaskGraphProcessor taskGraphProcessor;
	
	@Test
	public void testGraphProcessorOrderingOfNodesExecution() throws Exception{
		taskGraphProcessor = new TaskGraphProcessor();
		
		//test graph without errors
		TaskGraph graph = getGraph();
		//make sure graph is created
		assertThat(graph, not(nullValue()));
		
		//process the graph
		taskGraphProcessor.process(graph);
	}
	
	private TaskGraph getGraph() throws Exception{
		return new TaskGraphBuilder().
				name("graph with nodes").
				watcher(new ConsoleReporter()).
				nodes().
					par().
						node("Task 1-1", TestTask.class).end().
						node("Task 1-2", TestTask.class).end().
					seq().
						node("Task 2-3", TestTask.class).end().
						node("Task 3-4", TestTask.class).end().
					par().
						node("Task 4-5", TestTask.class).end().
					seq().
						node("Task 5-6", TestTask.class).end().
				build();
	}


	public static class TestTask extends ExecutableTask{
		@Override
		public void init(Map<String, Object> data) {
		}

		@Override
		public void execute() throws Exception {
			counter.incrementAndGet();
		}
	}
}