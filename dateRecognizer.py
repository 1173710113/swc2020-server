import recognizers_suite
from recognizers_date_time import recognize_datetime, Culture
import socket
import threading
import json


def main():
    # 创建服务器套接字
    serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # 获取本地主机名称
    host = "127.0.0.1"  # socket.gethostname()
    # 设置一个端口
    port = 12348
    # 将套接字与本地主机和端口绑定
    serversocket.bind((host, port))
    # 设置监听最大连接数
    serversocket.listen(5)
    # 获取本地服务器的连接信息
    myaddr = serversocket.getsockname()
    print("服务器地址:%s" % str(myaddr))
    # 循环等待接受客户端信息
    while True:
        # 获取一个客户端连接
        clientsocket, addr = serversocket.accept()
        print("连接地址:%s" % str(addr))
        try:
            t = DateRecognizer(clientsocket, 4096, "utf-8")  # 为每一个请求开启一个处理线程
            t.start()
            pass
        except Exception as identifier:
            print(identifier)
            pass
        pass
    serversocket.close()
    pass


class DateRecognizer(threading.Thread):
    # 初始化
    def __init__(self, socket, recvsize, encoding):
        threading.Thread.__init__(self)
        self.socket = socket
        self.socket.settimeout(0.25)
        self.recvsize = recvsize
        self.encoding = encoding
        self.TIMEOUTS = 5

    def dateRecognize(self, sentences):
        i = 0
        sentLen = len(sentences)
        marks = [0 for i in range(sentLen)]
        results = {}
        results['analyze'] = {}
        k = 0
        for sentence in sentences:
            tmpResult = {}
            tmpResult["sentence"] = sentence
            result = recognize_datetime(sentence, Culture.Chinese)
            if(len(result) is not 0):
                tmpResult['result'] = {}
                for j in range(len(result)):
                    tmpResult['result'][j] = {}
                    tmpResult['result'][j]['start'] = result[j].start
                    tmpResult['result'][j]['end'] = result[j].end
                    tmpResult['result'][j]['text'] = result[j].text
                    tmpResult['result'][j]['resolution'] = result[j].resolution['values'][0]
                results[k] = tmpResult
                results['analyze'][k] = sentence
                marks[i] = 1
                if(i > 0 and marks[i - 1] is 0):
                    marks[i - 1] = 1
                    results['analyze'][k] = sentences[i - 1] + results['analyze'][k]
                k+=1
            #elif(i > 0 and marks[i - 1] is 1):
                #    marks[i] = 1
                #    results['analyze'][k - 1] = results['analyze'][k - 1] + sentences[i]
            i += 1
        print(results)
        return json.dumps(results, ensure_ascii=False)

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
            print(msg)
            sentences = msg.strip(" ").split(" ")
            retMsg = self.dateRecognize(sentences)
            self.socket.send(retMsg.encode())
            print("finished one fetch, waiting for the next call")
        except Exception as identifier:
            self.socket.send("500".encode(self.encoding))
            print(identifier)
            print("finished one fetch, waiting for the next call")
        self.socket.close()
        print("任务结束.....")
        pass


if __name__ == '__main__':
    #ans = synonyms.nearby("天气")[0]
    main()
    '''
    s = DateRecognizer()
    sentences = []
    with open("syntaxTest.txt", "r", encoding='utf-8') as f:
        for sentence in f.readlines():
            sentences.append(sentence.strip("\n"))
    with open("output.txt", "w", encoding="utf-8") as f:
        f.write(s.dateRecognize(sentences) + "\n")
    with open("fuck.txt", "w", encoding="utf-8") as f:
        for i in range(10):
            f.write("a + \n")
            '''