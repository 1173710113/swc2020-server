package com.example.demo.service;

import com.example.demo.exception.TransferException;

public interface WaveProcessService {

	public String waveConvertToText(String filePath) throws TransferException;
	
	public void extractAlignResult(String filePath, String text, String classId);
}
