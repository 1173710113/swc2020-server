package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import com.example.demo.domain.AlignResult;
import com.example.demo.domain.EffectiveMarkRange;
import com.example.demo.domain.RecordMark;

public interface MarkCount {
  
  public List<EffectiveMarkRange> countingEffectiveMark(AlignResult alignResult, List<RecordMark> marks) throws IOException;
}
