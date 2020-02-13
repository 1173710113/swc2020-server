package com.example.demo.domain;

import java.util.Date;

public class CourseClass {

	private String id; //课堂id
	private String name; //课堂的名字
	private Date time; //课堂的上课时间
	private String course; //课堂对应的课程

	/**
	 * @param id
	 * @param name
	 * @param time
	 * @param course
	 */
	public CourseClass(String id, String name, Date time, String course) {
		super();
		this.id = id;
		this.name = name;
		this.time = time;
		this.course = course;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @return the course
	 */
	public String getCourse() {
		return course;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @param course the course to set
	 */
	public void setCourse(String course) {
		this.course = course;
	}

}
