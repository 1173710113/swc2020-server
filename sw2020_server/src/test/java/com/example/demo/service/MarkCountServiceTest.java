package com.example.demo.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.domain.EffectiveMarkRange;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Slf4j
public class MarkCountServiceTest {
	
	@Autowired
	private MarkCountService markCountService;
	
	@Test
	public void test() throws IOException {
		String classId = "1";
		String path="./resource/audio/xwlb.wav";

		markCountService.initialize(path, classId);
		List<EffectiveMarkRange> result = markCountService.getMarkedRanges(classId);
		for (int i = 0; i < result.size(); i++) {
			System.out.println("rangetext: " + result.get(i).getText());
			System.out.println("marknum: " + result.get(i).getCount());
			System.out.println();
			log.info("rangetext: " + result.get(i).getText());
			log.info("marknum: " + result.get(i).getCount());
		}

		List<EffectiveMarkRange> screenRes = markCountService.getMarkedRanges(classId, Arrays.asList("习近平"));
		System.out.println("筛选后：");
		for (int i = 0; i < screenRes.size(); i++) {
			System.out.println("rangetext: " + screenRes.get(i).getText());
			System.out.println("marknum: " + screenRes.get(i).getCount());
			System.out.println();
		}

		System.out.println("所有关键词:");
		System.out.println(markCountService.getAllKeyWords(classId));
	}
}
