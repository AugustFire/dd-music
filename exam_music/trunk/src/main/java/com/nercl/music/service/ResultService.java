package com.nercl.music.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.entity.Result;

public interface ResultService {

	void deleteAll();

	List<Result> list();

	List<Result> list(int page, String exid);

	Map<String, List<Result>> list(String exid, String name, String examNo, int page);

	Map<String, List<Result>> list(String exid, String name, String examNo);

	List<Result> getExamineeResults(String examineeId, String examId);

	Result get(String id);

	Result get(String eid, String exid, String epId);

	void save(Result result);

	List<Result> getResultsByAtrributes(String name, String idcard, String examNo, int page);

	boolean accumulate(String examineeId, String examId, String examPaperId, Integer score);

}
