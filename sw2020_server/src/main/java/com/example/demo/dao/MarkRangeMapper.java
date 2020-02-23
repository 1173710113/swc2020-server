package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.EffectiveMarkRange;

@Mapper
public interface MarkRangeMapper {

	/**
	 * 向数据库添加标记快
	 * @param markRange 要添加的标记块
	 */
	public void addMarkRange(EffectiveMarkRange markRange);
	
	/**
	 * 从数据库中删除指定的标记块
	 * @param id 指定标记块的id
	 */
	public void deleteMarkRange(String id);
	
	/**
	 * 从数据库查询指定的标记块
	 * @param id 指定标记块的id
	 * @return 标记块
	 */
	public EffectiveMarkRange queryMarkRange(String id);
	
	/**
	 * 从数据库中查询指定课堂的所有标记块
	 * @param classId 指定课堂的id
	 * @return 该课堂下的所有标记块集合，按count从大到小
	 */
	public List<EffectiveMarkRange> queryMarkRangeByClass(String classId);
	

	/**
	 * 
	 * @param classId
	 * @param keyWordText
	 * @return
	 */
	public List<EffectiveMarkRange> queryMarkRangeByClassAndText(String classId, String keyWordText);
}
