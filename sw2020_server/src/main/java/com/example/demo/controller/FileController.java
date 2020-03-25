/**
 * 
 */
package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jodconverter.DocumentConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.MyFile;
import com.example.demo.exception.MyException;
import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;
import com.example.demo.service.FileService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author msi-user
 *
 */
@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {

	private static final String CLASSPPT = "classppt";

	@Value("${file.ppt.folder}")
	private String pptFolder;

	@Value("${file.folder}")
	private String fileFolder;

	@Autowired
	private FileService fileService;
	
	@Autowired
	private DocumentConverter converter;

	/**
	 * 
	 * @param fileList
	 * @param request
	 * @param classId
	 * @param type
	 * @return
	 * @throws MyException
	 */
	@RequestMapping("/upload/{type}")
	@ResponseBody
	public MyResult upload(MultipartFile[] fileList, String classId, @PathVariable("type") final String type)
			throws MyException {

		switch (type) {
		case CLASSPPT:
			String folder = fileFolder + "/" + pptFolder + "/" + classId;
			File targetFile = new File(folder);
			if (!targetFile.exists() && !targetFile.isDirectory()) {
				targetFile.mkdirs();
			}
			// 循环存储文件
			int length = fileList.length;
			for (int i = 0; i < length; i++) {
				MultipartFile currentFile = fileList[i];
				MyFile pptFile = fileService.storeFile(currentFile, folder, classId);
				MyFile pdfFile = fileService.pptConvertToPDF(pptFile.getFilePath(), classId);
				fileService.pdfConvertToImg(pdfFile.getFilePath(), classId);
			}
			break;
		default:
			throw new MyException("文件异常");
		}
		return MyResultGenerator.successResult(null);
	}

	/**
	 * 
	 * @param fileName
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/download")
	@ResponseBody
	public String download(String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String realPath = request.getSession().getServletContext().getRealPath("upload/");
		File file = new File(realPath, fileName);
		FileInputStream stream = new FileInputStream(file);
		String extendFileName = fileName.substring(fileName.lastIndexOf("."));
		response.setContentType(request.getSession().getServletContext().getMimeType(extendFileName));
		response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
		response.setContentLengthLong(file.length());
		ServletOutputStream out = response.getOutputStream();
		FileCopyUtils.copy(stream, out);
		return "success";
	}
	
	@ResponseBody
	@RequestMapping("/query/pptimg")
	public MyResult queryPPTImg(String classId) {
		return MyResultGenerator.successResult(fileService.queryPPTImgByClass(classId));
	}
	
}
