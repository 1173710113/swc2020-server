package com.example.demo.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 课程类
 * 
 * @author msi-user
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class Course implements Serializable {

	private String id; // 课程id
	private String name; // 课程名
	private String teacherId; // 授课教师id
	private int maxVol; // 课程最大容量
	private String destination; // 授课地点
	private Date startTime; // 课程开始时间
	private Date endTime; // 课程结束时间
	private int realVol; // 课程已选人数

}
