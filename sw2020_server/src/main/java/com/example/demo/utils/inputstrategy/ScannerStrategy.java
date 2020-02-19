package com.example.demo.utils.inputstrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * implements IoStrategy using scanner.
 */
public class ScannerStrategy implements InputStrategy {
  private final List<String> lines;
  private int line = 0;
  private final int size;
  // Abstraction function:
  // AF(lines): An input instance reading from given path using scanner
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
  public ScannerStrategy(File file) throws FileNotFoundException {
    Scanner sc = new Scanner(file);
    lines = new ArrayList<>();
    while (sc.hasNextLine()) {
      lines.add(sc.nextLine());
    }
    size = lines.size();
    checkrep();
    sc.close();
  }

  @Override
  public String nextLine() {
    if (line == size) {
      return null;
    }
    line++;
    checkrep();
    return lines.get(line - 1);
  }

}
