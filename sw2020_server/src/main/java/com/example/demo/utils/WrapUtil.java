package com.example.demo.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.demo.domain.CourseClass;
import com.example.demo.vo.CourseClassVO;

public class WrapUtil {

	
	public static List<CourseClassVO> courseClassVar1(List<CourseClass> list) {
		List<CourseClassVO> varList = new ArrayList<>();
		for(CourseClass courseClass : list) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(courseClass.getTime());
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DATE);
			varList.add(new CourseClassVO(courseClass.getId(), courseClass.getName(), year, month, day, courseClass.getCourse()));
		}
		return varList;
	}
}
