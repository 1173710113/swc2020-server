package com.example.demo.controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.exception.MyException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class FileControllerTest {

	@Autowired
	private FileController fileController;
	
	@Test
	public void test() throws UnsupportedAudioFileException, IOException, MyException, ParseException {
		fileController.mergeWAV("121");
	}

}
