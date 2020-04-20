package com.example.demo.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;

import com.example.demo.exception.TransferException;

public interface WaveProcessService {

	public String waveConvertToText(String filePath) throws TransferException;
	
	public void extractAlignResult(String filePath, String text, String classId);
	
	public void extractSchedule(String classId) throws UnknownHostException, IOException, ParseException;
}
