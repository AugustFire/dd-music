package com.nercl.music.cloud.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.QuestionType;
import com.nercl.music.cloud.service.AnswerRecordService;
import com.nercl.music.cloud.service.QuestionService;
import com.nercl.music.constant.CList;
import com.nercl.music.util.QuestionToJsonUtil;
import com.nercl.music.util.page.PaginateSupportArray;

@RestController
public class QuestionController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private Gson gson;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private QuestionToJsonUtil questionToJsonUtil;

	@Autowired
	private AnswerRecordService answerRecordService;

	@PostMapping(value = "/question", produces = JSON_PRODUCES)
	public Map<String, Object> add(@RequestBody String json) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(json)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "json is null");
			return ret;
		}
		List<String> ids = questionService.save(json);
		ret.put("code", CList.Api.Client.OK);
		ret.put("ids", Joiner.on(",").join(ids));
		return ret;
	}

	@DeleteMapping(value = "/question", produces = JSON_PRODUCES)
	public Map<String, Object> delete(@RequestParam("ids") String[] qids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == qids || qids.length == 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "qids is null");
			return ret;
		}
		try {
			boolean success = questionService.delete(qids);
			if (success) {
				ret.put("code", CList.Api.Client.OK);
				ret.put("desc", "delete success");
			} else {
				ret.put("code", CList.Api.Client.INTERNAL_ERROR);
				ret.put("desc", "delete failed");
			}
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.INTERNAL_ERROR);
			ret.put("desc", "internal error");
		}
		return ret;
	}

	@PutMapping(value = "/question", produces = JSON_PRODUCES)
	public Map<String, Object> update(@RequestBody String json) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(json)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "json is null");
			return ret;
		}
		try {
			// 格式化请求参数
			Question question = new Question();
			try {
				question = gson.fromJson(json, Question.class);
			} catch (Exception e) {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "json format illegal");
				return ret;
			}
			questionService.update(question);
			ret.put("code", CList.Api.Client.OK);
			ret.put("desc", "update success");
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.INTERNAL_ERROR);
			ret.put("desc", "internal error");
		}
		return ret;
	}

	@PutMapping(value = "/question/{qid}/checked", produces = JSON_PRODUCES)
	public Map<String, Object> checked(@PathVariable String qid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(qid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "qid is null");
			return ret;
		}
		boolean success = questionService.checked(qid);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("desc", "checked success");
		} else {
			ret.put("code", CList.Api.Client.INTERNAL_ERROR);
			ret.put("desc", "checked failed");
		}
		return ret;
	}

	@GetMapping(value = "/question/{qid}", produces = JSON_PRODUCES)
	public Map<String, Object> get(@PathVariable String qid, String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(qid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "qid is null");
			return ret;
		}
		Question question = questionService.get(qid);
		if (null == question) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "question is null");
			return ret;
		}
		AnswerRecord ar = new AnswerRecord();
		ar.setUserId(uid);
		ar.setQuestionId(qid);
		List<AnswerRecord> records = Lists.newArrayList();
		try {
			records = answerRecordService.getByConditions(ar); // 根据用户id和试题id查询答案
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == records || records.isEmpty()) { // 如果没有答案，设置是否提交和是否批改都为false
			ret.put("is_commited", false);
			ret.put("is_marked", false);
			ret.put("comment", null);
		} else {
			boolean anyMatch = records.stream()
					.anyMatch(record -> null != record.getScore() || null != record.getComment()); // 如果有答案，是否提交为true，答案中有题目得分或者有题目有评价，则是否批改为true
			ret.put("is_commited", true);
			ret.put("is_marked", anyMatch);
			ret.put("comment", records.stream().map(AnswerRecord::getComment).collect(Collectors.joining(",")));
		}
		ret.put("code", CList.Api.Client.OK);
		ret.putAll(questionToJsonUtil.toJosn(question));
		return ret;
	}

	@GetMapping(value = "/questions", params = { "ids" }, produces = JSON_PRODUCES)
	public Map<String, Object> getByIds(String[] ids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == ids || ids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "ids is null");
			return ret;
		}
		Map<String, Object> questions = questionService.get(ids);
		ret.put("code", CList.Api.Client.OK);
		if (null == questions) {
			return ret;
		}
		ret.putAll(questions);
		return ret;
	}

	@GetMapping(value = "/question2/{qid}", produces = JSON_PRODUCES)
	public Map<String, Object> getById(@PathVariable String qid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(qid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "qid is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		Question question = questionService.get(qid);
		if (null != question) {
			ret.putAll(questionToJsonUtil.toJosn(question));
		}
		return ret;
	}

	/**
	 * 
	 * 
	 * 根据难度系数（高，低）、知识点、年级查询试题列表
	 * 
	 * @param knowledges
	 *            知识点
	 * @param gradeCode
	 *            年级码
	 * @param difficultyLow
	 *            难度系数最低值
	 * @param difficultyHigh
	 *            难度系数最高值
	 * @param questionTypes
	 *            题目类型
	 */
	@GetMapping(value = "/question_list", produces = JSON_PRODUCES)
	public Map<String, Object> questionList(
			@RequestParam(value = "knowledges", required = false) List<String> knowledges,
			@RequestParam(value = "grade_code", required = false) String gradeCode,
			@RequestParam(value = "difficulty_low", required = false) Float difficultyLow,
			@RequestParam(value = "difficulty_high", required = false) Float difficultyHigh,
			@RequestParam(value = "subject_type", required = false) String subjectType,
			@RequestParam(value = "question_types", required = false) List<String> questionTypes,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "page_num", required = true) int pageNum) {
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.OK);
		List<QuestionType> questionTypeList = Lists.newArrayList();
		if (questionTypes != null && !questionTypes.isEmpty()) {
			for (int i = 0; i < questionTypes.size(); i++) {
				try {
					questionTypeList.add(QuestionType.valueOf(questionTypes.get(i)));
				} catch (Exception e) {
					ret.put("code", CList.Api.Client.PROCESSING_FAILED);
					ret.put("desc", "no enum constant:" + questionTypes.get(i));
					return ret;
				}
			}
		}
		PaginateSupportArray<Question> questions = questionService.getquestionList(difficultyLow, difficultyHigh,
				knowledges, gradeCode, subjectType, questionTypeList, title, pageNum);
		List<Map<String, Object>> qs = questions.parallelStream().map(question -> {
			return questionToJsonUtil.toJosn(question);
		}).collect(Collectors.toList());
		ret.put("questions", qs);
		ret.put("page", questions.getPage());
		ret.put("total", questions.getTotal());
		ret.put("page_size", questions.getPageSize());
		return ret;
	}

	@GetMapping(value = "/find_questions", produces = JSON_PRODUCES)
	public Map<String, Object> getQuestions(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page, String title,
			String knowledge, String gradeCode, String songId, String questionType, String subjectType,
			Boolean isChecked) {
		Map<String, Object> ret = Maps.newHashMap();
		List<Question> questions = questionService.getQuestions(title, knowledge, gradeCode, songId, questionType,
				subjectType, isChecked, page);
		ret.put("code", CList.Api.Client.OK);
		if (null == questions) {
			return ret;
		}
		if (questions instanceof PaginateSupportArray) {
			PaginateSupportArray<Question> psa = (PaginateSupportArray<Question>) questions;
			ret.put("count", psa.getTotal());
			ret.put("page_size", psa.getPageSize());
			ret.put("pages", psa.getPage());
		}
		List<Map<String, Object>> list = questions.stream().map(question -> questionToJsonUtil.toJosn(question))
				.collect(Collectors.toList());
		ret.put("questions", list);
		return ret;
	}

	@GetMapping(value = "/questions/in_every_type", params = { "grade" }, produces = JSON_PRODUCES)
	public Map<String, Object> get(String grade) {
		Map<String, Object> ret = Maps.newHashMap();
		Map<String, Integer> questions = questionService.getQuestionsNumInEveryQuestionType(grade);
		ret.put("code", CList.Api.Client.OK);
		ret.put("questions", questions);
		return ret;
	}
}
