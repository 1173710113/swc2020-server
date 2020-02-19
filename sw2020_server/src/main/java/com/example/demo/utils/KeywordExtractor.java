package com.example.demo.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import com.example.demo.utils.configReader.Configreader;

public class KeywordExtractor {
	private final CorrespondUtil connect;

	/**
	 * 生成关键词抽取器的一个实例
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public KeywordExtractor() throws UnknownHostException, IOException {
		Configreader configreader = Configreader.reader("config.xml");
		connect = new CorrespondUtil(InetAddress.getByName(configreader.readItem("keywordExtractorServer")),
				InetAddress.getByName(configreader.readItem("keywordExtractorClient")),
				Integer.parseInt(configreader.readItem("keywordExtractorServerPort")),
				Integer.parseInt(configreader.readItem("keywordExtractorClientPort")));
	}

	/**
	 * 给定语料，从中抽取指定个数的关键词
	 * 
	 * @param corpus 给定的语料
	 * @param keyNum 需要抽取的关键词个数
	 * @return 收取的关键词按相关程度从大到小排序的列表
	 * @throws IOException
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
