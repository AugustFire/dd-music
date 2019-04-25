package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.question.ExamQuestion;

public interface ExamQuestionDao extends BaseDao<ExamQuestion, String> {

	List<ExamQuestion> getLookSingQuestions(int year);

	List<ExamQuestion> getBytopic(String topicId);

	List<ExamQuestion> listLookSingQuestions();

	List<ExamQuestion> getBySubjectType(Integer subjectType, int page);

	List<ExamQuestion> list(Integer type, String title, Float difficulty, int page);

}
