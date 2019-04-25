package com.nercl.music.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.nercl.music.dao.TopicDao;
import com.nercl.music.entity.authorize.Subject;
import com.nercl.music.entity.authorize.Topic;
import com.nercl.music.entity.authorize.TopicQuestion;
import com.nercl.music.service.SubjectService;
import com.nercl.music.service.TopicQuestionService;
import com.nercl.music.service.TopicService;
import com.nercl.music.service.TopicSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class TopicServiceImpl implements TopicService {

	@Autowired
	private TopicDao topicDao;

	@Autowired
	private TopicQuestionService topicQuestionService;

	@Autowired
	private TopicSubjectService topicSubjectService;

	@Autowired
	private SubjectService subjectService;

	@Override
	public List<Topic> list() {
		return topicDao.list();
	}

	@Override
	public Topic findByID(String id) {
		return topicDao.findByID(id);
	}

	@Override
	public Topic getNewest(Integer year) {
		return topicDao.getNewest(year);
	}

	@Override
	public List<Topic> list(int page) {
		return topicDao.list(page);
	}

	@Override
	public List<Topic> getByYear(Integer year) {
		return topicDao.getByYear(year);
	}

	@Override
	public List<Topic> getByTitle(Integer subjectType, String title, int page) {
		return topicDao.getByTitle(subjectType, title, page);
	}

	@Override
	public List<Topic> list(Integer subjectType) {
		return topicDao.list(subjectType);
	}

	@Override
	public List<Topic> query(String key) {
		return topicDao.query(key);
	}

	@Override
	public boolean save(Integer subjectType, String title, int year, String area, Integer fee, String[] sids,
	        Long startAt, Long endAt) {
		Topic topic = new Topic();
		topic.setSubjectType(subjectType);
		topic.setTitle(title);
		topic.setYear(year);
		topic.setArea(area);
		topic.setFee(fee);
		topic.setStartAt(startAt);
		topic.setEndAt(endAt);
		topicDao.save(topic);
		if (null != sids && sids.length > 0) {
			Arrays.stream(sids).forEach(sid -> {
				Subject subject = subjectService.get(sid);
				if (null != subject) {
					topicSubjectService.save(topic.getId(), topic.getTitle(), subject.getId(), subject.getTitle());
				}
			});
		}
		return !Strings.isNullOrEmpty(topic.getId());
	}

	@Override
	public boolean update(String id, Integer subjectType, String title, Integer year, String area, String[] sids,
	        Long startAt, Long endAt) {
		Topic topic = topicDao.findByID(id);
		if (null != topic) {
			topic.setSubjectType(subjectType);
			topic.setTitle(title);
			topic.setYear(year);
			topic.setArea(area);
			topicDao.update(topic);
			if (null != sids && sids.length > 0) {
				topicSubjectService.deleteByTopic(topic.getId());
				Arrays.stream(sids).forEach(sid -> {
					Subject subject = subjectService.get(sid);
					if (null != subject) {
						topicSubjectService.save(topic.getId(), topic.getTitle(), subject.getId(), subject.getTitle());
					}
				});
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean setQuestions(String tid, String[] qids) {
		Topic topic = topicDao.findByID(tid);
		if (null != topic) {
			removeQuestions(tid);
			topicQuestionService.save(tid, qids);
		}
		return true;
	}

	@Override
	public void removeQuestions(String tid) {
		Topic topic = topicDao.findByID(tid);
		if (null != topic) {
			List<TopicQuestion> topicQuestions = this.topicQuestionService.get(tid);
			if (null != topicQuestions) {
				topicQuestions.forEach(topicQuestion -> {
					topicQuestionService.delete(topicQuestion.getId());
				});
			}
		}
	}

	@Override
	public List<String> getQuestions(String tid) {
		Topic topic = topicDao.findByID(tid);
		if (null != topic) {
			List<TopicQuestion> topicQuestions = this.topicQuestionService.get(tid);
			if (null != topicQuestions) {
				List<String> ids = Lists.newArrayList();
				topicQuestions.forEach(question -> {
					ids.add(question.getExamQuestionId());
				});
				return ids;
			}
		}
		return null;
	}

	@Override
	public List<Topic> getPayed(String exerciserId) {
		return topicDao.getPayed(exerciserId);
	}

	@Override
	public Topic get(String topicId) {
		return topicDao.findByID(topicId);
	}

	@Override
	public void deleteTopicById(String id) {
		topicDao.deleteById(id);
	}

}
