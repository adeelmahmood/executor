package com.att.libs.executor.exceptions;

public class BuilderException extends Exception {
	private static final long serialVersionUID = -2472743543781558738L;

	public BuilderException(String msg){ 
		super(msg);
	}

	public BuilderException(String msg, Throwable e) {
		super(msg, e);
	} 
}
