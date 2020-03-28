/**
 * 
 */
package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

/**
 * @author msi-user
 *
 */
@Controller
@RequestMapping("/file")
public class FileController {

	private static final String CLASSPPT = "classppt";
	private static final String AUDIO = "audio";

	@Value("${file.ppt.folder}")
	private String pptFolder;

	@Value("${file.folder}")
	private String fileFolder;

	@Value("${file.audio.folder}")
	private String audioFolder;

	@Autowired
	private FileService fileService;

	/**
	 * 
	 * @param fileList
	 * @param request
	 * @param classId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/upload/{type}")
	@ResponseBody
	public MyResult upload(MultipartFile[] fileList, String classId, @PathVariable("type") final String type)
			throws Exception {
		String folder = getFileFolder(type) + "/" + classId;
		makeFileDir(folder);
		int length = fileList.length;
		for (int i = 0; i < length; i++) {
			MultipartFile currentFile = fileList[i];
			MyFile file = fileService.storeFile(currentFile, folder, classId);
			switch (type) {
			case CLASSPPT:
				fileService.addFileIndex(file);
				MyFile pdfFile = fileService.pptConvertToPDF(file.getFilePath(), classId);
				fileService.addFileIndex(pdfFile);
				List<String> imgFileList = fileService.pdfConvertToImg(pdfFile.getFilePath(), classId);
				for(String imgPath : imgFileList) {
					fileService.addImgFileIndex(imgPath, classId);
				}
				break;
			case AUDIO:
				fileService.addFileIndex(file);
				break;
			default:
				throw new MyException("文件位置异常");
			}
		}
		return MyResultGenerator.successResult(null);
	}

	private String getFileFolder(String type) throws MyException {
		switch (type) {
		case CLASSPPT:
			return fileFolder + "/" + pptFolder;
		case AUDIO:
			return fileFolder + "/" + audioFolder;
		default:
			throw new MyException("文件位置异常");
		}
	}

	private void makeFileDir(String folder) {
		File targetFile = new File(folder);
		if (!targetFile.exists() && !targetFile.isDirectory()) {
			targetFile.mkdirs();
		}
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
