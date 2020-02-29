package com.example.demo.utils.generate;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Template;
import com.example.demo.domain.AlignResult;
import com.example.demo.domain.RecordMark;
import com.example.demo.vo.EffectiveMarkRangeVO;
/**
 * 该类作为GenerateRange的实现类，根据所有标记的数据统计产生有效标记的句子范围，各句子范围间没有重复句子，范围间长短不一
 * 
 * @author 50136
 */
public class UnionRange extends GenerateRange{

  @Override
  public List<EffectiveMarkRangeVO> generateRange(AlignResult alignResult, List<RecordMark> marks) {
    // 标记总数
    int totalMark = marks.size();
    // list记录有效的mark对应的句子范围
    List<EffectiveMarkRangeVO> markRanges = new ArrayList<EffectiveMarkRangeVO>();
    String keyWordSentences = null;
    int tempMarkNum = 0;
    
    for (int j = 0; j < alignResult.getNumOfSentence() - (sentencesRange - 1); j = j + sentencesRange) {//直接跳到下一个range
      int start = alignResult.getBeginTime(j);
      int end = alignResult.getEndTime(j + 2);
      int rangeMarkNum = 0;
      for (int i = 0; i < totalMark; i++) {//统计mark数量
        if (start <= marks.get(i).getTime() && marks.get(i).getTime() <= end) {
          rangeMarkNum++;
        }
      }
      // 对于有效句子范围进行union统计，如：每3句为一组，考虑每组范围之间的重复，重复的放在一组中
      if (rangeMarkNum > 0) {//和前面进行union
        if (j <= backSentencesRange - 1) {//例如：当在0，1，2句时无法回溯到前3句，则只返回markrange的本来三句
          keyWordSentences = alignResult.getSentence(j) + alignResult.getSentence(j + 1) + alignResult.getSentence(j + 2);
          tempMarkNum = tempMarkNum + rangeMarkNum;
        } else {//否则需要加上回溯句子一同返回
          int start_j = j - backSentencesRange;
          if (!keyWordSentences.contains(alignResult.getSentence(start_j))) {//判断是否为一组中的第一个，是则需将前3句加入
            keyWordSentences = keyWordSentences + alignResult.getSentence(start_j) + alignResult.getSentence(start_j + 1) + alignResult.getSentence(start_j + 2)
            + alignResult.getSentence(start_j + 3) + alignResult.getSentence(start_j + 4) + alignResult.getSentence(start_j + 5);
          } else {
            keyWordSentences = keyWordSentences + alignResult.getSentence(j) + alignResult.getSentence(j + 1) + alignResult.getSentence(j + 2);
          }
          tempMarkNum = tempMarkNum + rangeMarkNum;
        }
      } else {//该组内无mark则不和前面union，从而避开重复
        EffectiveMarkRangeVO effectiveMarkRange = new EffectiveMarkRangeVO(keyWordSentences, tempMarkNum);
        markRanges.add(effectiveMarkRange);
        keyWordSentences = null;
        tempMarkNum = 0;
      }
    }
    
    if (tempMarkNum > 0) {//最后一组range未加入
      EffectiveMarkRangeVO effectiveMarkRange = new EffectiveMarkRangeVO(keyWordSentences, tempMarkNum);
      markRanges.add(effectiveMarkRange);
    }
    return markRanges;
  }

}
