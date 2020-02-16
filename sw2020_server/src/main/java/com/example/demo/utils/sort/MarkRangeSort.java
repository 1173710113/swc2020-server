package com.example.demo.utils.sort;

import java.util.List;
import com.example.demo.domain.EffectiveMarkRange;

public interface MarkRangeSort {
  /**
   * for 所有的range进行重要度排序
   * @param effectiveMarkRanges
   * @return
   */
  public List<EffectiveMarkRange> rangeSort(List<EffectiveMarkRange> effectiveMarkRanges);
}
