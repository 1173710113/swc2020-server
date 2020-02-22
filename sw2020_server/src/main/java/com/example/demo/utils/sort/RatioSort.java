package com.example.demo.utils.sort;

import java.util.Collections;
import java.util.List;
import com.example.demo.domain.EffectiveMarkRange;
/**
 * 实现接口规定的一种重要度排序方法
 * @author 50136
 *
 */
public class RatioSort implements MarkRangeSort{

  @Override
  public List<EffectiveMarkRange> rangeSort(List<EffectiveMarkRange> effectiveMarkRanges) {
    Collections.sort(effectiveMarkRanges);
    return effectiveMarkRanges;
  }

}
