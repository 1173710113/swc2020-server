package com.example.demo.exception;

import java.io.Serializable;

public class MyResult implements Serializable{
	
	private MyResult() {}
	public static MyResult newInstance() {
		return new MyResult();
	}
	
	private String msg;
	private boolean flag;
	private Object result;
	private long time;
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}
	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}
	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}
	/**
	 * @param meg the meg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}
	
	
}
