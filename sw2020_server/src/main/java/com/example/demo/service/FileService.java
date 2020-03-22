package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.MyFile;
import com.example.demo.exception.MyException;

public interface FileService {


	/**
	 * 
	 * @param file
	 * @param fileFolder
	 * @param classId
	 * @return
	 * @throws MyException
	 */
	public MyFile storeFile(MultipartFile file, String fileFolder, String classId) throws MyException;
	
	/**
	 * 将ppt转为pdf
	 * @param pptFilePath ppt文件的地址
	 * @return pdf的文件地址
	 */
	public MyFile pptConvertToPDF(String pptFilePath, String classId) throws MyException;
	
	/**
	 * 将pdf转成图片
	 * @param pdfFilePath pdf文件的地址
	 * @return 图片地址的集合
	 * @throws MyException
	 */
	public List<String> pdfConvertToImg(String pdfFilePath, String classId) throws MyException;
	
	
	public List<String> queryPPTImgByClass(String classId);
}
