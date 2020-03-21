package com.example.demo.controller;

import java.io.File;
import java.util.List;

import org.jodconverter.DocumentConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.exception.MyException;
import com.example.demo.service.FileConvertService;
import com.example.demo.service.PPTImgService;

/**
 * 
 * @author msi-user
 *
 */
@RequestMapping("/pdf")
@Controller
public class PDFController {

	@Autowired
	private DocumentConverter converter;
	
	@Autowired
	private FileConvertService service;
	
	@Autowired
	private PPTImgService imgService;

	@RequestMapping("/convert")
	public void toPdfFile() {
		File file = new File("src/main/resources/static/72.pptx");// 需要转换的文件
		try {
			File newFile = new File("src/main/resources/static");// 转换之后文件生成的地址
			if (!newFile.exists()) {
				newFile.mkdirs();
			}
			// 文件转化
			converter.convert(file).to(new File("src/main/resources/static/72.pdf")).execute();
			/**
			 * //使用response,将pdf文件以流的方式发送的前段 ServletOutputStream outputStream =
			 * response.getOutputStream(); InputStream in = new FileInputStream(new
			 * File("src/main/resources/static/72.pdf"));// 读取文件 // copy文件 int i =
			 * IOUtils.copy(in, outputStream); System.out.println(i); in.close();
			 * outputStream.close();
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@ResponseBody
	@RequestMapping("/convert/img")
	 public List<String> pdfToImage() throws MyException {
		String pdfPath = "src/main/resources/static/72.pdf";
		List<String> imgPathList = service.pdfConvertToImg(pdfPath, "12");
		imgService.addPPTImg(imgPathList, "12");
		return imgPathList;
		
	}


}
