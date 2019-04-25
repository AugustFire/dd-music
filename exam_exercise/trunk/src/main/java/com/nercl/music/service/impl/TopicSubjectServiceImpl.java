package com.nercl.music.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.nercl.music.dao.TopicSubjectDao;
import com.nercl.music.entity.authorize.TopicSubject;
import com.nercl.music.service.TopicSubjectService;

@Service
@Transactional
public class TopicSubjectServiceImpl implements TopicSubjectService {

	@Autowired
	private TopicSubjectDao topicSubjectDao;

	@Override
	public boolean save(String tid, String ttitle, String sid, String stitle) {
		TopicSubject topicSubject = this.getByTopicSubject(tid, sid);
		if (null == topicSubject) {
			topicSubject = new TopicSubject();
			topicSubject.setTopicId(tid);
			topicSubject.setTopicTitle(ttitle);
			topicSubject.setSubjectId(sid);
			topicSubject.setSubjectTitle(stitle);
			topicSubjectDao.save(topicSubject);
			return Strings.isNullOrEmpty(topicSubject.getId());
		}
		return false;
	}

	@Override
	public boolean deleteByTopic(String tid) {
		List<TopicSubject> topicSubjects = this.getByTopic(tid);
		if (null != topicSubjects) {
			topicSubjects.forEach(topicSubject -> {
				topicSubjectDao.delete(topicSubject);
			});
		}
		return true;
	}

	@Override
	public List<TopicSubject> getBySubject(String sid) {
		return topicSubjectDao.getBySubject(sid);
	}

	@Override
	public List<TopicSubject> getByTopic(String tid) {
		return topicSubjectDao.getByTopic(tid);
	}

	@Override
	public TopicSubject getByTopicSubject(String tid, String sid) {
		return topicSubjectDao.getByTopicSubject(tid, sid);
	}

}
