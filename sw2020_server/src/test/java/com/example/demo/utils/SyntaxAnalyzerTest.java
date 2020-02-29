package com.example.demo.utils;

import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Test;

class SyntaxAnalyzerTest {

  @Test
  void test() throws FileNotFoundException {
    SyntaxAnalyzer analyzer = new SyntaxAnalyzer();
    analyzer.analyze("请自班同学星期五在体育馆集合。");
  }

}
