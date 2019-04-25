package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.Answer;

public interface AnswerDao extends BaseDao<Answer, String> {

	List<Answer> list();

	Answer getByQuestion(String questionId);

}
