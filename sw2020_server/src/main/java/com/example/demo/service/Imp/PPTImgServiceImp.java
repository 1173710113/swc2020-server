package com.example.demo.service.Imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.PPTImgMapper;
import com.example.demo.service.PPTImgService;

@Service
public class PPTImgServiceImp implements PPTImgService{

	@Autowired
	private PPTImgMapper mapper;
	
	@Override
	public void addPPTImg(List<String> imgPathList, String classId) {
		for(String imgPath : imgPathList) {
			mapper.addImg(imgPath, classId);
		}
	}
	
	@Override
	public List<String> queryPPTImgByClass(String classId) {
		return mapper.queryImgByClass(classId);
	}

}
