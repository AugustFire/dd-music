package com.nercl.music.cloud.controller;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.nercl.music.cloud.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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

	/**
	 * 获取某个教室下所有的
	 * @param qid
	 * @param classRoomId
	 * @return
	 */
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

	/**
	 * 提交答卷/答题
	 * @param answer
	 * @return
	 */
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

		//传参 list<Answer>
		Map<String, Object> v = values.get(0);
		//取出第一个来获取userId和examPaperId

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

		//foreach
		values.forEach(value -> {
			// 保存答案
			String answ = (String) value.getOrDefault("answer", "");
			// 保存user_id
			String userId = (String) value.getOrDefault("user_id", "");
			// 保存question_id
			String questionId = (String) value.getOrDefault("question_id", "");
			if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(questionId)) {
				return;
			}
			String resourceId = (String) value.getOrDefault("resource_id", "");
			String roomId = (String) value.getOrDefault("room_id", "");

			Float score = ((Number) value.getOrDefault("score", 0F)).floatValue();

			String comment = (String) value.getOrDefault("comment", "");
			// 这次任务Id
			String taskId = (String) value.getOrDefault("task_id", "");

			// 章节Id
			String chapterId = (String) value.getOrDefault("chapter_id", "");

			String examId = (String) value.getOrDefault("exam_id", "");
			String examPaperId = (String) value.getOrDefault("exam_paper_id", "");
			String answerSource = (String) value.get("answer_source");

			Integer tempo = ((Number) value.getOrDefault("tempo", 0)).intValue();

			//获取该次考试下 这个试题设置的满分是多少
			Float fullScore = null;
			if (!Strings.isNullOrEmpty(examPaperId)) {
				fullScore = examPaperQuestionService.getScore(examPaperId, questionId).floatValue(); // 查询题目分数
			}
			Boolean isTrue = false;
			Question question = questionService.get(questionId);

			//单选题
			if (QuestionType.SINGLE_SELECT.equals(question.getQuestionType())) {
				List<Option> options = optionService.getByQuestion(question.getId());
				if (null != options && !options.isEmpty()) {
					isTrue = options.stream().anyMatch(option -> option.isTrue() && option.getValue().equals(answ));
				}
			}

			//多选题
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

			//创建作答详情
			AnswerRecord ar = new AnswerRecord();
			// FIXME 2 保存答案
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

	@Autowired
	private PendingScoredConsumer consumer;

	/**
	 *
	 * @param answerRecord 作答记录
	 */
	@Async
	void addScoredQueue(AnswerRecord answerRecord) {
		if (null == answerRecord) {
			return;
		}
		Question question = questionService.get(answerRecord.getQuestionId());
		if (null == question) {
			return;
		}
		//简答题
		if (QuestionType.SHORT_ANSWER == question.getQuestionType()) {
//			pendingScoredProducer.sendShortScoredMessage(answerRecord.getId());
			consumer.receiveShortPengdingScoreQueue(answerRecord.getId());
		}

		//除选择题/简答题以外的题目
		if (question.isSingQuestion()) {
//			pendingScoredProducer.sendSingScoredMessage(answerRecord.getId());
			consumer.receiveSingPengdingScoreQueue(answerRecord.getId());
		}
	}

	/**
	 * 保存指定作业,指定学生的作答情况,保存答案的方法直接使用了当前原有save(String answer)的基本方法
	 */
	@PostMapping(value ="/{taskId}/answers/{uid}",produces = JSON_PRODUCES)
	public Map<String, Object> saveAnswers(@PathVariable String taskId, @PathVariable String uid,@RequestBody String requestBody){
		Map<String, Object> ret = Maps.newHashMap();
		//判断taskId参数是否为空
		if (Strings.isNullOrEmpty(taskId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "taskId is null");
			return ret;
		}
		//判断uid参数是否为空
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		Map<String, Object> body= (Map<String, Object>) JSON.parse(requestBody);
		List answersList = (List) body.get("answersList");//答题列表
		String answerSource = (String) body.get("answerSource");//答题来源
		//判断答题列表是否为空
		if (null == answersList || answersList.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "answersList is null");
			return ret;
		}
		//遍历答题列表
		for (Object answer : answersList) {
			List<Map<String, Object>> values = gson.fromJson((String) answer, List.class);
			if (null == values || values.isEmpty()) {
				continue;//当前答题为空,执行下一个循环
			}
			Map<String, Object> v = values.get(0);
//			String uid = (String) v.getOrDefault("user_id", "");
			String pid = (String) v.getOrDefault("exam_paper_id", "");//考卷id
			Long now = Instant.now().toEpochMilli();//当前时间毫秒值
			List<String> rids = Lists.newArrayList();
			for (Map<String, Object> value : values) {
				String answ = (String) value.getOrDefault("answer", "");//答案
				String userId = uid;
				String questionId = (String) value.getOrDefault("question_id", "");//题目id
				if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(questionId)) {
					continue;
				}
				String resourceId = (String) value.getOrDefault("resource_id", "");//资源id
				String roomId = (String) value.getOrDefault("room_id", "");//课堂id
				Float score = ((Number) value.getOrDefault("score", 0F)).floatValue();//分数
				String comment = (String) value.getOrDefault("comment", "");//评语
				String chapterId = (String) value.getOrDefault("chapter_id", "");//章节
				String examId = (String) value.getOrDefault("exam_id", "");//考试id
				String examPaperId = (String) value.getOrDefault("exam_paper_id", "");//考卷id
				Integer tempo = ((Number) value.getOrDefault("tempo", 0)).intValue();//时间戳
				Float fullScore = null;
				if (!Strings.isNullOrEmpty(examPaperId)) {
					fullScore = examPaperQuestionService.getScore(examPaperId, questionId).floatValue(); // 查询题目分数
				}
				Boolean isTrue = false;//是否为正确答案
				Question question = questionService.get(questionId);
				//判断Question是否为存在
				if (null==question) {
					continue;
				}
				//题目如果是单选题
				if (QuestionType.SINGLE_SELECT.equals(question.getQuestionType())) {
					List<Option> options = optionService.getByQuestion(question.getId());
					if (null != options && !options.isEmpty()) {
						isTrue = options.stream().anyMatch(option -> option.isTrue() && option.getValue().equals(answ));
					}
				}
				//题目如果是多选题
				if (QuestionType.MULTI_SELECT.equals(question.getQuestionType())) {
					List<Option> options = optionService.getByQuestion(question.getId());
					if (null != options && !options.isEmpty() && !Strings.isNullOrEmpty(answ)) {
						List<String> ans = Splitter.on(",").splitToList(answ);
						boolean rightNumber = options.stream().filter(option -> option.isTrue()).count() == ans.size();
						isTrue = rightNumber && options.stream().filter(option -> option.isTrue())
								.allMatch(option -> ans.contains(option.getValue()));
					}
				}
				//题目是否是选择题
				if (question.isSelectQuestion()) {
					if (String.valueOf(AnswerSource.TASK).equals(answerSource)
							|| String.valueOf(AnswerSource.CHAPTER_TEST).equals(answerSource)
							|| String.valueOf(AnswerSource.MIDDLE_TERM_TEST).equals(answerSource)
							|| String.valueOf(AnswerSource.FINAL_TERM_TEST).equals(answerSource)
							|| String.valueOf(AnswerSource.OTHER).equals(answerSource)) {
						if (isTrue) {
							//答题正确
							score = fullScore;
						} else {
							score = 0F;
						}
					}
				}
				//创建作答详情
				AnswerRecord ar = new AnswerRecord();
				// FIXME 2 保存答案
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
				//考生信息
				Map<String, Object> classes = restTemplate.getForObject(ApiClient.GET_CLASS_USER, Map.class, userId);
				if (null != classes) {
					ar.setSchoolId((String) classes.getOrDefault("school_id", ""));//学校
					ar.setSchoolName((String) classes.getOrDefault("school_name", ""));
					if (null != classes.get("classes")) {//班级
						List<Map<String, String>> cls = (List<Map<String, String>>) classes.get("classes");
						ar.setClassId((String) cls.get(0).getOrDefault("class_id", ""));
						ar.setClassName((String) cls.get(0).getOrDefault("class_name", ""));
					}
					if (null != classes.get("grades")) {//年纪
						List<Map<String, String>> grades = (List<Map<String, String>>) classes.get("grades");
						ar.setGradeId((String) grades.get(0).getOrDefault("grade_id", ""));
						ar.setGradeName((String) grades.get(0).getOrDefault("grade_name", ""));
					}
				}

				String rid = answerRecordService.save(ar);//主键返回,生成的answerRecord的id
				if (!Strings.isNullOrEmpty(rid)) {
					rids.add(rid);
				}

				if (!Strings.isNullOrEmpty(roomId) && !Strings.isNullOrEmpty(userId) && !Strings.isNullOrEmpty((String) answer)
						&& String.valueOf(AnswerSource.EXERCISE).equals(answerSource)) {
					restTemplate.getForObject(ApiClient.ANSWER_NOTICE, Map.class, roomId, userId, answer);
				}
			}
			rids.forEach(rid -> {
				AnswerRecord record = answerRecordService.findById(rid);
				addScoredQueue(record);
			});
			ret.put("code", CList.Api.Client.OK);
			return ret;
		}


		return null;
	}

	/**
	 * 根据试题id、用户id和作业id查询用户作答情况
	 * 
	 * @param qid
	 *            试题id
	 * @param uid
	 *            用户id
	 * @param tid
	 * 			  作业id
	 */
	@GetMapping(value = "/{qid}/answer/{uid}/{tid}", produces = JSON_PRODUCES)
	public Map<String, Object> getUserAnswer(@PathVariable String qid, @PathVariable String uid,@PathVariable String tid) {
		Map<String, Object> ret = Maps.newHashMap();
		try {
			AnswerRecord ar = new AnswerRecord();
			ar.setQuestionId(qid);
			ar.setUserId(uid);
			ar.setTaskId(tid);
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
	 * @param
	 *
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
		//缺少对实体表字段is_marked的修改
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

	// FIXME 3 查询作答详细
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
