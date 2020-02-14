package com.example.demo.service;

import java.util.List;
import com.example.demo.domain.AlignResult;
import com.example.demo.domain.RecordMark;

public interface MarkCount {
  /**
   * compute effective marks
   * @param alignResult
   * @param marks
   */
  public void countingEffectiveMark(AlignResult alignResult, List<RecordMark> marks);
}
