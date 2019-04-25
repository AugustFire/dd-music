package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.ExamExamPaper;

public interface ExamPaperWeightDao extends BaseDao<ExamExamPaper, String> {

	ExamExamPaper get(String examId, String examPaperId);

	List<ExamExamPaper> getByExam(String examId);

}
