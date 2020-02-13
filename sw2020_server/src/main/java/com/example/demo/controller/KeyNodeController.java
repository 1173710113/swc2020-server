package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;
import com.example.demo.service.GraphService;

@Controller
@RequestMapping("/node")
public class KeyNodeController {
	@Autowired
	GraphService nodeService;
	
	@RequestMapping("/query")
	@ResponseBody
	public MyResult queryNodeByClass(String classId) {
		return MyResultGenerator.successResult(nodeService.queryGraphByClass(classId));
	}
}
