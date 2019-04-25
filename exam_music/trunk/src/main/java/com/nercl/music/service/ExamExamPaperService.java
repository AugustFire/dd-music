package com.nercl.music.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.entity.ExamExamPaper;

public interface ExamExamPaperService {

	boolean save(String examId, String examPaperId, Integer weight);

	ExamExamPaper get(String examId, String examPaperId);

	List<ExamExamPaper> getByExam(String examId);

	Map<String, Integer> getWeight(String examId);

	Map<String, Integer> getExpertMachineWeight(String examId);

}
