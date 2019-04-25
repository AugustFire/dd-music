package com.nercl.music.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.MachineResultDao;
import com.nercl.music.entity.question.MachineResult;

@Repository
public class MachineResultDaoImpl extends AbstractBaseDaoImpl<MachineResult, String> implements MachineResultDao {

	@Override
	public MachineResult get(String answerRecordId, String exerciserId, String examQuestionId) {
		String jpql = "from MachineResult mr where mr.answerRecordId = ?1 and mr.exerciserId = ?2 and mr.examQuestionId = ?3";
		List<MachineResult> mrs = this.executeQueryWithoutPaging(jpql, answerRecordId, exerciserId, examQuestionId);
		return null == mrs || mrs.isEmpty() ? null : mrs.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getMachineAvgScore() {
		String jpql = "select mr.examId,mr.examPaperId,mr.examQuestionId,AVG(mr.score) from MachineResult mr group by mr.examId,mr.examPaperId,mr.examQuestionId";
		Query query = entityManager.createQuery(jpql);
		List<Object[]> datas = query.getResultList();
		return datas;
	}

	@Override
	public MachineResult get(String answerRecordId) {
		String jpql = "from MachineResult mr where mr.answerRecordId = ?1";
		List<MachineResult> mrs = this.executeQueryWithoutPaging(jpql, answerRecordId);
		return null == mrs || mrs.isEmpty() ? null : mrs.get(0);
	}

	@Override
	public List<MachineResult> list(String examQuestionId) {
		String jpql = "from MachineResult mr where mr.examQuestionId = ?1";
		return this.executeQueryWithoutPaging(jpql, examQuestionId);
	}
}
