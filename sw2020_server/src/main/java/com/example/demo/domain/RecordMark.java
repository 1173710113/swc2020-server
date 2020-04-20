package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecordMark{
	String id;
	long time; //标记的时间毫秒数
	String mark; //关注care 疑问doubt
	String classId;//标注课堂的id
	String userId;//标记者的id
	
}
