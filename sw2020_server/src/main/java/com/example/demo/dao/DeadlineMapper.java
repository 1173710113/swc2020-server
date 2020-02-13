package com.example.demo.dao;

import com.example.demo.domain.Deadline;

/**
 * 用于直接与数据库交互的mapper类，处理deadline在数据库的增删改查
 * @author msi-user
 *
 */
public interface DeadlineMapper {

	/**
	 * 
	 * @param deadline
	 */
	public void addDeadline(Deadline deadline);

	/**
	 * 
	 * @param deadlineId
	 */
	public void deleteDeadline(String deadlineId);

	/**
	 * 
	 * @param deadLineId
	 */
	public void queryDeadline(String deadLineId);

	/**
	 * 
	 * @param deadlineId
	 * @param content
	 */
	public void updateDeadlineContent(String deadlineId, String content);

}
