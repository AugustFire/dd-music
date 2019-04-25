package com.nercl.music.cloud.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.classroom.TaskQuestion;

@Repository
public class TaskQuestionDaoImpl extends AbstractBaseDaoImpl<TaskQuestion, String> implements TaskQuestionDao {

	@Override
	public List<TaskQuestion> get(String tid) {
		String jpql = "from TaskQuestion tq where tq.taskId = ?1";
		return this.executeQueryWithoutPaging(jpql, tid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaskQuestion> get(List<String> tids) {
		String jpql = "from TaskQuestion tq where tq.taskId in ?1";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1, tids);
		return query.getResultList();
	}

}
