package com.att.libs.executor.tasks;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * ExecutableTask provides methods for subclasses to implement
 * and wrap the task functionality 
 * 
 * @author aq728y
 *
 */
public abstract class ExecutableTask implements Callable<Void>{

	public abstract void init(Map<String, Object> data);
	
	public Void call() throws Exception {
		//get task context data
		Map<String, Object> data = TaskContext.get();
		
		//call initialize function with task data
		init(data);
		
		//execute the task
		execute();
		return null;
	}
	
	public abstract void execute() throws Exception;
}