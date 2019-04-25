package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.ExamPaper;
import com.nercl.music.entity.user.Login;

public interface ExamPaperService {

	void save(String title, Integer year, Integer resolvedTime, Integer subjectType);

	void save(ExamPaper examPaper);

	List<ExamPaper> list(int page);
	
	void setCheckStatusDefault();

	List<ExamPaper> get(String name, int page);

	List<ExamPaper> get(int year);

	ExamPaper get(String examPaperId);

	ExamPaper getByExamAndSubjectType(String examId, Integer subjectType);

	ExamPaper getBySubjectType(Integer subjectType);

	boolean update(String id, String title, Integer year, Integer resolvedTime, Integer subjectType);

	void update(ExamPaper examPaper);

	List<ExamPaper> getByExam(String examId);

	boolean pass(String pid,Login login);

	boolean unpass(String pid,String reason,Login login);

}
