package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;
import com.example.demo.service.PPTImgService;

@RequestMapping("/pptimg")
@Controller
public class PPTImgController {

	@Autowired
	private PPTImgService service;
	
	@ResponseBody
	@RequestMapping("/query")
	public MyResult queryPPTImgByClass(String classId){
		return MyResultGenerator.successResult(service.queryPPTImgByClass(classId));
	}
	
}
