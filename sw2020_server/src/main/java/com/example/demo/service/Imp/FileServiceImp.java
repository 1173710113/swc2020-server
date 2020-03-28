package com.example.demo.service.Imp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

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

	@Autowired
	private MyFileMapper myFileMapper;

	@Autowired
	private PPTImgMapper pptImgMapper;

	@Override
	public MyFile storeFile(MultipartFile file, String fileFolder, String classId)
			throws IllegalStateException, IOException {

		String fileName = file.getOriginalFilename();
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

}
