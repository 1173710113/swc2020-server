package com.example.demo.service;

import java.util.Collections;
import java.util.List;
import com.example.demo.domain.RecordMark;
import com.example.demo.domain.AlignResult;

public class RatioMarkCount implements MarkCount{
  private int backSentencesRange = 3;
  private int sentencesRange = 3;
  private double effectiveRatio = 0.3;
  public void countingEffectiveMark(AlignResult alignResult, List<RecordMark> marks) {
    int totalMark = marks.size();
    String keyWordSentences;
    Collections.sort(marks);
    if (alignResult.getNumOfSentence() <= sentencesRange - 1) {
      throw new RuntimeException("句子总数不足");
    }
    for (int j = 0; j < alignResult.getNumOfSentence() - (sentencesRange - 1); j++) {
      int start = alignResult.getBeginTime(j);
      int end = alignResult.getEndTime(j + 2);
      int rangeMarkNum = 0;
      for (int i = 0; i < totalMark; i++) {
        if (start <= marks.get(i).time && marks.get(i).time <= end) {
          rangeMarkNum++;
        } else {
          break;
        }
      }
      if (rangeMarkNum/totalMark > effectiveRatio) {
        if (j <= backSentencesRange - 1) {//当在0，1，2句时无法回溯到前3句，则只返回markrange的本来三句
          keyWordSentences = alignResult.getSentence(j) + alignResult.getSentence(j + 1) + alignResult.getSentence(j + 2);
        } else {
          int start_j = j - (backSentencesRange - 1);
          keyWordSentences = alignResult.getSentence(start_j) + alignResult.getSentence(start_j + 1) + alignResult.getSentence(start_j + 2)
                                  + alignResult.getSentence(start_j + 3) + alignResult.getSentence(start_j + 4) + alignResult.getSentence(start_j + 5);
        }
        //调用关键词方法
      }
    }
    
    
  }
  
}
