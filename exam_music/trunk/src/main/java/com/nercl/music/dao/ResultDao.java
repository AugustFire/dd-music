package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.Result;

public interface ResultDao extends BaseDao<Result, String> {

	List<Result> list(int page, String exid);

	Result get(String eid, String exid, String epid);

	List<Result> getResultsByAttributes(String name, String idcard, String examNo, int page);

	List<Result> getExamineeResults(String examineeId, String examId);

	List<Result> list(String exid, String name, String examNo, int page);

	List<Result> list(String exid, String name, String examNo);

	void deleteAll();

}
