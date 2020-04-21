package com.example.demo.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.GraphRelationConfiguration;
import com.example.demo.domain.AlignResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RelationBuilder {
	private static final int maxKeywordNum = 10; // 关键词数量
	private static final int maxSynonymNum = 3; // 每个关键词近义词数量
	private static final int WIN = 5; // 滑动窗口的大小
	private CorrespondUtil connect;
	private List<String> tokens;
	private Set<String> keywords;
	private AlignResult ar;
	private Map<String, Set<String>> graph = new HashMap<>(); // 构建的图, 邻接表
	// 关键词映射到的句子的标号
	private Map<String, List<Integer>> keywordMapSentences = new HashMap<String, List<Integer>>();

	// @Value("${graph.server}")
	// private String server;
	//
	// @Value("${graph.client}")
	// private String client;
	//
	// @Value("${graph.server-port}")
	// private String serverPort;
	//
	// @Value("${graph.client-port}")
	// private String clientPort;

	public RelationBuilder() {
	}

	public RelationBuilder(GraphRelationConfiguration relationConfig) throws UnknownHostException, IOException {
		connect = new CorrespondUtil(InetAddress.getByName(relationConfig.getServer()),
				InetAddress.getByName(relationConfig.getClient()), relationConfig.getServerPort(),
				relationConfig.getClientPort());
	}

	/**
	 * 请求关键词、分词
	 * 
	 * @param ar 转写结果
	 * @throws IOException
	 */
	public void request(AlignResult ar) throws IOException {
		this.ar = ar;
		String text = ar.getText();
		JSONObject revJS = connect.sendMsgGetKeywords(text, maxKeywordNum, maxSynonymNum);
		List<String> tokens = JSONObject.parseArray(revJS.getJSONArray("tokens").toJSONString(), String.class);
		Set<String> keywords = new HashSet<>(
				JSONObject.parseArray(revJS.getJSONArray("keywords").toJSONString(), String.class));
	}

	/**
	 * 构建关键词图
	 */
	public void buildGraph() {
		for (String keyword : keywords) {
			graph.put(keyword, new HashSet<>());
			keywordMapSentences.put(keyword, new ArrayList<Integer>());
		}

		int sentenceIndex = 0;
		int currentEnd = ar.getSentence(0).length();
		int currentPos = 0;
		for (String word : tokens) {
			if (currentPos >= currentEnd) { // 下一个句子
				sentenceIndex++;
				currentEnd += ar.getSentence(sentenceIndex).length();
			}
			if (keywords.contains(word)) {
				keywordMapSentences.get(word).add(sentenceIndex);
			}
			currentPos += word.length(); // 下一个词
		}

		for (int i = 0; i < tokens.size(); i++) {
			if (keywords.contains(tokens.get(i))) {
				for (int j = i + 1; j <= i + WIN && j < tokens.size(); j++) {
					if (keywords.contains(tokens.get(j)) && !tokens.get(i).equals(tokens.get(j))) {
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
	public Set<String> getAdjacent(String keyword) {
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

	private List<String> getSentencesOfKeyword(String keyword) {
		List<String> res = new ArrayList<String>();
		for (Integer i : keywordMapSentences.get(keyword)) {
			res.add(ar.getSentence(i));
		}
		return res;
	}

	public static void main(String[] args) throws IOException {

//		try {
//			RelationBuilder builder = new RelationBuilder();
//			File file = new File("G:\\My_Document\\2019.12.29_SWC\\swc2020-server\\test.txt");
//			Scanner s = new Scanner(file);
//			String s1 = "";
//			while (s.hasNextLine()) {
//				s1 = s.nextLine();
//			}
//			builder.request(s1);
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String test = Files.readAllLines(new File("a.txt").toPath()).get(0);
		JSONObject revJS = JSONObject.parseObject(test);
		RelationBuilder rb = new RelationBuilder();
		List<String> tokens = JSONObject.parseArray(revJS.getJSONArray("tokens").toJSONString(), String.class);
		Set<String> keywords = new HashSet<>(
				JSONObject.parseArray(revJS.getJSONArray("keywords").toJSONString(), String.class));
		rb.keywords = keywords;
		rb.tokens = tokens;
		rb.buildGraph();
		for (String v : rb.getKeywordsSet()) {
			System.out.println(v + ": " + rb.getAdjacent(v));
		}
	}

}
