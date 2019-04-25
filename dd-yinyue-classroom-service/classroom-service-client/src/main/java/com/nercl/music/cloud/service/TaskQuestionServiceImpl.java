package com.nercl.music.cloud.service;

import java.util.List;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.nercl.music.cloud.dao.TaskQuestionDao;
import com.nercl.music.cloud.entity.classroom.TaskQuestion;

@Service
@Transactional
public class TaskQuestionServiceImpl implements TaskQuestionService {

	@Autowired
	private TaskQuestionDao taskQuestionDao;

	@Override
	public void deleteTaskQuestions(String tid, String[] qids) throws Exception {
		Stream.of(qids).forEach(qid -> {
			TaskQuestion tq = new TaskQuestion();
			tq.setQuestionId(qid);
			tq.setTaskId(tid);
			List<TaskQuestion> tqs = Lists.newArrayList();
			try {
				tqs = taskQuestionDao.findByConditions(tq);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null != tqs && !tqs.isEmpty()) {
				taskQuestionDao.delete(tqs.get(0));
			}
		});
	}

	@Override
	public List<TaskQuestion> findByConditions(TaskQuestion tq) throws Exception {
		return taskQuestionDao.findByConditions(tq);
	}

	@Override
	public String save(TaskQuestion taskQuestion) {
		taskQuestionDao.save(taskQuestion);
		return taskQuestion.getId();
	}

	@Override
	public List<TaskQuestion> get(String tid) {
		return taskQuestionDao.get(tid);
	}

	@Override
	public void delete(String id) {
		taskQuestionDao.deleteById(id);
	}

	@Override
	public List<TaskQuestion> get(List<String> tids) {
		return taskQuestionDao.get(tids);
	}

}
