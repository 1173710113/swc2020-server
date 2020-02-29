package com.example.demo.service.Imp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.example.demo.config.KeywordExtractorConfiguration;
import com.example.demo.config.MarkRangeConfiguration;
import com.example.demo.dao.KeyWordMapper;
import com.example.demo.dao.KeyWordMarkRangeRelationMapper;
import com.example.demo.dao.MarkRangeMapper;
import com.example.demo.dao.RecordMarkMapper;
import com.example.demo.domain.AlignResult;
import com.example.demo.domain.EffectiveMarkRange;
import com.example.demo.domain.KeyWord;
import com.example.demo.domain.RecordMark;
import com.example.demo.service.MarkCountService;
import com.example.demo.utils.KeywordExtractor;
import com.example.demo.utils.WavToTextUtil;
import com.example.demo.utils.generate.GenerateRange;
import com.example.demo.utils.generate.SeparateRange;
import com.example.demo.utils.sort.MarkRangeSort;
import com.example.demo.utils.sort.RatioSort;
import com.example.demo.vo.EffectiveMarkRangeVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 按照接口规定，可以按策略模式调用不同的实现方法以完成对于标记功能的service所需要的基本功能
 * 
 * @author 50136
 *
 */
@Service
@Slf4j
public class MarkCountServiceImp implements MarkCountService {

	@Autowired
	private MarkRangeConfiguration markRangeConfig;

	@Autowired
	private RecordMarkMapper recordMarkMapper;

	@Autowired
	private MarkRangeMapper markRangeMapper;

	@Autowired
	private KeyWordMapper keyWordMapper;

	@Autowired
	private KeyWordMarkRangeRelationMapper keyWordMarkRangeMapper;
	
	@Autowired
	private KeywordExtractorConfiguration keywordExtractorConfig;
	
	@Override
	public void initialize(String audioPath, String classId) throws IOException {
		// 有效范围List
		List<EffectiveMarkRangeVO> markedRanges = new ArrayList<>();

		AlignResult alignResult = WavToTextUtil.getAignResult(audioPath);
		GenerateRange generateRange = new SeparateRange();
		if (alignResult.getNumOfSentence() <= generateRange.sentencesRange - 1) {
			throw new RuntimeException("句子总数不足");
		}

		// 检索所有标记，返回结果按时间从小到大
		List<RecordMark> marks = recordMarkMapper.queryRecordMarkByClass(classId);

		// 生成句子范围
		markedRanges = generateRange.generateRange(alignResult, marks); // 生成所有块

		// 基于ratio对于结果排序
		MarkRangeSort markRangeSort = new RatioSort();
		markedRanges = markRangeSort.rangeSort(markedRanges);

		// 调用关键词方法，获取每个块的关键词列表
		for (EffectiveMarkRangeVO markRangeTemp : markedRanges) {
			EffectiveMarkRange markRange = new EffectiveMarkRange(null, markRangeTemp.getText(),
					markRangeTemp.getCount(), classId);

			// 向数据库添加标记块
			markRangeMapper.addMarkRange(markRange);

			// 抽取标记块的关键词
			KeywordExtractor keywordExtractor  = new KeywordExtractor(keywordExtractorConfig);
			List<String> keyWordList = keywordExtractor.keywordExtract(markRangeTemp.getText(),
					markRangeConfig.getMaxKeyword());
			keywordExtractor.close();

			// 将关键词添加进数据库
			for (String keyWordTemp : keyWordList) {
				// 查询数据库中是否已存在
				KeyWord keyWord = keyWordMapper.queryKeyWordByTextAndClass(keyWordTemp, classId);

				// 不存在
				if (keyWord == null) {
					// 添加关键词
					keyWord = new KeyWord(null, keyWordTemp, 0, classId);
					keyWordMapper.addKeyWord(keyWord);
				}
				
				log.info("keyword:" + JSON.toJSONString(keyWord));

				// 添加标记块与关键词的联系，并将关键词的count++
				keyWordMarkRangeMapper.addRelation(keyWord.getId(), markRange.getId());
				keyWordMapper.updateKeyWordCountPlus(keyWord.getId());
			}
		}
	}

	@Override
	public List<EffectiveMarkRange> getMarkedRanges(String classId, List<String> screenKeyWords) {
		Set<EffectiveMarkRange> set = new HashSet<>();
		for (String keyWordText : screenKeyWords) {
			set.retainAll(markRangeMapper.queryMarkRangeByClassAndText(classId, keyWordText));
		}
		List<EffectiveMarkRange> res = new ArrayList<EffectiveMarkRange>(set);
		Collections.sort(res, new Comparator<EffectiveMarkRange>() {

			public int compare(EffectiveMarkRange o1, EffectiveMarkRange o2) {
				return o2.getCount() - o1.getCount();
			}
		});
		return res;
	}

	@Override
	public List<EffectiveMarkRange> getMarkedRanges(String classId) {
		List<EffectiveMarkRange> list = markRangeMapper.queryMarkRangeByClass(classId);
		return list;
	}

	@Override
	public List<KeyWord> getAllKeyWords(String classId) {
		List<KeyWord> list = keyWordMapper.queryKeyWordByClass(classId);
		return list;
	}

}
