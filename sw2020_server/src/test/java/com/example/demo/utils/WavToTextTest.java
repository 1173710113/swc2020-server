package com.example.demo.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.domain.AlignResult;
import com.example.demo.exception.TransferException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class WavToTextTest {

	@Autowired
	private WavToTextUtil wavToTextUtil;
	
	
	@Test
	public void test() {
		String msg = "";
		try {
			msg = wavToTextUtil.getMessage("./resource/audio/xwlb.wav");
			System.out.println(msg);
		} catch (TransferException e) {
			e.printStackTrace();
		}

		AlignResult ar = new AlignResult("xwlb", msg);

		for (int i = 0; i < ar.getNumOfSentence(); i++) {
			System.out.println("begin: " + ar.getBeginTime(i));
			System.out.println("end: " + ar.getEndTime(i));
			System.out.println(ar.getSentence(i));
		}

		System.out.println("The text is: " + ar.getText());
	}

}
