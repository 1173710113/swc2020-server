package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domain.CourseClass;
import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;
import com.example.demo.service.CourseClassService;
import com.example.demo.utils.WrapUtil;

@Controller
@RequestMapping("/class")
public class CourseClassController {

	@Autowired
	CourseClassService service;
	
	@RequestMapping("/query")
	@ResponseBody
	public MyResult queryClass(String courseId) {
		List<CourseClass> list = service.queryClassByCourse(courseId);
		return MyResultGenerator.successResult(WrapUtil.courseClassVar1(list));
	}
}
