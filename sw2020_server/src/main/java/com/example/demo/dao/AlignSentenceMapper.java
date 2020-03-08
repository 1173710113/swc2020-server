package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.AlignSentence;

@Mapper
public interface AlignSentenceMapper {

	/**
	 * 
	 * @param alignSentence
	 */
	public void addAlignSentence(AlignSentence alignSentence);

	/**
	 * 
	 * @param id
	 */
	public void deleteAlignSentence(String id);

	/**
	 * 
	 * @param classId
	 * @return
	 */
	public List<AlignSentence> queryAlignSentenceByClass(String classId);
	
	/**
	 * 
	 * @param text
	 * @param classId
	 */
	public void addAlignTotalText(String text, String classId);
	
	/**
	 * 
	 * @param classId
	 * @return
	 */
	public String queryAlignTotalText(String classId);
}
