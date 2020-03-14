package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Schedule;

@Mapper
public interface ScheduleMapper {

	/**
	 * 
	 * @param schedule
	 */
	public void addSchedule(Schedule schedule);
	
	/**
	 * 
	 * @param id
	 */
	public void deleteSchedule(String id);
	
	/**
	 * 
	 * @param courseId
	 * @return
	 */
	public List<Schedule> queryScheduleByCourse(String courseId);
}
