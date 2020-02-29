package com.example.demo.service;

import java.util.Map;

import com.example.demo.vo.KeyGraph;

public interface GraphService {

	public Map<String, KeyGraph> queryGraphByClass (String classId);
}
