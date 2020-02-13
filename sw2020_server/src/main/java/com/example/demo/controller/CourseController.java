/**
 * 
 */
package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domain.Course;
import com.example.demo.domain.TeacherCourse;
import com.example.demo.exception.MyException;
import com.example.demo.exception.MyResult;
import com.example.demo.service.CourseService;


/**
 * @author msi-user
 *
 */
@Controller
@RequestMapping("/course")
public class CourseController {
	private final static Logger logger = LoggerFactory.getLogger(CourseController.class);
	

	@Autowired
	CourseService courseService;

	@RequestMapping("/add")
	public void addCourse(@RequestBody Course course) {
		courseService.addCourse(course);
	}
	
	@RequestMapping("/delete/{courseId}") 
	public void deleteCourse(@PathVariable("courseId")String courseId){
		courseService.deleteCourse(courseId);
	}

	@RequestMapping("/query/student")
	@ResponseBody
	public List<Course> queryCourseByStudentId(String id) {
		return courseService.queryCourseByStudentId(id);
	}
	
	@RequestMapping("/query/teacher")
	@ResponseBody
	public List<TeacherCourse> queryCourseByTeacherId(String id) {
		logger.info("Teacher:" + id + " query course");
		return courseService.queryCourseByTeacherId(id);
	}
	
	@RequestMapping("/enroll")
	@ResponseBody
	public MyResult enroll(String code, String studentId) throws MyException {
		return courseService.enroll(code, studentId);
	}
	
	@RequestMapping("/drop/{studentId}/{courseId}")
	@ResponseBody
	public String drop(@PathVariable("studentId")String studentId, @PathVariable("courseId")String courseId) {
		String data = courseService.dropCourse(studentId, courseId);
		return data;
	}
}
