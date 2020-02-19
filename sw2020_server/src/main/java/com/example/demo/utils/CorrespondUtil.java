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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CorrespondUtil {
    private final Socket socket;
    private final int TIMEOUT = 50000;
    private final PrintStream out;
    private final BufferedReader br;

    /**
     * 通讯类，用于与某个指定端口地址的服务进行简单地通讯，发送信息并获得返回值
     * @param clientAddress 客户机的地址
     * @param serverAddress 对应服务的地址
     * @param serverPort 服务的端口
     * @param clientPort 客户机的端口
     * @throws IOException
     */
    public CorrespondUtil(InetAddress clientAddress, InetAddress serverAddress, int serverPort, int clientPort)
            throws IOException {
        socket = new Socket(serverAddress, serverPort);
        socket.setSoTimeout(TIMEOUT);
        OutputStream os = socket.getOutputStream();
        out = new PrintStream(os);
        InputStream is = socket.getInputStream();
        br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        System.out.println("initiate the client service");
    }

    /**
     * 发送数据，并获得返回值的键值对
     * @param data 发送数据的字符串
     * @return 服务返回值的键值对
     * @throws IOException
     */
    public List<String> send(String data) throws IOException {
        out.print(data + "\n\n");
        String tmp;
        StringBuilder tmpString = new StringBuilder();
        while ((tmp = br.readLine()) != null) {
            System.out.println(tmp);
            System.out.println("fuck");
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
     * 关闭与python关键词抽取服务的连接
     * @throws IOException 
     */
    public void close() throws IOException {
        out.close();
        br.close();
        socket.close();
    }
}
