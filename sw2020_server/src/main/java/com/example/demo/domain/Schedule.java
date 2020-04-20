package com.example.demo.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Schedule {
	
	private String id;
	private Date date;
	private String content;
	private String classId;
}
