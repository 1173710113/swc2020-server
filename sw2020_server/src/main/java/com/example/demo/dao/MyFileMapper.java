package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.MyFile;

/**
 * 用于与数据库直接交互的mapper类，处理文件索引在数据库中的增删改查
 * @author msi-user
 *
 */
@Mapper
public interface MyFileMapper {
	
	/**
	 * 
	 * @param myFile
	 */
	public void addMyFile(MyFile myFile);
	
	/**
	 * 
	 * @param myFileId
	 */
	public void deleteMyFile(String myFileId);
	
	/**
	 * 
	 * @param myFileId
	 * @return
	 */
	public MyFile queryMyFile(String myFileId);
	
	/**
	 * 
	 * @param classId
	 * @return
	 */
	public List<MyFile> queryAudioFileByClass(String classId);
	

}
