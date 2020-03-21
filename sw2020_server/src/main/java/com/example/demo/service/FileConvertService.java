package com.example.demo.service;

import java.util.List;

import com.example.demo.exception.MyException;

public interface FileConvertService {

	/**
	 * 将ppt转为pdf
	 * @param pptFilePath ppt文件的地址
	 * @return pdf的文件地址
	 */
	public String pptConvertToPDF(String pptFilePath) throws MyException;
	
	/**
	 * 将pdf转成图片
	 * @param pdfFilePath pdf文件的地址
	 * @return 图片地址的集合
	 * @throws MyException
	 */
	public List<String> pdfConvertToImg(String pdfFilePath, String classId) throws MyException;
}
