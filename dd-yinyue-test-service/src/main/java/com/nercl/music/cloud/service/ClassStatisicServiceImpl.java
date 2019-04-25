package com.nercl.music.cloud.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.CompositeAbility;
import com.nercl.music.cloud.entity.Knowledge;
import com.nercl.music.cloud.entity.Maluation;
import com.nercl.music.cloud.entity.Option;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.QuestionType;
import com.nercl.music.cloud.entity.ability.Statistic;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.util.GraspValueUtil;
import com.nercl.music.util.QuestionToJsonUtil;

@Service
@Transactional
public class ClassStatisicServiceImpl implements ClassStatisicService {

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private GraspValueUtil graspValueUtil;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private OptionService optionService;

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private AnswerRecordMaluationService answerRecordMaluationService;

	@Autowired
	private StatisticService statisticService;

	@Autowired
	private QuestionToJsonUtil questionToJsonUtil;

	private final static long TWO_WEEK_MILLIS = 2 * 7 * 24 * 3600 * 1000;

	@Override
	public Map<String, Integer> getClassMusicAbilityTendency(String classId, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(classId, start, end);
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
	public Map<String, Integer> getClassesMusicAbility(String[] cids, long start, long end) {
		Map<String, Integer> result = Maps.newHashMap();
		Arrays.stream(cids).forEach(id -> {
			result.put(id, getClassMusicAbility(id, start, end));
		});
		return result;
	}

	private Integer getClassMusicAbility(String cid, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
		if (null == records || records.isEmpty()) {
			return 0;
		}
		return graspValueUtil.getGraspValue(records);
	}

	@Override
	public Map<String, Object> getClassAbilityStatistics(String cid, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		Map<String, Object> result = Maps.newHashMap();
		Map<String, Integer> graspMap = Maps.newHashMap();
		Map<String, Integer> numMap = Maps.newHashMap();
		Map<String, Boolean> bigerThanMap = Maps.newHashMap();
		Map<String, Object> classes = getClass(cid);
		String gid = (String) classes.getOrDefault("gid", "");
		String sid = (String) classes.getOrDefault("sid", "");
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
			Statistic sta = statisticService.getGradeCompositeAbilityStatistics(String.valueOf(parent), gid, sid, end);
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
	public Map<String, Object> getClassKnowledgeStatistics(String cid, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		Map<String, Object> result = Maps.newHashMap();
		Map<String, Integer> graspMap = Maps.newHashMap();
		Map<String, Integer> numMap = Maps.newHashMap();
		Map<String, Boolean> bigerThanMap = Maps.newHashMap();
		List<Knowledge> knowledges = knowledgeService.getTop();
		Map<String, Object> classes = getClass(cid);
		String gid = (String) classes.getOrDefault("gid", "");
		String sid = (String) classes.getOrDefault("sid", "");
		knowledges.forEach(knowledge -> {
			List<AnswerRecord> rs = records.parallelStream().filter(record -> {
				Question question = record.getQuestion();
				return null != question && Strings.nullToEmpty(question.getKnowledges()).contains(knowledge.getNo());
			}).collect(Collectors.toList());
			graspMap.put(knowledge.getNo(), graspValueUtil.getGraspValue(rs));
			numMap.put(knowledge.getNo(), rs.size());
			Statistic sta = statisticService.getGradeKnowledgeStatistics(knowledge.getNo(), gid, sid, end);
			if (null != sta) {
				bigerThanMap.put(knowledge.getNo(), graspMap.get(knowledge.getNo()) > sta.getGrasp());
			} else {
				bigerThanMap.put(knowledge.getNo(), true);
			}
		});
		result.put("average_score", graspMap);
		result.put("exercise_amount", numMap);
		result.put("bigger_than_average", bigerThanMap);
		return result;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getClass(String cid) {
		Map<String, Object> classes = restTemplate.getForObject(ApiClient.GET_CLASS, Map.class, cid);
		return classes;
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

	@SuppressWarnings("unchecked")
	private List<Map<String, String>> getStudents(String cid) {
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_CLASSSTUDENTS, Map.class, cid);
		if (null == ret || null == ret.get("students")) {
			return null;
		}
		return (List<Map<String, String>>) ret.get("students");
	}

	@Override
	public Map<String, Map<String, Integer>> getClassMusicAbilityTendency(String[] cids, long start, long end) {
		Map<String, Map<String, Integer>> result = Maps.newHashMap();
		Arrays.stream(cids).forEach(cid -> {
			result.put(cid, getClassMusicAbilityTendency(cid, start, end));
		});
		return result;
	}

	@Override
	public Map<String, Object> getClassesAbilityStatistics(String[] cids, long start, long end) {
		Map<String, Object> result = Maps.newHashMap();
		Arrays.stream(cids).forEach(cid -> {
			result.put(cid, getClassAbilityStatistics(cid, start, end));
		});
		return result;
	}

	@Override
	public Map<String, Integer> getClassSubAbilityStatistics(String cid, CompositeAbility ca, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		List<String> cas = Arrays.stream(CompositeAbility.values())
				.filter(c -> null != c.getParent() && c.getParent().equals(ca)).map(c -> c.toString())
				.collect(Collectors.toList());
		if (null == cas || cas.isEmpty()) {
			return null;
		}
		Map<String, Integer> result = Maps.newHashMap();
		cas.forEach(c -> {
			List<AnswerRecord> rs = records.parallelStream().filter(record -> {
				Question question = record.getQuestion();
				return null != question
						&& Strings.nullToEmpty(question.getCompositeAbilitys()).contains(String.valueOf(c));
			}).collect(Collectors.toList());
			result.put(String.valueOf(c), graspValueUtil.getGraspValue(rs));
		});
		return result;
	}

	@Override
	public Map<String, Integer> getClassSubKnowledgeStatistics(String cid, String knowledge, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
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
	public Map<String, Object> getClassesKnowledgeStatistics(String[] cids, long start, long end) {
		Map<String, Object> result = Maps.newHashMap();
		Arrays.stream(cids).forEach(id -> {
			result.put(id, getClassKnowledgeStatistics(id, start, end));
		});
		return result;
	}

	@Override
	public List<Map<String, Object>> getQuestionsByAbility(String cid, CompositeAbility ca, AnswerSource source,
			long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, source, start, end);
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
	public List<Map<String, Object>> getQuestionsByKnowledge(String cid, String knowledgeNo, AnswerSource source,
			long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, source, start, end);
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

	@Override
	public Map<String, Object> getAnswerPeopleAccuracy(String classId, String questionId, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(classId, questionId, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		Map<String, Object> result = Maps.newHashMap();
		result.put("question_id", questionId);
		Question question = questionService.get(questionId);
		if (null == question) {
			return result;
		}
		boolean isSelect = QuestionType.SINGLE_SELECT.equals(question.getQuestionType())
				|| QuestionType.MULTI_SELECT.equals(question.getQuestionType());
		boolean isCreationOrArt = QuestionType.FILL_WORD_CREATING.equals(question.getQuestionType())
				|| QuestionType.RHYTHM_CREATING.equals(question.getQuestionType())
				|| QuestionType.MELODY_CREATING.equals(question.getQuestionType())
				|| QuestionType.SONG_CREATING.equals(question.getQuestionType())
				|| QuestionType.ASSIGN_MUSIC_CREATING.equals(question.getQuestionType())
				|| QuestionType.ART_ACT.equals(question.getQuestionType());
		if (isSelect) {
			long count = records.parallelStream().filter(record -> record.getIsTrue()).count();
			BigDecimal divisor = new BigDecimal(count);
			BigDecimal dividend = new BigDecimal(records.size());
			BigDecimal divide = divisor.divide(dividend);
			result.put("accuracy", divide.doubleValue());
		} else if (isCreationOrArt) {
			Map<String, List<AnswerRecord>> group = records.parallelStream()
					.collect(Collectors.groupingBy(record -> record.getUserId()));
			Map<String, Integer> levels = Maps.newHashMap();
			group.forEach((userId, rs) -> {
				if (null == rs) {
					return;
				}
				Integer score = rs.stream().mapToInt(r -> answerRecordMaluationService.getScoreAtOneRecord(r.getId()))
						.sum();
				score = score / rs.size();
				String level = String.valueOf(Maluation.Level.getLevle(score));
				if (null == levels.get(level)) {
					levels.put(level, 1);
				} else {
					levels.put(level, levels.get(level) + 1);
				}
			});
			result.put("levels", levels);
		} else {
			Double score = records.parallelStream().mapToDouble(record -> record.getScore()).sum();
			score = null == score || score <= 0 ? 0 : score;
			result.put("average_score", score / records.size());
		}
		Map<String, List<AnswerRecord>> group = records.parallelStream()
				.collect(Collectors.groupingBy(record -> record.getUserId()));
		if (null != group) {
			result.put("people_num", group.size());
		}
		return result;
	}

	@Override
	public Map<Object, Object> getQuestionAnswerDetails(String cid, String qid, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, qid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		Question question = questionService.get(qid);
		if (null == question) {
			return null;
		}
		Map<Object, Object> result = Maps.newHashMap();
		int total = getStudentNum(cid);
		result.put("total", total);
		result.put("question_type", question.getQuestionType());
		Map<String, List<AnswerRecord>> group = records.parallelStream()
				.collect(Collectors.groupingBy(record -> record.getUserId()));
		int noAnswers = total - group.size();
		result.put("no_answers", noAnswers < 0 ? 0 : noAnswers);
		if (isSelect(question)) {
			Map<String, List<AnswerRecord>> gr = records.parallelStream()
					.filter(record -> !Strings.isNullOrEmpty(record.getAnswer()))
					.collect(Collectors.groupingBy(record -> record.getAnswer()));
			gr.forEach((ans, rs) -> {
				result.put(ans, null == rs ? 0 : rs.size());
			});
			List<Option> options = optionService.getByQuestion(question.getId());
			if (null != options) {
				List<Map<String, Object>> ops = Lists.newArrayList();
				options.forEach(option -> {
					Map<String, Object> op = Maps.newHashMap();
					op.put("title", option.getContent());
					op.put("value", option.getValue());
					op.put("is_true", option.getIsTrue());
					op.put("image", option.getOptionImage());
					op.put("xml_path", option.getXmlPath());
					ops.add(op);
				});
				result.put("options", ops);
			}
			List<Map<String, String>> students = getStudents(cid);
			if (null == students) {
				return result;
			}
			List<Map<String, String>> items = Lists.newArrayList();
			students.parallelStream().forEach(student -> {
				String id = student.getOrDefault("id", "");
				String name = student.getOrDefault("name", "");
				List<AnswerRecord> ars = group.get(id);
				if (null != ars && !ars.isEmpty()) {
					Map<String, String> item = Maps.newHashMap();
					item.put(name, ars.get(0).getAnswer());
					items.add(item);
				}
			});
			result.put("items", items);
		} else {
			Map<Float, List<AnswerRecord>> go = records.parallelStream()
					.collect(Collectors.groupingBy(record -> record.getScore()));
			go.forEach((score, rs) -> {
				result.put(null == score ? 0 : score, null == rs ? 0 : rs.size());
			});
		}
		return result;
	}

	private boolean isSelect(Question question) {
		return QuestionType.SINGLE_SELECT.equals(question.getQuestionType())
				|| QuestionType.MULTI_SELECT.equals(question.getQuestionType());
	}

	private Map<String, Object> json(AnswerRecord record) {
		Question question = record.getQuestion();
		if (null == question) {
			return null;
		}
		Map<String, Object> map = questionToJsonUtil.toJosn(question);
		map.put("answer_source", record.getAnswerSource());
		if (isSelect(question)) {
			map.put("is_true", record.getIsTrue());
		} else {
			map.put("score", record.getScore());
		}
		map.put("record_id", record.getId());
		map.put("exam_id", record.getExamId());
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Map<String, Object>> getTeacherMusicAbility(String sid, String gid, long start, long end) {
		Map<String, List<Map<String, String>>> teachersClasses = getTeachersClasses(sid, gid);
		if (null == teachersClasses) {
			return null;
		}
		System.out.println("------teachersClasses:" + teachersClasses);
		Map<String, Map<String, Object>> results = Maps.newHashMap();
		teachersClasses.forEach((tid, classes) -> {
			if (null == classes) {
				return;
			}
			Map<String, Object> result = Maps.newHashMap();
			results.put(tid, result);
			Double grasp = classes.stream().mapToDouble(cl -> getClassMusicAbility(cl.get("id"), start, end)).sum();
			result.put("grasp", grasp / classes.size());

			Map<String, Float> abilityMap = Maps.newHashMap();
			classes.forEach(cl -> {
				Map<String, Object> abilitys = getClassAbilityStatistics(cl.get("id"), start, end);
				if (null == abilitys) {
					return;
				}
				Map<String, Float> score = (Map<String, Float>) abilitys.get("average_score");
				if (null == score) {
					return;
				}
				score.forEach((key, value) -> {
					if (!abilityMap.keySet().contains(key)) {
						abilityMap.put(key, value);
					} else {
						abilityMap.put(key, abilityMap.get(key) + value);
					}
				});
			});
			abilityMap.forEach((key, value) -> {
				result.put(key, value / classes.size());
			});

			Map<String, Float> knowledgeMap = Maps.newHashMap();
			classes.forEach(cl -> {
				Map<String, Object> knowledges = getClassKnowledgeStatistics(cl.get("id"), start, end);
				if (null == knowledges) {
					return;
				}
				Map<String, Float> score = (Map<String, Float>) knowledges.get("average_score");
				if (null == score) {
					return;
				}
				score.forEach((key, value) -> {
					if (!knowledgeMap.keySet().contains(key)) {
						knowledgeMap.put(key, value);
					} else {
						knowledgeMap.put(key, knowledgeMap.get(key) + value);
					}
				});
			});
			knowledgeMap.forEach((key, value) -> {
				result.put(key, value / classes.size());
			});
		});
		return results;
	}

	@SuppressWarnings("unchecked")
	private Map<String, List<Map<String, String>>> getTeachersClasses(String sid, String gid) {
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_TEACHERS_CLASSES, Map.class, sid, gid);
		if (null != ret && null != ret.get("teachers_classes")) {
			Map<String, List<Map<String, String>>> teachersClasses = (Map<String, List<Map<String, String>>>) ret
					.get("teachers_classes");
			return teachersClasses;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getClassRanking(String cid, long start, long end) {
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_CLASS, Map.class, cid);
		if (null == ret || null == ret.get("id")) {
			return "";
		}
		String gid = (String) ret.getOrDefault("gid", "");
		String sid = (String) ret.getOrDefault("sid", "");
		if (Strings.isNullOrEmpty(gid) || Strings.isNullOrEmpty(sid)) {
			return "";
		}
		ret = restTemplate.getForObject(ApiClient.GET_CLASSES, Map.class, sid, gid);
		if (null == ret || null == ret.get("classes")) {
			return "";
		}
		Map<String, Integer> map = Maps.newHashMap();
		List<Map<String, String>> classes = (List<Map<String, String>>) ret.get("classes");
		classes.forEach(cls -> {
			String id = cls.get("id");
			Integer value = statisticService.getClassStatistics(id, end) / getStudentNum(id);
			map.put(id, value);
		});
		Integer me = map.get(cid);
		int ranking = 1;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() > me) {
				ranking++;
			}
		}
		return String.valueOf(ranking) + "/" + classes.size();
	}

}
