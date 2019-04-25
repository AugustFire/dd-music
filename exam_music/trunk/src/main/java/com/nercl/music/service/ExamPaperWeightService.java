package com.nercl.music.service;

import com.nercl.music.entity.ExamExamPaper;

public interface ExamPaperWeightService {

	boolean save(String examId, String examPaperId, Integer value);

	ExamExamPaper get(String examId, String examPaperId);

}
