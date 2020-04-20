package com.example.demo.service;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.dao.MyFileMapper;
import com.example.demo.domain.MyFile;
import com.example.demo.exception.MyException;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class FileServiceTest1 {

	@Autowired
	private FileService fileService;
	@Autowired
	private MyFileMapper mapper;
	
	
	@Test
	public void test() throws UnsupportedAudioFileException, IOException, MyException, InterruptedException {
		MyFile file = mapper.queryMyFile("25");
		fileService.convertAACToWAV(file);
	}
	
	
	

}
