package com.example.demo.utils;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Test;
import com.example.demo.utils.inputstrategy.InputStrategy;

class SyntaxAnalyzerTest {

  @Test
  void test() throws FileNotFoundException {
    SyntaxAnalyzer analyzer = new SyntaxAnalyzer();
    analyzer.analyze("请自班同学星期五在体育馆集合。");
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
