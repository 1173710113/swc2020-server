package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseClassVO {

	private String id; //课堂id
	private String name; //课堂名字
	private int year; //课堂开始的年份
	private int month; //课堂开始的月份
	private int day; //课堂开始的day
	private String course; //课堂所属的课程

}
