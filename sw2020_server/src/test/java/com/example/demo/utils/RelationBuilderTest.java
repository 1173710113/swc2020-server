package com.example.demo.utils;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.demo.config.GraphRelationConfiguration;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Slf4j
class RelationBuilderTest {

  @Autowired
  private GraphRelationConfiguration relationConfig;
  
  @Test
  void test() {
    try {
      RelationBuilder builder = new RelationBuilder(relationConfig);
      File file = new File("G:\\My_Document\\2019.12.29_SWC\\swc2020-server");
      Scanner s = new Scanner(file);
      String s1 = "";
      while (s.hasNextLine()) {
        s1 = s.nextLine();
      }
      builder.request(s1);
    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
