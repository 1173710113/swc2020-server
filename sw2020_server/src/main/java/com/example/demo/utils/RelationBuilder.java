package com.example.demo.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.KeywordExtractorConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RelationBuilder {
	private static final int maxKeywordNum = 10; // 关键词数量
	private static final int maxSynonymNum = 3; // 每个关键词近义词数量
	private static final int WIN = 5; // 滑动窗口的大小
	private CorrespondUtil connect;
	private List<String> tokens;
	private Set<String> keywords;
	private Map<String, List<String>> graph = new HashMap<>(); // 构建的图, 邻接表

	@Autowired
	private KeywordExtractorConfiguration keywordExtractorConfig;

	public RelationBuilder() throws UnknownHostException, IOException {
		connect = new CorrespondUtil(InetAddress.getByName(keywordExtractorConfig.getServer()),
				InetAddress.getByName(keywordExtractorConfig.getClient()), keywordExtractorConfig.getServerPort(),
				keywordExtractorConfig.getClientPort());
	}

	/**
	 * 请求关键词、分词
	 * 
	 * @param text 原文章
	 * @throws IOException
	 */
	public void request(String text) throws IOException {
		JSONObject revJS = connect.sendMsgGetKeywords(text, maxKeywordNum, maxSynonymNum);
		tokens = revJS.getJSONArray("tokens").toJavaList(String.class);
		keywords = new HashSet<>(revJS.getJSONArray("keywords").toJavaList(String.class));
		for (String keyword : keywords) {
		    System.out.print(keyword + " ");
			graph.put(keyword, new ArrayList<>());
		}
	}

	/**
	 * 构建关键词图
	 */
	public void buildGraph() {
		for (int i = 0; i < tokens.size(); i++) {
			if (keywords.contains(tokens.get(i))) {
				for (int j = i + 1; j <= i + WIN && j < tokens.size(); j++) {
					if (keywords.contains(tokens.get(j))) {
						graph.get(tokens.get(i)).add(tokens.get(j));
					}
				}
			}
		}
	}

	/**
	 * 获取某个关键词的相邻节点
	 * 
	 * @param keyword
	 * @return
	 */
	public List<String> getAdjacent(String keyword) {
		return graph.get(keyword);
	}

	/**
	 * 获取所有关键词
	 * 
	 * @return
	 */
	public Set<String> getKeywordsSet() {
		return keywords;
	}

	/**
	 * TEST
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	  try {
      RelationBuilder builder = new RelationBuilder();
      File file = new File("G:\\My_Document\\2019.12.29_SWC\\swc2020-server");
      Scanner s = new Scanner(file);
      String s1 = "";
      while(s.hasNextLine()) {
        s1 = s.nextLine();
      }
      builder.request(s1);
    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
	}
}
