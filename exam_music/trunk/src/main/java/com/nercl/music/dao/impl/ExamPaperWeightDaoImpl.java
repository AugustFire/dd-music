package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.ExamPaperWeightDao;
import com.nercl.music.entity.ExamExamPaper;

@Repository
public class ExamPaperWeightDaoImpl extends AbstractBaseDaoImpl<ExamExamPaper, String> implements ExamPaperWeightDao {

	@Override
	public ExamExamPaper get(String examId, String examPaperId) {
		String jpql = "from ExamExamPaper eep where eep.examId = ?1 and eep.examPaperId = ?2";
		List<ExamExamPaper> examExamPapers = executeQueryWithoutPaging(jpql, examId, examPaperId);
		return null == examExamPapers || examExamPapers.isEmpty() ? null : examExamPapers.get(0);
	}

	@Override
	public List<ExamExamPaper> getByExam(String examId) {
		String jpql = "from ExamExamPaper eep where eep.examId = ?1";
		List<ExamExamPaper> examExamPapers = executeQueryWithoutPaging(jpql, examId);
		return examExamPapers;
	}

}
