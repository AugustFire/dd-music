package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.ExamPaper;
import com.nercl.music.cloud.entity.ExamPaperType;

public interface ExamPaperDao extends BaseDao<ExamPaper, String> {

	List<ExamPaper> getByExam(String examId);

	List<ExamPaper> getAllByExam(String examId);

	ExamPaper getByExamAndPaperType(String examId, ExamPaperType examPaperType);

	/**
	 * 查询某用户创建的所有试卷
	 * 
	 * @param uid
	 *            试卷创建者id
	 */
	List<ExamPaper> getExamPapersByUserId(String uid);

	ExamPaper getNonFinished(String uid);

}
