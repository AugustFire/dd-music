package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.TopicSubjectDao;
import com.nercl.music.entity.authorize.TopicSubject;

@Repository
public class TopicSubjectDaoImpl extends AbstractBaseDaoImpl<TopicSubject, String> implements TopicSubjectDao {

	@Override
	public TopicSubject getByTopicSubject(String tid, String sid) {
		String jpql = "from TopicSubject ts where ts.topicId = ?1 and ts.subjectId = ?2";
		List<TopicSubject> topicSubjects = executeQueryWithoutPaging(jpql, tid, sid);
		return null == topicSubjects || topicSubjects.isEmpty() ? null : topicSubjects.get(0);
	}

	@Override
	public List<TopicSubject> getBySubject(String sid) {
		String jpql = "from TopicSubject ts where ts.subjectId = ?1";
		List<TopicSubject> topicSubjects = executeQueryWithoutPaging(jpql, sid);
		return topicSubjects;
	}

	@Override
	public List<TopicSubject> getByTopic(String tid) {
		String jpql = "from TopicSubject ts where ts.topicId = ?1";
		List<TopicSubject> topicSubjects = executeQueryWithoutPaging(jpql, tid);
		return topicSubjects;
	}

}
