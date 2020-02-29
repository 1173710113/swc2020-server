package com.example.demo.utils;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.config.KeywordExtractorConfiguration;
import com.example.demo.service.MarkCountServiceTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Slf4j
class KeywordExtractorTest {

	@Autowired
	private KeywordExtractorConfiguration keywordExtractorConfig;

	@Test
	void testKeywordExtract() throws UnknownHostException, IOException {
		KeywordExtractor extractor = new KeywordExtractor(keywordExtractorConfig);
		String corpus = "那么，作为通信系统，如果我们给出一个通信系统的模型的话，通信系统主要包括信源和信宿。信源就是发生和产生信息的地方。信宿也就是信息要到达的地方。换句话说，信源是发送信息的，信宿是接受信息的。";
		List<String> keywords = extractor.keywordExtract(corpus, 5);
		for (String keyword : keywords) {
			System.out.println(keyword);
		}
		corpus = "作为通信系统，如果我们给出一个通信系统的模型的话，通信系统主要包括信源和信宿。信源就是发生和产生信息的地方。信宿也就是信息要到达的地方。换句话说，信源是发送信息的，信宿是接受信息的。";
		// keywords = extractor.keywordExtract(corpus, 5);
		// for (String keyword : keywords) {
		// System.out.println(keyword);
		// }
		extractor.close();
	}

}
