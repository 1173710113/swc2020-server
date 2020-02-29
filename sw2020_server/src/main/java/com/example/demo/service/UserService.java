/**
 * 
 */
package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.exception.MyException;
import com.example.demo.exception.MyResult;

/**
 * 用于处理用户注册、登入、更改用户信息的服务类
 * @author msi-user
 *
 */
public interface UserService{

	/**
	 * add user into database
	 * @param user
	 * @return null if user register fail, else return user
	 */
	public User register(User user);

	/**
	 * to verify user login
	 * @param user
	 * @return null if user login fail, else return user
	 * @throws MyException 
	 */
	public User login(String id, String password) throws MyException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws MyException
	 */
	public String getUserType(String id) throws MyException;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public String getUserName(String id) throws MyException;

	/**
	 * 
	 * @param userId is the user's need to update password 
	 * @param oldPass is the old password 
	 * @param newPass is the new password
	 * @return true if the user update password success, else return false
	 */
	public boolean updatePasssword(String userId, String newPass);
	
	/**
	 * 
	 * @param name should not be null or ''.
	 * @return
	 */
	public String updateUserName(String name);
	
	/**
	 * 
	 * @param sex should be 男\女.
	 * @return
	 */
	public String updateUserSex(String sex);

}
