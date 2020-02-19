package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.RecordMarkPO;

/**
 * 
 * @author msi-user
 *
 */
@Mapper
public interface RecordMarkMapper {
	
	/**
	 * 向数据库添加录音标记
	 * @param recordMark
	 */
	public void addRecordMark(RecordMarkPO recordMark);
	
	/**
	 * 从数据库删除指定录音标记
	 * @param recordMarkId 指定录音标记的id
	 */
	public void deleteRecordMark(String recordMarkId);
	
	/**
	 * 搜索关于指定课堂的所有录音标记
	 * @param classId 指定课堂的id
	 * @return 该课堂的所有录音标记
	 */
	public List<RecordMarkPO> queryRecordMarkByClass(String classId);
}
