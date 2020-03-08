import synonyms
import socket
import threading

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

class Synonymser(threading.Thread):
    #初始化
    def __init__(self, socket, recvsize, encoding):
        threading.Thread.__init__(self)
        self.socket = socket
        self.socket.settimeout(0.25)
        self.recvsize = recvsize
        self.encoding = encoding

    def getsynonyms(self, word):
        ans = synonyms.nearby(word)[0]
        stringAns = ""
        for word in ans:
            stringAns = stringAns + ans + " "
        return stringAns

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
                if(commandLine[0] == "getsynonyms"):
                    retMsg = self.extractSent(commandLine[1])
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

if __name__ == '__main__':
    #ans = synonyms.nearby("天气")[0]
    s = synonyms()
    ans = s.nearby("天气")[0]
    stringAns = ""
    for word in ans:
        stringAns = stringAns + ans + " "
    print(stringAns)
    '''
    extracter = Extracter()
    extracter.extract(sys.argv[1], sys.argv[2])
    a = input()
    while(a != "exit"):
        b = input()
        extracter.extract(a, b)
        a = input()
    '''