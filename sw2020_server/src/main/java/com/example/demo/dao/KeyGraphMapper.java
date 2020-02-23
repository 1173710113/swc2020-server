package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Node;
import com.example.demo.domain.Sentence;

/**
 * 直接与数据库交互，处理知识图谱的存储，更新与查询的接口
 * @author msi-user
 *
 */
@Mapper
public interface KeyGraphMapper {
	
	/**
	 * 向数据库添加节点
	 * @param node 时需要向数据库添加的节点
	 */
	public void addNode(Node node);
	
	
	/**
	 * 从数据库中删除节点
	 * @param nodeId 需要删除的节点id
	 */
	public void deleteNode(String nodeId);
	
	/**
	 * 更新数据库中指定节点的内容
	 * @param nodeId 指定节点的id
	 * @param newContent 更新的内容
	 */
	public void updateNodeContent(String nodeId, String newContent);

	/**
	 * 从数据库中查询对应课堂的节点
	 * @param classId 对应课堂的id
	 * @return 对应课堂的所有节点
	 */
	public List<Node> queryNodeByClass(String classId);

	/**
	 * 向数据库中添加两个节点的关系
	 * @param parentNode 父节点的id
	 * @param childNode 子节点的id
	 */
	public void addRelation(String parentNode, String childNode);
	
	/**
	 * 从数据库中删除两个节点的关系
	 * @param parentNode 父节点的id
	 * @param childNode 子节点的id
	 */
	public void deleteRelation(String parentNode, String childNode);
	
	/**
	 * 从数据库中查询与对应节点有关的节点
	 * @param nodeId 对应节点的id
	 * @return 所有与对应节点有关的节点的id
	 */
	public List<String> queryRelationByNode(String nodeId);


	/**
	 * 向数据库中添加句子
	 * @param sentence 需要添加的句子
	 */
	public void addSentence(Sentence sentence);
	
	/**
	 * 从数据库中删除对应句子
	 * @param sentenceId 对应句子的id
	 */
	public void deleteSentence(String sentenceId);
	
	/**
	 * 更新数据库中对应句子的内容
	 * @param sentenceId 对应句子的id
	 * @param newContent 更新的内容
	 */
	public void updateSentenceContent(String sentenceId, String newContent);
	
	/**
	 * 从数据库中查询与对应节点有关的句子
	 * @param nodeId 对应节点的id
	 * @return 所有与对应节点有关的句子
	 */
	public List<Sentence> querySentenceByNode(String nodeId);

}
