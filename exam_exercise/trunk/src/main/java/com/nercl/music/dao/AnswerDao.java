package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.question.Answer;

public interface AnswerDao extends BaseDao<Answer, String> {

	List<Answer> list();

	Answer getByQuestion(String questionId);

}
