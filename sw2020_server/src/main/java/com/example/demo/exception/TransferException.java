package com.example.demo.exception;

import java.io.IOException;

// 用于描述讯飞转写时的异常信息
public class TransferException extends IOException {
	private static final long serialVersionUID = 1L;

	private String taskId; // 语音转写任务序号
	private int ecode; // 错误代号
	private String failed; // 错误描述信息

	public TransferException(String taskId, int ecode, String failed) {
		this.taskId = taskId;
		this.ecode = ecode;
		this.failed = failed;
	}

	/**
	 * @return 语音转写任务序号
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @return 错误代号
	 */
	public int getEcode() {
		return ecode;
	}

	/**
	 * @return 错误描述信息
	 */
	public String getFailed() {
		return failed;
	}

	@Override
	public String toString() {
		return "task was fail. task_id:" + taskId + "\n" + "ecode=" + ecode + "\n" + "failed=" + failed;
	}
}
