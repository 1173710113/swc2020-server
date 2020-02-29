package com.example.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.config.KeywordExtractorConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 通讯辅助类，用于建立关键词抽取类与python服务之间的连接，并接收服务器传回的返回值，进行处理得到关键词的列表
 * 
 * @author EvanClark
 *
 */
@Slf4j
public class CorrespondUtil {
	private final Socket socket;
	private final int TIMEOUT = 50000;
	private final PrintStream out;
	private final BufferedReader br;

	/**
	 * 通讯类，用于与某个指定端口地址的服务进行简单地通讯，发送信息并获得返回值
	 * 
	 * @param clientAddress 客户机的地址
	 * @param serverAddress 对应服务端的地址
	 * @param serverPort    服务的端口， 应当为1-65535
	 * @param clientPort    客户机的端口， 应当为1-65535
	 * @throws IOException 当创建socket时发生了I/O错误
	 */
	public CorrespondUtil(InetAddress clientAddress, InetAddress serverAddress, int serverPort, int clientPort)
			throws IOException {
		log.info("init correspond");
		this.socket = new Socket(serverAddress, serverPort);
		this.socket.setSoTimeout(TIMEOUT);
		OutputStream os = socket.getOutputStream();
		this.out = new PrintStream(os);
		InputStream is = socket.getInputStream();
		this.br = new BufferedReader(new InputStreamReader(is, "utf-8"));
	}

	/**
	 * 发送数据，并获得返回值的键值对
	 * 
	 * @param data 需要发送的数据的字符串
	 * @return 服务返回值的列表， 返回抽取的关键词的列表
	 * @throws IOException 当读取服务返回的数据时发生了错误
	 */
	public List<String> send(String data) throws IOException {
		out.print(data + "\n\n");
		String tmp;
		StringBuilder tmpString = new StringBuilder();
		while ((tmp = br.readLine()) != null) {
			System.out.println(tmp);
			tmpString.append(tmp + "\n");
		}
		String[] ranks = tmpString.toString().split("\n");
		List<String> keywords = new ArrayList<>();
		for (String rank : ranks) {
			if (rank == "") {
				continue;
			}
			String[] ranking = rank.split(" ");
			keywords.add(ranking[0]);
		}
		return keywords;
	}

	/**
	 * 关闭与python关键词抽取服务的连接
	 * 
	 * @throws IOException 关闭与服务的连接时出现I/O错误
	 */
	public void close() throws IOException {
		out.close();
		br.close();
		socket.close();
	}
}
