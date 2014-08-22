package com.xinhuanet.pay.po;

public class WindowConfig {
	private String id;
	private String sql;
	public WindowConfig(String id, String sql) {
		super();
		this.id = id;
		this.sql = sql;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String toString(){
		return "page id:"+id+" sql:"+sql;
	}
}
