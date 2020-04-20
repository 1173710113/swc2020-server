package com.example.demo.utils;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.dao.MyFileMapper;
import com.example.demo.domain.MyFile;
import com.example.demo.exception.MyException;
import com.example.demo.utils.wav.WavCut;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class WavTest {

	@Autowired
	private WavCut wavCut;
	
	@Autowired
	private MyFileMapper mapper;
	
	@Test
	public void test() throws IOException, MyException {
		MyFile file = mapper.queryMyFile("89");
		wavCut.cut(file, 0, 5);
	}

}
