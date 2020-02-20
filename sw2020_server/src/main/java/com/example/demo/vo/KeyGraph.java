package com.example.demo.vo;

import java.util.List;

import com.example.demo.domain.Node;
import com.example.demo.domain.Sentence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KeyGraph {
	private String id; // 节点的id
	private String content; // 节点的内容
	private List<String> relation; // 与节点相关的节点的id
	private List<Sentence> sentence; // 与节点相关的句子
	private String classId; // 节点属于的课堂id

	/**
	 * 
	 * @param node
	 */
	public KeyGraph(Node node, List<String> relation, List<Sentence> sentence) {
		this.id = node.getId();
		this.content = node.getContent();
		this.relation = relation;
		this.classId = node.getClassId();
		this.sentence = sentence;
	}

}
