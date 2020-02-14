package com.example.demo.service;

import java.util.Collections;
import java.util.List;
import com.example.demo.domain.RecordMark;
import com.example.demo.domain.AlignResult;

public class MarkCount {
  private int backSentencesRange = 3;
  private int sentencesRange = 3;
  private double effectiveRatio = 0.3;
  public void countingEffectiveMark(AlignResult alignResult, List<RecordMark> marks) {
    int totalMark = marks.size();
    Collections.sort(marks);
    
    
  }
  
  public static void main(String[] args) {
    
  }
}
