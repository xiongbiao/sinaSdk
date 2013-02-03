package com.kkpush.app.domain;

import java.io.Serializable;
/** 
 * 应用类别
 * @author xiongbiao
 *
 */
public class AppType  implements  Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pid;
	private String typeName;
	private String description;
	private int id;
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
