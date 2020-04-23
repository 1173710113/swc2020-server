#!/usr/bin/python
import socket
import threading
import gensim
import json
import jieba
from textrank4zh import TextRank4Keyword
from typing import List
model = gensim.models.Word2Vec.load("wiki.zh.text.model")

def main():
    # 创建服务器套接字
    serversocket = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    # 获取本地主机名称
    host = "127.0.0.1"#socket.gethostname()
    # 设置一个端口
    port = 12344
    # 将套接字与本地主机和端口绑定
    serversocket.bind((host,port))
    # 设置监听最大连接数
    serversocket.listen(5)
    # 获取本地服务器的连接信息
    myaddr = serversocket.getsockname()
    print("服务器地址:%s"%str(myaddr))
    # 循环等待接受客户端信息
    while True:
        # 获取一个客户端连接
        clientsocket,addr = serversocket.accept()
        print("连接地址:%s" % str(addr))
        try:
            t = Spliter(clientsocket, 4096, "utf-8")#为每一个请求开启一个处理线程
            t.start()
            pass
        except Exception as identifier:
            print(identifier)
            pass
        pass
    serversocket.close()
    pass

class Spliter(threading.Thread):
    #初始化
    def __init__(self, socket, recvsize, encoding):
        threading.Thread.__init__(self)
        self.tr4w = TextRank4Keyword()
        self.socket = socket
        self.socket.settimeout(0.25)
        self.recvsize = recvsize
        self.encoding = encoding
        self.TIMEOUTS = 5



    def run(self):
        print("开启新线程.....")
        i = 0 # counting for timeout times
        decodeFlag = True 
        print(i)
        try:
            #接受数据
            msg = ''
            msgTot = bytes()
            while (decodeFlag):
                # 读取recvsize个字节
                try:
                    rec = None
                    rec = self.socket.recv(self.recvsize)
                    print(len(rec))
                    if rec is None:
                        i = 0
                    else:
                        msgTot += rec
                except socket.timeout as exception:
                    print(str(i) + "-----------")
                    if rec is None:
                        i += 1
                        if(i == self.TIMEOUTS):
                            decodeFlag = False
                    else:
                        msgTot += rec
                    pass
                # 解码
                try:
                    if decodeFlag:
                        msg = msgTot.decode(self.encoding)
                        # 文本接受是否完毕，因为python socket不能自己判断接收数据是否完毕，
                        # 所以需要自定义协议标志数据接受完毕
                        if msg.endswith('\n\n'):
                            msg=msg[:-2]
                            decodeFlag = False
                            break
                except UnicodeError as unfinished:
                    pass
            commandLine = msg.split("\t\t")
            retMsg = self.process(commandLine[0], int(commandLine[1]), int(commandLine[2]))
            print(retMsg)
            self.socket.send(retMsg)
            print("finished one fetch, waiting for the next call")
        except Exception as identifier:
            #self.socket.send("500".encode(self.encoding))
            print(identifier)
            print("finished one fetch, waiting for the next call")
        self.socket.close()
        print("任务结束.....")
        pass

    def process(self, lines, keywordNum, synonyomsNum):
        self.tr4w.analyze(text=lines, lower=True, window=4)
        keywords = []
        for word in self.tr4w.get_keywords(keywordNum, word_min_len=1):
            keywords.append(word.word)
        tokens = jieba.lcut(lines)
        # extend keywords using genomism
        keywords.extend(self.synomnyons(keywords, synonyomsNum))
        kewords = list(set(kewords))
        ans = {}
        ans['tokens'] = tokens
        ans['keywords'] = keywords
        return json.dumps(ans).encode()

    def synomnyons(self, keywords: List[str], synonyomsNum: int)->List[str]:
        ans = []
        for keyword in keywords:
            try:
                similar = model.most_similar(keyword)
                for i in range(0, min(len(similar), synonyomsNum)):
                    if similar[i][0] in keywords:
                        ans.append(similar[i][0])
            except Exception as identifier:
                print(identifier)
                pass
        return ans

if __name__ == '__main__':
    main()
    '''
    extracter = Extracter()
    extracter.extract(sys.argv[1], sys.argv[2])
    a = input()
    while(a != "exit"):
        b = input()
        extracter.extract(a, b)
        a = input()
    '''