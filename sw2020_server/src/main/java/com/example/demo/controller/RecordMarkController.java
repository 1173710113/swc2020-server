package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.example.demo.domain.RecordMark;
import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;
import com.example.demo.service.RecordMarkService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/mark")
public class RecordMarkController {

	@Autowired
	RecordMarkService recordMarkService;
	
	@ResponseBody
	@RequestMapping("/add")
	public MyResult addRecordMark(@RequestBody RecordMark recordMark) {
		log.info("try to add remark:" + JSON.toJSONString(recordMark));
		return MyResultGenerator.successResult(recordMarkService.addRecordMark(recordMark));
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	public MyResult deleteRecordMark(String recordMarkId) {
		recordMarkService.deleteRecordMark(recordMarkId);
		return MyResultGenerator.successResult(null);
	}
	
	@ResponseBody
	@RequestMapping("/query/class")
	public MyResult queryRecordMarkByClass(String classId) {
		return MyResultGenerator.successResult(recordMarkService.queryRecordMarkByClass(classId));
	}
	
	@ResponseBody
	@RequestMapping("/query/class/user")
	public MyResult queryRecordMarkByClassAndUser(String classId, String userId) {
		return MyResultGenerator.successResult(recordMarkService.queryRecordMarkByClassAndUser(classId, userId));
	}
	
	
}
