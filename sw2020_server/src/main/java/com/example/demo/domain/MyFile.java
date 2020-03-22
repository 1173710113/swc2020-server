package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 用于索引服务器文件的类
 * @author msi-user
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class MyFile {
	private String id;
	private String  filePath;
	private String fileName;
	private String classId;
}
