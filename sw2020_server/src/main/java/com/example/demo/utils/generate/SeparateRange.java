package com.example.demo.utils.generate;

import java.util.ArrayList;
import java.util.List;
import com.example.demo.domain.AlignResult;
import com.example.demo.domain.RecordMark;
import com.example.demo.vo.EffectiveMarkRangeVO;
/**
 * 该类作为GenerateRange的实现类，根据所有标记的数据统计产生有效标记的句子范围，各句子范围间可能有重复句子，各范围长短一致
 * 
 * @author 50136
 */
public class SeparateRange extends GenerateRange{
  
  public List<EffectiveMarkRangeVO> generateRange(AlignResult alignResult, List<RecordMark> marks) {
    // 标记总数
    int totalMark = marks.size();
    // list记录有效的mark对应的句子范围
    List<EffectiveMarkRangeVO> markRanges = new ArrayList<EffectiveMarkRangeVO>();
    
    for (int j = 0; j < alignResult.getNumOfSentence() - (sentencesRange - 1); j = j + sentencesRange) {//直接跳到下一个range
      String keyWordSentences;
      int start = alignResult.getBeginTime(j);
      int end = alignResult.getEndTime(j + 2);
      int rangeMarkNum = 0;
      for (int i = 0; i < totalMark; i++) {//统计mark数量
        if (start <= marks.get(i).getTime() && marks.get(i).getTime() <= end) {
          rangeMarkNum++;
        }
      }
      // 对于有效句子范围进行separate统计，即每3句为一组，不考虑每组范围之间的重复
      if (j <= backSentencesRange - 1) {//当在0，1，2句时无法回溯到前3句，则只返回markrange的本来三句
        keyWordSentences = alignResult.getSentence(j) + alignResult.getSentence(j + 1) + alignResult.getSentence(j + 2);
      } else {
        int start_j = j - backSentencesRange;
        keyWordSentences = alignResult.getSentence(start_j) + alignResult.getSentence(start_j + 1) + alignResult.getSentence(start_j + 2)
                                + alignResult.getSentence(start_j + 3) + alignResult.getSentence(start_j + 4) + alignResult.getSentence(start_j + 5);
      }
      EffectiveMarkRangeVO effectiveMarkRange = new EffectiveMarkRangeVO(keyWordSentences, rangeMarkNum);
      markRanges.add(effectiveMarkRange);
    }
    
    return markRanges;
  }

}
