package com.example.demo.utils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.RecordMark;
import com.example.demo.vo.TimeOfPPT;

@Component
public class MarkwithPPT {
	public List<TimeOfPPT> marksPPT(List<RecordMark> marks, File timeofppt) throws IOException, ParseException {
		List<TimeOfPPT> timeofPPTs = new ArrayList<>();
		String content = FileUtils.readFileToString(timeofppt, "UTF-8");
		JSONArray jsonArray = JSON.parseArray(content);
		int jsonArraySize = jsonArray.size();
		for(int i = 0; i < jsonArraySize; i++) {
			JSONObject object = jsonArray.getJSONObject(i);
			int order = object.getIntValue("order");
			int page = object.getIntValue("page");
			Date date = object.getDate("time");
			long second = date.getTime();
			timeofPPTs.add(new TimeOfPPT(order, page, second));
		}

		for (RecordMark recordMark : marks) {
			long recordMarkTime = recordMark.getTime();
			int add = 0;
			for (TimeOfPPT timeofPPT2 : timeofPPTs) {
				if (!(recordMarkTime > timeofPPT2.getTime())) {
					if (recordMark.getMark().equals("care")) {
						timeofPPT2.addCareMarkCount();
					} else {
						timeofPPT2.addDoubtMarkCount();
					}
					add = 1;
					break;
				}
			}
			if (add == 0) {
				if (recordMark.getMark().equals("care")) {
					timeofPPTs.get(timeofPPTs.size() - 1).addCareMarkCount();
				} else {
					timeofPPTs.get(timeofPPTs.size() - 1).addDoubtMarkCount();
				}

			}
		}

		return timeofPPTs;
	}
}
