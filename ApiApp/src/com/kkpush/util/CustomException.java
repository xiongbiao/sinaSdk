package com.kkpush.util;

public class CustomException extends Exception {
	
	
	private Object obj = null;
	
	public Object getObject() {
		return obj;
	}
	public CustomException() {
		super();
	}
	public CustomException(String msg) {
		super(msg);
	}
	public CustomException(String msg, Object obj)
	{
		super(msg);
		this.obj = obj;
	}
	public CustomException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public CustomException(Throwable cause) {
		super(cause);
	}

}
