package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PPTImgMapper {

	/**
	 * 
	 * @param src
	 * @param classId
	 */
	public void addImg(String src, String classId);
	
	/**
	 * 
	 * @param classId
	 * @return
	 */
	public List<String> queryImgByClass(String classId);
}
