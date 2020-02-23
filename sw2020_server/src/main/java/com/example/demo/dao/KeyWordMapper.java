package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.KeyWord;

@Mapper
public interface KeyWordMapper {

	/**
	 * 向数据库添加关键词
	 * 
	 * @param keyWord
	 */
	public void addKeyWord(KeyWord keyWord);

	/**
	 * 从数据库中删除关键词
	 * 
	 * @param id 关键词id
	 */
	public void deleteKeyWord(String id);

	/**
	 * 
	 * @param id
	 */
	public void updateKeyWordCountPlus(String id);

	/**
	 * 
	 * @param id
	 */
	public void updateKeyWordCountMinus(String id);

	/**
	 * 从数据库查询关键词
	 * 
	 * @param id 关键词id
	 * @return 关键词
	 */
	public KeyWord queryKeyWord(String id);

	/**
	 * 从数据库查询关键词内容与text相等且课堂与classId相等的关键词
	 * 
	 * @param text    查询的内容
	 * @param classId 查询的课堂
	 * @return 关键词
	 */
	public KeyWord queryKeyWordByTextAndClass(String text, String classId);

	/**
	 * 从数据库中查询该节课堂所有的关键词
	 * 
	 * @param classId 课堂id
	 * @return 所有的关键词的集合，按count从大到小排序
	 */
	public List<KeyWord> queryKeyWordByClass(String classId);
}
