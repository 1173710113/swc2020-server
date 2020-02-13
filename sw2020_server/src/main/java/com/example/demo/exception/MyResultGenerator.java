package com.example.demo.exception;

public final class MyResultGenerator {
	public static MyResult result(boolean flag, String msg, Object result, Throwable throwable) {
		MyResult myResult = MyResult.newInstance();
		myResult.setFlag(flag);
		myResult.setMsg(msg == "" ? "执行成功":msg);
		myResult.setResult(result);
		myResult.setTime(System.currentTimeMillis());
		return myResult;
	}
	
	public static MyResult successResult(Object result) {
		return result(true, "", result, null);
	}
	
	public static MyResult errorResult(String msg, Throwable throwable) {
		return result(false, msg, "", throwable);
	}
}
