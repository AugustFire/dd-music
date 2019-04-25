package com.nercl.music.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.nercl.music.dao.TopicQuestionDao;
import com.nercl.music.entity.authorize.TopicQuestion;
import com.nercl.music.service.TopicQuestionService;

@Service
@Transactional
public class TopicQuestionServiceImpl implements TopicQuestionService {

	@Autowired
	private TopicQuestionDao topicQuestionDao;

	@Override
	public void save(String topicId, String[] examQuestionIds) {
		if (null == examQuestionIds || examQuestionIds.length < 1) {
			return;
		}
		Arrays.stream(examQuestionIds).forEach(qid -> {
			if (!Strings.isNullOrEmpty(qid)) {
				TopicQuestion topicQuestion = get(topicId, qid);
				if (null == topicQuestion) {
					topicQuestion = new TopicQuestion();
					topicQuestion.setExamQuestionId(qid);
					topicQuestion.setTopicId(topicId);
					topicQuestionDao.save(topicQuestion);
				}
			}
		});
	}

	@Override
	public TopicQuestion get(String topicId, String examQuestionId) {
		return topicQuestionDao.get(topicId, examQuestionId);
	}

	@Override
	public void removeQuestions(String tid) {

		topicQuestionDao.removeQuestions(tid);
	}

	@Override
	public List<TopicQuestion> get(String topicId) {
		return topicQuestionDao.get(topicId);
	}

	@Override
	public void delete(String id) {
		topicQuestionDao.deleteById(id);
	}

}
