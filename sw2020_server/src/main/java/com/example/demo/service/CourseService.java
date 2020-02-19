/**
 * 
 */
package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.Course;
import com.example.demo.exception.MyException;
import com.example.demo.exception.MyResult;
import com.example.demo.vo.TeacherCourse;

/**
 * @author msi-user
 *
 */
public interface CourseService {

	/**
	 * 
	 * @param course
	 */
	public void addCourse(Course course);

	/**
	 * 
	 * @param courseId
	 */
	public void deleteCourse(String courseId);

	/**
	 * 
	 * @param studentId
	 * @return
	 */
	public List<Course> queryCourseByStudentId(String studentId);

	/**
	 * 
	 * @param teacherId
	 * @return
	 */
	public List<TeacherCourse> queryCourseByTeacherId(String teacherId);

	public List<String> getCourseStudent(String courseId);
	
	/**
	 * 
	 * @param studentId
	 * @param courseId
	 * @return 教师id
	 */
	public String isStudentEnrollClass(String studentId, String courseId) throws MyException;

	public void enroll(String code, String studentId) throws MyException;

	public String dropCourse(String studentId, String courseId);

}
