package com.example.demo.domain;

/**
 * 从文本中抽取出来的知识图谱中的点
 * 
 * @author msi-user
 *
 */
public class Node {

	private String id; //节点的id
	private String content; //节点的内容
	private String classId; //节点对应的课堂id

	/**
	 * @param id
	 * @param content
	 * @param classId
	 */
	public Node(String id, String content, String classId) {
		super();
		this.id = id;
		this.content = content;
		this.classId = classId;
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
	 * @param classId the classId to set
	 */
	public void setClassId(String classId) {
		this.classId = classId;
	}

}
