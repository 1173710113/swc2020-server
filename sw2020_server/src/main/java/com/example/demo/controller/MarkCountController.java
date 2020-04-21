package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domain.EffectiveMarkRange;
import com.example.demo.domain.KeyWord;
import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;
import com.example.demo.service.MarkCountService;

@Controller
@RequestMapping("/markcount")
public class MarkCountController {

	@Autowired
	private MarkCountService markCountService;
	
	@ResponseBody
	@RequestMapping("/query/keywords")
	public MyResult queryAllKeyWords(String classId) {
		List<KeyWord> keyWords = markCountService.getAllKeyWords(classId);
		return MyResultGenerator.successResult(keyWords);
	}
	
	@ResponseBody
	@RequestMapping("/query/markrange")
	public MyResult queryAllAlignSentence(String classId) {
		List<EffectiveMarkRange> effectiveMarkRanges = markCountService.getMarkedRanges(classId);
		return MyResultGenerator.successResult(effectiveMarkRanges);
	}
}
