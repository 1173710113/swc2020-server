package com.example.demo.service;

import com.example.demo.domain.MyFile;

/**
 * 用来进行文件索引管理的服务类
 * @author msi-user
 *
 */
public interface MyFileService {
	
	/**
	 * save file
	 * @param myFile
	 */
	public void addMyFile(MyFile myFile);
	
	/**
	 * delete file
	 * @param myFileId
	 */
	public void deleteFile(String myFileId);
	
}
