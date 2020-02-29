/**
 * 
 */
package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.CourseClass;

/**
 * @author msi-user
 *
 */
@Mapper
public interface CourseClassMapper {

	
	/**
	 * 
	 * @param courseClass
	 */
	public void addClass(CourseClass courseClass);
	
	/**
	 * 
	 * @param courseId
	 * @return
	 */
	public List<CourseClass> queryClassByCourse(String courseId); 
}
