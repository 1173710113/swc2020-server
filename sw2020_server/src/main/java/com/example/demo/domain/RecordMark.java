package com.example.demo.domain;

public class RecordMark implements Comparable<RecordMark>{
  public int time; // 标记在音频文件中的时间戳(单位ms)
  public String mark;// 关注care——c 疑问doubt——d
  @Override
  public int compareTo(RecordMark o) {
    if (this.time > o.time) {
      return 1;
    }
    return 0;
  }
  public RecordMark(int time, String mark) {
    super();
    this.time = time;
    this.mark = mark;
  }
  
}
