/**
 * 
 */
package com.example.demo.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.User;

/**
 * 用于与数据库直接交互的mapper接口， 处理用户信息的增删改查
 * @author msi-user
 *
 */
@Mapper
public interface UserMapper {

	/**
	 * add user into database
	 * @param user
	 */
	public void addUser(User user);

	/**
	 * delete user where user'id == userId
	 * @param userId
	 */
	public void deleteUser(String userId);

	/**
	 * query user where user'id = userId
	 * @param userId
	 * @return
	 */
	public User queryUser(String userId);

	/**
	 * update user's password where user's id = userId
	 * @param userId the target user's id
	 * @param pass the password need to be updated
	 */
	public void updateUserPassword(String userId, String pass);
}
