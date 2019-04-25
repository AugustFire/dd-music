package com.nercl.music.cloud.service;


import com.nercl.music.cloud.entity.Answer;

public interface AnswerService {

	void save(Answer answer);
	
	Answer getByQuestion(String qid);

}
