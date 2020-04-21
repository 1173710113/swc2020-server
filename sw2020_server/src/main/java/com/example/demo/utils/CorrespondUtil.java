package com.example.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 通讯辅助类，用于建立关键词抽取类与python服务之间的连接，并接收服务器传回的返回值，进行处理得到关键词的列表
 * 
 * @author EvanClark
 *
 */
@Slf4j
public class CorrespondUtil {
  private final Socket socket;
  private final int TIMEOUT = 50000;
  private final PrintStream out;
  private final BufferedReader br;

  /**
   * 通讯类，用于与某个指定端口地址的服务进行简单地通讯，发送信息并获得返回值
   * 
   * @param clientAddress 客户机的地址
   * @param serverAddress 对应服务端的地址
   * @param serverPort 服务的端口， 应当为1-65535
   * @param clientPort 客户机的端口， 应当为1-65535
   * @throws IOException 当创建socket时发生了I/O错误
   */
  public CorrespondUtil(InetAddress clientAddress, InetAddress serverAddress, int serverPort,
      int clientPort) throws IOException {
    log.info("init correspond");
    System.out.println(serverAddress.toString());
    System.out.println(serverPort);
    this.socket = new Socket(serverAddress, serverPort);
    this.socket.setSoTimeout(TIMEOUT);
    OutputStream os = socket.getOutputStream();
    this.out = new PrintStream(os);
    InputStream is = socket.getInputStream();
    this.br = new BufferedReader(new InputStreamReader(is, "utf-8"));
  }

  /**
   * 发送句子的列表，经过时间抽取服务处理，获得对应的json返回结果
   * 
   * @param data 需要抽取时间的句子列表
   * @return 返回结果的json串
   * @throws IOException 当I/O错误发生
   */
  public JSONObject sendJson(List<String> data) throws IOException {
    String sentences = "";
    for (String sentece : data) {
      sentences = sentences + " " + sentece;
    }
    out.print(sentences + "\n\n");
    String tmp;
    StringBuilder tmpString = new StringBuilder();
    while ((tmp = br.readLine()) != null) {
      tmpString.append(tmp + "\n");
    }
    tmp = tmpString.toString();
    System.out.println(tmp);
    JSONObject response = JSONObject.parseObject(tmp);
    return response;
  }

  /**
   * 发送数据，并获得返回值的键值对
   * 
   * @param data 需要发送的数据的字符串
   * @return 服务返回值的列表， 返回抽取的关键词的列表
   * @throws IOException 当读取服务返回的数据时发生了错误
   */
  public List<String> send(String data) throws IOException {
    out.print(data + "\n\n");
    String tmp;
    StringBuilder tmpString = new StringBuilder();
    while ((tmp = br.readLine()) != null) {
      System.out.println(tmp);
      tmpString.append(tmp + "\n");
    }
    String[] ranks = tmpString.toString().split("\n");
    List<String> keywords = new ArrayList<>();
    for (String rank : ranks) {
      if (rank == "") {
        continue;
      }
      String[] ranking = rank.split(" ");
      keywords.add(ranking[0]);
    }
    return keywords;
  }

  /**
   * 给钟哥发文章，从他那里得到提取的关键词、分词结果
   * 
   * @param msg 整个文章
   * @param maxKeywordNum 要抽取的最大关键词的数量
   * @param maxSynonymNum 每个关键词的最大近义词数量
   * @return Json串，包含了分词结果和关键词
   * @throws IOException
   */
  public JSONObject sendMsgGetKeywords(String msg, int maxKeywordNum, int maxSynonymNum)
      throws IOException {
    String data = msg + "\t\t" + maxKeywordNum + "\t\t" + maxSynonymNum + "\n\n";
    out.print(data);
    String tmp;
    StringBuilder tmpString = new StringBuilder();
    while ((tmp = br.readLine()) != null) {
      tmpString.append(tmp + "\n");
    }
    tmp = tmpString.toString();
    System.out.println(tmp);
    JSONObject response = JSONObject.parseObject(tmp);
    return response;
  }

  /**
   * 关闭与python关键词抽取服务的连接
   * 
   * @throws IOException 关闭与服务的连接时出现I/O错误
   */
  public void close() throws IOException {
    out.close();
    br.close();
    socket.close();
  }

	/*public static void main(String[] args) {
		String test = "{\"tokens\": [\"111\", \"222\"], \"keywords\": [\"fff\", \"fefe\"]}";
		JSONObject revJS = JSONObject.parseObject(test);
		List<String> tokens = JSONObject.parseArray(revJS.getJSONArray("tokens").toJSONString(), String.class);
		Set<String> keywords = new HashSet<>(
				JSONObject.parseArray(revJS.getJSONArray("keywords").toJSONString(), String.class));
		System.out.println(tokens.get(0));
	}*/
 /* public static void main(String[] args) {
    String test =
        "{\"同学你好！\",\"我是来自哈尔滨工业大学的聂兰顺，\",\"很高兴能够和大家一起学习计算机网络这个课程。\",\"我们这一章\",\"来一起学习应用层。\",\"我们已经知道 internet的话，整个是它的体系结构是符合TCPIp协议站的。\",\"应用层在什么地方？我们说它就在协议站的最上层。\",\"这一讲，\",\"或者说这一章我们会学习哪些内容呢？\",\"我们首先快速的带领大家来看一下，有一个全局的认识。\",\"首先我们会来学习\",\"网络应用的体系结构，\",\"我们来认识网络应用有哪几种体系结构，\",\"它和单机应用有哪些不一样的地方？\",\"第2个方面我们要来一起学习一下，一起来分析一下。\",\"网络应用对底层的，我们说对传输层、网络层、链路层以及物理层，\",\"由他们构成的网络基础设施，\",\"有哪些服务方面的需求，我们从可靠性带宽、\",\"食盐等方面进行分析。\",\"然后的话我们快速的看一下 internet的传输层提供了什么样的\",\"我们说传输服务模型，当然不是我们这一章的重点，\",\"我们在探索原理的时候会讲到，但在这里我们会先来宏观的看一下，\",\"然后的话我们这一章非常重要的，\",\"我们会和大家一起来具体的学习几个网络应用，特别是学习网络应用的核心，也就是应用层的协议。\",\"我们会学习web应用以及HTTP协议，\",\"我们会学习也没有应用，是一个典型的设计，多种应用层协议的这样一种应用，所以的话在这里面我们会看到有smtp协议、\",\"pop协议、mf协议等等。\",\"我们也会学习DNS。\",\"这个应用也许是大家比较陌生的，\",\"它是一种非常典型的在应用层实现网络核心功能的这样一种东西。\",\"另外我们也会学习p to p这个方面的应用。\",\"好，然后的话最后一个方面是我们如何来开发网络应用。\",\"所以我们会讲到扫黑的编程，也就是说我们如何利用扫黑的编程\",\"来开发简单的网络应用是我们要学习的。\",\"有了这样一个整个的学习过程之后，我们相信大家一我们对网络层\",\"对网络应用的构成，\",\"网络应用的协议，\",\"应该会有一个基本的认识和了解。\",\"另外我们也希望通过这样的学习，大家能够初步掌握构造网络应用的这种知识和技能。\",\"好，让我们一起开始这一章的学习。\"]"
            + "}";
    JSONObject revJS = JSONObject.parseObject(test);
    List<String> tokens =
        JSONObject.parseArray(revJS.getJSONArray("tokens").toJSONString(), String.class);
    Set<String> keywords = new HashSet<>(
        JSONObject.parseArray(revJS.getJSONArray("keywords").toJSONString(), String.class));
    System.out.println(tokens.get(0));
  }*/
}
