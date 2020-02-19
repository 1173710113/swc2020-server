package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.RecordMarkMapper;
import com.example.demo.domain.RecordMarkPO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecordMarkServiceImp implements RecordMarkService {

	@Autowired
	RecordMarkMapper recordMarkMapper;

	@Override
	public RecordMarkPO addRecordMark(RecordMarkPO recordMark) {
		recordMarkMapper.addRecordMark(recordMark);
		return recordMark;
	}

	@Override
	public void deleteRecordMark(String recordMarkId) {
		recordMarkMapper.deleteRecordMark(recordMarkId);

	}

	@Override
	public List<RecordMarkPO> queryRecordMarkByClass(String classId) {
		return recordMarkMapper.queryRecordMarkByClass(classId);
	}

}
