package com.example.demo.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 表示语音转写的结果, 并带有对齐信息 */
public class AlignResult {

	private String name; // 标识名称

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
	public AlignResult(String name, String msg) {
		this.name = name;

		StringBuilder textBf = new StringBuilder();
		Pattern pattern = Pattern
				.compile("\"bg\":\"(\\d+)\",\"ed\":\"(\\d+)\",\"onebest\":\"(.*?)\",\"speaker\":\"(\\d+)\"");
		Matcher m = pattern.matcher(msg);

		while (m.find()) {
			int beginTime = Integer.parseInt(m.group(1));
			int endTime = Integer.parseInt(m.group(2));
			String sentence = m.group(3);

			textBf.append(sentence);
			sentences.add(sentence);
			timeStamp.add(new Timespan(beginTime, endTime));
		}

		text = textBf.toString();
	}

	/**
	 * 获取结果的标识名称
	 * 
	 * @return
	 */
	public String getName() {
		return name;
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

	// ........Test.......
//	public static void main(String[] args) {
//		String msg = "[{\"bg\":\"180\",\"ed\":\"1640\",\"onebest\":\"各位观众晚上。\",\"speaker\":\"0\"},{\"bg\":\"1650\",\"ed\":\"2750\",\"onebest\":\"好晚上好！\",\"speaker\":\"0\"},{\"bg\":\"2760\",\"ed\":\"7720\",\"onebest\":\"今天是1月17号，星期五，农历12月23，欢迎收看新闻联播节目！\",\"speaker\":\"0\"},{\"bg\":\"7730\",\"ed\":\"9510\",\"onebest\":\"今天节目的主要内容有。\",\"speaker\":\"0\"},{\"bg\":\"9960\",\"ed\":\"12970\",\"onebest\":\"习近平离京对缅甸进行国事访问！\",\"speaker\":\"0\"},{\"bg\":\"12980\",\"ed\":\"17930\",\"onebest\":\"习近平抵达内比都开始对缅甸联邦共和国进行国事访问！\",\"speaker\":\"0\"},{\"bg\":\"17930\",\"ed\":\"21940\",\"onebest\":\"习近平出席缅甸总统温民举行的欢迎仪式！\",\"speaker\":\"0\"},{\"bg\":\"21950\",\"ed\":\"25510\",\"onebest\":\"习近平会见缅甸国务资政昂山素季。\",\"speaker\":\"0\"},{\"bg\":\"25510\",\"ed\":\"35090\",\"onebest\":\"习近平对政法工作作出重要指示，强调着力提高政法工作现代化水平，建设更高水平的平安中国，法治中国！\",\"speaker\":\"0\"},{\"bg\":\"35100\",\"ed\":\"35400\",\"onebest\":\"哦\",\"speaker\":\"0\"},{\"bg\":\"35400\",\"ed\":\"39500\",\"onebest\":\"李克强同在华外国专家举行新春座谈会！\",\"speaker\":\"0\"},{\"bg\":\"39500\",\"ed\":\"44160\",\"onebest\":\"李克强致电祝贺米出资金出任俄罗斯联邦政府总理！\",\"speaker\":\"0\"},{\"bg\":\"44160\",\"ed\":\"54830\",\"onebest\":\"全国政协党组举行集体学习，学习贯彻习近平总书记在中央纪委4次全会上的重要讲话和全会精神！汪洋主持并讲话。\",\"speaker\":\"0\"},{\"bg\":\"54830\",\"ed\":\"64130\",\"onebest\":\"2019年我国经济发展质量稳步提升，国内生产总值将近100万亿人民币，人均国内生产总值突破1万美元，\",\"speaker\":\"0\"},{\"bg\":\"64270\",\"ed\":\"71100\",\"onebest\":\"联合国发布报告认为，中国经济未来会有积极表现，能够推动世界经济持续发展。\",\"speaker\":\"0\"},{\"bg\":\"71110\",\"ed\":\"76870\",\"onebest\":\"各位观众，今天的新闻联播大约需要38分钟，接下来请看详细报道。\",\"speaker\":\"0\"}]";
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
