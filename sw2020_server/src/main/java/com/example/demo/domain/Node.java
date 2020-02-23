package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 从文本中抽取出来的知识图谱中的点
 * 
 * @author msi-user
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class Node {

	private String id; // 节点的id
	private String content; // 节点的内容
	private int count;//节点被标记的次数
	private String classId; // 节点对应的课堂id

}
