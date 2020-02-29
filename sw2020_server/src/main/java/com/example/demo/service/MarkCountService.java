package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import com.example.demo.domain.EffectiveMarkRange;
import com.example.demo.domain.KeyWord;

/**
 * 该接口规定了对于标记功能的service所需要的基本功能
 * 
 * @author 50136
 *
 */
public interface MarkCountService {

	/**
	 * 根据转写对齐结果、用户标记结果，初始化本对象
	 * 
	 * @param alignResult 转写对齐结果
	 * @param marks       用户标记结果
	 */
	public void initialize(String audioPath, String classId) throws IOException;

	/**
	 * 获取所有关键词(按被标记的次数从大到小排列) <BR>
	 * 注：调用此方法之前必须先调用initialize方法
	 * 
	 * @return 按被标记的次数从大到小排列的keyword的List, List中的keywords不会存在重复
	 */

	public List<KeyWord> getAllKeyWords(String classId);

	/**
	 * 获取被标记的所有块(按被标记的次数从大到小排列) <BR>
	 * 注：调用此方法之前必须先调用initialize方法
	 * 
	 * @return 按被标记的次数从大到小排列的标记块的列表 (全部)
	 * @throws IOException
	 */
	public List<EffectiveMarkRange> getMarkedRanges(String classId);

	/**
	 * 根据筛选关键词，获取含有指定筛选关键词的块 (按被标记的次数从大到小排列) <BR>
	 * 注：调用此方法之前必须先调用initialize方法
	 * 
	 * @param screenKeyWords 指定的筛选关键词。要求所有的筛选关键词 ∈ this.getAllKeyWords()
	 * @return 含有指定筛选关键词的块的所有列表 (按被标记的次数从大到小排列)
	 */
	public List<EffectiveMarkRange> getMarkedRanges(String classId, List<String> screenKeyWords);

}
