package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AlignSentence {
	private String id;
	private String text;
	private int startTime;
	private int endTime;
	private String classId;
}
