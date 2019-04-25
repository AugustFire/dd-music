package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.ExamPaper;

public interface ExamPaperDao extends BaseDao<ExamPaper, String> {

	List<ExamPaper> list(int page);

	List<ExamPaper> list();

	List<ExamPaper> get(String name, int page);

	List<ExamPaper> get(int year);

	ExamPaper get(String examPaperId);

	ExamPaper getBySubjectType(Integer subjectType);

	ExamPaper getByExamAndSubjectType(String examId, Integer subjectType);

	List<ExamPaper> getByExam(String examId);

}
