package com.nercl.music.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.MachineResultDao;
import com.nercl.music.entity.MachineResult;

@Repository
public class MachineResultDaoImpl extends AbstractBaseDaoImpl<MachineResult, String> implements MachineResultDao {

	@Override
	public MachineResult get(String examId, String examPaperId, String examineeId, String examQuestionId) {
		String jpql = "from MachineResult mr where mr.examId = ?1 and mr.examPaperId = ?2 and mr.examineeId = ?3 and mr.examQuestionId = ?4";
		List<MachineResult> mrs = this.executeQueryWithoutPaging(jpql, examId, examPaperId, examineeId, examQuestionId);
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

}
