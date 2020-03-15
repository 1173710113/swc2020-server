package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.exception.MyException;
import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;
import com.example.demo.service.UserService;

/**
 * 用于与前端直接交互，处理前端数据返回服务层返回的数据
 * @author msi-user
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	/**
	 * 请求登入:http://服务器地址:8082/sw2020/user/login
	 * 请求的body包含的字段id，password
	 * @param id 用户名
	 * @param password 密码
	 * @return 成功的返回结果
	 * @throws MyException 失败的返回结果
	 */
	@RequestMapping("/login")
	@ResponseBody
	public MyResult login(String id, String password) throws MyException {
		return MyResultGenerator.successResult(userService.login(id, password));
	}
}
