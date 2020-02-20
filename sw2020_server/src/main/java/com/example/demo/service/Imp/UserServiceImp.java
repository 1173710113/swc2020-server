package com.example.demo.service.Imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserMapper;
import com.example.demo.domain.User;
import com.example.demo.exception.MyException;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImp implements UserService{
	@Autowired
	private UserMapper mapper;

	@Override
	public User register(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User login(String id, String password) throws MyException {
		User userTemp = mapper.queryUser(id);
		//存在账号
		if(userTemp != null) {
			//密码正确
			if(userTemp.getPassword().equals(password)) {
				log.info("用户:{" + id + "}登入成功");
				return userTemp;
			} else {
				throw new MyException("密码错误");
			}
		} else {
			throw new MyException("账号不存在");
		}
	}
	
	@Override
	public String getUserType(String id) throws MyException{
		User userTemp = mapper.queryUser(id);
		if(userTemp != null) {
			String type = userTemp.getType();
			if(type == null) {
				throw new MyException("系统异常");
			}
			return type;
		} else {
			throw new MyException("账号不存在");
		}
	}
	
	@Override
	public String getUserName(String id) throws MyException {
		User userTemp = mapper.queryUser(id);
		if(userTemp == null) {
			throw new MyException("账号不存在");
		}
		String name = userTemp.getName();
		if(name == null) {
			throw new MyException("系统异常");
		} 
		return name;
	}

	@Override
	public boolean updatePasssword(String userId, String newPass) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String updateUserName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateUserSex(String sex) {
		// TODO Auto-generated method stub
		return null;
	}

}
