package com.example.demo.utils.generate;

import java.util.List;
import com.example.demo.domain.AlignResult;
import com.example.demo.domain.RecordMark;
import com.example.demo.vo.EffectiveMarkRangeVO;
/**
 * GenerateRange作为抽象类，表示根据所有标记的数据统计产生有效标记的句子范围
 * 
 * @author 50136
 */
public abstract class GenerateRange {
  public int backSentencesRange = 3;
  public int sentencesRange = 3;
  /**
   *   根据所有标记产生所在句子范围
   *   
   * @param alignResult
   * @param marks need to be sorted according to time
   */
  public List<EffectiveMarkRangeVO> generateRange(AlignResult alignResult, List<RecordMark> marks) {
    return null;
  }
}
