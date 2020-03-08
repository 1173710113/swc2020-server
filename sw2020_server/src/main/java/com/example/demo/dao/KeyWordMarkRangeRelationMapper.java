package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KeyWordMarkRangeRelationMapper {

	/**
	 * 添加指定关键词和标记块的关系
	 * @param keyWordId 关键词的id
	 * @param markRangeId 标记块的id
	 */
	public void addRelation(String keyWordId, String markRangeId);
	
	/**
	 * 删除指定关键词和标记块的关系
	 * @param keyWordId 关键词的id
	 * @param markRangeId 标记块的id
	 */
	public void deleteRelation(String keyWordId, String markRangeId);
	
	/**
	 * 
	 * @param keywordId
	 * @return
	 */
	public List<String> queryRelationByKeyword(String keywordId);
}
