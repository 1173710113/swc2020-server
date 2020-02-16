package com.example.demo.domain;

public class EffectiveMarkRange implements Comparable<EffectiveMarkRange>{
  private String rangeText;
  private int markNum;
  

  public EffectiveMarkRange(String rangeText, int markNum) {
    super();
    this.rangeText = rangeText;
    this.markNum = markNum;
  }
  public String getRangeText() {
    return rangeText;
  }
  public void setRangeText(String rangeText) {
    this.rangeText = rangeText;
  }
  public int getMarkNum() {
    return markNum;
  }
  public void setMarkNum(int markNum) {
    this.markNum = markNum;
  }
  @Override
  public int compareTo(EffectiveMarkRange o) {
    return o.markNum - this.markNum;
  }
}
