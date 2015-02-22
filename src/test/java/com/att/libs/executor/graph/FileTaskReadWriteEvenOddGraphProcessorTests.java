package com.att.libs.executor.graph;

import org.junit.Test;

import com.att.libs.executor.graph.builders.TaskGraphBuilder;
import com.att.libs.executor.graph.impl.TaskGraph;
import com.att.libs.executor.graph.processors.TaskGraphProcessor;
import com.att.libs.executor.tasks.CombineInputFilesTask;
import com.att.libs.executor.tasks.CreateInputFileTask;
import com.att.libs.executor.tasks.EvenOddFileTask;

public class FileTaskReadWriteEvenOddGraphProcessorTests {

	@Test
	public void test() throws Exception {
		TaskGraph graph = new TaskGraphBuilder()
			.name("file tasks graph")
			.nodes()
				.par()
					.node("Create Input File 1", CreateInputFileTask.class).dataAs("file:file-1.txt,start:1,end:5").end()
					.node("Create Input File 2", CreateInputFileTask.class).dataAs("file:file-2.txt,start:6,end:10").end()
				.seq()
					.node("Combine Input Files", CombineInputFilesTask.class).dataAs("files:file-1.txt_file-2.txt,combinedFile:combined-input-file.txt").end()
				.par()
					.node("Even File", EvenOddFileTask.class).dataAs("type:even,inputFile:combined-input-file.txt,outputFile:even-file.txt").end()
					.node("Odd File", EvenOddFileTask.class).dataAs("type:odd,inputFile:combined-input-file.txt,outputFile:odd-file.txt").end()
			.build();
		
		new TaskGraphProcessor().process(graph);
	}
}