package com.example.demo.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.List;

import com.example.demo.config.KeywordExtractorConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 关键词抽取类，用于建立与Python关键词抽取服务之间的连接， 通过服务调用的形式提供给Python服务语料，并获得返回的关键词数据
 * 
 * @author EvanClark
 *
 */
@Slf4j
public class KeywordExtractor {

	private CorrespondUtil connect;

	/**
	 * 生成关键词抽取器的一个实例，实例将会从config.xml读取对应的配置文件
	 * 
	 * @throws UnknownHostException 当从配置文件中读取的客户端或服务器地址非可用的地址时
	 * @throws IOException          创建与服务器的连接时发生了I/O错误
	 */
	public KeywordExtractor(KeywordExtractorConfiguration keywordExtractorConfig)
			throws UnknownHostException, IOException {
		log.info("init extractor");
		connect = new CorrespondUtil(InetAddress.getByName(keywordExtractorConfig.getServer()),
				InetAddress.getByName(keywordExtractorConfig.getClient()), keywordExtractorConfig.getServerPort(),
				keywordExtractorConfig.getClientPort());
	}

	/**
	 * 给定语料，从中抽取指定个数的关键词
	 * 
	 * @param corpus 给定的语料，语料中不应当包含换行符等控制符号
	 * @param keyNum 需要抽取的关键词个数，0<关键词个数<20
	 * @return 收取的关键词按相关程度从大到小排序的列表
	 * @throws IOException 当读取服务返回的数据时发生了错误
	 */
	public List<String> keywordExtract(String corpus, int keyNum) throws IOException {
		List<String> keywords;
		keywords = connect.send(corpus + " " + keyNum);
		return keywords;
	}

	/**
	 * 关闭与python关键词抽取服务的连接
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		connect.close();
	}
}
