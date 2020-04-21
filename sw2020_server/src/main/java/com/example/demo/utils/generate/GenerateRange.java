package com.example.demo.utils.generate;

import java.util.List;

import com.example.demo.domain.AlignSentence;
import com.example.demo.domain.RecordMark;
import com.example.demo.vo.AlignResult;
import com.example.demo.vo.EffectiveMarkRangeVO;

/**
 * GenerateRange作为抽象类，表示根据所有标记的数据统计产生有效标记的句子范围
 * 
 * @author 50136
 */
public abstract class GenerateRange {
	public int backSentencesRange = 1;
	public int sentencesRange = 1;

	// note: 必须满足： backSentencesRange <= sentencesRange

	/**
	 * 根据所有标记产生所在句子范围
	 * 
	 * @param alignResult
	 * @param marks       need to be sorted according to time
	 */
	public List<EffectiveMarkRangeVO> generateRange(List<AlignSentence> alignSentences, List<RecordMark> marks) {
		return null;
	}
}
