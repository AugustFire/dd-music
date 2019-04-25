package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.ExamPaper;
import com.nercl.music.cloud.entity.ExamPaperType;

public interface ExamPaperService {

	boolean save(String title, Integer score, String examId, String producerId, String producerName);

	boolean update(ExamPaper examPaper);

	boolean update(String id, String title, Integer score, Integer resolvedTime, Float difficulty, Integer secretLevel);

	boolean update(String id, String title, Integer score, ExamPaper.Status status, Integer resolvedTime,
			Float difficulty);

	boolean delete(String examPaperId);

	List<ExamPaper> getByExam(String examId);

	List<ExamPaper> getAllByExam(String examId);

	ExamPaper getByExamAndPaperType(String examId, ExamPaperType examPaperType);

	ExamPaper get(String id);

	List<ExamPaper> get(String cid, long beginAt, long endAt);

	ExamPaper getNonFinished(String eid);

	/**
	 * 新增试卷
	 * 
	 * @param examPaper
	 */
	String save(ExamPaper examPaper);

	/**
	 * 查询某用户创建的所有试卷
	 * 
	 * @param uid
	 *            试卷创建者id
	 */
	List<ExamPaper> getExamPapersByUserId(String uid);

	boolean clear(String id);

}
