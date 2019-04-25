package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.Exam;

public interface ExamDao extends BaseDao<Exam, String> {

	List<Exam> list(int page);

	Exam get(int year);

	List<Exam> getByYear(int year);

	Exam get(String id);

	Exam getUsedToExam(int year);
}
