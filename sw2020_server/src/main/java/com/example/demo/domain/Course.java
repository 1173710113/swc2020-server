package com.example.demo.domain;

import java.io.Serializable;

/**
 * 课程类
 * 
 * @author msi-user
 *
 */
public class Course implements Serializable {
	private String id;
	private String name;
	private String teacherId;
	private String teacherName;
	private int maxVol;
	private String destination;
	private String startTime;
	private String endTime;
	private int realVol;

	/**
	 * @param id
	 * @param name
	 * @param teacherId
	 * @param teacherName
	 * @param maxVol
	 * @param destination
	 * @param startTime
	 * @param endTime
	 * @param realVol
	 */
	public Course(String id, String name, String teacherId, String teacherName, int maxVol, String destination,
			String startTime, String endTime, int realVol) {
		super();
		this.id = id;
		this.name = name;
		this.teacherId = teacherId;
		this.teacherName = teacherName;
		this.maxVol = maxVol;
		this.destination = destination;
		this.startTime = startTime;
		this.endTime = endTime;
		this.realVol = realVol;
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
	 * @return the teacherId
	 */
	public String getTeacherId() {
		return teacherId;
	}

	/**
	 * @return the teacherName
	 */
	public String getTeacherName() {
		return teacherName;
	}

	/**
	 * @return the maxVol
	 */
	public int getMaxVol() {
		return maxVol;
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @return the realVol
	 */
	public int getRealVol() {
		return realVol;
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
	 * @param teacherId the teacherId to set
	 */
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	/**
	 * @param teacherName the teacherName to set
	 */
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	/**
	 * @param maxVol the maxVol to set
	 */
	public void setMaxVol(int maxVol) {
		this.maxVol = maxVol;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @param realVol the realVol to set
	 */
	public void setRealVol(int realVol) {
		this.realVol = realVol;
	}

}
