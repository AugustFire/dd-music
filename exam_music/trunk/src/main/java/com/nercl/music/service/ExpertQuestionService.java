package com.nercl.music.service;

import com.nercl.music.entity.ExpertQuestion;

public interface ExpertQuestionService {

	void delete(String questionId);

	void save(ExpertQuestion eq);

}
