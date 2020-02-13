package com.example.demo.domain;

public class Sentence {

	private String id; //句子id
	private String content; //句子内容
	private String nodeId; //与句子有关的节点

	/**
	 * @param id
	 * @param content
	 * @param nodeId
	 */
	public Sentence(String id, String content, String nodeId) {
		super();
		this.id = id;
		this.content = content;
		this.nodeId = nodeId;
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
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
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
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

}
