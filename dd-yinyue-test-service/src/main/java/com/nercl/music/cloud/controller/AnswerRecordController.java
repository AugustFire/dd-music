package com.nercl.music.cloud.controller;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.ExamPaper;
import com.nercl.music.cloud.entity.ExamPaperQuestion;
import com.nercl.music.cloud.entity.Option;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.QuestionType;
import com.nercl.music.cloud.service.AnswerRecordService;
import com.nercl.music.cloud.service.ExamPaperQuestionService;
import com.nercl.music.cloud.service.ExamPaperService;
import com.nercl.music.cloud.service.OptionService;
import com.nercl.music.cloud.service.PendingScoredProducer;
import com.nercl.music.cloud.service.QuestionService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;
import com.nercl.music.util.QuestionToJsonUtil;

@RestController
public class AnswerRecordController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private Gson gson;

	@Autowired
	private OptionService optionService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private ExamPaperQuestionService examPaperQuestionService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private QuestionToJsonUtil questionToJsonUtil;

	@Autowired
	private PendingScoredProducer pendingScoredProducer;

	@GetMapping(value = "/answers", produces = JSON_PRODUCES)
	public Map<String, Object> getAnswers(String qid, String classRoomId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(qid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "qid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(classRoomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classRoomId is null");
			return ret;
		}
		List<AnswerRecord> records = answerRecordService.get(classRoomId, qid);
		ret.put("code", CList.Api.Client.OK);
		if (null != records) {
			List<Map<String, Object>> list = Lists.newArrayList();
			ret.put("answers", list);
			records.forEach(record -> {
				Map<String, Object> map = Maps.newHashMap();
				list.add(map);
				map.put("answer", record.getAnswer());
				map.put("student_id", record.getUserId());
				map.put("question_id", record.getQuestionId());
				map.put("resource_id", record.getResourceId());
			});
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/answer", produces = JSON_PRODUCES)
	public Map<String, Object> save(String answer) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(answer)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "answer is null");
			return ret;
		}
		List<Map<String, Object>> values = gson.fromJson(answer, List.class);
		if (null == values || values.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "answer is null");
			return ret;
		}
		Map<String, Object> v = values.get(0);
		String uid = (String) v.getOrDefault("user_id", "");
		String pid = (String) v.getOrDefault("exam_paper_id", "");
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "user_id is null");
			return ret;
		}
		if (!Strings.isNullOrEmpty(pid)) {
			boolean hasAnswer = answerRecordService.hasAnswer(pid, uid);
			if (hasAnswer) {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "has answer");
				return ret;
			}
		}
		Long now = Instant.now().toEpochMilli();
		List<String> rids = Lists.newArrayList();
		values.forEach(value -> {
			String answ = (String) value.getOrDefault("answer", "");
			String userId = (String) value.getOrDefault("user_id", "");
			String questionId = (String) value.getOrDefault("question_id", "");
			if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(questionId)) {
				return;
			}
			String resourceId = (String) value.getOrDefault("resource_id", "");
			String roomId = (String) value.getOrDefault("room_id", "");

			Float score = ((Number) value.getOrDefault("score", 0F)).floatValue();

			String comment = (String) value.getOrDefault("comment", "");
			String taskId = (String) value.getOrDefault("task_id", "");
			String chapterId = (String) value.getOrDefault("chapter_id", "");

			String examId = (String) value.getOrDefault("exam_id", "");
			String examPaperId = (String) value.getOrDefault("exam_paper_id", "");
			String answerSource = (String) value.get("answer_source");

			Integer tempo = ((Number) value.getOrDefault("tempo", 0)).intValue();

			Float fullScore = null;
			if (!Strings.isNullOrEmpty(examPaperId)) {
				fullScore = examPaperQuestionService.getScore(examPaperId, questionId).floatValue(); // 查询题目分数
			}
			Boolean isTrue = false;
			Question question = questionService.get(questionId);
			if (QuestionType.SINGLE_SELECT.equals(question.getQuestionType())) {
				List<Option> options = optionService.getByQuestion(question.getId());
				if (null != options && !options.isEmpty()) {
					isTrue = options.stream().anyMatch(option -> option.isTrue() && option.getValue().equals(answ));
				}
			}

			if (QuestionType.MULTI_SELECT.equals(question.getQuestionType())) {
				List<Option> options = optionService.getByQuestion(question.getId());
				if (null != options && !options.isEmpty() && !Strings.isNullOrEmpty(answ)) {
					List<String> ans = Splitter.on(",").splitToList(answ);
					boolean rightNumber = options.stream().filter(option -> option.isTrue()).count() == ans.size();
					isTrue = rightNumber && options.stream().filter(option -> option.isTrue())
							.allMatch(option -> ans.contains(option.getValue()));
				}
			}

			if (question.isSelectQuestion()) {
				if (String.valueOf(AnswerSource.TASK).equals(answerSource)
						|| String.valueOf(AnswerSource.CHAPTER_TEST).equals(answerSource)
						|| String.valueOf(AnswerSource.MIDDLE_TERM_TEST).equals(answerSource)
						|| String.valueOf(AnswerSource.FINAL_TERM_TEST).equals(answerSource)
						|| String.valueOf(AnswerSource.OTHER).equals(answerSource)) {
					if (isTrue) {
						score = fullScore;
					} else {
						score = 0F;
					}
				}
			}

			AnswerRecord ar = new AnswerRecord();
			ar.setAnswer(answ);
			ar.setQuestionId(questionId);
			ar.setUserId(userId);
			ar.setResourceId(resourceId);
			ar.setClassRoomId(roomId);
			ar.setTimestamp(now);

			ar.setIsTrue(isTrue);
			ar.setScore(score);
			ar.setFullScore(fullScore);

			ar.setComment(comment);
			ar.setTaskId(taskId);
			ar.setChapterId(chapterId);

			ar.setExamId(Strings.emptyToNull(examId));
			ar.setExamPaperId(Strings.emptyToNull(examPaperId));
			if (!Strings.isNullOrEmpty(answerSource)) {
				ar.setAnswerSource(AnswerSource.valueOf(answerSource));
			}
			ar.setTempo(tempo);

			Map<String, Object> classes = restTemplate.getForObject(ApiClient.GET_CLASS_USER, Map.class, userId);
			if (null != classes) {
				ar.setSchoolId((String) classes.getOrDefault("school_id", ""));
				ar.setSchoolName((String) classes.getOrDefault("school_name", ""));
				if (null != classes.get("classes")) {
					List<Map<String, String>> cls = (List<Map<String, String>>) classes.get("classes");
					ar.setClassId((String) cls.get(0).getOrDefault("class_id", ""));
					ar.setClassName((String) cls.get(0).getOrDefault("class_name", ""));
				}
				if (null != classes.get("grades")) {
					List<Map<String, String>> grades = (List<Map<String, String>>) classes.get("grades");
					ar.setGradeId((String) grades.get(0).getOrDefault("grade_id", ""));
					ar.setGradeName((String) grades.get(0).getOrDefault("grade_name", ""));
				}
			}

			String rid = answerRecordService.save(ar);
			if (!Strings.isNullOrEmpty(rid)) {
				rids.add(rid);
			}

			if (!Strings.isNullOrEmpty(roomId) && !Strings.isNullOrEmpty(userId) && !Strings.isNullOrEmpty(answer)
					&& String.valueOf(AnswerSource.EXERCISE).equals(answerSource)) {
				restTemplate.getForObject(ApiClient.ANSWER_NOTICE, Map.class, roomId, userId, answer);
			}
		});
		rids.forEach(rid -> {
			AnswerRecord record = answerRecordService.findById(rid);
			addScoredQueue(record);
		});
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	private void addScoredQueue(AnswerRecord answerRecord) {
		if (null == answerRecord) {
			return;
		}
		Question question = questionService.get(answerRecord.getQuestionId());
		if (null == question) {
			return;
		}
		if (QuestionType.SHORT_ANSWER == question.getQuestionType()) {
			pendingScoredProducer.sendShortScoredMessage(answerRecord.getId());
		}
		if (question.isSingQuestion()) {
			pendingScoredProducer.sendSingScoredMessage(answerRecord.getId());
		}
	}

	/**
	 * 根据试题id、用户id查询用户作答情况
	 * 
	 * @param qid
	 *            试题id
	 * @param uid
	 *            用户id
	 */
	@GetMapping(value = "/{qid}/answer/{uid}", produces = JSON_PRODUCES)
	public Map<String, Object> getUserAnswer(@PathVariable String qid, @PathVariable String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		try {
			AnswerRecord ar = new AnswerRecord();
			ar.setQuestionId(qid);
			ar.setUserId(uid);
			List<AnswerRecord> records = answerRecordService.getByConditions(ar);
			if (null != records && !records.isEmpty()) {
				AnswerRecord record = records.get(0);
				List<Option> options = optionService.getByQuestion(record.getQuestion().getId());
				ret.put("answer", record);
				ret.put("options", options);
			}
			ret.put("code", CList.Api.Client.OK);
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "inner exception");
		}
		return ret;
	}

	/**
	 * 给学生作答点评
	 * 
	 * @param score
	 *            评分分数
	 * @param comment
	 *            点评内容
	 */
	@PutMapping(value = "/answer/{recordId}", produces = JSON_PRODUCES)
	public Map<String, Object> markTaskAnswer(@PathVariable String recordId, @RequestParam Float score,
			@RequestBody String requestBody) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(recordId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "recordId is null");
			return ret;
		}
		AnswerRecord answerRecord = answerRecordService.findById(recordId);
		if (null == answerRecord) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "answerRecord is not exist");
			return ret;
		}
		answerRecord.setScore(score);
		answerRecord.setComment(requestBody);
		answerRecordService.update(answerRecord);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@GetMapping(value = "/answer_records", produces = JSON_PRODUCES)
	public Map<String, Object> getUserAnswerRecords(String uid, @RequestParam(name = "exam_id") String examId,
			@RequestParam(name = "exam_paper_id") String examPaperId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(examId) || Strings.isNullOrEmpty(examPaperId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examId or examPaperId is null");
			return ret;
		}
		ExamPaper examPaper = examPaperService.get(examPaperId);
		if (null == examPaper) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examPaper null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("exam_paper_score", examPaper.getScore());
		List<AnswerRecord> records = answerRecordService.get(uid, examId, examPaperId);
		if (null == records || records.isEmpty()) {
			return ret;
		}
		List<ExamPaperQuestion> epqs = examPaperQuestionService.getByExamPaper(examPaperId);
		if (null == epqs || epqs.isEmpty()) {
			return ret;
		}
		List<Integer> scores = Lists.newArrayList();
		List<Map<String, Object>> questions = epqs.parallelStream().map(epq -> {
			Question question = epq.getQuestion();
			Map<String, Object> q = questionToJsonUtil.toJosn(question);
			ExamPaperQuestion qpq = examPaperQuestionService.getByExamPaperAndQuestion(examPaperId, question.getId());
			if (null != qpq) {
				q.put("score", qpq.getScore());
			}
			Optional<AnswerRecord> optional = records.stream().filter(record -> {
				Question qu = record.getQuestion();
				return null != qu && qu.getId().equals(question.getId());
			}).findFirst();
			if (optional.isPresent()) {
				Float score = optional.get().getScore();
				if (question.isSelectQuestion()) {
					q.put("is_true", optional.get().getIsTrue());
				}
				Integer t = null == score ? 0 : score.intValue();
				q.put("student_score", t);
				scores.add(t);
				q.put("user_answer", optional.get().getAnswer());
				q.put("user_answer_resource", optional.get().getResourceId());
			}
			return q;
		}).collect(Collectors.toList());
		ret.put("questions", questions);
		if(scores.isEmpty()){
			ret.put("student_score", 0);
		}else{
			ret.put("student_score", scores.parallelStream().mapToInt(s -> s).sum());
		}
		return ret;
	}

	/**
	 * 根据作业id、用户id查询用户作答情况
	 * 
	 * @param taskId
	 *            作业id
	 * @param uid
	 *            用户id
	 */
	@GetMapping(value = "/task_answers", produces = JSON_PRODUCES)
	public Map<String, Object> getUserTaskAnswer(String taskId, String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		List<AnswerRecord> records = null;
		try {
			AnswerRecord ar = new AnswerRecord();
			ar.setTaskId(taskId);
			ar.setUserId(uid);
			records = answerRecordService.getByConditions(ar);
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "inner exception");
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("is_commited", null != records && !records.isEmpty());
		return ret;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/students_has_task_answer", produces = JSON_PRODUCES)
	public Map<String, Object> getStudentsHasTaskAnswer(String classroomId, String taskId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(classroomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroomId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(taskId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "taskId is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("task_id", taskId);
		ret.put("classroom_id", classroomId);
		Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_CLASSROOM_STUDENTS, Map.class, classroomId);
		if (null == res || null == res.get("students")) {
			return ret;
		}
		List<Map<String, Object>> ts = (List<Map<String, Object>>) res.get("students");
		List<Map<String, Object>> has_answers = ts.stream().map(t -> {
			Map<String, Object> map = Maps.newHashMap();
			String id = (String) t.get("id");
			String name = (String) t.get("name");
			map.put("id", id);
			map.put("name", name);
			AnswerRecord ar = new AnswerRecord();
			ar.setTaskId(taskId);
			ar.setUserId(id);
			List<AnswerRecord> list = null;
			try {
				list = answerRecordService.getByConditions(ar);
			} catch (Exception e) {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "inner exception");
				return ret;
			}
			if (null == list || list.isEmpty()) {
				map.put("has_answer", false);
			} else {
				map.put("has_answer", true);
			}
			return map;
		}).collect(Collectors.toList());
		ret.put("has_answers", has_answers);
		return ret;
	}

	@GetMapping(value = "/task_amount", produces = JSON_PRODUCES)
	public Map<String, Object> getTaskAmount(@RequestParam(name = "task_id") String taskId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(taskId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "taskId is null");
			return ret;
		}
		AnswerRecord ar = new AnswerRecord();
		ar.setTaskId(taskId);
		List<AnswerRecord> records = Lists.newArrayList();
		try {
			records = answerRecordService.getByConditions(ar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.put("code", CList.Api.Client.OK);
		if (null == records || records.isEmpty()) {
			ret.put("commit_amount", 0);
			return ret;
		}
		Map<String, List<AnswerRecord>> collect = records.stream()
				.collect(Collectors.groupingBy(record -> record.getUserId()));
		ret.put("commit_amount", collect.keySet().size());
		return ret;
	}
}
