package com.example.demo.controller;

import java.io.File;

import org.jodconverter.DocumentConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
