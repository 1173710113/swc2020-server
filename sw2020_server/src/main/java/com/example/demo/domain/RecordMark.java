package com.example.demo.domain;

public class RecordMark implements Comparable<RecordMark>{
  public int time; // 标记在音频文件中的时间戳(单位ms)
  public String mark;// 关注care 疑问doubt
  @Override
  public int compareTo(RecordMark o) {
    if (this.time > o.time) {
      return 1;
    }
    return 0;
  }
  
}
