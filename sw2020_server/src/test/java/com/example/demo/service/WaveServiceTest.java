package com.example.demo.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class WaveServiceTest {

	@Autowired
	private WaveProcessService waveProcessService;
	@Test
	public void test() throws UnknownHostException, IOException, ParseException {
	waveProcessService.extractSchedule("121");
	}

}
