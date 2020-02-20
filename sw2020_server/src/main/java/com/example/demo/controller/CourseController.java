/**
 * 
 */
package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domain.Course;
import com.example.demo.exception.MyException;
import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;
import com.example.demo.service.CourseService;
import com.example.demo.service.UserService;
import com.example.demo.vo.CourseVO;

import lombok.extern.slf4j.Slf4j;


/**
 * @author msi-user
 *
 */
@Slf4j
@Controller
@RequestMapping("/course")
public class CourseController {
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	UserService userService;

	@RequestMapping("/add")
	public void addCourse(@RequestBody Course course) {
		courseService.addCourse(course);
	}
	
	@RequestMapping("/delete") 
	public void deleteCourse(String courseId){
		courseService.deleteCourse(courseId);
	}

	@RequestMapping("/query/student")
	@ResponseBody
	public List<CourseVO> queryCourseByStudentId(String id) throws MyException {
		log.info("Student:" + id + " query course");
		List<Course> courseList = courseService.queryCourseByStudentId(id);
		List<CourseVO> courseVOList = new ArrayList<>();
		for(Course course : courseList) {
			CourseVO courseVO = new CourseVO();
			courseVO.setCourse(course);
			courseVO.setCode(courseService.queryCode(course.getId()));
			courseVO.setTeacherName(userService.getUserName(course.getTeacherId()));
			courseVOList.add(courseVO);
		}
		return courseVOList;
	}
	
	@RequestMapping("/query/teacher")
	@ResponseBody
	public List<CourseVO> queryCourseByTeacherId(String id) throws MyException {
		log.info("Teacher:" + id + " query course");
		List<Course> courseList = courseService.queryCourseByTeacherId(id);
		List<CourseVO> courseVOList = new ArrayList<>();
		for(Course course : courseList) {
			CourseVO courseVO = new CourseVO();
			courseVO.setCourse(course);
			courseVO.setCode(courseService.queryCode(course.getId()));
			courseVO.setTeacherName(userService.getUserName(course.getTeacherId()));
			courseVOList.add(courseVO);
		}
		return courseVOList;
	}
	
	@RequestMapping("/enroll")
	@ResponseBody
	public MyResult enroll(String code, String studentId) throws MyException {
		courseService.enroll(code, studentId);
		return MyResultGenerator.successResult(null);
	}
	
	@RequestMapping("/drop")
	@ResponseBody
	public String drop(String studentId, String courseId) {
		String data = courseService.dropCourse(studentId, courseId);
		return data;
	}
}
