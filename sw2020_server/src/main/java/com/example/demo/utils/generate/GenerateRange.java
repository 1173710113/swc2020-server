package com.example.demo.utils.generate;

import java.util.List;
import com.example.demo.domain.AlignResult;
import com.example.demo.domain.EffectiveMarkRange;
import com.example.demo.domain.RecordMark;

public abstract class GenerateRange {
  public int backSentencesRange = 3;
  public int sentencesRange = 3;
  /**
     *   根据所有标记产生所在句子范围
   * @param alignResult
   * @param marks need to be sorted according to time
   */
  public List<EffectiveMarkRange> generateRange(AlignResult alignResult, List<RecordMark> marks) {
    return null;
  }
}
