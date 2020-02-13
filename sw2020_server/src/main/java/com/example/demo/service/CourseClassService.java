package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.CourseClass;

public interface CourseClassService {
	
	List<CourseClass> queryClassByCourse(String courseId);

}
