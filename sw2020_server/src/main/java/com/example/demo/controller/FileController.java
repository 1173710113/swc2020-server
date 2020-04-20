/**
 * 
 */
package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.UnsupportedAudioFileException;

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
	private static final String AUDIOAAC = "aac";
	private static final String NORMAL = "normal";
	private static final String PPTIMAGE = "pptimage";

	@Value("${file.ppt.folder}")
	private String pptFolder;

	@Value("${file.folder}")
	private String fileFolder;

	@Value("${file.audio.folder}")
	private String audioFolder;
	
	@Value("${file.normal.folder}")
	private String normalFolder;
	
	@Value("${file.pptimg.folder}")
	private String pptImgFolder;

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
	public MyResult upload(MultipartFile file, String classId, @PathVariable("type") final String type)
			throws Exception {
		String folder = getFileFolder(type) + "/" + classId;
		makeFileDir(folder);
			MyFile fileIndex = fileService.storeFile(file,folder, classId);
			switch (type) {
			case CLASSPPT:
				fileService.addFileIndex(fileIndex);
				MyFile pdfFile = fileService.pptConvertToPDF(fileIndex.getFilePath(), classId);
				fileService.addFileIndex(pdfFile);
				List<String> imgFileList = fileService.pdfConvertToImg(pdfFile.getFilePath(), classId);
				for(String imgPath : imgFileList) {
					fileService.addImgFileIndex(imgPath, classId);
				}
				break;
			case AUDIO:
				fileService.addFileIndex(fileIndex);
				break;
			case NORMAL:
				fileService.addFileIndex(fileIndex);
				break;
			case AUDIOAAC:
				fileService.addFileIndex(fileIndex);
				MyFile wavFile = fileService.convertAACToWAV(fileIndex);
				fileService.addFileIndex(wavFile);
				break;
			case PPTIMAGE:
				String fileName = fileIndex.getFileName();
				fileService.addImgFileIndex(pptImgFolder + "/" + classId + "/" + fileName, classId);
				break;
			default:
				throw new MyException("文件类型异常:" + type);
			}
		
		return MyResultGenerator.successResult(null);
	}

	private String getFileFolder(String type) throws MyException {
		switch (type) {
		case CLASSPPT:
			return fileFolder + "/" + pptFolder;
		case AUDIO:
			return fileFolder + "/" + audioFolder;
		case AUDIOAAC:
			return fileFolder + "/" + audioFolder;
		case PPTIMAGE:
			return fileFolder + " /" + pptImgFolder;
		case NORMAL:
			return fileFolder + "/" + normalFolder;
		default:
			throw new MyException("文件位置异常:" + type);
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
	
	@ResponseBody
	@RequestMapping("/merge")
	public MyResult mergeWAV(String classId) throws UnsupportedAudioFileException, IOException, MyException {
		MyFile mergeFile = fileService.mergeWAV(classId);
		fileService.addFileIndex(mergeFile);
		return MyResultGenerator.successResult(mergeFile);
	}
	
	

}
