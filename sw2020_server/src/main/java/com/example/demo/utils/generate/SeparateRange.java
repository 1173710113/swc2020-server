package com.example.demo.utils.generate;

import java.util.ArrayList;
import java.util.List;
import com.example.demo.domain.AlignResult;
import com.example.demo.domain.EffectiveMarkRange;
import com.example.demo.domain.RecordMark;

public class SeparateRange extends GenerateRange{
  
  public List<EffectiveMarkRange> generateRange(AlignResult alignResult, List<RecordMark> marks) {
    int totalMark = marks.size();
    List<EffectiveMarkRange> markRanges = new ArrayList<EffectiveMarkRange>();
    
    for (int j = 0; j < alignResult.getNumOfSentence() - (sentencesRange - 1); j = j + sentencesRange) {//直接跳到下一个range
      String keyWordSentences;
      int start = alignResult.getBeginTime(j);
      int end = alignResult.getEndTime(j + 2);
      int rangeMarkNum = 0;
      for (int i = 0; i < totalMark; i++) {//统计mark数量
        if (start <= marks.get(i).time && marks.get(i).time <= end) {
          rangeMarkNum++;
        }
      }
      if (j <= backSentencesRange - 1) {//当在0，1，2句时无法回溯到前3句，则只返回markrange的本来三句
        keyWordSentences = alignResult.getSentence(j) + alignResult.getSentence(j + 1) + alignResult.getSentence(j + 2);
      } else {
        int start_j = j - backSentencesRange;
        keyWordSentences = alignResult.getSentence(start_j) + alignResult.getSentence(start_j + 1) + alignResult.getSentence(start_j + 2)
                                + alignResult.getSentence(start_j + 3) + alignResult.getSentence(start_j + 4) + alignResult.getSentence(start_j + 5);
      }
      EffectiveMarkRange effectiveMarkRange = new EffectiveMarkRange(keyWordSentences, rangeMarkNum);
      markRanges.add(effectiveMarkRange);
    }
    
    return markRanges;
  }

}
