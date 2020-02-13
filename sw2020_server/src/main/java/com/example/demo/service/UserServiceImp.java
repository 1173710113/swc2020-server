package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserMapper;
import com.example.demo.domain.User;
import com.example.demo.exception.MyException;
import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;

@Service
public class UserServiceImp implements UserService{
	@Autowired
	private UserMapper mapper;

	@Override
	public User register(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyResult login(String id, String password) throws MyException {
		User userTemp = mapper.queryUser(id);
		//存在账号
		if(userTemp != null) {
			//密码正确
			if(userTemp.getPassword().equals(password)) {
				return MyResultGenerator.successResult(userTemp);
			} else {
				throw new MyException("密码错误");
			}
		} else {
			throw new MyException("账号不存在");
		}
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
