package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;
import com.example.demo.service.ScheduleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/schedule")
public class ScheduleController {

	@Autowired
	private ScheduleService service;
	
	@ResponseBody
	@RequestMapping("/query/course")
	public MyResult queryScheduleByCourse(String courseId) {
		return MyResultGenerator.successResult(service.queryScheduleByCourse(courseId));
	}
}
