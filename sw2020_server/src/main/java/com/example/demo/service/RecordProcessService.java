package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.Deadline;
import com.example.demo.domain.Discussion;
import com.example.demo.domain.Node;
import com.example.demo.domain.MyFile;

/**
 * 用于语音处理、文本分析的服务类
 * @author msi-user
 *
 */
public interface RecordProcessService {
	
	/**
	 * convert audio record to text
	 * 
	 * @param myFile the index to find the audio file saved in server
	 * @return the index to find the text filed converted, null if failed 
	 */
	public MyFile changeToText(MyFile myFile);
	
	/**
	 * extract knowledge form text
	 * 
	 * @param myFile is the index to find the text file saved in server
	 * @return list of key nodes extracted form the text file
	 */
	public List<Node> extractKnowledge(MyFile myFile);
	
	/**
	 * extract deadline form text
	 * 
	 * @param myFile is the index to find the text file saved in server
	 * @return the list of deadlines extracted from the text
	 */
	public List<Deadline> extractDeadline(MyFile myFile);
	
	/**
	 * extract discussion from text 
	 * 
	 * @param myFile is the index to find text file saved in server
	 * @return the list of discussions extracted from the text
	 */
	public List<Discussion> extractDiscussion(MyFile myFile);

}
