package com.example.demo.utils.generate;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Template;
import com.example.demo.domain.AlignResult;
import com.example.demo.domain.EffectiveMarkRange;
import com.example.demo.domain.RecordMark;

public class UnionRange extends GenerateRange{

  @Override
  public List<EffectiveMarkRange> generateRange(AlignResult alignResult, List<RecordMark> marks) {
    int totalMark = marks.size();
    List<EffectiveMarkRange> markRanges = new ArrayList<EffectiveMarkRange>();
    String keyWordSentences = null;
    int tempMarkNum = 0;
    
    for (int j = 0; j < alignResult.getNumOfSentence() - (sentencesRange - 1); j = j + sentencesRange) {//直接跳到下一个range
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
      if (rangeMarkNum > 0) {//和前面进行union
        if (j <= backSentencesRange - 1) {//当在0，1，2句时无法回溯到前3句，则只返回markrange的本来三句
          keyWordSentences = alignResult.getSentence(j) + alignResult.getSentence(j + 1) + alignResult.getSentence(j + 2);
          tempMarkNum = tempMarkNum + rangeMarkNum;
        } else {
          int start_j = j - (backSentencesRange - 1);
          if (!keyWordSentences.contains(alignResult.getSentence(start_j))) {//判断是否为一组中的第一个，是则需将前3句加入
            keyWordSentences = keyWordSentences + alignResult.getSentence(start_j) + alignResult.getSentence(start_j + 1) + alignResult.getSentence(start_j + 2)
            + alignResult.getSentence(start_j + 3) + alignResult.getSentence(start_j + 4) + alignResult.getSentence(start_j + 5);
          } else {
            keyWordSentences = keyWordSentences + alignResult.getSentence(j) + alignResult.getSentence(j + 1) + alignResult.getSentence(j + 2);
          }
          tempMarkNum = tempMarkNum + rangeMarkNum;
        }
      } else {//该组内无mark则不和前面union
        EffectiveMarkRange effectiveMarkRange = new EffectiveMarkRange(keyWordSentences, tempMarkNum);
        markRanges.add(effectiveMarkRange);
        keyWordSentences = null;
        tempMarkNum = 0;
      }
    }
    
    if (tempMarkNum > 0) {//最后一组range未加入
      EffectiveMarkRange effectiveMarkRange = new EffectiveMarkRange(keyWordSentences, tempMarkNum);
      markRanges.add(effectiveMarkRange);
    }
    return markRanges;
  }

}
