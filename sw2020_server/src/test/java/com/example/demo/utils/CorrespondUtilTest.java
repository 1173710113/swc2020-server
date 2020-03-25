package com.example.demo.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.utils.configReader.Configreader;
import com.example.demo.utils.inputstrategy.InputStrategy;

class CorrespondUtilTest {

  @Test
  void testSendJson() throws NumberFormatException, UnknownHostException, IOException {
    Configreader config = Configreader.reader("config.xml");
    CorrespondUtil corres =
        new CorrespondUtil(InetAddress.getByName(config.readItem("DateExtractorClient")),
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
  }

}
