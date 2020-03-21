/**
 * 
 */
package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;

import lombok.extern.slf4j.Slf4j;

/**
 * @author msi-user
 *
 */
@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {

	@RequestMapping("/upload")
	@ResponseBody
	public MyResult upload(MultipartFile[] file, HttpServletRequest request) {
		int length = file.length;
		for (int i = 0; i < length; i++) {
			MultipartFile currentFile = file[i];
			String fileName = currentFile.getOriginalFilename();
			String realPath = request.getSession().getServletContext().getRealPath("/upload/");
			File targetFile = new File(realPath);
			log.info(realPath);
			// create folder if the directory is not exist
			if (!targetFile.exists() && !targetFile.isDirectory()) {
				targetFile.mkdirs();
			}
			File dir = new File(targetFile, fileName);
			try {
				currentFile.transferTo(dir);
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return MyResultGenerator.errorResult(e.getMessage(), e);
			}
		}

		return MyResultGenerator.successResult(null);
	}

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

}