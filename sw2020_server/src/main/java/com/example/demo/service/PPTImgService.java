package com.example.demo.service;

import java.util.List;

public interface PPTImgService {

	/**
	 * 
	 * @param imgPath
	 * @param classId
	 */
	public void addPPTImg(List<String> imgPathList, String classId);
	
	
	/**
	 * 
	 * @param classId
	 * @return
	 */
	public List<String> queryPPTImgByClass(String classId);
}
