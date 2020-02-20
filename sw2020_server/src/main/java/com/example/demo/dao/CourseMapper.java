/**
 * 
 */
package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.Course;
import com.example.demo.domain.CourseCode;

/**
 * @author msi-user
 *
 */
@Mapper
public interface CourseMapper {

	/**
	 * 添加课程
	 * 
	 * @param course 要添加的课程
	 */
	public void addCourse(Course course);

	/**
	 * 删除指定课程
	 * 
	 * @param id 指定课程的id
	 */
	public void deleteCourse(String id);

	/**
	 * 查找课程
	 * @param courseId 课程id
	 * @return 课程
	 */
	public Course queryCourse(String courseId);
	
	/**
	 * 
	 * @param courseId
	 * @return
	 */
	public String queryCourseTeacher(String courseId);

	/**
	 * 搜索指定学生所选的所有课程
	 * 
	 * @param studentId 指定学生的id
	 * @return 该学生的所有课程
	 */
	public List<Course> queryCourseByStudentId(String studentId);

	/**
	 * 搜索指定老师所教的所有课程
	 * 
	 * @param teacherId 指定老师的id
	 * @return 老师教的所有课程
	 */
	public List<Course> queryCourseByTeacherId(String teacherId);

	/**
	 * 获得选修指定课程的所有学生
	 * 
	 * @param courseId 指定课程的id
	 * @return 选修这门课的所有学生的id
	 */
	public List<String> getStudentByCourse(String courseId);

	/**
	 * 获得课程的CourseCode
	 * 
	 * @param code 该CourseCode的code
	 * @return 该code的CourseCode
	 */
	public CourseCode queryCode(String code);

	/**
	 * 获得课程的CourseCode
	 * 
	 * @param courseId 该课程的id
	 * @return 该课程的CourseCode
	 */
	public CourseCode queryCodeByCourse(String courseId);

	/**
	 * 学生加入课程
	 * 
	 * @param student 学生id
	 * @param course  课程id
	 */
	public void enroll(String student, String course);

	/**
	 * 将课程的选课人数加1
	 * 
	 * @param courseId 课程id
	 */
	public void updateCourseCountPlus(String courseId);

	/**
	 * 将课程的选课人数减1
	 * 
	 * @param courseId 课程id
	 */
	public void updateCourseCountMinus(String courseId);

	/**
	 * 获得课程的最大课程
	 * @param courseId
	 * @return
	 */
	public int queryCourseMaxVol(String courseId);

	public int queryCourseRealVol(String courseId);

	public String isStudentInCourse(String studentId, String courseId);

	public void dropCourse(String studentId, String courseId);

	public void addCode(String courseId, String code, String dueTime);
}
