package com.example.demo.utils;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.RecordMark;

public class MarkwithPPT {
  public List<TimeofPPT> marksPPT(List<RecordMark> marks, File timeofppt) throws IOException, ParseException {
    String content= FileUtils.readFileToString(timeofppt,"UTF-8");
    List<TimeofPPT> timeofPPTs = JSON.parseArray(content, TimeofPPT.class);
    
    for (int i = 0; i < timeofPPTs.size(); i++) {
      TimeofPPT ppt = timeofPPTs.get(i);
      SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
      ppt.setTimeofPPT(sdf2.parse(ppt.getTime()));
    }

    for (RecordMark recordMark : marks) {
      Date markDate = new Date();
      markDate.setTime(recordMark.getTime());
      int add = 0;
      for (TimeofPPT timeofPPT2 : timeofPPTs) {
        if (!markDate.after(timeofPPT2.getTimeofPPT())) {
          if (recordMark.getMark().equals("care")) {
            timeofPPT2.careMarksCount++;
          } else{
            timeofPPT2.doubtMarksCount++;
          }
          add = 1;
          break;
        }
      }
      if (add == 0) {
        if (recordMark.getMark().equals("care")) {
          timeofPPTs.get(timeofPPTs.size() - 1).careMarksCount++;
        } else{
          timeofPPTs.get(timeofPPTs.size() - 1).doubtMarksCount++;
        }
        
      }
    }
    
    return timeofPPTs;
  }
}
