package com.example.demo.utils.inputstrategy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * implements IoStrategy using BufferedReader.
 */
public class BufferStrategy implements InputStrategy {
  private final List<String> lines;
  private int line = 0;
  private final int size;

  // Abstraction function:
  // AF(lines): An input instance reading from given path using BufferedReader
  // Representation invariant:
  // line <= size
  // Safety from rep exposure:
  // all fields are private and final, lines will only be produced once

  /*
   * Checking rep invariant.
   */
  public void checkrep() {
    assert line <= size;
  }

  /**
   * Reading input stream from file path.
   * 
   * @param file the input file path
   * @throws FileNotFoundException when there is no such file
   */
  public BufferStrategy(File file) throws FileNotFoundException {
    BufferedReader sc = new BufferedReader(new FileReader(file));
    String tmp;
    lines = new ArrayList<>();
    try {
      while ((tmp = sc.readLine()) != null) {
        lines.add(tmp);
      }
      size = lines.size();
    } catch (IOException e) {
      throw new FileNotFoundException();
    }
    try {
      sc.close();
    } catch (IOException e) {
      throw new FileNotFoundException();
    }
  }

  @Override
  public String nextLine() {
    if (line == size) {
      return null;
    }
    line++;
    return lines.get(line - 1);
  }
}
