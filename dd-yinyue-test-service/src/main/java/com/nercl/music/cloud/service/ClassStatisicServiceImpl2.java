package com.nercl.music.cloud.service;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import com.nercl.music.cloud.entity.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.util.GraspValueUtil;
import com.nercl.music.util.QuestionToJsonUtil;


@Service
@Transactional
public class ClassStatisicServiceImpl2 implements ClassStatisicService2 {

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private GraspValueUtil graspValueUtil;

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ExamService examService;

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private Gson gson;

	@Value("${dd-yinyue.act.data}")
	private String act;

	@Autowired
	private QuestionToJsonUtil questionToJsonUtil;

	@Autowired
	private AnswerRecordMaluationService answerRecordMaluationService;

	private static final String STANDARD_NOTES = "standardNotes";

	private final static long TWO_WEEK_MILLIS = 2 * 7 * 24 * 3600 * 1000;

	@Override
	public Map<String, Object> getClassMusicTendency(String classId, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(classId, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		end = System.currentTimeMillis();
		Map<String, Object> result = Maps.newLinkedHashMap();
		int slice = (int) ((end - start) / TWO_WEEK_MILLIS);
		slice = slice < 1 ? 1 : slice;
		IntStream.range(1, slice + 2).forEach(t -> {
			long begin = start + t * TWO_WEEK_MILLIS;
			Double grasp = records.parallelStream().filter(record -> record.getTimestamp() <= begin)
					.mapToDouble(record -> graspValueUtil.getGraspValue(record)).sum();
			result.put(String.valueOf(begin), grasp.intValue());
		});
		return result;
	}

	@Override
	public Map<String, Object> getClassesMusicAbility(String[] cids, long start, long end) {
		Map<String, Object> result = Maps.newHashMap();
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
	public Map<String, Object> getClassKnowledgeStatistics(String cid, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		Map<String, Object> result = Maps.newHashMap();
		List<Knowledge> knowledges = knowledgeService.getTop();
		knowledges.forEach(knowledge -> {
			List<AnswerRecord> rs = records.parallelStream().filter(record -> {
				Question question = record.getQuestion();
				return null != question && Strings.nullToEmpty(question.getKnowledges()).contains(knowledge.getNo());
			}).collect(Collectors.toList());
			result.put(knowledge.getNo(), graspValueUtil.getGraspValue(rs) / getStudentNum(cid));
			result.put(knowledge.getNo() + "_exercise_amount", rs.size());
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
	public Map<String, Object> getClassAbilityStatistics(String cid, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		Map<String, Object> result = Maps.newHashMap();
		List<CompositeAbility> parents = Arrays.stream(CompositeAbility.values()).filter(ca -> null == ca.getParent())
				.collect(Collectors.toList());
		parents.forEach(parent -> {
			List<AnswerRecord> rs = records.parallelStream().filter(record -> {
				Question question = record.getQuestion();
				return null != question
						&& Strings.nullToEmpty(question.getCompositeAbilitys()).contains(String.valueOf(parent));
			}).collect(Collectors.toList());
			result.put(String.valueOf(parent), graspValueUtil.getGraspValue(rs) / getStudentNum(cid));
			result.put(String.valueOf(parent) + "_exercise_amount", rs.size());
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getClassStudentsMusicScore(String cid, long start, long end) {
		Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_CLASSSTUDENTS2, Map.class, cid);
		if (null == res || null == res.get("students")) {
			return null;
		}
		List<Map<String, Object>> students = (List<Map<String, Object>>) res.get("students");
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
		if (null == records || records.isEmpty()) {
			return students;
		}
		Map<String, List<AnswerRecord>> group = records.parallelStream()
				.collect(Collectors.groupingBy(record -> record.getUserId()));
		group.forEach((uid, rs) -> {
			students.forEach(student -> {
				String u = (String) student.get("id");
				if (uid.equals(u)) {
					Integer score = graspValueUtil.getGraspValue(rs);
					score = null == score ? 0 : score;
					student.put("score", score);
				}
			});
		});

//		students.stream().sorted((s1, s2) -> {
//		return  ((Integer) s1.get("score")- (Integer) s2.get("score"));
//		});
		List<Map<String,Object>> list= students.stream().sorted( Comparator.comparingInt(s-> (Integer) s.getOrDefault("score",0))).collect(Collectors.toList());
        Collections.reverse(list);
		return list;
	}

	@Override
	public List<Map<String, Object>> getClassMusicExamStatData(String cid, long start, long end) {
		List<ExamPaper> papers = examPaperService.get(cid, start, end);
		if (null == papers || papers.isEmpty()) {
			return null;
		}
		List<String> pids = papers.parallelStream().map(paper -> paper.getId()).collect(Collectors.toList());
		List<Map<String, Object>> results = Lists.newArrayList();
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		Map<String, List<AnswerRecord>> group = records.parallelStream()
				.filter(record -> !Strings.isNullOrEmpty(record.getExamPaperId()))
				.collect(Collectors.groupingBy(record -> record.getExamPaperId()));

		group.forEach((pid, rs) -> {
			if (!pids.contains(pid)) {
				return;
			}
			Map<String, Object> result = Maps.newHashMap();
			ExamPaper examPaper = examPaperService.get(pid);
			if (null != examPaper) {
				Integer score = rs.parallelStream().mapToInt(r -> {
					Float s = r.getScore();
					return null == s || s < 0 ? 0 : s.intValue();
				}).sum();
				result.put("eid", examPaper.getExamId());
				result.put("pid", pid);
				result.put("title", examPaper.getTitle());
				result.put("type", examPaper.getExamPaperType());
				@SuppressWarnings("unchecked")
				Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_CLASS_STUDENT_NUM2, Map.class, cid);
				if (null == res || null == res.get("num")) {
					result.put("total_people", 0);
				} else {
					result.put("total_people", (Integer) res.get("num"));
				}
				Integer num = answerRecordService.getAnswerUserNum(pid, cid);
				num = null == num || num < 1 ? 0 : num;
				result.put("answer_people_num", num);
				num = 0 == num ? 1 : num;
				result.put("average_score", (Integer) (score / num));
				results.add(result);
			}
		});
		papers.forEach(paper -> {
			if (!group.containsKey(paper.getId())) {
				Map<String, Object> result = Maps.newHashMap();
				result.put("eid", paper.getExamId());
				result.put("pid", paper.getId());
				result.put("title", paper.getTitle());
				result.put("type", paper.getExamPaperType());
				@SuppressWarnings("unchecked")
				Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_CLASS_STUDENT_NUM2, Map.class, cid);
				if (null == res || null == res.get("num")) {
					result.put("total_people", 0);
				} else {
					result.put("total_people", (Integer) res.get("num"));
				}
				result.put("answer_people_num", 0);
				result.put("average_score", 0);
				results.add(result);
			}
		});
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getExamUserScore(String cid, String eid) {
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_CLASSSTUDENTS2, Map.class, cid);
		if (null == ret || null == ret.get("students")) {
			return null;
		}
		List<Map<String, Object>> students = (List<Map<String, Object>>) ret.get("students");
		if (null == students || students.isEmpty()) {
			return null;
		}
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, eid);
		if (null == records || records.isEmpty()) {
			students.forEach(student -> {
				student.put("score", 0);
				student.put("joined_exam", false);
			});
			return students;
		}
		Map<String, List<AnswerRecord>> group = records.parallelStream()
				.collect(Collectors.groupingBy(r -> r.getUserId()));
		students.forEach(student -> {
			String uid = (String) student.get("id");
			List<AnswerRecord> rs = group.get(uid);
			if (null == rs || rs.isEmpty()) {
				student.put("score", 0);
				student.put("joined_exam", false);
				return;
			}
			student.put("joined_exam", true);
			student.put("score", rs.parallelStream().mapToInt(r -> {
				Float score = r.getScore();
				return null == score || score < 0 ? 0 : score.intValue();
			}).sum());
		});
		List<Map<String,Object>> list= students.stream().sorted( Comparator.comparingInt(s-> (Integer) s.getOrDefault("score",0))).collect(Collectors.toList());
        Collections.reverse(list);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getQuestionStatData(String cid, long start, long end) {
		Map<String, Object> result = Maps.newHashMap();
		List<String> parents = Arrays.stream(CompositeAbility.values()).filter(ca -> null == ca.getParent())
				.map(ca -> String.valueOf(ca)).collect(Collectors.toList());
		List<Question> questions = examService.getExamQuestions(cid, start, end);
		Map<String, Object> data = Maps.newHashMap();
		result.putIfAbsent("test", data);
		data.put("total", questions.size());
		questions.forEach(question -> {
			parents.forEach(parent -> {
				if (Strings.nullToEmpty(question.getCompositeAbilitys()).contains(parent)) {
					if (!data.containsKey(parent)) {
						data.put(parent, 1);
					} else {
						data.put(parent, (Integer) data.get(parent) + 1);
					}
				}
			});
		});

		questions = Lists.newArrayList();
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_TASK_QUESTIONS2, Map.class, cid, start, end);
		if (null != ret && null != ret.get("qids")) {
			List<String> qids = (List<String>) ret.get("qids");
			if (null != qids && !qids.isEmpty()) {
				questions.addAll(qids.stream().map(qid -> questionService.get(qid)).collect(Collectors.toList()));
			}
		}
		Map<String, Object> data2 = Maps.newHashMap();
		result.putIfAbsent("task", data2);
		data2.put("total", questions.size());
		questions.forEach(question -> {
			parents.forEach(parent -> {
				if (Strings.nullToEmpty(question.getCompositeAbilitys()).contains(parent)) {
					if (!data2.containsKey(parent)) {
						data2.put(parent, 1);
					} else {
						data2.put(parent, (Integer) data2.get(parent) + 1);
					}
				}
			});
		});

		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
		Map<String, Object> data3 = Maps.newHashMap();
		result.putIfAbsent("exercise", data3);
		if (null == records || records.isEmpty()) {
			data3.put("total", 0);
			return result;
		}
		Map<String, List<AnswerRecord>> group = records.parallelStream()
				.filter(record -> AnswerSource.EXERCISE.equals(record.getAnswerSource()))
				.collect(Collectors.groupingBy(record -> String.valueOf(record.getQuestionId())));
		data3.put("total", group.keySet().size());
		group.forEach((key, rs) -> {
			Question question = rs.get(0).getQuestion();
			parents.forEach(parent -> {
				if (Strings.nullToEmpty(question.getCompositeAbilitys()).contains(parent)) {
					if (!data3.containsKey(parent)) {
						data3.put(parent, 1);
					} else {
						data3.put(parent, (Integer) data3.get(parent) + 1);
					}
				}
			});
		});
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getActQuestionNoteStatData(String cid, long start, long end) {
		Map<String, Object> result = Maps.newHashMap();
		List<Question> questions = examService.getExamQuestions(cid, start, end);
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_TASK_QUESTIONS2, Map.class, cid, start, end);
		if (null != ret && null != ret.get("qids")) {
			List<String> qids = (List<String>) ret.get("qids");
			if (null != qids && !qids.isEmpty()) {
				questions.addAll(qids.stream().map(qid -> questionService.get(qid)).collect(Collectors.toList()));
			}
		}
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
		if (null != records && !records.isEmpty()) {
			Map<String, List<AnswerRecord>> group = records.parallelStream()
					.filter(record -> AnswerSource.EXERCISE.equals(record.getAnswerSource()))
					.collect(Collectors.groupingBy(record -> String.valueOf(record.getQuestionId())));
			if (null != group) {
				questions.addAll(
						group.keySet().stream().map(qid -> questionService.get(qid)).collect(Collectors.toList()));
			}
		}
		Map<String, Object> sightSinging = Maps.newHashMap();
		Map<String, Object> sing = Maps.newHashMap();
		Map<String, Object> behindBackSing = Maps.newHashMap();
		Map<String, Object> behindBackPerformance = Maps.newHashMap();
		Map<String, Object> performance = Maps.newHashMap();
		String countKey = "question_count";
		questions.forEach(question -> {
			List<AnswerRecord> rs = answerRecordService.getClassRecord(cid, question.getId(), start, end);
			if (QuestionType.SIGHT_SINGING.equals(question.getQuestionType())) {
				result.putIfAbsent("SIGHT_SINGING", sightSinging);
				increaseCountKey(sightSinging, countKey);
				statPitchInterval(sightSinging, rs);
			} else if (QuestionType.SING.equals(question.getQuestionType())) {
				result.putIfAbsent("SING", Maps.newHashMap());
				increaseCountKey(sing, countKey);
				statPitchInterval(sing, rs);
			} else if (QuestionType.BEHIND_BACK_SING.equals(question.getQuestionType())) {
				result.putIfAbsent("BEHIND_BACK_SING", behindBackSing);
				increaseCountKey(behindBackSing, countKey);
				statPitchInterval(behindBackSing, rs);
			} else if (QuestionType.BEHIND_BACK_PERFORMANCE.equals(question.getQuestionType())) {
				result.putIfAbsent("BEHIND_BACK_PERFORMANCE", behindBackPerformance);
				increaseCountKey(behindBackPerformance, countKey);
				statPitchInterval(behindBackPerformance, rs);
			} else if (QuestionType.PERFORMANCE.equals(question.getQuestionType())) {
				result.putIfAbsent("PERFORMANCE", Maps.newHashMap());
				increaseCountKey(performance, countKey);
				statPitchInterval(performance, rs);
			}
		});
		return result;
	}

	@Override
	public List<Map<String, Object>> getActSongs(String cid, String question_type, String answer_source, long start,
			long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		List<AnswerRecord> rs = records.stream()
				.filter(record -> String.valueOf(record.getAnswerSource()).equals(answer_source)).filter(record -> {
					Question question = record.getQuestion();
					return null != question && String.valueOf(question.getQuestionType()).equals(question_type);
				}).collect(Collectors.toList());
		if (null == rs || rs.isEmpty()) {
			return null;
		}
		Map<String, List<AnswerRecord>> group = rs.stream()
				.collect(Collectors.groupingBy(record -> record.getQuestionId()));
		Set<String> qids = group.keySet();
		List<Map<String, Object>> qus = qids.stream().map(qid -> {
			Map<String, Object> qu = Maps.newHashMap();
			Question question = questionService.get(qid);
			if (null != question) {
				qu.put("id", qid);
				qu.put("tilte", question.getTitle());
				return qu;
			}
			return null;
		}).collect(Collectors.toList());
		return qus.stream().filter(qu -> null != qu).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getActStudents(String cid, String sid, String answer_source, long start,
			long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		List<AnswerRecord> rs = records.stream()
				.filter(record -> String.valueOf(record.getAnswerSource()).equals(answer_source)).filter(record -> {
					Question question = record.getQuestion();
					return null != question && question.getId().equals(sid);
				}).collect(Collectors.toList());
		if (null == rs || rs.isEmpty()) {
			return null;
		}
		Map<String, List<AnswerRecord>> group = rs.stream()
				.collect(Collectors.groupingBy(record -> record.getUserId()));
		Set<String> uids = group.keySet();
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_USERS, Map.class, Joiner.on(",").join(uids));
		if (null == ret || null == ret.get("users")) {
			return null;
		}
		List<Map<String, Object>> users = (List<Map<String, Object>>) ret.get("users");
		users.forEach(user -> {
			user.remove("phone");
			user.remove("email");
			user.remove("age");
			user.remove("gender");
		});
		return users;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getHotChart(String cid, String sid, String uid, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		List<AnswerRecord> rs = records.stream().filter(record -> {
			Question question = record.getQuestion();
			return null != question && question.getId().equals(sid);
		}).collect(Collectors.toList());
		if (null == rs || rs.isEmpty()) {
			return null;
		}
		Map<String, Object> data = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			statSpeedAndStandardNotes(data, rs.get(0));
			statEveryNote(data, (Map<String, String>) data.get(STANDARD_NOTES), rs);
		} else {
			List<AnswerRecord> rrs = rs.stream().filter(r -> uid.equals(r.getUserId())).collect(Collectors.toList());
			statSpeedAndStandardNotes(data, rrs.get(0));
			statEveryNote(data, (Map<String, String>) data.get(STANDARD_NOTES), rrs);
		}
		return data;
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

	/**
	 * 根据知识点查询试题
	 * @param cid 班级id
	 * @param knowledgeNo 知识点编号
	 * @param source 题目来源
	 * @param start 学期开始
	 * @param end	学期结束
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getQuestionsByKnowledge(String cid, String knowledgeNo, AnswerSource source,
			long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(cid, source, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> questions = Lists.newArrayList();
		//并行执行流,过滤筛选出question不为空,以及question中knowledges属性字符串是否包含knowledgeNo对应的值
		records.parallelStream().filter(record -> {
			Question question = record.getQuestion();
//			return null != question && Strings.nullToEmpty(question.getKnowledges()).contains(knowledgeNo);
			//因为全局只有一处调用该方法,所以根据前端测试综合测评 -> 音乐知识 只显示音乐常识和乐理类型的需求,现在将其改为只包含音乐常识和乐理类型
			return null != question && Strings.nullToEmpty(question.getKnowledges()).contains(knowledgeNo)&&(SubjectType.GENERAL_KNOWLEDGE.equals(question.getSubjectType())||SubjectType.THEORY.equals(question.getSubjectType()));
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
			long count = records.parallelStream().filter(record -> null != record.getIsTrue() && record.getIsTrue())
					.count();
			result.put("accuracy", count * 100 / records.size());
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

	private void increaseCountKey(Map<String, Object> data, String countKey) {
		if (!data.containsKey(countKey)) {
			data.put(countKey, 1);
		} else {
			data.put(countKey, (Integer) data.get(countKey) + 1);
		}
	}

	@SuppressWarnings("unchecked")
	private void statPitchInterval(Map<String, Object> data, List<AnswerRecord> records) {
		if (null == records || records.isEmpty()) {
			return;
		}
		records.forEach(record -> {
			File json = new File(act + File.separator + record.getId() + ".json");
			if (!json.exists()) {
				return;
			}
			String s = null;
			try {
				s = FileUtils.readFileToString(json, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (Strings.isNullOrEmpty(s)) {
				return;
			}
			Map<String, Object> map = gson.fromJson(s, Map.class);
			Integer noteCount = (Integer) map.getOrDefault("NoteCount", 0);
			Integer sampleSpeed = (Integer) map.getOrDefault("SampleSpeed", 0);
			Integer voiceSpeed = (Integer) map.getOrDefault("VoiceSpeed", 0);
			Integer pitchAccuracy = (Integer) map.getOrDefault("PitchAccuracy", 0);
			Integer rhythmAccuracy = (Integer) map.getOrDefault("RhythmAccuracy", 0);
			Integer accuracy = (Integer) map.getOrDefault("Accuracy", 0);
			if (!data.containsKey("note_count")) {
				data.put("note_count", noteCount);
			} else {
				data.put("note_count", (Integer) data.get("noteCount") + noteCount);
			}
			if (!data.containsKey("standard_speed")) {
				data.put("standard_speed", sampleSpeed);
			} else {
				data.put("standard_speed", (Integer) data.get("standard_speed") + sampleSpeed);
			}
			if (!data.containsKey("actual_speed")) {
				data.put("actual_speed", voiceSpeed);
			} else {
				data.put("actual_speed", (Integer) data.get("actual_speed") + voiceSpeed);
			}
			if (!data.containsKey("pitch_accuracy")) {
				data.put("pitch_accuracy", pitchAccuracy);
			} else {
				data.put("pitch_accuracy", (Integer) data.get("pitch_accuracy") + pitchAccuracy);
			}
			if (!data.containsKey("rhythm_accuracy")) {
				data.put("rhythm_accuracy", rhythmAccuracy);
			} else {
				data.put("rhythm_accuracy", (Integer) data.get("rhythm_accuracy") + rhythmAccuracy);
			}
			if (!data.containsKey("complete")) {
				data.put("complete", accuracy);
			} else {
				data.put("complete", (Integer) data.get("complete") + accuracy);
			}
		});
		Integer noteCount = (Integer) data.getOrDefault("note_count", 0);
		data.put("note_count", (Integer) (noteCount / records.size()));

		Integer standardSpeed = (Integer) data.getOrDefault("standard_speed", 0);
		data.put("standard_speed", (Integer) (standardSpeed / records.size()));

		Integer actualSpeed = (Integer) data.getOrDefault("actual_speed", 0);
		data.put("actual_speed", (Integer) (actualSpeed / records.size()));

		Integer pitchAccuracy = (Integer) data.getOrDefault("pitch_accuracy", 0);
		data.put("pitch_accuracy", (Integer) (pitchAccuracy / records.size()));

		Integer rhythmAccuracy = (Integer) data.getOrDefault("rhythm_accuracy", 0);
		data.put("rhythm_accuracy", (Integer) (rhythmAccuracy / records.size()));

		Integer accuracy = (Integer) data.getOrDefault("complete", 0);
		data.put("complete", (Integer) (accuracy / records.size()));
	}

	@SuppressWarnings("unchecked")
	private void statSpeedAndStandardNotes(Map<String, Object> data, AnswerRecord record) {
		File json = new File(act + File.separator + record.getId() + ".json");
		if (!json.exists()) {
			return;
		}
		String s = null;
		try {
			s = FileUtils.readFileToString(json, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Strings.isNullOrEmpty(s)) {
			return;
		}
		Map<String, String> standardNotes = Maps.newHashMap();
		data.put(STANDARD_NOTES, standardNotes);
		Map<String, Object> map = gson.fromJson(s, Map.class);
		List<Map<String, Object>> sms = (List<Map<String, Object>>) map.get("SourceMusic");
		if (null == sms || sms.isEmpty()) {
			return;
		}
		sms.stream().forEach(sm -> {
			String no = (String) sm.get("SequenceNo");
			String note = (String) sm.get("NoteName");
			if (Strings.isNullOrEmpty(no) || Strings.isNullOrEmpty(note)) {
				return;
			}
			standardNotes.put(no, note);
		});
		Integer sampleSpeed = (Integer) map.getOrDefault("SampleSpeed", 0);
		Integer voiceSpeed = (Integer) map.getOrDefault("VoiceSpeed", 0);
		data.put("standard_speed", sampleSpeed);
		data.put("actual_speed", voiceSpeed);
	}

	@SuppressWarnings("unchecked")
	private void statEveryNote(Map<String, Object> data, Map<String, String> standardNotes,
			List<AnswerRecord> records) {
		if (null == records || records.isEmpty()) {
			return;
		}
		Map<String, Note> notes = Maps.newHashMap();
		standardNotes.forEach((no, name) -> {
			notes.put(no, new Note(name, 0, 0));
		});
		records.parallelStream().forEach(record -> {
			File json = new File(act + File.separator + record.getId() + ".json");
			if (!json.exists()) {
				return;
			}
			String s = null;
			try {
				s = FileUtils.readFileToString(json, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (Strings.isNullOrEmpty(s)) {
				return;
			}
			Map<String, Object> map = gson.fromJson(s, Map.class);
			List<Map<String, Object>> vms = (List<Map<String, Object>>) map.get("VoiceMusic");
			if (null == vms || vms.isEmpty()) {
				return;
			}
			vms.parallelStream().forEach(vm -> {
				String no = (String) vm.get("SequenceNo");
				if (Strings.isNullOrEmpty(no) || !notes.containsKey(no)) {
					return;
				}
				String noteName = (String) vm.getOrDefault("NoteName", "");
				if (Strings.isNullOrEmpty(noteName)) {
					return;
				}
				Note note = notes.get(no);
				if (noteName.equals(note.getName())) {
					Integer rhythmAccuracy = (Integer) vm.getOrDefault("RhythmAccuracy", 0);
					Integer pitchAccuracy = (Integer) vm.getOrDefault("PitchAccuracy", 0);
					note.setRhythmAccuracy(rhythmAccuracy + note.getRhythmAccuracy());
					note.setPitchAccuracy(pitchAccuracy + note.getPitchAccuracy());
					note.setRightNum(note.getRightNum() + 1);
				} else {
					Map<String, Integer> wrongNotes = note.getWrongNotes();
					if (wrongNotes.containsKey(noteName)) {
						wrongNotes.put(noteName, wrongNotes.get(noteName) + 1);
					} else {
						wrongNotes.put(noteName, 1);
					}
				}
			});
		});
		List<Map<String, Object>> ns = Lists.newArrayList();
		notes.forEach((no, note) -> {
			Map<String, Object> n = Maps.newHashMap();
			n.put("name", note.getName());
			Integer rhythmAccuracy = note.getRhythmAccuracy();
			Integer pitchAccuracy = note.getPitchAccuracy();
			note.setRhythmAccuracy(rhythmAccuracy / records.size());
			note.setPitchAccuracy(pitchAccuracy / records.size());
			n.put("pitch_accuracy", note.getPitchAccuracy());
			n.put("interval_accuracy", note.getRhythmAccuracy());
			n.put("right_num", note.getRightNum());
			n.put("wrong_notes", note.getWrongNotes());
			ns.add(n);
		});
		data.put("notes", ns);
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

	private boolean isSelect(Question question) {
		return QuestionType.SINGLE_SELECT.equals(question.getQuestionType())
				|| QuestionType.MULTI_SELECT.equals(question.getQuestionType());
	}

	class Note {

		private String no;

		private String name;

		private Integer rhythmAccuracy;

		private Integer pitchAccuracy;

		private Integer rightNum;

		private Map<String, Integer> wrongNotes = Maps.newHashMap();

		public Note(String name, Integer rhythmAccuracy, Integer pitchAccuracy) {
			this.name = name;
			this.rhythmAccuracy = rhythmAccuracy;
			this.pitchAccuracy = pitchAccuracy;
		}

		public String getNo() {
			return no;
		}

		public void setNo(String no) {
			this.no = no;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getRhythmAccuracy() {
			return rhythmAccuracy;
		}

		public void setRhythmAccuracy(Integer rhythmAccuracy) {
			this.rhythmAccuracy = rhythmAccuracy;
		}

		public Integer getPitchAccuracy() {
			return pitchAccuracy;
		}

		public void setPitchAccuracy(Integer pitchAccuracy) {
			this.pitchAccuracy = pitchAccuracy;
		}

		public Integer getRightNum() {
			return rightNum;
		}

		public void setRightNum(Integer rightNum) {
			this.rightNum = rightNum;
		}

		public Map<String, Integer> getWrongNotes() {
			return wrongNotes;
		}
	}
}
