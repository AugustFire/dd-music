package com.nercl.music.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.entity.question.ExamQuestion;

public interface ExamQuestionService {

	void save(ExamQuestion examQuestion);

	ExamQuestion get(String id);

	List<Map<String, Object>> getLookSingQuestions(int year);

	List<Map<String, Object>> getBySubjectType(Integer subjectType, int page);

	List<ExamQuestion> getQuestionsBySubjectType(Integer subjectType, int page);

	List<Map<String, Object>> getBytopic(String topicId, String exerciserId);

	List<Map<String, Object>> listLookSingQuestions();

	List<ExamQuestion> list(Integer type, String title, Float difficulty, int page);

	boolean delete(String[] ids);
	
	void update(ExamQuestion examQuestion);

}
