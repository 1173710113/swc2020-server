package com.example.demo.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import com.example.demo.exception.TransferException;

public interface WaveProcessService {
  /**
   * 使用讯飞服务获取转写结果
   * 
   * @param wavPath 文件路径
   * @return
   * @throws TransferException
   */
  public String waveConvertToText(String filePath) throws TransferException;

  /**
   * 调用讯飞转写服务, 对wavPath路径的.wav文件进行转写, 并对齐转写结果
   * 
   * @param wavPath .wav文件路径
   * @return 已对齐的转写结果
   * @throws TransferException 当服务器有问题、录音有问题等时候, 抛出该改转写异常, 包含任务id、错误码等
   */
  public void extractAlignResult(String filePath, String text, String classId);

  /**
   * 根据时间抽取服务的结果, 获取一个本类对象
   * 
   * @param jsonObject 时间抽取服务的结果
   * @throws ParseException
   */
  public void extractSchedule(String classId)
      throws UnknownHostException, IOException, ParseException;
}
