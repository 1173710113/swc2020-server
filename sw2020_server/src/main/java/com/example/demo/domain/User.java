package com.example.demo.domain;

/**
 * 用户类
 * 
 * @author msi-user
 *
 */
public class User {
	private String id; //用户id
	private String password; //用户密码
	private String type; //用户角色
	private String name; //用户名字
	private String sex; //用户性别

	/**
	 * @param id
	 * @param password
	 * @param type
	 * @param name
	 * @param sex
	 */
	private User(String id, String password, String type, String name, String sex) {
		super();
		this.id = id;
		this.password = password;
		this.type = type;
		this.name = name;
		this.sex = sex;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

}
