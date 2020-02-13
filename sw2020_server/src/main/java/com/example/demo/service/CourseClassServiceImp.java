package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.CourseClassMapper;
import com.example.demo.domain.CourseClass;

@Service
public class CourseClassServiceImp implements CourseClassService{

	@Autowired
	CourseClassMapper mapper;
	@Override
	public List<CourseClass> queryClassByCourse(String courseId) {
		return mapper.queryClassByCourse(courseId);
	}

}
