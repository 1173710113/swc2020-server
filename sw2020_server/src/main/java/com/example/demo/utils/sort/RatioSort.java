package com.example.demo.utils.sort;

import java.util.Collections;
import java.util.List;
import com.example.demo.domain.EffectiveMarkRange;

public class RatioSort implements MarkRangeSort{

  @Override
  public List<EffectiveMarkRange> rangeSort(List<EffectiveMarkRange> effectiveMarkRanges) {
    Collections.sort(effectiveMarkRanges);
    return effectiveMarkRanges;
  }

}
