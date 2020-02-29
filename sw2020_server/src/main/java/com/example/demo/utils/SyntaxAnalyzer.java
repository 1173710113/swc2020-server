package com.example.demo.utils;

import java.io.FileNotFoundException;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.utils.configReader.Configreader;

/**
 * 依存句法分析器，能够对length<=62的句子进行依存句法分析
 * 
 * @author EvanClark
 *
 */
public class SyntaxAnalyzer {
  private final LtpFetcher fetcher;
  private final Configreader configreader;

  /**
   * 生成一个依存句法分析器的实例
   * 
   * @throws FileNotFoundException 当没有找到对应的配置文件，或者appid， apiKey
   */
  public SyntaxAnalyzer() throws FileNotFoundException {
    configreader = Configreader.reader("config.xml");
    String appID = configreader.readItem("APPID");
    String apiKey = configreader.readItem("LTPAPIKey");
    if (appID == null || apiKey == null) {
      throw new FileNotFoundException("There is no APPID or correspondAPIKey!");
    }
    fetcher = new LtpFetcher(appID, apiKey);
  }


  /**
   * 对输入文本进行依存句法分析
   * @param text 需要进行依存句法分析的文本，length<=62
   * @return 得到的依存句法分析结果
   * @throws IllegalArgumentException 当输入的文本不满足讯飞云服务的要求
   */
  public String analyze(String text) throws IllegalArgumentException {
    JSONObject response = JSONObject.parseObject(fetcher.fetch("cws", text));
    JSONObject data = response.getJSONObject("data");
    if (!data.containsKey("word")) {
      throw new IllegalArgumentException(response.getString("desc"));
    }
    List<String> tokens = JSON.parseArray(data.getJSONArray("word").toJSONString(), String.class);
    response = JSONObject.parseObject(fetcher.fetch("dp", text));
    data = response.getJSONObject("data");
    if (!data.containsKey("dp")) {
      throw new IllegalArgumentException(response.getString("desc"));
    }
    List<JSONObject> nodes =
        JSON.parseArray(data.getJSONArray("dp").toJSONString(), JSONObject.class);
    String relationships = "relations:";
    String fathers = "parents:";
    for (JSONObject object : nodes) {
      relationships = relationships + "\t" + object.get("relate");
      fathers = fathers + "\t" + object.get("parent");
    }
    String words = "tokens:\t";
    String counts = "index:\t";
    int i = 0;
    for (String word : tokens) {
      counts = counts + "\t" + i;
      i++;
      words = words + "\t" + word;
    }
    System.out.println(words);
    System.out.println(counts);
    System.out.println(fathers);
    System.out.println(relationships);
    System.out.println("-------------------------------------------------------------");

    return "fuck";
  }
}
