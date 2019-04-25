package com.nercl.music.cloud.service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.AnswerRecordMaluation;
import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.CompositeAbility;
import com.nercl.music.cloud.entity.Knowledge;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.QuestionType;
import com.nercl.music.cloud.entity.ability.Statistic;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.util.GraspValueUtil;
import com.nercl.music.util.QuestionToJsonUtil;

@Service
@Transactional
public class UserStatisicServiceImpl implements UserStatisicService {

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private GraspValueUtil graspValueUtil;

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private AnswerRecordMaluationService answerRecordMaluationService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private QuestionToJsonUtil questionToJsonUtil;

	@Autowired
	private StatisticService statisticService;

	private final static float PASS_LINE = (float) 0.6;

	private final static long TWO_WEEK_MILLIS = 2 * 7 * 24 * 3600 * 1000;

	@Override
	public Map<String, Integer> getUserMusicAbility(String userId, long start, long end) {
		List<AnswerRecord> records = answerRecordService.get(userId, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		Map<String, Integer> result = Maps.newLinkedHashMap();
		int slice = (int) ((end - start) / TWO_WEEK_MILLIS);
		slice = slice < 1 ? 1 : slice;
		IntStream.range(1, slice + 2).forEach(t -> {
			long begin = start + t * TWO_WEEK_MILLIS;
			Double grasp = records.parallelStream().filter(record -> record.getTimestamp() <= begin)
					.mapToDouble(record -> graspValueUtil.getGraspValue(record)).sum();
			grasp = null == grasp ? 0 : grasp;
			result.put(String.valueOf(begin), grasp.intValue());
		});
		return result;
	}

	@Override
	public Map<String, Object> getUserKnowledgeStatistics(String uid, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getRecords(uid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		Map<String, Object> result = Maps.newHashMap();
		List<Knowledge> top = knowledgeService.getTop();
		Iterator<Knowledge> iterator = top.iterator();
		Map<String, Integer> graspMap = Maps.newHashMap();
		Map<String, Integer> numMap = Maps.newHashMap();
		Map<String, Boolean> bigerThanMap = Maps.newHashMap();
		while (iterator.hasNext()) {
			Knowledge knowledge = (Knowledge) iterator.next();
			String key = knowledge.getNo();
			List<AnswerRecord> collect = records.stream()
					.filter(record -> record.getQuestion().getKnowledges().contains(key)).collect(Collectors.toList());
			graspMap.put(key, graspValueUtil.getGraspValue(collect));
			numMap.put(key, collect.size());
			Statistic sta = statisticService.getClassKnowledgeStatistics(key, "", end);
			if (null != sta) {
				bigerThanMap.put(key, graspMap.get(key) > sta.getGrasp());
			} else {
				bigerThanMap.put(key, true);
			}
		}
		result.put("average_score", graspMap);
		result.put("exercise_amount", numMap);
		result.put("bigger_than_average", bigerThanMap);
		return result;
	}

	@Override
	public Map<String, Integer> getUserSubKnowledgeStatistics(String uid, String knowledge, long start, long end) {
		List<AnswerRecord> records = answerRecordService.get(uid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		List<Knowledge> knowledges = knowledgeService.getChildren(knowledge);
		if (null == knowledges || knowledges.isEmpty()) {
			return null;
		}
		Map<String, Integer> result = Maps.newHashMap();
		knowledges.forEach(k -> {
			List<AnswerRecord> rs = records.parallelStream().filter(record -> {
				Question question = record.getQuestion();
				return null != question && Strings.nullToEmpty(question.getKnowledges()).contains(k.getNo());
			}).collect(Collectors.toList());
			result.put(k.getNo(), graspValueUtil.getGraspValue(rs));
		});
		return result;
	}

	@Override
	public Map<String, Object> getUserAbilityStatistics(String uid, long start, long end) {
		List<AnswerRecord> records = answerRecordService.get(uid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		Map<String, Object> result = Maps.newHashMap();
		Map<String, Integer> graspMap = Maps.newHashMap();
		Map<String, Integer> numMap = Maps.newHashMap();
		Map<String, Boolean> bigerThanMap = Maps.newHashMap();
		List<CompositeAbility> parents = Arrays.stream(CompositeAbility.values()).filter(ca -> null == ca.getParent())
				.collect(Collectors.toList());
		parents.forEach(parent -> {
			List<AnswerRecord> rs = records.parallelStream().filter(record -> {
				Question question = record.getQuestion();
				return null != question
						&& Strings.nullToEmpty(question.getCompositeAbilitys()).contains(String.valueOf(parent));
			}).collect(Collectors.toList());
			graspMap.put(String.valueOf(parent), graspValueUtil.getGraspValue(rs));
			numMap.put(String.valueOf(parent), rs.size());
			Statistic sta = statisticService.getClassCompositeAbilityStatistics(String.valueOf(parent), "", end);
			if (null != sta) {
				bigerThanMap.put(String.valueOf(parent), graspMap.get(String.valueOf(parent)) > sta.getGrasp());
			} else {
				bigerThanMap.put(String.valueOf(parent), true);
			}
		});
		result.put("average_score", graspMap);
		result.put("exercise_amount", numMap);
		result.put("bigger_than_average", bigerThanMap);
		return result;
	}

	@Override
	public Map<String, Integer> getUserSubAbilityStatistics(String uid, CompositeAbility parent, long start, long end) {
		List<AnswerRecord> records = answerRecordService.get(uid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		List<String> cas = CompositeAbility.getChildren(parent).stream().map(CompositeAbility::toString)
				.collect(Collectors.toList());
		if (null == cas || cas.isEmpty()) {
			return null;
		}
		Map<String, Integer> result = Maps.newHashMap();
		cas.forEach(ca -> {
			List<AnswerRecord> rs = records.parallelStream().filter(record -> {
				Question question = record.getQuestion();
				return null != question
						&& Strings.nullToEmpty(question.getCompositeAbilitys()).contains(String.valueOf(ca));
			}).collect(Collectors.toList());
			result.put(String.valueOf(ca), graspValueUtil.getGraspValue(rs));
		});
		return result;
	}

	@Override
	public List<Map<String, Object>> getErrorQuestions(String uid, AnswerSource source, CompositeAbility ca,
			String knowledge, long start, long end) {
		List<AnswerRecord> records = answerRecordService.get(uid, source, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> result = Lists.newArrayList();
		List<AnswerRecord> errors = records.parallelStream().filter(record -> {
			if (null == ca) {
				return true;
			} else {
				Question question = record.getQuestion();
				return null != question
						&& Strings.nullToEmpty(question.getCompositeAbilitys()).contains(String.valueOf(ca));
			}
		}).filter(record -> {
			if (Strings.isNullOrEmpty(knowledge)) {
				return true;
			} else {
				Question question = record.getQuestion();
				return null != question && Strings.nullToEmpty(question.getKnowledges()).contains(knowledge);
			}
		}).filter(record -> {
			Question question = record.getQuestion();
			if (null == question) {
				return false;
			}
			boolean selectError = (QuestionType.SINGLE_SELECT.equals(question.getQuestionType())
					|| QuestionType.MULTI_SELECT.equals(question.getQuestionType())) && !record.getIsTrue();
			boolean shortError = !QuestionType.SINGLE_SELECT.equals(question.getQuestionType())
					&& !QuestionType.MULTI_SELECT.equals(question.getQuestionType())
					&& record.getFullScore() * PASS_LINE > record.getScore();
			return selectError || shortError;
		}).collect(Collectors.toList());
		if (null == errors) {
			return result;
		}
		errors.forEach(e -> {
			result.add(json(e));
		});
		return result;
	}

	@Override
	public List<Map<String, Object>> getQuestionsByAbility(String uid, CompositeAbility ca, AnswerSource source,
			long start, long end) {
		List<AnswerRecord> records = answerRecordService.get(uid, source, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> questions = Lists.newArrayList();
		records.parallelStream().filter(record -> {
			Question question = record.getQuestion();
			return null != question
					&& Strings.nullToEmpty(question.getCompositeAbilitys()).contains(String.valueOf(ca));
		}).forEach(r -> {
			Map<String, Object> map = json(r);
			map.put("composite_ability", ca);
			map.put("record_id", r.getId());
			map.put("exam_id", r.getExamId());
			questions.add(map);
		});
		return questions;
	}

	@Override
	public List<Map<String, Object>> getQuestionsByKnowledge(String uid, String knowledgeNo, AnswerSource source,
			long start, long end) {
		List<AnswerRecord> records = answerRecordService.get(uid, source, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> questions = Lists.newArrayList();
		records.parallelStream().filter(record -> {
			Question question = record.getQuestion();
			return null != question && Strings.nullToEmpty(question.getKnowledges()).contains(knowledgeNo);
		}).forEach(r -> {
			Map<String, Object> map = json(r);
			map.put("record_id", r.getId());
			map.put("exam_id", r.getExamId());
			questions.add(map);
		});
		return questions;
	}

	private Map<String, Object> json(AnswerRecord record) {
		Question question = record.getQuestion();
		if (null == question) {
			return null;
		}
		Map<String, Object> map = questionToJsonUtil.toJosn(question);
		map.put("record_id", record.getId());
		map.put("exam_id", record.getExamId());
		map.put("answer", record.getAnswer());
		map.put("answer_source", record.getAnswerSource());
		boolean isSelect = QuestionType.SINGLE_SELECT.equals(question.getQuestionType())
				|| QuestionType.MULTI_SELECT.equals(question.getQuestionType());
		boolean isCreationOrArt = QuestionType.FILL_WORD_CREATING.equals(question.getQuestionType())
				|| QuestionType.RHYTHM_CREATING.equals(question.getQuestionType())
				|| QuestionType.MELODY_CREATING.equals(question.getQuestionType())
				|| QuestionType.SONG_CREATING.equals(question.getQuestionType())
				|| QuestionType.ASSIGN_MUSIC_CREATING.equals(question.getQuestionType())
				|| QuestionType.ART_ACT.equals(question.getQuestionType());
		if (isSelect) {
			map.put("is_true", record.getIsTrue());
		} else if (isCreationOrArt) {
			List<AnswerRecordMaluation> arms = answerRecordMaluationService.getByRecord(record.getId());
			if (null == arms) {
				return map;
			}
			List<Map<String, Object>> options = arms.stream().map(arm -> {
				Map<String, Object> option = Maps.newHashMap();
				option.put("option", arm.getMaluationOption());
				option.put("score", arm.getScore());
				return option;
			}).collect(Collectors.toList());
			map.put("options", options);
		} else {
			map.put("score", record.getScore());
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getUserRanking(String uid, long start, long end) {
		Map<String, Object> classes = restTemplate.getForObject(ApiClient.GET_CLASS_USER, Map.class, uid);
		if (null == classes || null == classes.get("classes")) {
			return null;
		}
		List<Map<String, String>> cls = (List<Map<String, String>>) classes.get("classes");
		String cid = (String) cls.get(0).getOrDefault("class_id", "");
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
		if (null == records || records.isEmpty()) {
			return "";
		}
		Map<String, List<AnswerRecord>> group = records.parallelStream()
				.collect(Collectors.groupingBy(r -> r.getUserId()));
		Map<String, Integer> map = Maps.newHashMap();
		group.forEach((userId, rs) -> {
			map.put(userId, graspValueUtil.getGraspValue(rs));
		});
		Integer me = map.get(uid);
		int ranking = 1;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() > me) {
				ranking++;
			}
		}
		return String.valueOf(ranking) + "/" + getStudentNum(cid);
	}

	@SuppressWarnings("unchecked")
	private int getStudentNum(String classId) {
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_CLASSSTUDENT_NUM, Map.class, classId);
		if (null == ret || null == ret.get("num")) {
			return 1;
		}
		int num = (int) ret.getOrDefault("num", 1);
		return 0 == num ? 1 : num;
	}

}
