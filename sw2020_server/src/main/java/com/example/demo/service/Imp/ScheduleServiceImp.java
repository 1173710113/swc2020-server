package com.example.demo.service.Imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ScheduleMapper;
import com.example.demo.domain.Schedule;
import com.example.demo.service.ScheduleService;

@Service
public class ScheduleServiceImp implements ScheduleService{

	@Autowired
	private ScheduleMapper mapper;
	
	@Override
	public List<Schedule> queryScheduleByCourse(String courseId) {
		return mapper.queryScheduleByCourse(courseId);
	}

}
