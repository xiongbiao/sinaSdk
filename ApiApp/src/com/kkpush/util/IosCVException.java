package com.kkpush.util;

public class IosCVException extends Exception{

	private Object obj = null;
	
	public Object getObject() {
		return obj;
	}
	public IosCVException() {
		super();
	}
	public IosCVException(String msg) {
		super(msg);
	}
	public IosCVException(String msg, Object obj)
	{
		super(msg);
		this.obj = obj;
	}
	public IosCVException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public IosCVException(Throwable cause) {
		super(cause);
	}

}
