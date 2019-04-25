package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.AnswerRecordMaluation;
import com.nercl.music.cloud.entity.MaluationOption;


@Repository
public class AnswerRecordMaluationDaoImpl extends AbstractBaseDaoImpl<AnswerRecordMaluation, String>
		implements AnswerRecordMaluationDao {

	@Override
	public List<AnswerRecordMaluation> getByRecord(String rid) {
		String jpql = "from AnswerRecordMaluation arm where arm.answerRecordId = ?1";
		List<AnswerRecordMaluation> arms = executeQueryWithoutPaging(jpql, rid);
		return arms;
	}

	@Override
	public List<AnswerRecordMaluation> getByRecordAndOption(String rid, String option) {
		String jpql = "from AnswerRecordMaluation arm where arm.answerRecordId = ?1 and arm.maluationOption = ?2";
		List<AnswerRecordMaluation> arms = executeQueryWithoutPaging(jpql, rid, MaluationOption.valueOf(option));
		return arms;
	}

}
