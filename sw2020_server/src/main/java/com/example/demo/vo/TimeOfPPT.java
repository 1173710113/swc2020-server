package com.example.demo.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeOfPPT {
  private int order;
  private int page;
  private long time;
  private int doubtMarksCount=0;
  private int careMarksCount=0;
  
  public TimeOfPPT(int order, int page, long time) {
	  this.order = order;
	  this.page = page;
	  this.time = time;
  }
  
  public void addDoubtMarkCount() {
	  doubtMarksCount++;
  }
  
  public void addCareMarkCount() {
	  careMarksCount++;
  }
  
}
