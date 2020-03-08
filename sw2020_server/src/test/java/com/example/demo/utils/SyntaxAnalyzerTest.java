package com.example.demo.utils;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Test;
import com.example.demo.utils.inputstrategy.InputStrategy;

class SyntaxAnalyzerTest {

  void test() throws FileNotFoundException {
    SyntaxAnalyzer analyzer = new SyntaxAnalyzer();
    analyzer.analyze("这里为什么要引入字符串表，课下留给同学们思考。");
  }
  
  @Test
  void testFile() throws FileNotFoundException {
    SyntaxAnalyzer analyzer = new SyntaxAnalyzer();
    File file = new File("syntaxTest.txt");
    InputStrategy input = InputStrategy.input(file);
    String tmp;
    while ((tmp = input.nextLine()) != null) {
      analyzer.analyze(tmp);
    }
  }

}
