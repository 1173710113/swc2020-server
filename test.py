import recognizers_suite
from recognizers_date_time import recognize_datetime, Culture

result = recognize_datetime("今天的课就上到这里，下周记得交作业, 下午五点上交，今天和明天天气都很好", Culture.Chinese)
print(result[0].resolution)
