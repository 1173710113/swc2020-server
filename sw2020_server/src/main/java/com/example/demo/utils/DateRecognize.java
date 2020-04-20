package com.example.demo.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.TimeExtractResult;
import com.example.demo.exception.MyException;
import com.example.demo.utils.SyntaxAnalyzer;
import com.example.demo.utils.configReader.Configreader;
import com.example.demo.utils.inputstrategy.InputStrategy;


public class DateRecognize {
  
  public String recognize(String text) throws FileNotFoundException {
    SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer();
    List<Set<String>> analyzeList = syntaxAnalyzer.analyze(text);
    Set<String> vOBSet = new HashSet<String>();
    Set<String> dBLSet = new HashSet<String>();
    Set<String> cOOSet = new HashSet<String>();
    Set<String> sBVSet = new HashSet<String>();
    //DBL --> 同学、
    dBLSet.add("同学");
    dBLSet.add("学生");
    //VOB --> 集合、实验、思考、交流、作业、预习、内容、
    //小测、报告、审核、参加、回去、改进、改、参与
    BufferedReader vobReader = new BufferedReader(new FileReader("./VOB.txt"));
    String vobString;
    try {
      while ((vobString = vobReader.readLine()) != null) {
        String[] vobStrings = vobString.split("、");
        for (int i = 0; i < vobStrings.length; i++) {
          vOBSet.add(vobStrings[i]);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    //COO --> 交、留给、想到、改、推迟、提前、安装、预习、回去、举行、领
    BufferedReader cooReader = new BufferedReader(new FileReader("./COO.txt"));
    String cooString;
    try {
      while ((cooString = cooReader.readLine()) != null) {
        String[] cooStrings = cooString.split("、");
        for (int i = 0; i < cooStrings.length; i++) {
          cOOSet.add(cooStrings[i]);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    //SBV --> 同学、作业、日期、教室、地点
    sBVSet.add("同学");
    sBVSet.add("学生");
    sBVSet.add("作业");
    sBVSet.add("功课");
    sBVSet.add("任务");
    sBVSet.add("日期");
    sBVSet.add("限期");
    sBVSet.add("时间");
    sBVSet.add("教室");
    sBVSet.add("地点");
    
//    Set<String> lessVOBSet = new HashSet<String>();
//    for(String vob : analyzeList.get(0)) {
//      if (vob.length() > 2) {
//        vob = vob.substring(0, 2);
//      }
//      lessVOBSet.add(vob);
//    }
    
    analyzeList.get(0).retainAll(vOBSet);
    analyzeList.get(1).retainAll(dBLSet);
    analyzeList.get(2).retainAll(cOOSet);
    analyzeList.get(3).retainAll(sBVSet);
//    lessVOBSet.retainAll(vOBSet);
    
    String results;
    if (analyzeList.get(0).size() > 0) {//vob类型含有关键词
      for (String vob: analyzeList.get(0)) {
        results = syntaxAnalyzer.fatherMap.get(vob) + " " + vob + " ";
        return results;
      }
    }
    //SBV和COO类型含有关键词
    if (analyzeList.get(3).size() > 0 && analyzeList.get(2).size() > 0) {
      for (String sbv: analyzeList.get(3)) {
        for (String coo: analyzeList.get(2)) {
          results = sbv + " " + coo + " ";
          return results;
        }
      }
    } 
    //DBL和COO类型含有关键词
    if (analyzeList.get(1).size() > 0 && analyzeList.get(2).size() > 0) {
      for (String dbl: analyzeList.get(1)) {
        for (String coo: analyzeList.get(2)) {
          results = dbl + " " + coo + " ";
          return results;
        }
      }
    }
    return null;
  }
  
  public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, ParseException {
    Configreader config = Configreader.reader("config.xml");
    CorrespondUtil corres = new CorrespondUtil(InetAddress.getByName(config.readItem("DateExtractorClient")),
            InetAddress.getByName(config.readItem("DateExtractorServer")),
            Integer.parseInt(config.readItem("DateExtractorServerPort")),
            Integer.parseInt(config.readItem("DateExtractorClientPort")));
    InputStrategy inputStrategy = InputStrategy.input(new File("../syntaxTest.txt"));
    List<String> list = new ArrayList<String>();
    String tmp;
    while ((tmp = inputStrategy.nextLine()) != null) {
        list.add(tmp);
    }
    JSONObject obj = corres.sendJson(list);
    
    TimeExtractResult ter = new TimeExtractResult(obj);
    DateRecognize dateRecognize = new DateRecognize();
    
    for (int i = 0; i < ter.getNumOfAnalyze(); i++) {
        System.out.println("sentence = " + ter.getAnalyze(i));
        System.out.println("Date = " + ter.getDate(i));
        System.out.println(dateRecognize.recognize(ter.getAnalyze(i)));
    }
    
    
    
//    BufferedReader testReader = new BufferedReader(new FileReader("./syntaxTest.txt"));
//    String testString;
//    try {
//      while ((testString = testReader.readLine()) != null) {
//        System.out.println(testString);
//        System.out.println(dateRecognize.recognize(testString));
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
    
//    System.out.println(dateRecognize.recognize("1999年，世界上最强的钟哥诞生了。世界从此陷入了钟哥的通知。"));
  }
}
