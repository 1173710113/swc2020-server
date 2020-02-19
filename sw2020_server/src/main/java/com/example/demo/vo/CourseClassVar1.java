package com.example.demo.vo;

public class CourseClassVar1 {

	private String id;
	private String name;
	private int year;
	private int month;
	private int day;
	private String course;

	/**
	 * @param id
	 * @param name
	 * @param year
	 * @param month
	 * @param day
	 * @param classId
	 */
	public CourseClassVar1(String id, String name, int year, int month, int day, String course) {
		super();
		this.id = id;
		this.name = name;
		this.year = year;
		this.month = month;
		this.day = day;
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
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @return the classId
	 */
	public String getClassId() {
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
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}

	/**
	 * @param classId the classId to set
	 */
	public void setClassId(String classId) {
		this.course = classId;
	}

}
