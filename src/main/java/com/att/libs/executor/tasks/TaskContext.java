package com.att.libs.executor.tasks;

import java.io.Serializable;
import java.util.Map;

/**
 * TaskContext provides thread level data for a given task
 * 
 * @author aq728y
 *
 */
public class TaskContext implements Serializable{
	
	private static final long serialVersionUID = -7027950557814715850L;

	private static final ThreadLocal<Map<String,Object>> context = new ThreadLocal<Map<String,Object>>();
	
	public static void set(Map<String, Object> data){
		context.set(data);
	}
	
	public static void unset(){
		context.remove();
	}
	
	public static Map<String, Object> get(){
		return context.get();
	}
}
