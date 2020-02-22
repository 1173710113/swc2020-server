package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.dao.RecordMarkMapper;
import com.example.demo.domain.AlignResult;
import com.example.demo.domain.EffectiveMarkRange;
import com.example.demo.domain.RecordMark;
import com.example.demo.utils.KeywordExtractor;
import com.example.demo.utils.WavToTextUtil;
import com.example.demo.utils.generate.GenerateRange;
import com.example.demo.utils.generate.SeparateRange;
import com.example.demo.utils.sort.MarkRangeSort;
import com.example.demo.utils.sort.RatioSort;
/**
 * 按照接口规定，可以按策略模式调用不同的实现方法以完成对于标记功能的service所需要的基本功能
 * @author 50136
 *
 */
public class RatioMarkCount implements MarkCount {
    //	private double effectiveRatio = 0;// 先得出全部的标记句子范围——6句
	private static final int MAX_KEYWORD = 5;

	// 有效范围和关键词之间的关系
	private Map<EffectiveMarkRange, List<String>> keyWordsOfRangeMap = new HashMap<>();

	// 关键词和有效范围之间的关系(便于基于关键词的筛选)
	// (key:关键词 value:关键词所在的EffectiveMarkRange们)
	private Map<String, Set<EffectiveMarkRange>> rangesOfKeywordMap = new HashMap<>();

	// 关键词List, 获取
	private List<KeyWord> keyWords = new ArrayList<>();
	// 有效范围List
	private List<EffectiveMarkRange> markedRanges = new ArrayList<>();
	// 记录每个关键词的标记次数
	private Map<String, Integer> markedNumOfKeyWord = new HashMap<>();
	
	private String classId;
	
	@Autowired
	private RecordMarkMapper recordMarkMapper;
	
	public void setClassId(String classId) {
		this.classId = classId;
	}

	@Override
	public void initialize(AlignResult alignResult, List<RecordMark> marks) throws IOException {

		GenerateRange generateRange = new SeparateRange();
		if (alignResult.getNumOfSentence() <= generateRange.sentencesRange - 1) {
			throw new RuntimeException("句子总数不足");
		}

		// 生成句子范围
		Collections.sort(marks);
		markedRanges = generateRange.generateRange(alignResult, marks); // 生成所有块

		// 调用关键词方法，获取每个块的关键词列表
		for (int i = 0; i < markedRanges.size(); i++) {
			KeywordExtractor keywordExtractor = new KeywordExtractor();
			List<String> keyWordList = keywordExtractor.keywordExtract(markedRanges.get(i).getRangeText(), MAX_KEYWORD);
			keyWordsOfRangeMap.put(markedRanges.get(i), keyWordList);
			keywordExtractor.close();
		}

		// 基于ratio对于结果排序
		MarkRangeSort markRangeSort = new RatioSort();
		markedRanges = markRangeSort.rangeSort(markedRanges);

		// 构建关键词Map(key:关键词 value:关键词所在的EffectiveMarkRange们)

		for (Map.Entry<EffectiveMarkRange, List<String>> entry : keyWordsOfRangeMap.entrySet()) {
			for (String keyWord : entry.getValue()) {
				if (rangesOfKeywordMap.containsKey(keyWord)) {
					rangesOfKeywordMap.get(keyWord).add(entry.getKey());
					markedNumOfKeyWord.put(keyWord, markedNumOfKeyWord.get(keyWord) + entry.getKey().getMarkNum());
				} else {
					rangesOfKeywordMap.put(keyWord, new HashSet<>());
					markedNumOfKeyWord.put(keyWord, entry.getKey().getMarkNum());
				}
			}
		}

		// 构建按标记次数排序的关键词列表
		for (Map.Entry<String, Integer> entry : markedNumOfKeyWord.entrySet()) {
			keyWords.add(new KeyWord(entry.getKey(), entry.getValue()));
		}
		Collections.sort(keyWords); // 按标记次数从大到小排序

	}

	public List<EffectiveMarkRange> getMarkedRanges() {
		return new ArrayList<>(markedRanges);
	}

	@Override
	public List<String> getAllKeyWords() {
		List<String> res = new ArrayList<>();
		for (KeyWord k : keyWords) {
			res.add(k.str);
		}
		return res;
	}

	@Override
	public int getMarkedNumOfKeyWord(String keyWord) {
		return markedNumOfKeyWord.get(keyWord);
	}

	@Override
	public List<EffectiveMarkRange> getMarkedRanges(List<String> screenKeyWords) {
		Set<EffectiveMarkRange> set = new HashSet<>(rangesOfKeywordMap.get(screenKeyWords.get(0)));

		for (int i = 1; i < screenKeyWords.size(); i++) {
			set.retainAll(rangesOfKeywordMap.get(screenKeyWords.get(i)));
		}

		List<EffectiveMarkRange> res = new ArrayList<EffectiveMarkRange>(set);
		Collections.sort(res);
		return res;
	}

	private class KeyWord implements Comparable<KeyWord> {
		String str;
		int numOfMark;

		public KeyWord(String str, int numOfMark) {
			this.str = str;
			this.numOfMark = numOfMark;
		}

		@Override
		public int compareTo(KeyWord o) {
			return o.numOfMark - this.numOfMark;
		}

	}

	public static void main(String[] args) throws IOException {
		Random random = new Random();
		//List<RecordMark> marks = recordMarkMapper.queryRecordMarkById(classId) //从数据库读取recordMark
		List<RecordMark> marks = new ArrayList<RecordMark>();
		for (int i = 0; i < 30; i++) {
			// 77000
			int ran = random.nextInt(77000) + 1;
			RecordMark recordMark = new RecordMark(ran, "c");
			marks.add(recordMark);
		}
		
		AlignResult alignResult = WavToTextUtil.getAignResult("./resource/audio/xwlb.wav");
		RatioMarkCount ratioMarkCount = new RatioMarkCount();
		for (int i = 0; i < marks.size(); i++) {
			System.out.println("marks: " + marks.get(i).time);
		}

		ratioMarkCount.initialize(alignResult, marks);
		List<EffectiveMarkRange> result = ratioMarkCount.getMarkedRanges();
		for (int i = 0; i < result.size(); i++) {
			System.out.println("rangetext: " + result.get(i).getRangeText());
			System.out.println("marknum: " + result.get(i).getMarkNum());
			System.out.println("keyword: " + ratioMarkCount.keyWordsOfRangeMap.get(result.get(i)));
			System.out.println();
		}

		List<EffectiveMarkRange> screenRes = ratioMarkCount.getMarkedRanges(Arrays.asList("习近平"));
		System.out.println("筛选后：");
		for (int i = 0; i < screenRes.size(); i++) {
			System.out.println("rangetext: " + screenRes.get(i).getRangeText());
			System.out.println("marknum: " + screenRes.get(i).getMarkNum());
			System.out.println("keyword: " + ratioMarkCount.keyWordsOfRangeMap.get(screenRes.get(i)));
			System.out.println();
		}

		System.out.println("所有关键词:");
		System.out.println(ratioMarkCount.getAllKeyWords());

	}
}
