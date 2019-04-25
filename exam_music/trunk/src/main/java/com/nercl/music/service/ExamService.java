package com.nercl.music.service;

import java.util.Date;
import java.util.List;

import com.nercl.music.entity.Exam;

public interface ExamService {

	List<Exam> list(int page);

	Exam getUsedToExam(int year);

	Exam get(String id);

	Exam save(String title, String intro, Date startAt, Date endAt, String[] pids);

	boolean update(String id, String title, String intro, Date startAt, Date endAt, String[] pids);

	void setExamPapers(String id, String[] pids, Integer[] weights, Integer machineWeight, Integer expertWeight);

	void delete(String id);

	List<Exam> getByYear(Integer year);

	boolean usedToExam(String id);
}
