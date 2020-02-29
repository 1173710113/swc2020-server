package com.example.demo.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseClass {

	private String id; // 课堂id
	private String name; // 课堂的名字
	private Date time; // 课堂的上课时间
	private String course; // 课堂对应的课程

}
