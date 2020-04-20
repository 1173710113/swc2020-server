package com.example.demo.service.Imp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jodconverter.DocumentConverter;
import org.jodconverter.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dao.MyFileMapper;
import com.example.demo.dao.PPTImgMapper;
import com.example.demo.domain.MyFile;
import com.example.demo.exception.MyException;
import com.example.demo.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImp implements FileService {

	@Autowired
	private DocumentConverter converter;

	@Value("${file.pdf.folder}")
	private String pdfStorageFolder;

	@Value("${file.pptimg.folder}")
	private String pptImgFolder;

	@Value("${file.pdf.convert.img.dpi}")
	private int imgDpi;

	@Value("${file.folder}")
	private String fileFolder;

	@Value("${file.audio.folder}")
	private String audioFolder;

	@Autowired
	private MyFileMapper myFileMapper;

	@Autowired
	private PPTImgMapper pptImgMapper;

	@Override
	public MyFile storeFile(MultipartFile file, String fileFolder, String classId)
			throws IllegalStateException, IOException {

		String fileName = file.getOriginalFilename();
		log.info(fileName);
		File dir = new File(fileFolder + "/" + fileName);
		file.transferTo(dir.getAbsoluteFile());
		String filePath = dir.getPath();
		log.info(filePath);
		MyFile myFile = new MyFile(null, filePath, fileName, classId);
		return myFile;
	}

	@Override
	public MyFile pptConvertToPDF(String pptFilePath, String classId) throws OfficeException {
		File pptFile = new File(pptFilePath);// 需要转换的文件
		File pdfFolder = new File(fileFolder + "/" + pdfStorageFolder + "/" + classId);// 转换之后文件生成的地址
		if (!pdfFolder.exists()) {
			pdfFolder.mkdirs();
		}
		// 文件转化
		String pptFileName = pptFile.getName();
		String pdfFileName = pptFileName.substring(0, pptFileName.indexOf('.')) + ".pdf";
		File pdfFile = new File(pdfFolder.getPath() + "/" + pdfFileName);
		log.info(pptFile.getPath() + " to " + pdfFile.getPath());
		converter.convert(pptFile).to(pdfFile).execute();
		MyFile myFile = new MyFile(null, pdfFile.getPath(), pdfFile.getName(), classId);
		return myFile;
	}

	@Override
	public List<String> pdfConvertToImg(String pdfFilePath, String classId) throws Exception {
		List<String> imgPathList = new ArrayList<>();
		File file = null;
		PDDocument pdDocument = null;
		try {
			file = new File(pdfFilePath);
			pdDocument = PDDocument.load(file);
			PDFRenderer renderer = new PDFRenderer(pdDocument);
			int pageCount = pdDocument.getNumberOfPages();
			/* dpi越大转换后越清晰，相对转换速度越慢 */
			File imgFolder = new File(fileFolder + "/" + pptImgFolder + "/" + classId);
			if (!imgFolder.exists()) {
				imgFolder.mkdirs();
			}
			for (int i = 0; i < pageCount; i++) {
				String fileName = i + ".png";
				String filePath = imgFolder.getPath() + "/" + fileName;
				File dstFile = new File(filePath);
				BufferedImage image = renderer.renderImageWithDPI(i, imgDpi);
				ImageIO.write(image, "png", dstFile);
				imgPathList.add(pptImgFolder + "/" + classId + "/" + fileName);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				// 这里需要关闭PDDocument，不然如果想要删除pdf文件时会提示文件正在使用，无法删除的情况
				pdDocument.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return imgPathList;
	}

	@Override
	public List<String> queryPPTImgByClass(String classId) {
		return pptImgMapper.queryImgByClass(classId);
	}

	@Override
	public void addFileIndex(MyFile file) {
		myFileMapper.addMyFile(file);
	}

	@Override
	public void addImgFileIndex(String imgFilePath, String classId) {
		pptImgMapper.addImg(imgFilePath, classId);
	}

	@Override
	public MyFile mergeWAV(String classId) throws UnsupportedAudioFileException, IOException, MyException {
		List<MyFile> myFileList = myFileMapper.queryAudioFileByClass(classId);
		List<File> fileList = new ArrayList<>();
		for (MyFile myFile : myFileList) {
			File file = new File(myFile.getFilePath());
			fileList.add(file);
		}
		List<InputStream> inputList = new ArrayList<>();
		int totalLength = 0;
		AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(fileList.get(0));
		for (File file : fileList) {
			AudioFileFormat temp = AudioSystem.getAudioFileFormat(file);
			if (!audioFileFormat.getFormat().matches(temp.getFormat())) {
				throw new MyException("音频文件格式不匹配");
			}
			if (!audioFileFormat.getType().equals(temp.getType())) {
				throw new MyException("音频文件类型不匹配");
			}
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
			inputList.add(audioInputStream);
			totalLength += audioInputStream.getFrameLength();
		}
		Enumeration<InputStream> enumeration = Collections.enumeration(inputList);
		SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
		if (totalLength > 0) {
			File mergeFile = new File(fileFolder + "/" + audioFolder + "/" + classId + "/merge.wav");
			AudioSystem.write(new AudioInputStream(sequenceInputStream, audioFileFormat.getFormat(), totalLength),
					audioFileFormat.getType(), mergeFile);
			MyFile mergeFileIndex = new MyFile(null, mergeFile.getPath(), mergeFile.getName(), classId);
			return mergeFileIndex;
		} else {
			sequenceInputStream.close();
			throw new MyException("录音合并出错");
		}
	}

	@Override
	public MyFile convertAACToWAV(MyFile myFile) throws IOException, InterruptedException {
		String sourceFileName = myFile.getFileName();
		String fileName = sourceFileName.substring(0, sourceFileName.lastIndexOf('.')) + ".wav";
		log.info(fileName);
		String sourceFilePath = myFile.getFilePath();
		String filePath = sourceFilePath.substring(0, sourceFilePath.lastIndexOf('.')) + ".wav";
		log.info(filePath);
		File fileTarget = new File(filePath);
		File fileSource = new File(myFile.getFilePath());
		String cmd = "ffmpeg -i " + fileSource.getAbsolutePath() + " " + fileTarget.getAbsolutePath();
		log.info(cmd);
		Process process = Runtime.getRuntime().exec(cmd);
		process.waitFor();
		MyFile newFile = new MyFile(null, filePath, fileName, myFile.getClassId());
		return newFile;
	}

}
