package com.att.libs.executor.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.libs.executor.graph.builders.TaskGraphBuilder;
import com.att.libs.executor.graph.impl.TaskGraph;
import com.att.libs.executor.graph.impl.TaskNode;
import com.att.libs.executor.tasks.ExecutableTask;

/**
 * TaskGraphBuilderTests
 * 
 * @author aq728y
 *
 */
public class TaskGraphBuilderTests {

	@Test
	public void testGraphCreation() throws Exception{
		TaskGraph graph = getGraph();
		
		//make sure graph is created
		assertThat(graph, not(nullValue()));
		//check graph nodes existence
		assertThat(graph.getNodes(), not(nullValue()));
		//check graph nodes size
		assertThat(graph.getNodes().size(), equalTo(14));
				
		//get graph node
		TaskNode node = new ArrayList<TaskNode>(graph.getNodes()).get(0);
		//check graph nodes properties
		assertThat(node.getOrder(), equalTo(1));
		assertThat(node.getTask().getCanonicalName(), endsWith("TestTask"));
		
		System.out.println(graph);
	}
	
	private TaskGraph getGraph() throws Exception{
		return new TaskGraphBuilder().
				name("graph with nodes").
				nodes().
					par().
						node("Task 1", TestTask.class).pre(TestTask.class).end().
						node("Task 1", TestTask.class).pre(TestTask.class).end().
					seq().
						node("Task 2", TestTask.class).pre(TestTask.class).end().
						node("Task 3", TestTask.class).post(TestTask.class).end().
					seq().
						node("Task 5", TestTask.class).pre(TestTask.class).end().
					par().
						node("Task 6", TestTask.class).end().
						node("Task 6", TestTask.class).end().
					seq().
						node("Task 7", TestTask.class).pre(TestTask.class).end().
					par().
						node("Task 8", TestTask.class).pre(TestTask.class).end().
					par().
						node("Task 8", TestTask.class).pre(TestTask.class).end().
					seq().
						node("Task 9", TestTask.class).pre(TestTask.class).end().
						node("Task 10", TestTask.class).pre(TestTask.class).end().
					par().
						node("Task 11", TestTask.class).pre(TestTask.class).end().
						node("Task 11", TestTask.class).pre(TestTask.class).end().
				build();
	}	
	
	public static final class TestTask extends ExecutableTask{
		private static final Logger log = LoggerFactory.getLogger(TestTask.class);
		
		TestTask() { }
		
		String key;
		
		@Override
		public void init(Map<String, Object> data) {
			key = data.get("key").toString();
			log.debug("Task init with key " + key);
		}

		@Override
		public void execute() throws Exception {
			log.debug("Task " + Thread.currentThread().getName() + " execute with key " + key);
			Thread.sleep(1000);
		}
	}
}