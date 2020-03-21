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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.exception.MyException;
import com.example.demo.service.FileConvertService;

@Service
public class FileConvertServiceImp implements FileConvertService {

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

	@Override
	public String pptConvertToPDF(String pptFilePath) throws MyException {
		File pptFile = new File(pptFilePath);// 需要转换的文件
		try {
			File pdfFolder = new File(fileFolder + "/" + pdfStorageFolder);// 转换之后文件生成的地址
			if (!pdfFolder.exists()) {
				pdfFolder.mkdirs();
			}
			// 文件转化
			File pdfFile = new File(pdfFolder.getPath() + "/" + pptFile.getName());
			converter.convert(pptFile).to(pdfFile).execute();
			return pdfFile.getPath();
		} catch (Exception e) {
			throw new MyException("文件处理异常");
		}
	}

	@Override
	public List<String> pdfConvertToImg(String pdfFilePath, String classId) throws MyException {

		List<String> imgPathList = new ArrayList<>();
		File file = null;
		PDDocument pdDocument = null;
		try {
			file = new File(pdfFilePath);
			pdDocument = PDDocument.load(file);
			PDFRenderer renderer = new PDFRenderer(pdDocument);
			int pageCount = pdDocument.getNumberOfPages();
			/* dpi越大转换后越清晰，相对转换速度越慢 */
			File imgFolder = new File(fileFolder + "/" + pptImgFolder+"/" + classId);
			if(! imgFolder.exists()) {
				imgFolder.mkdirs();
			}
			for (int i = 0; i < pageCount; i++) {
				String fileName = i + ".png";
				String filePath = imgFolder.getPath() + "/" + fileName;
				File dstFile = new File(filePath);
				BufferedImage image = renderer.renderImageWithDPI(i, imgDpi);
				ImageIO.write(image, "png", dstFile);
				imgPathList.add(pptImgFolder + "/" + classId + "/" +  fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
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

}
