package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.CourseClass;

public interface CourseClassService {
	
	/**
	 * 
	 * @param courseClass
	 * @return
	 */
	public CourseClass addClass(CourseClass courseClass);
	
	/**
	 * 
	 * @param courseId
	 * @return
	 */
	List<CourseClass> queryClassByCourse(String courseId);

}
