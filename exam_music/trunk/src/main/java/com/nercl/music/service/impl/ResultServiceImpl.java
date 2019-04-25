package com.nercl.music.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.ResultDao;
import com.nercl.music.entity.Result;
import com.nercl.music.service.ResultService;

@Service
@Transactional
public class ResultServiceImpl implements ResultService {

	@Autowired
	private ResultDao resultDao;

	@Override
	public List<Result> list(int page, String exid) {
		return this.resultDao.list(page, exid);
	}

	@Override
	public Map<String, List<Result>> list(String exid, String name, String examNo, int page) {
		List<Result> results = this.resultDao.list(exid, name, examNo, page);
		Map<String, List<Result>> resultMap = results.stream().collect(Collectors.groupingBy(Result::getExamineeId));
		return resultMap;
	}

	@Override
	public Map<String, List<Result>> list(String exid, String name, String examNo) {
		List<Result> results = this.resultDao.list(exid, name, examNo);
		Map<String, List<Result>> resultMap = results.stream().collect(Collectors.groupingBy(Result::getExamineeId));
		return resultMap;
	}

	@Override
	public Result get(String id) {
		return this.resultDao.findByID(id);
	}

	@Override
	public Result get(String examineeId, String examId, String examPaperId) {
		return this.resultDao.get(examineeId, examId, examPaperId);
	}

	@Override
	public List<Result> getResultsByAtrributes(String name, String idcard, String examNo, int page) {
		return resultDao.getResultsByAttributes(name, idcard, examNo, page);
	}

	@Override
	public void save(Result result) {
		this.resultDao.save(result);
	}

	@Override
	public boolean accumulate(String examineeId, String examId, String examPaperId, Integer score) {
		Result result = this.get(examineeId, examId, examPaperId);
		if (null == result) {
			result = new Result();
			result.setExamId(examId);
			result.setExamPaperId(examPaperId);
			result.setExamineeId(examineeId);
			this.save(result);
		}
		Integer s = result.getScore();
		s = (null == s ? 0 : s);
		result.setScore(s + score);
		this.resultDao.update(result);
		return true;
	}

	@Override
	public List<Result> getExamineeResults(String examineeId, String examId) {
		List<Result> results = this.resultDao.getExamineeResults(examineeId, examId);
		return results;
	}

	@Override
	public void deleteAll() {
		this.resultDao.deleteAll();
	}

	@Override
	public List<Result> list() {
		// TODO Auto-generated method stub
		return null;
	}

}
