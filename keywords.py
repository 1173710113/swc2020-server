import socket
import threading
from textrank4zh import TextRank4Keyword

def main():
    # 创建服务器套接字
    serversocket = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    # 获取本地主机名称
    host = "127.0.0.1"#socket.gethostname()
    # 设置一个端口
    port = 12345
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
            t = Extracter(clientsocket, 4096, "utf-8")#为每一个请求开启一个处理线程
            t.start()
            pass
        except Exception as identifier:
            print(identifier)
            pass
        pass
    serversocket.close()
    pass

class Extracter(threading.Thread):
    #初始化
    def __init__(self, socket, recvsize, encoding):
        threading.Thread.__init__(self)
        self.tr4w = TextRank4Keyword()
        self.socket = socket
        self.socket.settimeout(0.25)
        self.recvsize = recvsize
        self.encoding = encoding



    def run(self):
        print("开启新线程.....")
        flag = True
        while(flag):
            try:
                #接受数据
                msg = ''
                while True:
                    # 读取recvsize个字节
                    try:
                        rec = None
                        rec = self.socket.recv(self.recvsize)
                        if rec is None:
                            flag = False
                            break
                        else:
                            flag = True
                    except socket.timeout as exception:
                        if rec is None:
                            flag = False
                            break
                        else:
                            flag = True
                            print(flag)
                        pass
                    # 解码
                    msg += rec.decode(self.encoding)
                    # 文本接受是否完毕，因为python socket不能自己判断接收数据是否完毕，
                    # 所以需要自定义协议标志数据接受完毕
                    if msg.endswith('\n\n'):
                        msg=msg[:-2]
                        break
                if not flag:
                    continue
                commandLine = msg.split(" ")
                retMsg = self.extractSent(commandLine[0], int(commandLine[1]))
                print(retMsg)
                self.socket.send(retMsg)
                print("finished one fetch, waiting for the next call")
                continue
            except Exception as identifier:
                self.socket.send("500".encode(self.encoding))
                print(identifier)
                print("finished one fetch, waiting for the next call")
                break
        self.socket.close()
        print("任务结束.....")
        pass

    def extractSent(self, line, wordNum):
        self.tr4w.analyze(text=line, lower=True, window=4)
        keywords = ""
        for word in self.tr4w.get_keywords(wordNum, word_min_len=1):
            keywords = keywords + word.word + " " + str(word.weight) + "\n"
        return (keywords).encode()

    #处理一个句子的列表，将其分词
    def extract(self, inpath, oupath):
        text = []
        with open(inpath, "r", encoding="utf-8") as f:
            text = f.read()
        self.tr4w.analyze(text=text, lower=True, window=4)
        with open(oupath, "w", encoding="utf-8") as f:
            for word in self.tr4w.get_keywords(20, word_min_len=1):
                f.write(word.word + " " + str(word.weight) + "\n")
        print("successfully extract")

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