package com.example.demo.service.Imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.CourseClassMapper;
import com.example.demo.domain.CourseClass;
import com.example.demo.service.CourseClassService;

@Service
public class CourseClassServiceImp implements CourseClassService{

	@Autowired
	CourseClassMapper mapper;
	
	@Override
	public CourseClass addClass(CourseClass courseClass) {
		mapper.addClass(courseClass);
		return courseClass;
	}
	
	@Override
	public List<CourseClass> queryClassByCourse(String courseId) {
		return mapper.queryClassByCourse(courseId);
	}

}
