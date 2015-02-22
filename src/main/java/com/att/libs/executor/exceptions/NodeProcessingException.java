package com.att.libs.executor.exceptions;

public class NodeProcessingException extends Exception {

	private static final long serialVersionUID = 3294898313552063275L;

	public NodeProcessingException(String msg){
		super(msg);
	}
	
	public NodeProcessingException(String msg, Throwable e) {
		super(msg, e);
	}
}
