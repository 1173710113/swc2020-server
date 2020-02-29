package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.RecordMark;

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
	public void addRecordMark(RecordMark recordMark);
	
	/**
	 * 从数据库删除指定录音标记
	 * @param recordMarkId 指定录音标记的id
	 */
	public void deleteRecordMark(String recordMarkId);
	
	/**
	 * 更新录音标记的标记内容
	 * @param recordMarkId 要更新的录音标记的id
	 * @param newMark 新的标记内容
	 */
	public void updateRecordMark(String recordMarkId, String newMark);
	
	/**
	 * 搜索关于指定课堂的所有录音标记
	 * @param classId 指定课堂的id
	 * @return 该课堂的所有录音标记，按照标记时间从小到大
	 */
	public List<RecordMark> queryRecordMarkByClass(String classId);
	
	/**
	 * 搜索指定课堂下指定用户的录音标记
	 * @param classId 课堂id
	 * @param userId 用户id
	 * @return 录音标记的集合
	 */
	public List<RecordMark> queryRecordMarkByClassAndUser(String classId, String userId);
}
