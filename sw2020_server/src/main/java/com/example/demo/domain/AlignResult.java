package com.example.demo.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表示语音转写的结果, 并带有对齐信息
 * 
 * @author xjy
 */
public class AlignResult {

	private String filePath; // 转写的音频文件路径

	private String text; // 转写的总文本串, 编码为UTF-8
	private List<String> sentences = new ArrayList<>(); // String的每个句子的开始位置
	private List<Timespan> timeStamp = new ArrayList<>(); // 音频文件的时间戳(单位ms), 句子对应
	/*
	 * sentenceStamp.size() == timeStamp.size()恒成立
	 */

	/**
	 * 根据服务器返回的数据, 生成AlignResult对象
	 * 
	 * @param msg 服务器返回的数据
	 */
	public AlignResult(String filePath, String msg) {
		this.filePath = filePath;

		StringBuilder textBf = new StringBuilder();
		// 匹配每句话的正则表达式
		Pattern pattern = Pattern
				.compile("\"bg\":\"(\\d+)\",\"ed\":\"(\\d+)\",\"onebest\":\"(.*?)\",\"speaker\":\"(\\d+)\""
						+ ",\"wordsResultList\":\\[(.*?)\\]\\}");
		// 匹配一句话中的分词信息的正则表达式
		Pattern wordsPat = Pattern.compile(
				"\"alternativeList\":\\[\\],\"wc\":\".*?\",\"wordBg\":\".*?\",\"wordEd\":\".*?\",\"wordsName\":\"(.*?)\",\"wp\":\"(.*?)\"");
		Matcher m = pattern.matcher(msg);

		while (m.find()) { // 匹配每一句话
			int beginTime = Integer.parseInt(m.group(1)); // 句子开始时间
			int endTime = Integer.parseInt(m.group(2)); // 结束时间
			StringBuilder sentence = new StringBuilder();
			String words = m.group(5); // 分词信息
			Matcher wordsMac = wordsPat.matcher(words);
			while (wordsMac.find()) { // 匹配每个分词项
				String wordsName = wordsMac.group(1); // 分词内容
				String wp = wordsMac.group(2); // 词性
				if (!wp.equals("s")) { // 去除语气词
					sentence.append(wordsName);
				}
			}

			textBf.append(sentence);
			String temp = "";
			if (!(temp = sentence.toString()).equals("")) {
				sentences.add(temp);
				timeStamp.add(new Timespan(beginTime, endTime));
			}

		}

		text = textBf.toString();
	}

	/**
	 * 获取转写的文件路径
	 * 
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * 获取句子总数
	 * 
	 * @return
	 */
	public int getNumOfSentence() {
		return sentences.size();
	}

	/**
	 * 获取整个文本串
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * 获得第num句话的文本(从0开始计数)
	 * 
	 * @param num 0 <= num <= getNumOfSentence()-1
	 * @return
	 */
	public String getSentence(int num) {
		return sentences.get(num);
	}

	/**
	 * 获得第num句话的音频开始时间 (从0开始计数)
	 * 
	 * @param num 0 <= num <= getNumOfSentence()-1
	 * @return 第num句话的音频开始时间(单位ms)
	 */
	public int getBeginTime(int num) {
		return timeStamp.get(num).beginTime;
	}

	/**
	 * 获得第num句话的音频结束时间 (从0开始计数)
	 * 
	 * @param num 0 <= num <= getNumOfSentence()-1
	 * @return 第num句话的音频结束时间(单位ms)
	 */
	public int getEndTime(int num) {
		return timeStamp.get(num).endTime;
	}

	private class Timespan {

		int beginTime;
		int endTime;

		Timespan(int beginTime, int endTime) {
			this.beginTime = beginTime;
			this.endTime = endTime;
		}
	}

//	// ........Test.......
//	public static void main(String[] args) throws IOException {
//		List<String> lines = Files.readAllLines(new File("./resource/audio/test.txt").toPath());
//		String msg = lines.get(0);
//		AlignResult ar = new AlignResult("xwlb", msg);
//		for (int i = 0; i < ar.getNumOfSentence(); i++) {
//			System.out.println("begin: " + ar.getBeginTime(i));
//			System.out.println("end: " + ar.getEndTime(i));
//			System.out.println(ar.getSentence(i));
//			System.out.println();
//		}
//
//		System.out.println("The text is: " + ar.getText());
//	}
}
