package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.Schedule;

public interface ScheduleService {

	/**
	 * 
	 * @param courseId
	 * @return
	 */
	public List<Schedule> queryScheduleByCourse(String courseId);
}
