package com.example.demo.utils;

import java.io.FileNotFoundException;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.utils.configReader.Configreader;

public class SyntaxAnalyzer {
  private final LtpFetcher fetcher;
  private final Configreader configreader;

  public SyntaxAnalyzer() throws FileNotFoundException {
    configreader = Configreader.reader("config.xml");
    String appID = configreader.readItem("APPID");
    String apiKey = configreader.readItem("LTPAPIKey");
    if (appID == null || apiKey == null) {
      throw new FileNotFoundException("There is no APPID or correspondAPIKey!");
    }
    fetcher = new LtpFetcher(appID, apiKey);
  }


  public String analyze(String text) throws IllegalArgumentException {
    JSONObject response = JSONObject.parseObject(fetcher.fetch("cws", text));
    JSONObject data = response.getJSONObject("data");
    if (!data.containsKey("word")) {
      throw new IllegalArgumentException(response.getString("desc"));
    }
    List<String> tokens = JSON.parseArray(data.getJSONArray("word").toJSONString(),String.class);
    response = JSONObject.parseObject(fetcher.fetch("dp", text));
    data = response.getJSONObject("data");
    if (!data.containsKey("dp")) {
      throw new IllegalArgumentException(response.getString("desc"));
    }
    List<JSONObject> nodes = JSON.parseArray(data.getJSONArray("dp").toJSONString(),JSONObject.class);
    String relationships = "";
    for(JSONObject object: nodes) {
      relationships = relationships + "\t" + object.get("relate");
    }
    relationships = relationships + "\n";
    String words = "";
    for(String word: tokens) {
      words = words + "\t" + word;
    }
    words = words + "\n";
    System.out.println(words);
    System.out.println(relationships);
    return "fuck";
  }
}
