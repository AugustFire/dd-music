package com.nercl.music.cloud.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.nercl.music.cloud.dao.AnswerRecordMaluationDao;
import com.nercl.music.cloud.entity.AnswerRecordMaluation;
import com.nercl.music.cloud.entity.Maluation;
import com.nercl.music.cloud.entity.MaluationOption;

@Service
@Transactional
public class AnswerRecordMaluationServiceImpl implements AnswerRecordMaluationService {

	@Autowired
	private AnswerRecordMaluationDao answerRecordMaluationDao;

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private Gson gson;

	private static final Integer CREATION_FULL_SCORE = 100;

	@Override
	public List<AnswerRecordMaluation> getByRecordAndOption(String rid, String option) {
		return answerRecordMaluationDao.getByRecordAndOption(rid, option);
	}

	@Override
	public boolean save(String json) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> values = gson.fromJson(json, List.class);
		if (null == values) {
			return false;
		}
		values.forEach(value -> {
			String recordId = (String) value.get("record_id");
			if (Strings.isNullOrEmpty(recordId)) {
				return;
			}
			String option = (String) value.get("option");
			boolean correct = Arrays.stream(MaluationOption.values()).anyMatch(o -> String.valueOf(o).equals(option));
			if (!correct) {
				return;
			}
			List<AnswerRecordMaluation> arms = getByRecordAndOption(recordId, option);
			if (null != arms && !arms.isEmpty()) {
				return;
			}
			Integer score = ((Number) value.get("score")).intValue();
			save(recordId, option, score);
		});
		String recordId = (String) values.get(0).get("record_id");
		boolean success = answerRecordService.setCreationScore(recordId, CREATION_FULL_SCORE,
				getScoreAtOneRecord(recordId));
		return success;
	}

	@Override
	public boolean save(String recordId, String option, Integer score) {
		AnswerRecordMaluation arm = new AnswerRecordMaluation();
		arm.setAnswerRecordId(recordId);
		MaluationOption mo = MaluationOption.valueOf(option);
		arm.setMaluationOption(mo);
		score = score < mo.getLowScore() ? mo.getLowScore() : score > mo.getHighScore() ? mo.getHighScore() : score;
		arm.setScore(score);
		answerRecordMaluationDao.save(arm);
		return !Strings.isNullOrEmpty(arm.getId());
	}

	@Override
	public List<AnswerRecordMaluation> getByRecord(String rid) {
		return answerRecordMaluationDao.getByRecord(rid);
	}

	@Override
	public Integer getScoreAtOneRecord(String rid) {
		List<AnswerRecordMaluation> arms = getByRecord(rid);
		if (null == arms) {
			return null;
		}
		return arms.stream().mapToInt(arm -> arm.getScore()).sum();
	}

	@Override
	public Maluation.Level getLevel(String rid) {
		Integer score = getScoreAtOneRecord(rid);
		return Maluation.Level.getLevle(score);
	}

}
