import sys
from textrank4zh import TextRank4Keyword
class Extracter(object):
    #初始化
    def __init__(self):
        self.tr4w = TextRank4Keyword()

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
    extracter = Extracter()
    extracter.extract(sys.argv[1], sys.argv[2])
    a = input()
    while(a != "exit"):
        b = input()
        extracter.extract(a, b)
        a = input()