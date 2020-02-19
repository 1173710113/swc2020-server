package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.RecordMarkPO;

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
	public RecordMarkPO addRecordMark(RecordMarkPO recordMark);

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
	public List<RecordMarkPO> queryRecordMarkByClass(String classId);
}
