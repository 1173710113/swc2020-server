/**
 * 
 */
package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

	/**
	 * 获得学生所上的所有课
	 * 请求地址:http//服务器地址:8082/sw2020/course/query/student
	 * 请求body中附加的字段id
	 * @param id 用户id
	 * @return 返回的消息体
	 * @throws MyException
	 */
	@RequestMapping("/query/student")
	@ResponseBody
	public MyResult queryCourseByStudentId(String id) throws MyException {
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
		return MyResultGenerator.successResult(courseVOList);
	}
	
	/**
	 * 获得教师所上的所有课
	 * 请求地址:http//服务器地址:8082/sw2020/course/query/teacher
	 * 请求body中附加的字段id
	 * @param id 用户id
	 * @return 返回的消息体
	 * @throws MyException
	 */
	@RequestMapping("/query/teacher")
	@ResponseBody
	public MyResult queryCourseByTeacherId(String id) throws MyException {
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
		return MyResultGenerator.successResult(courseVOList);
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
