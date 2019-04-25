package com.nercl.music.service;

import com.nercl.music.entity.Answer;

public interface AnswerService {

	boolean saveAnswer(Answer answer);

	Answer getByQuestion(String questionId);

}
