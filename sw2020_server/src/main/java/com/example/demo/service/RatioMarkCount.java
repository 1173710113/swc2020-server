package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.example.demo.domain.RecordMark;
import com.example.demo.exception.TransferException;
import com.example.demo.utils.KeywordExtractor;
import com.example.demo.utils.WavToTextUtil;
import com.example.demo.utils.generate.GenerateRange;
import com.example.demo.utils.generate.SeperateRange;
import com.example.demo.utils.generate.UnionRange;
import com.example.demo.utils.sort.MarkRangeSort;
import com.example.demo.utils.sort.RatioSort;
import com.example.demo.domain.AlignResult;
import com.example.demo.domain.EffectiveMarkRange;

public class RatioMarkCount implements MarkCount{
  private double effectiveRatio = 0;//先得出全部的标记句子范围——6句
  //有效范围和关键词之间的关系
  public Map<EffectiveMarkRange, List<String>> keyWordMap = new HashMap<EffectiveMarkRange, List<String>>();
  /**
   * compute effective marks
   * @param alignResult
   * @param marks
   * @throws IOException 
   */
  public List<EffectiveMarkRange> countingEffectiveMark(AlignResult alignResult, List<RecordMark> marks) throws IOException {
    GenerateRange generateRange = new UnionRange();
    if (alignResult.getNumOfSentence() <= generateRange.sentencesRange - 1) {
      throw new RuntimeException("句子总数不足");
    }
    
    //生成句子范围
    Collections.sort(marks);
    List<EffectiveMarkRange> effectiveMarkRanges = generateRange.generateRange(alignResult, marks);
    //调用关键词方法
    for (int i = 0; i < effectiveMarkRanges.size(); i++) {
      KeywordExtractor keywordExtractor = new KeywordExtractor();
      List<String> keyWordList = keywordExtractor.keywordExtract(effectiveMarkRanges.get(i).getRangeText(), 3);
      keyWordMap.put(effectiveMarkRanges.get(i), keyWordList);
      keywordExtractor.close();
    }
    //基于ratio对于结果排序
    MarkRangeSort markRangeSort = new RatioSort();
    List<EffectiveMarkRange> sortedEffectiveMarkRanges = markRangeSort.rangeSort(effectiveMarkRanges);
    return sortedEffectiveMarkRanges;
  }
  
  public static void main(String[] args) throws IOException {
    Random random = new Random();
    List<RecordMark> marks = new ArrayList<RecordMark>();
    for (int i = 0; i < 30; i++) {
      //77000
      int ran = random.nextInt(77000) + 1;
      RecordMark recordMark = new RecordMark(ran, "c");
      marks.add(recordMark);
    }
    AlignResult alignResult = WavToTextUtil.getAignResult("./resource/audio/xwlb.wav");
    RatioMarkCount ratioMarkCount = new RatioMarkCount();
    for (int i = 0; i < marks.size(); i++) {
      System.out.println("marks: " + marks.get(i).time);
    }
    List<EffectiveMarkRange> result = ratioMarkCount.countingEffectiveMark(alignResult, marks);
    for (int i = 0; i < result.size(); i++) {
      System.out.println("rangetext: " + result.get(i).getRangeText());
      System.out.println("marknum: " + result.get(i).getMarkNum());
      System.out.println("keyword: " + ratioMarkCount.keyWordMap.get(result.get(i)));
      System.out.println();
    }
    
  }
}
