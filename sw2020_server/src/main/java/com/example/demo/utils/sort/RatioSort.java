package com.example.demo.utils.sort;

import java.util.Collections;
import java.util.List;

import com.example.demo.vo.EffectiveMarkRangeVO;
/**
 * 实现接口规定的一种重要度排序方法
 * @author 50136
 *
 */
public class RatioSort implements MarkRangeSort{

  @Override
  public List<EffectiveMarkRangeVO> rangeSort(List<EffectiveMarkRangeVO> effectiveMarkRanges) {
    Collections.sort(effectiveMarkRanges);
    return effectiveMarkRanges;
  }

}
