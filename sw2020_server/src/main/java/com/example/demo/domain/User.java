package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户类
 * 
 * @author msi-user
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class User {
	private String id; // 用户id
	private String password; // 用户密码
	private String type; // 用户角色
	private String name; // 用户名字
	private String sex; // 用户性别

}
