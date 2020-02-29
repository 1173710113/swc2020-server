package com.example.demo.vo;

import java.io.Serializable;
import java.util.Date;

import com.example.demo.domain.Course;
import com.example.demo.domain.CourseCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class CourseVO implements Serializable {

	private String id; // 课程id
	private String name; // 课程名
	private String teacherId; // 授课教师id
	private String teacherName; // 授课教师名字
	private int maxVol; // 课程最大容量
	private String destination; // 授课地点
	private Date startTime; // 课程开始时间
	private Date endTime; // 课程结束时间
	private int realVol; // 课程已选人数
	private CourseCode code;//选课码
	
	public void setCourse(Course course) {
		this.id = course.getId();
		this.name = course.getName();
		this.teacherId = course.getTeacherId();
		this.maxVol = course.getMaxVol();
		this.destination = course.getDestination();
		this.startTime = course.getStartTime();
		this.endTime = course.getEndTime();
		this.realVol = course.getRealVol();
	}

}
