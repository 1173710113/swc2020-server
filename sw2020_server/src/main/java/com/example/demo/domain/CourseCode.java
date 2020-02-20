package com.example.demo.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseCode {
	String code; //选课码
	String courseId; //选课码对应的课程id
	Date dueTime; //选课码失效日期
}
