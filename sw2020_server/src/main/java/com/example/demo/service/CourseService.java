/**
 * 
 */
package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.Course;
import com.example.demo.domain.TeacherCourse;
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
	public List<TeacherCourse> queryCourseByTeacherId(String teacherId);

	public List<String> getCourseStudent(String courseId);

	public MyResult enroll(String code, String studentId) throws MyException;

	public String dropCourse(String studentId, String courseId);

}
