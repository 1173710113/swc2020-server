package com.example.demo.service.Imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.KeyGraphMapper;
import com.example.demo.domain.Node;
import com.example.demo.service.GraphService;
import com.example.demo.vo.KeyGraph;

@Service
public class GraphServiceImp implements GraphService{

	@Autowired
	KeyGraphMapper mapper;
	
	@Override
	public Map<String, KeyGraph> queryGraphByClass(String classId) {
		Map<String, KeyGraph> map = new HashMap<>();
		List<Node> nodes = mapper.queryNodeByClass(classId);
		for(Node node:nodes) {
			map.put(node.getId(), new KeyGraph(node, mapper.queryRelationByNode(node.getId()), mapper.querySentenceByNode(node.getId())));
		}
		return map;
	}

}
