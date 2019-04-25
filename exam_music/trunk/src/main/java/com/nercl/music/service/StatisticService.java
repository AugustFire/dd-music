package com.nercl.music.service;

import java.util.Map;

public interface StatisticService {

	Map<String, Object> statExam(String examId);

	Map<String, Object> statExaminee(String examId, String examPaperId);

	Map<String, Object> statExamPaper(String examId, String examPaperId);

}
