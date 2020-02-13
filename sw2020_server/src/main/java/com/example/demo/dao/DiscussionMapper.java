package com.example.demo.dao;

import com.example.demo.domain.Discussion;

/**
 * 用于与数据库直接交互的mapper类， 处理讨论在数据库的增删改查
 * @author msi-user
 *
 */
public interface DiscussionMapper {

	/**
	 * 
	 * @param discussion
	 */
	public void addDiscussion(Discussion discussion);

	/**
	 * 
	 * @param discussionId
	 */
	public void deleteDiscussion(String discussionId);

	/**
	 * 
	 * @param discussionId
	 * @return
	 */
	public Discussion queryDiscussion(String discussionId);

}
