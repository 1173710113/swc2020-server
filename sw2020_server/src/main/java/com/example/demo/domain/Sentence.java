package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Sentence {

	private String id; // 句子id
	private String content; // 句子内容
	private String nodeId; // 与句子有关的节点

}
