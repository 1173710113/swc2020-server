package com.example.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.util.DigestUtils;

/**
 * 用于从讯飞云服务端获取LTP提供的NLP有关服务
 * 
 * @author EvanClark
 *
 */
public class LtpFetcher {
  private final String appID;
  private final String apiKey;
  private final String urlPrefix = "http://ltpapi.xfyun.cn/v1/";

  /**
   * 生成一个获取器，能够重复向讯飞云服务器请求对应的服务
   * 
   * @param appID 讯飞云服务的appID
   * @param apiKey 讯飞云服务的apiKey，通常，一个apiKey仅能请求一类服务
   */
  public LtpFetcher(String appID, String apiKey) {
    this.appID = appID;
    this.apiKey = apiKey;
  }

  /**
   * 发起一次LTP服务器的请求
   * 
   * @param func 请求的服务的对应function id，从讯飞云服务后台能够获取
   * @param text 需要进行操作的文本，文本的长度不应当长于500字节，即<=62个字
   * @return 讯飞云服务器返回的json格式的串
   */
  public String fetch(String func, String text) {
    String urlText = urlPrefix + func;
    String CurTime = String.valueOf(System.currentTimeMillis() / 1000);
    String xparam = "eyJ0eXBlIjoiZGVwZW5kZW50In0=";
    text = "text=" + text;
    String checkSum = DigestUtils.md5DigestAsHex((apiKey + CurTime + xparam).getBytes());
    HttpURLConnection connection = null;
    InputStream is = null;
    OutputStream os = null;
    BufferedReader br = null;
    String result = null;
    try {
      URL url = new URL(urlText);
      // 通过远程url连接对象打开连接
      connection = (HttpURLConnection) url.openConnection();
      // 设置连接请求方式
      connection.setRequestMethod("POST");
      // 设置连接主机服务器超时时间：15000毫秒
      connection.setConnectTimeout(15000);
      // 设置读取主机服务器返回数据超时时间：60000毫秒
      connection.setReadTimeout(60000);

      // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
      connection.setDoOutput(true);
      // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
      connection.setDoInput(true);
      connection.setRequestProperty("X-Appid", appID);
      connection.setRequestProperty("X-CurTime", CurTime);
      connection.setRequestProperty("X-Param", xparam);
      connection.setRequestProperty("X-CheckSum", checkSum);
      // 通过连接对象获取一个输出流
      os = connection.getOutputStream();
      // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
      os.write(text.getBytes());
      // 通过连接对象获取一个输入流，向远程读取
      if (connection.getResponseCode() == 200) {
        is = connection.getInputStream();
        // 对输入流对象进行包装:charset根据工作项目组的要求来设置
        br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        StringBuffer sbf = new StringBuffer();
        String temp = null;
        // 循环遍历一行一行读取数据
        while ((temp = br.readLine()) != null) {
          sbf.append(temp);
          sbf.append("\r\n");
        }
        result = sbf.toString();
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      // 关闭资源
      if (null != br) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (null != os) {
        try {
          os.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (null != is) {
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      // 断开与远程地址url的连接
      connection.disconnect();
    }
    return result;
  }
}
