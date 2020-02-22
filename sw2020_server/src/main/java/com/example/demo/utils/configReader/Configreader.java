package com.example.demo.utils.configReader;

import java.io.FileNotFoundException;

/**
 * 配置文件的读取类，读取不同格式的配置文件时，能够调用不同的配置文件读取策略
 * 
 * @author EvanClark
 *
 */
public interface Configreader {
  /**
   * 新建一个读取配置文件的reader，读取不同格式配置文件时，使用不同的配置文件读取策略
   * 
   * @param filePath 读取的配置文件的相对路径
   * @return 返回配置文件的读取器
   * @throws FileNotFoundException 当没有找到对应的文件时
   */
  public static Configreader reader(String filePath) throws FileNotFoundException {
    return new ConfigReaderSimple(filePath);
  }

  /**
   * 读取配置文件中，指定项目的配置
   * 
   * @param itemName 需要获取的配置名
   * @return 对应的实际配置，当配置找不到时，返回null
   */
  public String readItem(String itemName);
}
