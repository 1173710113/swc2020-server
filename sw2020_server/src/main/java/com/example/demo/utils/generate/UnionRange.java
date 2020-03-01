package com.example.demo.utils.generate;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.domain.AlignResult;
import com.example.demo.domain.RecordMark;
import com.example.demo.vo.EffectiveMarkRangeVO;

/**
 * 该类作为GenerateRange的实现类，根据所有标记的数据统计产生有效标记的句子范围，各句子范围间没有重复句子，范围间长短不一
 * 
 * @author 50136
 */
public class UnionRange extends GenerateRange {

//	private static int GROUP_LEN = 3;
	@Override
	public List<EffectiveMarkRangeVO> generateRange(AlignResult alignResult, List<RecordMark> marks) {
		// 标记总数
		int totalMark = marks.size();
		// list记录有效的mark对应的句子范围
		List<EffectiveMarkRangeVO> markRanges = new ArrayList<EffectiveMarkRangeVO>();
		StringBuilder keyWordSentences = new StringBuilder();
		int tempMarkNum = 0;
		int startTime = 0;
		int endTime = 0; // 记录markRange的开始和结束时间

		for (int j = 0; j < alignResult.getNumOfSentence() - (sentencesRange - 1); j = j + sentencesRange) {// 直接跳到下一个range
			int start = alignResult.getBeginTime(j);
			int end = alignResult.getEndTime(j + sentencesRange - 1);
			int rangeMarkNum = 0;
			for (int i = 0; i < totalMark; i++) {// 统计mark数量
				if (start <= marks.get(i).getTime() && marks.get(i).getTime() <= end) {
					rangeMarkNum++;
				}
			}
			// 对于有效句子范围进行union统计，如：每3句为一组，考虑每组范围之间的重复，重复的放在一组中

			if (rangeMarkNum > 0) { // 本组被标记

				tempMarkNum += rangeMarkNum;
				if (keyWordSentences.length() == 0) { // 新的一个MarkRange
					int k = j;
					if (j >= backSentencesRange) { // 如果不是第一组，往前回溯
						k = j - backSentencesRange;
					}
					startTime = alignResult.getBeginTime(k);

					for (; k < j + sentencesRange; k++) {
						keyWordSentences.append(alignResult.getSentence(k));
					}
					endTime = alignResult.getEndTime(k - 1);
				} else {
					int k;
					for (k = j; k < j + sentencesRange; k++) {
						keyWordSentences.append(alignResult.getSentence(k));
					}

					endTime = alignResult.getEndTime(k - 1);
				}

			} else {// 该组内无mark则不和前面union，从而避开重复
				EffectiveMarkRangeVO effectiveMarkRange = new EffectiveMarkRangeVO(keyWordSentences.toString(),
						tempMarkNum, startTime, endTime);
				markRanges.add(effectiveMarkRange);
				keyWordSentences = new StringBuilder();
				tempMarkNum = 0;
			}
		}

		if (tempMarkNum > 0) {// 最后一组range未加入
			EffectiveMarkRangeVO effectiveMarkRange = new EffectiveMarkRangeVO(keyWordSentences.toString(), tempMarkNum,
					startTime, endTime);
			markRanges.add(effectiveMarkRange);
		}
		return markRanges;
	}

}
