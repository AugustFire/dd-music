package com.nercl.music.dao;

import com.nercl.music.entity.ExpertQuestion;

public interface ExpertQuestionDao extends BaseDao<ExpertQuestion, String> {
	
	void deleteByQuestion(String questionId);

}
