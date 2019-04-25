package com.nercl.music.cloud.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.CompositeAbility;
import com.nercl.music.cloud.entity.Exam;
import com.nercl.music.cloud.entity.Exam.ExamType;
import com.nercl.music.cloud.entity.ExamPaper;
import com.nercl.music.cloud.entity.ExamPaperType;
import com.nercl.music.cloud.entity.Knowledge;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.util.GraspValueUtil;

@Service
@Transactional
public class StatisticServiceImpl2 implements StatisticService2 {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private ExamService examService;

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private ExamPaperQuestionService examPaperQuestionService;

	@Autowired
	private GraspValueUtil graspValueUtil;

	private final static long TWO_WEEK_MILLIS = 2 * 7 * 24 * 3600 * 1000;

	private final static double PASS_LINE = 0.6;

	private final static double SEVENTY_UNDER_LINE = 0.7;

	private final static double OUTSTANDING_LINE = 0.8;

	private final static double NINETY_UNDER_LINE = 0.9;

	@Override
	public Map<String, Object> getGradeMusicTendency(String sid, String gid, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getGradeRecord(gid, sid, start, end);
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
	public Map<String, Object> getGradeAbilityAverageScore(String sid, String gid, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getGradeRecord(gid, sid, start, end);
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
			result.put(String.valueOf(parent),
					(Integer) ((graspValueUtil.getGraspValue(rs) / getStudentNum(gid, sid))));
		});
		return result;
	}

	@Override
	public Map<String, Object> getGradeKnowledgeAverageScore(String sid, String gid, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getGradeRecord(gid, sid, start, end);
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
			result.put(knowledge.getNo(), (Integer) (graspValueUtil.getGraspValue(rs) / getStudentNum(gid, sid)));
		});
		return result;
	}

	@Override
	public List<Map<String, Object>> getGradeMusicExamStatData(String sid, String gid, long start, long end) {
		List<Map<String, Object>> results = Lists.newArrayList();
		List<AnswerRecord> records = answerRecordService.getGradeRecord(gid, sid, start, end);
		if (null == records || records.isEmpty()) {
			return null;
		}
		Map<Optional<String>, List<AnswerRecord>> group = records.parallelStream()
				.collect(Collectors.groupingBy(record -> Optional.ofNullable(record.getExamPaperId())));
		group.forEach((pid, rs) -> {
			Map<String, Object> result = Maps.newHashMap();
			if (pid.isPresent()) {
				ExamPaper examPaper = examPaperService.get(pid.get());
				if (null != examPaper && ExamPaperType.MUSIC_ABILITY == examPaper.getExamPaperType()) {
					Integer score = graspValueUtil.getGraspValue(rs);
					result.put("eid", examPaper.getExamId());
					result.put("pid", pid);
					result.put("title", examPaper.getTitle());
					result.put("type", examPaper.getExamPaperType());
					@SuppressWarnings("unchecked")
					Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_GRADE_STUDENT_NUM2, Map.class,
							sid, gid);
					if (null == res || null == res.get("num")) {
						result.put("total_people", 0);
					} else {
						result.put("total_people", (Integer) res.get("num"));
					}
					Integer num = answerRecordService.getAnswerUserNum(pid.get(), sid, gid);
					num = num < 1 ? 1 : num;
					result.put("answer_people_num", num);
					result.put("average_score", (Integer) (score / num));
					results.add(result);
				}
			}
		});
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getTeacherMusicMiddleExamStatData(String sid, String gcode, long start, long end) {
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_TEACHER_INFO, Map.class, sid);
		if (null == ret || null == ret.get("teacher_class_num")) {
			return null;
		}
		List<Map<String, Object>> teacherClasses = (List<Map<String, Object>>) ret.get("teacher_class_num");
		if (null == teacherClasses || teacherClasses.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> tcs = teacherClasses.parallelStream()
				.filter(tc -> gcode.equals(tc.getOrDefault("gcode", ""))).collect(Collectors.toList());
		List<Exam> exams = examService.list(sid, gcode, start, end);
		if (null == exams || exams.isEmpty()) {
			return null;
		}
		Map<String, Integer> abilityPassMap = Maps.newHashMap();
		Map<String, Integer> abilityOutStandingMap = Maps.newHashMap();
		Map<String, Integer> abilityAverageMap = Maps.newHashMap();
		Map<String, Integer> abilityTestNumMap = Maps.newHashMap();
		Map<String, Integer> actPassMap = Maps.newHashMap();
		Map<String, Integer> actOutStandingMap = Maps.newHashMap();
		Map<String, Integer> actAverageMap = Maps.newHashMap();
		Map<String, Integer> actTestNumMap = Maps.newHashMap();
		tcs.forEach(tc -> {
			String tid = (String) tc.getOrDefault("id", "");
			if (Strings.isNullOrEmpty(tid)) {
				return;
			}
			List<String> cids = (List<String>) tc.get("class_ids");
			if (null == cids || cids.isEmpty()) {
				return;
			}
			exams.parallelStream().filter(exam -> ExamType.MIDDLE_TERM_TEST == exam.getExamType()).forEach(exam -> {
				List<ExamPaper> papers = examPaperService.getByExam(exam.getId());
				if (null == papers || papers.isEmpty()) {
					return;
				}
				papers.parallelStream().forEach(paper -> {
					Integer fullScore = paper.getScore();
					cids.parallelStream().forEach(cid -> {
						Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_CLASS_STUDENTS_V2, Map.class,
								cid);
						if (null == res || null == res.get("students")) {
							return;
						}
						List<Map<String, Object>> students = (List<Map<String, Object>>) res.get("students");
						students.parallelStream().forEach(student -> {
							String uid = (String) student.getOrDefault("id", "");
							List<AnswerRecord> records = answerRecordService.get(uid, exam.getId(), paper.getId());
							if (null == records || records.isEmpty()) {
								return;
							}
							Integer score = records.parallelStream()
									.mapToInt(record -> null == record.getScore() ? 0 : record.getScore().intValue())
									.sum();
							if (ExamPaperType.MUSIC_ABILITY == paper.getExamPaperType()) {
								if (null != score && score >= fullScore * PASS_LINE) {
									Integer count = abilityPassMap.getOrDefault(tid, 0);
									abilityPassMap.put(tid, ++count);
								}
								if (null != score && score >= fullScore * OUTSTANDING_LINE) {
									Integer count = abilityOutStandingMap.getOrDefault(tid, 0);
									abilityOutStandingMap.put(tid, ++count);
								}
								Integer total = abilityAverageMap.getOrDefault(tid, 0);
								abilityAverageMap.put(tid, total + score);
								Integer testNum = abilityTestNumMap.getOrDefault(tid, 0);
								abilityTestNumMap.put(tid, ++testNum);
							} else {
								if (null != score && score >= fullScore * PASS_LINE) {
									Integer count = actPassMap.getOrDefault(tid, 0);
									actPassMap.put(tid, ++count);
								}
								if (null != score && score >= fullScore * OUTSTANDING_LINE) {
									Integer count = actOutStandingMap.getOrDefault(tid, 0);
									actOutStandingMap.put(tid, ++count);
								}
								Integer total = actAverageMap.getOrDefault(tid, 0);
								actAverageMap.put(tid, total + score);
								Integer testNum = actTestNumMap.getOrDefault(tid, 0);
								actTestNumMap.put(tid, ++testNum);
							}
						});
					});
				});
			});
			Map<String, Integer> abilityTestData = Maps.newHashMap();
			Integer abilityAverage = abilityAverageMap.get(tid);
			abilityAverage = null == abilityAverage ? 0 : abilityAverage;
			Integer abilityTestNum = abilityTestNumMap.get(tid);
			abilityTestNum = null == abilityTestNum ? 1 : abilityTestNum;
			abilityTestData.put("ability_average", abilityAverage / abilityTestNum);

			Integer abilityPass = abilityPassMap.get(tid);
			abilityPass = null == abilityPass ? 0 : abilityPass;
			abilityTestData.put("ability_pass", abilityPass / abilityTestNum * 100);

			Integer abilityOutStanding = abilityOutStandingMap.get(tid);
			abilityOutStanding = null == abilityOutStanding ? 0 : abilityOutStanding;
			abilityTestData.put("ability_outstanding", abilityOutStanding / abilityTestNum * 100);
			tc.put("ability_test", abilityTestData);

			Map<String, Integer> actTestData = Maps.newHashMap();

			Integer actAverage = actAverageMap.get(tid);
			actAverage = null == actAverage ? 0 : actAverage;
			Integer actTestNum = actTestNumMap.get(tid);
			actTestNum = null == actTestNum ? 1 : actTestNum;
			actTestData.put("act_average", actAverage / actTestNum);

			Integer actPass = actPassMap.get(tid);
			actPass = null == actPass ? 0 : actPass;
			actTestData.put("act_pass", actPass / actTestNum * 100);

			Integer actOutStanding = actOutStandingMap.get(tid);
			actOutStanding = null == actOutStanding ? 0 : actOutStanding;
			actTestData.put("act_outstanding", actOutStanding / actTestNum * 100);
			tc.put("act_test", actTestData);

			Map<String, Integer> testData = Maps.newHashMap();
			testData.put("average", (abilityAverage + actAverage) / (abilityTestNum + actTestNum));

			testData.put("pass", (abilityPass + actPass) / (abilityTestNum + actTestNum) * 100);

			testData.put("outstanding", (abilityOutStanding + actOutStanding) / (abilityTestNum + actTestNum) * 100);
			tc.put("test", testData);
		});
		return tcs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getTeacherMusicFinalExamStatData(String sid, String gcode, long start, long end) {
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_TEACHER_INFO, Map.class, sid);
		if (null == ret || null == ret.get("teacher_class_num")) {
			return null;
		}
		List<Map<String, Object>> teacherClasses = (List<Map<String, Object>>) ret.get("teacher_class_num");
		if (null == teacherClasses || teacherClasses.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> tcs = teacherClasses.parallelStream()
				.filter(tc -> gcode.equals(tc.getOrDefault("gcode", ""))).collect(Collectors.toList());
		List<Exam> exams = examService.list(sid, gcode, start, end);
		if (null == exams || exams.isEmpty()) {
			return null;
		}
		Map<String, Integer> abilityPassMap = Maps.newHashMap();
		Map<String, Integer> abilityOutStandingMap = Maps.newHashMap();
		Map<String, Integer> abilityAverageMap = Maps.newHashMap();
		Map<String, Integer> abilityTestNumMap = Maps.newHashMap();
		Map<String, Integer> actPassMap = Maps.newHashMap();
		Map<String, Integer> actOutStandingMap = Maps.newHashMap();
		Map<String, Integer> actAverageMap = Maps.newHashMap();
		Map<String, Integer> actTestNumMap = Maps.newHashMap();
		tcs.forEach(tc -> {
			String tid = (String) tc.getOrDefault("id", "");
			if (Strings.isNullOrEmpty(tid)) {
				return;
			}
			List<String> cids = (List<String>) tc.get("class_ids");
			if (null == cids || cids.isEmpty()) {
				return;
			}
			exams.parallelStream().filter(exam -> ExamType.FINAL_TERM_TEST == exam.getExamType()).forEach(exam -> {
				List<ExamPaper> papers = examPaperService.getByExam(exam.getId());
				if (null == papers || papers.isEmpty()) {
					return;
				}
				papers.parallelStream().forEach(paper -> {
					Integer fullScore = paper.getScore();
					cids.parallelStream().forEach(cid -> {
						Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_CLASS_STUDENTS_V2, Map.class,
								cid);
						if (null == res || null == res.get("students")) {
							return;
						}
						List<Map<String, Object>> students = (List<Map<String, Object>>) res.get("students");
						students.parallelStream().forEach(student -> {
							String uid = (String) student.getOrDefault("id", "");
							List<AnswerRecord> records = answerRecordService.get(uid, exam.getId(), paper.getId());
							if (null == records || records.isEmpty()) {
								return;
							}
							Integer score = records.parallelStream()
									.mapToInt(record -> null == record.getScore() ? 0 : record.getScore().intValue())
									.sum();
							if (ExamPaperType.MUSIC_ABILITY == paper.getExamPaperType()) {
								if (null != score && score >= fullScore * PASS_LINE) {
									Integer count = abilityPassMap.getOrDefault(tid, 0);
									abilityPassMap.put(tid, ++count);
								}
								if (null != score && score >= fullScore * OUTSTANDING_LINE) {
									Integer count = abilityOutStandingMap.getOrDefault(tid, 0);
									abilityOutStandingMap.put(tid, ++count);
								}
								Integer total = abilityAverageMap.getOrDefault(tid, 0);
								abilityAverageMap.put(tid, total + score);
								Integer testNum = abilityTestNumMap.getOrDefault(tid, 0);
								abilityTestNumMap.put(tid, ++testNum);
							} else {
								if (null != score && score >= fullScore * PASS_LINE) {
									Integer count = actPassMap.getOrDefault(tid, 0);
									actPassMap.put(tid, ++count);
								}
								if (null != score && score >= fullScore * OUTSTANDING_LINE) {
									Integer count = actOutStandingMap.getOrDefault(tid, 0);
									actOutStandingMap.put(tid, ++count);
								}
								Integer total = actAverageMap.getOrDefault(tid, 0);
								actAverageMap.put(tid, total + score);
								Integer testNum = actTestNumMap.getOrDefault(tid, 0);
								actTestNumMap.put(tid, ++testNum);
							}
						});
					});
				});
			});
			Map<String, Integer> abilityTestData = Maps.newHashMap();
			Integer abilityAverage = abilityAverageMap.get(tid);
			abilityAverage = null == abilityAverage ? 0 : abilityAverage;

			Integer abilityTestNum = abilityTestNumMap.get(tid);
			abilityTestNum = null == abilityTestNum ? 1 : abilityTestNum;

			abilityTestData.put("ability_average", abilityAverage / abilityTestNum);

			Integer abilityPass = abilityPassMap.get(tid);
			abilityPass = null == abilityPass ? 0 : abilityPass;

			abilityTestData.put("ability_pass", abilityPass / abilityTestNum * 100);

			Integer abilityOutStanding = abilityOutStandingMap.get(tid);
			abilityOutStanding = null == abilityOutStanding ? 0 : abilityOutStanding;
			abilityTestData.put("ability_outstanding", abilityOutStanding / abilityTestNum * 100);
			tc.put("ability_test", abilityTestData);

			Map<String, Integer> actTestData = Maps.newHashMap();
			Integer actAverage = actAverageMap.get(tid);
			actAverage = null == actAverage ? 0 : actAverage;

			Integer actTest = actTestNumMap.get(tid);
			actTest = null == actTest ? 1 : actTest;
			actTestData.put("act_average", actAverage / actTest);

			Integer actPass = actPassMap.get(tid);
			actTestData.put("act_pass", actPass / actTest * 100);

			Integer actOutStanding = actOutStandingMap.get(tid);
			actOutStanding = null == actOutStanding ? 1 : actOutStanding;
			actTestData.put("act_outstanding", actOutStanding / actTest * 100);

			tc.put("act_test", actTestData);
			Map<String, Integer> testData = Maps.newHashMap();

			testData.put("average", (abilityAverage + actAverage) / (abilityTestNum + actTest));
			testData.put("pass", (abilityPass + actPass) / (abilityTestNum + actTest) * 100);
			testData.put("outstanding", (abilityOutStanding + actOutStanding) / (abilityTestNum + actTest) * 100);
			tc.put("test", testData);
		});
		return tcs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getTeacherClassBasicData(String sid, String gcode, long start, long end) {
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_TEACHER_INFO, Map.class, sid);
		if (null == ret || null == ret.get("teacher_class_num")) {
			return null;
		}
		List<Map<String, Object>> teacherClasses = (List<Map<String, Object>>) ret.get("teacher_class_num");
		if (null == teacherClasses || teacherClasses.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> tcs = teacherClasses.parallelStream()
				.filter(tc -> gcode.equals(tc.getOrDefault("gcode", ""))).collect(Collectors.toList());
		tcs.forEach(tc -> {
			List<String> cids = (List<String>) tc.get("class_ids");
			tc.put("teach_class_time", 0);
			tc.put("tool_used_time", 0);
			tc.put("exercise_distribute_time", 0);
			tc.put("file_translated_time", 0);
			tc.put("task_distribute_time", getTaskDistributeTime(cids));
			tc.put("task_commented_time", getTaskCommentedTime(cids));
			tc.put("chapter_test_time", getChapterTestTime(sid, gcode, start, end));
			tc.put("tounament_time", 0);
			tc.put("act_time", 0);
			tc.put("open_class_time", 0);
			tc.put("task_creating_num", getTaskCreatingNum(cids));
			tc.put("test_creating_num", getTestCreatingNum(sid, gcode, start, end));
		});
		return tcs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getTeacherClassMusicStatData(String sid, String gcode, long start, long end) {
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_TEACHER_INFO, Map.class, sid);
		if (null == ret || null == ret.get("teacher_class_num")) {
			return null;
		}
		List<Map<String, Object>> teacherClasses = (List<Map<String, Object>>) ret.get("teacher_class_num");
		if (null == teacherClasses || teacherClasses.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> tcs = teacherClasses.parallelStream()
				.filter(tc -> gcode.equals(tc.getOrDefault("gcode", ""))).collect(Collectors.toList());
		Map<String, Integer> passMap = Maps.newHashMap();
		Map<String, Integer> outstandingMap = Maps.newHashMap();
		Map<String, Integer> sixtyUnderMap = Maps.newHashMap();
		Map<String, Integer> seventyUnderMap = Maps.newHashMap();
		Map<String, Integer> eightyUnderMap = Maps.newHashMap();
		Map<String, Integer> ninetyUnderMap = Maps.newHashMap();
		Map<String, Integer> ninetyOverMap = Maps.newHashMap();
		tcs.forEach(tc -> {
			String tid = (String) tc.getOrDefault("id", "");
			if (Strings.isNullOrEmpty(tid)) {
				return;
			}
			Integer num = (Integer) tc.get("student_num");
			if (null == num || num <= 0) {
				return;
			}
			List<String> cids = (List<String>) tc.get("class_ids");
			if (null == cids || cids.isEmpty()) {
				return;
			}
			List<AnswerRecord> records = answerRecordService.getClassRecord(cids, start, end);
			if (null == records || records.isEmpty()) {
				return;
			}
			Integer graps = graspValueUtil.getGraspValue(records);
			tc.put("average", (Integer) (graps / num));
			cids.parallelStream().forEach(cid -> {
				Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_CLASS_STUDENTS_V2, Map.class, cid);
				if (null == res || null == res.get("students")) {
					return;
				}
				List<Map<String, Object>> students = (List<Map<String, Object>>) res.get("students");
				if (null == students || students.isEmpty()) {
					return;
				}
				students.parallelStream().forEach(student -> {
					String uid = (String) student.getOrDefault("id", "");
					List<AnswerRecord> rs = answerRecordService.get(uid, cid, start, end);
					if (null == rs || rs.isEmpty()) {
						return;
					}
					Double score = rs.parallelStream().mapToDouble(r -> r.getScore()).sum();
					Double fullScore = rs.parallelStream().mapToDouble(r -> r.getFullScore()).sum();
					Double rate = score / fullScore;
					if (rate >= PASS_LINE) {
						passMap.put(tid, passMap.getOrDefault(tid, 0) + 1);
					}
					if (rate >= OUTSTANDING_LINE) {
						outstandingMap.put(tid, outstandingMap.getOrDefault(tid, 0) + 1);
					}
					if (rate < PASS_LINE) {
						sixtyUnderMap.put(tid, sixtyUnderMap.getOrDefault(tid, 0) + 1);
					} else if (rate < SEVENTY_UNDER_LINE) {
						seventyUnderMap.put(tid, seventyUnderMap.getOrDefault(tid, 0) + 1);
					} else if (rate < OUTSTANDING_LINE) {
						eightyUnderMap.put(tid, eightyUnderMap.getOrDefault(tid, 0) + 1);
					} else if (rate < NINETY_UNDER_LINE) {
						ninetyUnderMap.put(tid, ninetyUnderMap.getOrDefault(tid, 0) + 1);
					} else {
						ninetyOverMap.put(tid, ninetyOverMap.getOrDefault(tid, 0) + 1);
					}
				});
			});
			tc.put("pass_rate", (Integer) (passMap.getOrDefault(tid, 0) / num * 100));
			tc.put("outstanding_rate", (Integer) (outstandingMap.getOrDefault(tid, 0) / num * 100));
			tc.put("60以下", sixtyUnderMap.getOrDefault(tid, 0));
			tc.put("60-69", seventyUnderMap.getOrDefault(tid, 0));
			tc.put("70-79", eightyUnderMap.getOrDefault(tid, 0));
			tc.put("80-89", ninetyUnderMap.getOrDefault(tid, 0));
			tc.put("90及以上", ninetyOverMap.getOrDefault(tid, 0));
		});
		return tcs;
	}

	@SuppressWarnings("unchecked")
	private Integer getTaskDistributeTime(List<String> cids) {
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_TASKS, Map.class, Joiner.on(",").join(cids));
		if (null != ret && null != ret.get("tasks")) {
			List<Map<String, Object>> tasks = (List<Map<String, Object>>) ret.get("tasks");
			return tasks.size();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	private Integer getTaskCommentedTime(List<String> cids) {
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_TASKS, Map.class, Joiner.on(",").join(cids));
		if (null != ret && null != ret.get("comments")) {
			List<Map<String, Object>> comments = (List<Map<String, Object>>) ret.get("comments");
			return comments.size();
		}
		return 0;
	}

	private Integer getChapterTestTime(String sid, String gcode, long start, long end) {
		List<Exam> exams = examService.list(sid, gcode, start, end);
		if (null == exams || exams.isEmpty()) {
			return 0;
		}
		long count = exams.parallelStream().filter(exam -> ExamType.CHAPTER_TEST == exam.getExamType()).count();
		return count <= 0 ? 0 : Long.valueOf(count).intValue();
	}

	@SuppressWarnings("unchecked")
	private Integer getTaskCreatingNum(List<String> cids) {
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_TASK_QUESTIONS, Map.class,
				Joiner.on(",").join(cids));
		if (null != ret && null != ret.get("qids")) {
			List<String> qids = (List<String>) ret.get("qids");
			if (null == qids) {
				return 0;
			}
			long count = qids.parallelStream().filter(qid -> {
				Question question = questionService.get(qid);
				return null != question && question.isCreatingQuestion();
			}).count();
			return count <= 0 ? 0 : Long.valueOf(count).intValue();
		}
		return 0;
	}

	private Integer getTestCreatingNum(String sid, String gcode, long start, long end) {
		List<Exam> exams = examService.list(sid, gcode, start, end);
		if (null == exams || exams.isEmpty()) {
			return 0;
		}
		List<Integer> num = Lists.newArrayList();
		exams.forEach(exam -> {
			List<ExamPaper> examPapers = examPaperService.getByExam(exam.getId());
			if (null == examPapers || examPapers.isEmpty()) {
				return;
			}
			examPapers.forEach(examPaper -> {
				List<Question> questions = examPaperQuestionService.getQuestionByExamPaper(examPaper.getId());
				if (null == questions || questions.isEmpty()) {
					return;
				}
				long count = questions.parallelStream().filter(question -> question.isCreatingQuestion()).count();
				num.add(count <= 0 ? 0 : Long.valueOf(count).intValue());
			});
		});
		return num.parallelStream().mapToInt(n -> n).sum();
	}

	@SuppressWarnings("unchecked")
	private int getStudentNum(String gid, String sid) {
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_GRADE_STUDENT_NUM, Map.class, sid, gid);
		if (null != ret && null != ret.get("num")) {
			int num = (Integer) ret.get("num");
			num = num < 1 ? 1 : num;
			return num;
		}
		return 1;
	}

}
