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
import com.example.demo.exception.MyException;
import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;
import com.example.demo.service.CourseService;
import com.example.demo.utils.CodeUtil;
import com.example.demo.utils.ValidateUtil;
import com.example.demo.vo.TeacherCourse;

/**
 * @author msi-user
 *
 */
@Service
public class CourseServiceImp implements CourseService {

	@Autowired
	CourseMapper courseMapper;

	@Override
	public void addCourse(Course course) {
		String code = CodeUtil.createData();
		String result = courseMapper.queryCode(code);
		//若选课码没有被占用
		if(ValidateUtil.isEmpty(result)) {
			courseMapper.addCourse(course);
			courseMapper.addCode(course.getId(), code, course.getStartTime());
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
	public List<TeacherCourse> queryCourseByTeacherId(String teacherId) {
		List<Course> courseList = new ArrayList<>();
		List<TeacherCourse> teacherCourseList = new ArrayList<>();
		courseList.addAll(courseMapper.queryCourseByTeacherId(teacherId));
		for(int i = 0; i< courseList.size(); i++) {
			TeacherCourse course = new TeacherCourse(courseList.get(i));
			String code = courseMapper.queryCodeByCourse(courseList.get(i).getId());
			if(code == null) {
				code = "";
			}
			course.setCode(code);
			teacherCourseList.add(course);
		}
		return teacherCourseList;
	}

	@Override
	public List<String> getCourseStudent(String courseId) {
		
		return courseMapper.getStudentByCourse(courseId);
	}

	@Override
	public void enroll(String code, String studentId) throws MyException {
		String course = courseMapper.queryCode(code);
		//若选课码错误
		if(ValidateUtil.isEmpty(course)) {
			throw new MyException("选课码错误");
		}
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

}
