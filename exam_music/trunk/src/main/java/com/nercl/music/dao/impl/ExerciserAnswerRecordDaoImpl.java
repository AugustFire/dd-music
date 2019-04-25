package com.nercl.music.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.nercl.music.dao.ExerciserAnswerRecordDao;
import com.nercl.music.entity.ExerciserAnswerRecord;

@Repository
public class ExerciserAnswerRecordDaoImpl extends AbstractBaseDaoImpl<ExerciserAnswerRecord, String>
        implements ExerciserAnswerRecordDao {

	@Override
	public List<ExerciserAnswerRecord> get(String examId, String examPaperId, String exerciserId, String examQuestionId) {
		List<Object> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from ExerciserAnswerRecord ar where 1=1");
		if (StringUtils.isNotBlank(examId)) {
			jpql.append(" and ar.examId = ?" + (++paramCount));
			params.add(examId);
		}
		if (StringUtils.isNotBlank(examPaperId)) {
			jpql.append(" and ar.examPaperId = ?" + (++paramCount));
			params.add(examPaperId);
		}
		if (StringUtils.isNotBlank(exerciserId)) {
			jpql.append(" and ar.exerciserId = ?" + (++paramCount));
			params.add(exerciserId);
		}
		if (StringUtils.isNotBlank(examQuestionId)) {
			jpql.append(" and ar.examQuestionId = ?" + (++paramCount));
			params.add(examQuestionId);
		}
		List<ExerciserAnswerRecord> records = this.executeQueryWithoutPaging(jpql.toString(), params.toArray());
		return records;
	}

}
