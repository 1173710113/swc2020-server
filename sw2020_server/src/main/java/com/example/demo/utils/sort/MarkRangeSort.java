package com.example.demo.utils.sort;

import java.util.List;

import com.example.demo.vo.EffectiveMarkRangeVO;
/**
 * 规定了对于range进行重要度排序的工具类的基本方法
 * @author 50136
 *
 */
public interface MarkRangeSort {
  /**
   * for 所有的range进行重要度排序
   * @param effectiveMarkRanges
   * @return
   */
  public List<EffectiveMarkRangeVO> rangeSort(List<EffectiveMarkRangeVO> effectiveMarkRanges);
}
