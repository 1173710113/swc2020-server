package com.example.demo.vo;

import java.util.List;

import com.example.demo.domain.Node;
import com.example.demo.domain.Sentence;

public class KeyGraph {
	private String id; //节点的id
	private String content; //节点的内容
	private List<String> relation; //与节点相关的节点的id
	private List<Sentence> sentence; //与节点相关的句子
	private String classId; //节点属于的课堂id

	/**
	 * @param id
	 * @param content
	 * @param relation
	 * @param sentence
	 * @param classId
	 */
	public KeyGraph(String id, String content, List<String> relation, List<Sentence> sentence, String classId) {
		super();
		this.id = id;
		this.content = content;
		this.relation = relation;
		this.sentence = sentence;
		this.classId = classId;
	}

	/**
	 * 
	 * @param node
	 */
	public KeyGraph(Node node, List<String> relation,List<Sentence> sentence) {
		this.id = node.getId();
		this.content = node.getContent();
		this.relation = relation;
		this.classId = node.getClassId();
		this.sentence = sentence;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @return the relation
	 */
	public List<String> getRelation() {
		return relation;
	}

	/**
	 * @return the sentence
	 */
	public List<Sentence> getSentence() {
		return sentence;
	}

	/**
	 * @return the classId
	 */
	public String getClassId() {
		return classId;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @param relation the relation to set
	 */
	public void setRelation(List<String> relation) {
		this.relation = relation;
	}

	/**
	 * @param sentence the sentence to set
	 */
	public void setSentence(List<Sentence> sentence) {
		this.sentence = sentence;
	}

	/**
	 * @param classId the classId to set
	 */
	public void setClassId(String classId) {
		this.classId = classId;
	}

}
