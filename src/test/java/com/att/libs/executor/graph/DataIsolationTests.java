package com.att.libs.executor.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.att.libs.executor.exceptions.BuilderException;
import com.att.libs.executor.exceptions.GraphProcessingException;
import com.att.libs.executor.graph.builders.TaskGraphBuilder;
import com.att.libs.executor.graph.impl.TaskGraph;
import com.att.libs.executor.graph.impl.TaskNode;
import com.att.libs.executor.graph.processors.TaskGraphProcessor;
import com.att.libs.executor.tasks.ExecutableTask;

public class DataIsolationTests {

	@Test
	public void test() throws BuilderException, GraphProcessingException {
		TaskGraph graph = new TaskGraphBuilder()
								.name("data isolation test graph")
								.nodes()
									.seq()
										.node("Task 1", TestTask.class).dataAs("myid:1").pre(TestTask.class).post(TestTask.class).end()
										.node("Task 2", TestTask.class).dataAs("myid:1").end()
								.build();
		new TaskGraphProcessor().process(graph);
		System.out.println(graph);
		
		List<TaskNode> nodes = new ArrayList<TaskNode>(graph.getNodes());
		//make sure data was separately incremented
		String myid1 = nodes.get(0).getData().get("myid").toString();
		String myid2 = nodes.get(1).getData().get("myid").toString();
		assertThat(myid1, equalTo(String.valueOf(4)));
		assertThat(myid2, equalTo(String.valueOf(2)));
	}

	public static class TestTask extends ExecutableTask{		
		@Override
		public void init(Map<String, Object> data) {
			String myid = data.get("myid").toString();
			System.out.println("id passed to me " + myid);
			//increment myid
			String newmyid = String.valueOf(Integer.parseInt(myid)+1);
			//change the id in given data object
			data.put("myid", newmyid);
		}

		@Override
		public void execute() throws Exception {
		}
	}
}