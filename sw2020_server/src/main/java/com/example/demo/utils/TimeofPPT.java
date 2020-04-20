package com.example.demo.utils;

import java.util.Date;

public class TimeofPPT {
  int order;
  int page;
  Date timeofPPT;
  String time;
  int doubtMarksCount=0;
  int careMarksCount=0;
  
  public TimeofPPT(int order, int page, Date timeofPPT, String time) {
    super();
    this.order = order;
    this.page = page;
    this.timeofPPT = timeofPPT;
    this.time = time;
  }
  public int getOrder() {
    return order;
  }
  public void setOrder(int order) {
    this.order = order;
  }
  public int getPage() {
    return page;
  }
  public void setPage(int page) {
    this.page = page;
  }
  public Date getTimeofPPT() {
    return timeofPPT;
  }
  public void setTimeofPPT(Date timeofPPT) {
    this.timeofPPT = timeofPPT;
  }
  public String getTime() {
    return time;
  }
  public void setTime(String time) {
    this.time = time;
  }
}
