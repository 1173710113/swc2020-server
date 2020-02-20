/**
 * 
 */
package com.example.demo.service.Imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.CourseMapper;
import com.example.demo.domain.Course;
import com.example.demo.domain.CourseCode;
import com.example.demo.exception.MyException;
import com.example.demo.service.CourseService;
import com.example.demo.utils.CodeUtil;
import com.example.demo.utils.ValidateUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author msi-user
 *
 */
@Slf4j
@Service
public class CourseServiceImp implements CourseService {

	@Autowired
	CourseMapper courseMapper;

	@Override
	public void addCourse(Course course) {
		String code = CodeUtil.createData();
		CourseCode result = courseMapper.queryCode(code);
		//若选课码没有被占用
		if(result == null) {
			courseMapper.addCourse(course);
			CourseCode courseCode = new CourseCode(code, course.getId(), course.getStartTime());
			courseMapper.addCode(courseCode);
		}
	}

	@Override
	public void deleteCourse(String courseId) {
		courseMapper.deleteCourse(courseId);

	}

	@Override
	public List<Course> queryCourseByStudentId(String studentId) {
		List<Course> courseList = new ArrayList<>();
		courseList.addAll(courseMapper.queryCourseByStudentId(studentId));
		return courseList;
	}

	@Override
	public List<Course> queryCourseByTeacherId(String teacherId) {
		List<Course> courseList = courseMapper.queryCourseByTeacherId(teacherId);
		log.info(String.format("Teacher{%s} find %d course", teacherId, courseList.size()));
		return courseList;
	}

	@Override
	public List<String> getCourseStudent(String courseId) {
		
		return courseMapper.getStudentByCourse(courseId);
	}

	@Override
	public void enroll(String code, String studentId) throws MyException {
		CourseCode courseCode = courseMapper.queryCode(code);
		//若选课码错误
		if(courseCode == null) {
			throw new MyException("选课码错误");
		}
		String course = courseCode.getCourseId();
		int real = courseMapper.queryCourseRealVol(course);
		int max = courseMapper.queryCourseMaxVol(course);
		//如果课程已满
		if(real >= max) {
			throw new MyException("课程已满");
		}
		String inCourse = courseMapper.isStudentInCourse(studentId, course);
		//已经选了这门课
		if(inCourse != null) {
			throw new MyException("已加入该课程，请勿重复选课");
		}
		courseMapper.enroll(studentId, course);
		courseMapper.updateCourseCountPlus(course);
	}

	@Override
	public String dropCourse(String studentId, String courseId) {
		String tempCourse = courseMapper.isStudentInCourse(studentId, courseId);
		if(ValidateUtil.isEmpty(tempCourse)) {
			return "fail";
		}
		if(courseMapper.queryCourseRealVol(courseId) < 1) {
			return "fail";
		}
		courseMapper.dropCourse(studentId, courseId);
		courseMapper.updateCourseCountMinus(courseId);
		return "success";
	}

	@Override
	public String isStudentEnrollClass(String studentId, String courseId) throws MyException {
		String courseTemp = courseMapper.isStudentInCourse(studentId, courseId);
		if(courseTemp == null) {
			throw new MyException("未加入课程");
		} else {
			String teacherId = courseMapper.queryCourseTeacher(courseId);
			return teacherId;
		}
	}

	@Override
	public CourseCode queryCode(String courseId) {
		
		return courseMapper.queryCodeByCourse(courseId);
	}

}
