package com.example.demo.domain;

import java.util.ArrayList;
import java.util.List;

/* 表示语音转写的结果, 并带有对齐信息 */
public class AlignResult {
	
	private String name;  // 标识名称
	private String text;  // 转写的文本串, 编码为UTF-8
	private List<Integer> sentenceStamp = new ArrayList<>(); // String的每个句子的开始位置
	private List<Integer> timeStamp = new ArrayList<>();  // 音频文件的时间戳(单位ms), 与句子分割点对应
	                                                      //(唯一不同是, 多一个音频结束时间)
	/*
	 * sentenceStamp.size()+1 == timeStamp.size()恒成立
	 */
	
	
	/**
	 * 根据服务器返回的数据, 生成AlignResult对象
	 * @param msg 服务器返回的数据
	 */
	public AlignResult(String name, String msg) {
		// 待写
	}
	
	
	/**
	 * 获取结果的标识名称
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 获取句子总数
	 * @return
	 */
	public int getNumOfSentence() {
		return sentenceStamp.size();
	}
	
	/**
	 * 获取整个文本串
	 * @return
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * 获得第num句话的文本(从0开始计数)
	 * @param num 0 <= num <= getNumOfSentence()-1 
	 * @return 
	 */
	public String getSentence(int num) {
		if (num < sentenceStamp.size() - 1) {
			return text.substring(sentenceStamp.get(num), sentenceStamp.get(num + 1));
		}
		else {
			return text.substring(sentenceStamp.get(num));
		}
	}
	
	/**
	 * 获得第num句话的音频开始时间 (从0开始计数)
	 * @param num 0 <= num <= getNumOfSentence()-1 
	 * @return 第num句话的音频开始时间(单位ms)
	 */
	public int getBeginTime(int num) {
		return timeStamp.get(num);
	}
	
	/**
	 * 获得第num句话的音频结束时间 (从0开始计数)
	 * @param num 0 <= num <= getNumOfSentence()-1 
	 * @return 第num句话的音频结束时间(单位ms)
	 */
	public int getEndTime(int num) {
		return timeStamp.get(num + 1);
	}
	
}
