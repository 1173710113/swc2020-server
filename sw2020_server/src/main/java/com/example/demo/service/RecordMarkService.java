package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.RecordMark;

/**
 * 
 * @author msi-user
 *
 */
public interface RecordMarkService {

	/**
	 * 添加录音标记
	 * 
	 * @param recordMark
	 */
	public RecordMark addRecordMark(RecordMark recordMark);

	/**
	 * 删除录音标记
	 * 
	 * @param recordMarkId 指定录音标记的id
	 */
	public void deleteRecordMark(String recordMarkId);

	/**
	 * 搜索指定课堂下的所有录音标记
	 * 
	 * @param classId 指定课堂的id
	 * @return
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
