/**
 * 
 */
package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.Course;
import com.example.demo.domain.CourseCode;
import com.example.demo.exception.MyException;
import com.example.demo.exception.MyResult;

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
	public List<Course> queryCourseByTeacherId(String teacherId);

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
	
	/**
	 * 查询课程的选课码
	 * @param courseId 课程id
	 * @return 选课码
	 */
	public CourseCode queryCode(String courseId);

}
