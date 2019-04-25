package com.nercl.music.cloud.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.nercl.music.cloud.entity.Exam;
import com.nercl.music.cloud.entity.ExamPaper;
import com.nercl.music.cloud.entity.ExamPaperQuestion;
import com.nercl.music.cloud.entity.ExamPaperType;
import com.nercl.music.cloud.entity.Option;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.QuestionType;
import com.nercl.music.cloud.service.AnswerRecordService;
import com.nercl.music.cloud.service.ExamPaperQuestionService;
import com.nercl.music.cloud.service.ExamPaperService;
import com.nercl.music.cloud.service.ExamService;
import com.nercl.music.cloud.service.OptionService;
import com.nercl.music.cloud.service.QuestionService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;
import com.nercl.music.util.CloudFileUtil;
import com.nercl.music.util.QuestionToJsonUtil;
import com.nercl.music.util.ZipFileUtil;

@RestController
public class ExamPaperController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ExamService examService;

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private OptionService optionService;

	@Autowired
	private ExamPaperQuestionService examPaperQuestionService;

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private Gson gson;

	@Autowired
	private CloudFileUtil cloudFileUtil;

	@Value("${test.question.zip}")
	private String zipPath;

	@Autowired
	private ZipFileUtil zipFileUtil;

	@Autowired
	private QuestionToJsonUtil questionToJsonUtil;

	@Autowired
	private RestTemplate restTemplate;

	private Lock lock = new ReentrantLock();

	private static final String ATTR = "basic:creationTime";

	/**
	 * 根据考试id查询考试对应的试卷
	 * @param examId
	 * @return
	 */
	@GetMapping(value = "/exam_papers")
	public Map<String, Object> list(String examId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(examId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examId is null");
			return ret;
		}
		Exam exam = examService.findById(examId);
		if (null == exam) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "exam is null");
			return ret;
		}
		List<ExamPaper> examPapers = examPaperService.getByExam(examId);
		ret.put("code", CList.Api.Client.OK);
		if (null == examPapers || examPapers.isEmpty()) {
			return ret;
		}
		List<Map<String, Object>> papers = examPapers.stream().map(paper -> {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", paper.getId());
			map.put("title", paper.getTitle());
			map.put("school", exam.getSchoolId());
			map.put("score", paper.getScore());
			map.put("producerId", paper.getProducerId());
			map.put("producerName", paper.getProducerName());
			List<Question> questions = examPaperQuestionService.getQuestionByExamPaper(paper.getId());
			map.put("questionNum", null == questions || questions.isEmpty() ? 0 : questions.size());
			map.put("difficulty", paper.getDifficulty());
			map.put("examPaperType", paper.getExamPaperType());
			map.put("resolvedTime", paper.getResolvedTime());
			map.put("has_answer", answerRecordService.hasAnswer(paper.getId()));
			return map;
		}).collect(Collectors.toList());
		ret.put("exam_papers", papers);
		return ret;
	}

     /**
      * 根据考卷id查询考卷
      * @param eid
      * @return
      */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/exam_paper/{eid}/info")
	public Map<String, Object> get(@PathVariable String eid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(eid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "eid is null");
			return ret;
		}
		ExamPaper examPaper = examPaperService.get(eid);
		if (null == examPaper) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examPaper is null");
			return ret;
		}
		Exam exam = examPaper.getExam();
		if (null == exam) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "exam is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("id", examPaper.getId());
		ret.put("title", examPaper.getTitle());
		ret.put("score", examPaper.getScore());
		ret.put("exam_paper_type", examPaper.getExamPaperType());
		ret.put("resolved_time", examPaper.getResolvedTime());
		ret.put("exam_id", examPaper.getExamId());
		ret.put("exam_title", exam.getTitle());
		ret.put("exam_type", exam.getExamType());
		ret.put("start_at", exam.getStartAt());
		ret.put("end_at", exam.getEndAt());
		Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_GRADE, Map.class, exam.getGrade());
		if (null != res && null != res.get("gradeList")) {
			List<Map<String, Object>> grades = (List<Map<String, Object>>) res.get("gradeList");
			if (!grades.isEmpty()) {
				ret.put("grade_id", grades.get(0).get("id"));
				ret.put("grade_name", grades.get(0).get("name"));
				ret.put("grade_code", exam.getGrade());
			}
		}
		return ret;
	}

	/**
	 * 获取某个考试的试题篮的信息
	 * @param eid
	 * @return
	 */
	@GetMapping(value = "/exam_paper/non_finished")
	public Map<String, Object> getNonFinished(String eid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(eid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "eid is null");
			return ret;
		}
		ExamPaper examPaper = examPaperService.getNonFinished(eid);
		ret.put("code", CList.Api.Client.OK);
		if (null == examPaper) {
			return ret;
		}
		ret.put("id", examPaper.getId());
		ret.put("title", examPaper.getTitle());
		ret.put("exam_paper_type", examPaper.getExamPaperType());
		List<ExamPaperQuestion> epqs = examPaperQuestionService.getByExamPaper(examPaper.getId());
		if (null == epqs || epqs.isEmpty()) {
			return ret;
		}
		Map<String, List<Question>> group = epqs.parallelStream().map(epq -> epq.getQuestion())
				.collect(Collectors.groupingBy(q -> String.valueOf(q.getQuestionType())));
		group.forEach((key, value) -> {
			ret.put(key, value.size());
		});

		List<Question> questions = examPaperQuestionService.getQuestionByExamPaper(examPaper.getId());
		if (null == questions || questions.isEmpty()) {
			return ret;
		}
		List<String> ids = questions.stream().map(question -> question.getId()).collect(Collectors.toList());
		ret.put("ids", ids);
		return ret;
	}

	/**
	 * 清空某个人的试题篮的信息
	 * @param id
	 * @return
	 */
	@PutMapping(value = "/exam_paper/{id}/cleared")
	public Map<String, Object> clear(@PathVariable String id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
			return ret;
		}
		boolean success = examPaperService.clear(id);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cleared error");
		}
		return ret;
	}

	/**
	 * 手工组卷
	 * @param examPaper
	 * @return
	 */
	@PostMapping(value = "/exam_paper", produces = JSON_PRODUCES)
	public Map<String, Object> add(ExamPaper examPaper) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(examPaper.getExamId())) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(examPaper.getTitle())) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(examPaper.getProducerId())) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "producerId is null");
			return ret;
		}
		if (examPaper.getScore().intValue() == 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "score is null");
			return ret;
		}
		if (null == examPaper.getExamPaperType()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examPaperType is null");
			return ret;
		}
		ExamPaper temp = examPaperService.getByExamAndPaperType(examPaper.getExamId(), examPaper.getExamPaperType());
		if (null != temp) {
			ret.put("code", CList.Api.Client.LOGIC_ERROR);
			ret.put("desc", "has this type examPaper");
			return ret;
		}
		examPaper.setStatus(ExamPaper.Status.FINISHED);
		String paperId = examPaperService.save(examPaper);
		ret.put("code", CList.Api.Client.OK);
		ret.put("examPaperId", paperId);
		return ret;
	}

	/**
	 * 添加一个试题篮
	 * @param uid
	 * @param eid
	 * @param examPaperType
	 * @return
	 */
	@PostMapping(value = "/exam_paper/to_basket", produces = JSON_PRODUCES)
	public Map<String, Object> addToBasket(String uid, String eid, String examPaperType) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(eid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "eid is null");
			return ret;
		}
		boolean valid = Arrays.stream(ExamPaperType.values()).anyMatch(type -> type.toString().equals(examPaperType));
		if (!valid) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examPaperType is error");
			return ret;
		}
		ExamPaper examPaper = examPaperService.getNonFinished(eid);
		if (null != examPaper) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "has basket");
			return ret;
		}
		examPaper = new ExamPaper();
		examPaper.setExamId(eid);
		examPaper.setProducerId(uid);
		examPaper.setStatus(ExamPaper.Status.NON_FINISHED);
		examPaper.setExamPaperType(ExamPaperType.valueOf(examPaperType));
		String paperId = examPaperService.save(examPaper);
		ret.put("code", CList.Api.Client.OK);
		ret.put("exam_paper_id", paperId);
		return ret;
	}

	/**
	 * 设置某个试题篮的examPaperType
	 * @param eid
	 * @param examPaperType
	 * @return
	 */
	@PutMapping("/exam_paper/{eid}/exam_paper_type")
	public Map<String, Object> set(@PathVariable String eid, String examPaperType) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(eid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
			return ret;
		}
		boolean valid = Arrays.stream(ExamPaperType.values()).anyMatch(type -> type.toString().equals(examPaperType));
		if (!valid) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examPaperType is error");
			return ret;
		}
		ExamPaper examPaper = examPaperService.get(eid);
		if (null == examPaper) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examPaper is null");
			return ret;
		}
		examPaper.setExamPaperType(ExamPaperType.valueOf(examPaperType));
		boolean success = examPaperService.update(examPaper);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("exam_paper_id", eid);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "update examPaper is error");
		}
		return ret;
	}

	/**
	 * 保存一个试题篮
	 * @param eid
	 * @param title
	 * @param examPaperType
	 * @param score
	 * @param resolvedTime
	 * @param difficulty
	 * @return
	 */
	@PutMapping("/exam_paper/{eid}/finished")
	public Map<String, Object> finished(@PathVariable String eid, String title, String examPaperType, Integer score,
			Integer resolvedTime, Float difficulty) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(eid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		boolean valid = Arrays.stream(ExamPaperType.values()).anyMatch(type -> type.toString().equals(examPaperType));
		if (!valid) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examPaperType is error");
			return ret;
		}
		if (null == score || score <= 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "score is error");
			return ret;
		}
		if (null == resolvedTime || resolvedTime <= 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "resolvedTime is error");
			return ret;
		}
		if (null == difficulty || difficulty <= 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "difficulty is error");
			return ret;
		}
		ExamPaper examPaper = examPaperService.get(eid);
		if (null == examPaper) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examPaper is null");
			return ret;
		}
		ExamPaper temp = examPaperService.getByExamAndPaperType(examPaper.getExamId(),
				ExamPaperType.valueOf(examPaperType));
		if (null != temp) {
			ret.put("code", CList.Api.Client.LOGIC_ERROR);
			ret.put("desc", "has this type examPaper");
			return ret;
		}
		boolean success = examPaperService.update(eid, title, score, ExamPaper.Status.FINISHED, resolvedTime,
				difficulty);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("exam_paper_id", eid);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "update error");
		}
		return ret;
	}

	/**
	 * 修改试卷
	 * @param id
	 * @param title
	 * @param score
	 * @param resolvedTime
	 * @param difficulty
	 * @param secretLevel
	 * @return
	 */
	@PutMapping("/exam_paper/{id}")
	public Map<String, Object> edit(@PathVariable String id, String title, Integer score, Integer resolvedTime,
			Float difficulty, Integer secretLevel) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		boolean success = examPaperService.update(id, title, score, resolvedTime, difficulty, secretLevel);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("exam_paper_id", id);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "update error");
		}
		return ret;
	}

	/**
	 * 删除试卷
	 * @param id
	 * @return
	 */
	@DeleteMapping("/exam_paper/{id}")
	public Map<String, Object> delete(@PathVariable String id) {
		Map<String, Object> ret = Maps.newHashMap();
		boolean success = this.examPaperService.delete(id);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("exam_id", id);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "delete error");
		}
		return ret;
	}

	/**
	 * 设置试题分数
	 * @param id
	 * @param questionId
	 * @param score
	 * @return
	 */
	@PutMapping(value = "/exam_paper/{id}/set_score")
	public Map<String, Object> setScore(@PathVariable String id, String questionId, Integer score) {
		Map<String, Object> ret = Maps.newHashMap();
		boolean success = examPaperQuestionService.setScore(id, questionId, score);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("exam_paper_id", id);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "set score error");
		}
		return ret;
	}

	/**
	 * 删除试题
	 * @param eid
	 * @param questionId
	 * @return
	 */
	@DeleteMapping(value = "/exam_paper/{eid}/remove_question")
	public Map<String, Object> remove(@PathVariable String eid, String questionId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(eid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "eid is null");
		}
		if (Strings.isNullOrEmpty(questionId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "questionId is null");
		}
		boolean success = examPaperQuestionService.removeQuestion(eid, questionId);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("question_id", questionId);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "delete error");
		}
		return ret;
	}

	/**
	 * 删除某个类型的试题
	 * @param eid
	 * @param questionType
	 * @return
	 */
	@DeleteMapping(value = "/exam_paper/{eid}/remove_questions")
	public Map<String, Object> remove2(@PathVariable String eid, String questionType) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(eid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "eid is null");
		}
		boolean valid = Arrays.stream(QuestionType.values()).anyMatch(type -> type.toString().equals(questionType));
		if (!valid) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "questionType is error");
			return ret;
		}
		boolean success = examPaperQuestionService.removeQuestions(eid, QuestionType.valueOf(questionType));
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "delete error");
		}
		return ret;
	}

	/**
	 * 添加试题
	 * @param id
	 * @param questionIds
	 * @return
	 */
	@PutMapping(value = "/exam_paper/{id}/set_questions")
	public Map<String, Object> setQuestions(@PathVariable String id, String[] questionIds) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == questionIds || questionIds.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "questionIds is null");
			return ret;
		}
		this.examPaperQuestionService.setQuestionsToExamPaper(id,
				Arrays.stream(questionIds).collect(Collectors.toList()), null);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 查询试卷中的全部试题
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/exam_paper/{id}/questions")
	public Map<String, Object> preview(@PathVariable String id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
			return ret;
		}
		ExamPaper examPaper = examPaperService.get(id);
		if (null == examPaper) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examPaper is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("examPaper", examPaper);
		ret.put("has_answer", answerRecordService.hasAnswer(id));
		List<ExamPaperQuestion> epqs = examPaperQuestionService.getByExamPaper(id);
		if (null == epqs || epqs.isEmpty()) {
			return ret;
		}
		Set<String> qids = Sets.newHashSet();
		List<Map<String, Object>> questions = epqs.parallelStream().map(epq -> {
			Question question = epq.getQuestion();
			qids.add(question.getId());
			Map<String, Object> q = questionToJsonUtil.toJosn(question);
			ExamPaperQuestion qpq = examPaperQuestionService.getByExamPaperAndQuestion(id, question.getId());
			if (null != qpq) {
				q.put("score", qpq.getScore());
			}
			return q;
		}).collect(Collectors.toList());
		ret.put("questions", questions);
		System.out.println("----questions size:" + questions.size());
		System.out.println("----qids size:" + qids.size());
		return ret;
	}

	/**
	 * 自动组卷（音乐素养）
	 * @param examId
	 * @param title
	 * @param score
	 * @param resolvedTime
	 * @param singleNum
	 * @param singleScore
	 * @param multiNum
	 * @param multiScore
	 * @param shortNum
	 * @param shortScore
	 * @param diffculty
	 * @param knowledge
	 * @return
	 */
	@PostMapping(value = "/exam_paper/auto/music_ability")
	public Map<String, Object> autoMusicAbility(String examId, String title, Integer score, Integer resolvedTime,
			Integer singleNum, Integer singleScore, Integer multiNum, Integer multiScore, Integer shortNum,
			Integer shortScore, Float diffculty, String[] knowledge) {
		Map<String, Object> ret = Maps.newHashMap();
		ExamPaper temp = examPaperService.getByExamAndPaperType(examId, ExamPaperType.MUSIC_ABILITY);
		if (null != temp) {
			ret.put("code", CList.Api.Client.LOGIC_ERROR);
			ret.put("desc", "has this type examPaper");
			return ret;
		}
		if (Strings.isNullOrEmpty(examId) || Strings.isNullOrEmpty(title) || null == score || score <= 0
				|| null == resolvedTime || resolvedTime <= 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "params is error");
			return ret;
		}
		Exam exam = examService.findById(examId);
		if (null == exam) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "exam is null");
			return ret;
		}
		if ((null == singleNum || singleNum <= 0 || null == singleScore || singleScore <= 0)
				&& (null == multiNum || multiNum <= 0 || null == multiScore || multiScore <= 0)
				&& (null == shortNum || shortNum <= 0 || null == shortScore || shortScore <= 0)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "params is error");
			return ret;
		}
		List<QuestionType> types = Lists.newArrayList();
		boolean enough = false;
		if (null != singleNum && singleNum > 0) {
			enough = questionService.hasEnoughQuestions(singleNum, exam.getGrade(), QuestionType.SINGLE_SELECT);
			if (!enough) {
				ret.put("code", CList.Api.Client.LOGIC_ERROR);
				ret.put("question_type", QuestionType.SINGLE_SELECT);
				ret.put("desc", "has no enough questions in this questionType");
				return ret;
			}
			types.add(QuestionType.SINGLE_SELECT);
		}
		if (null != multiNum && multiNum > 0) {
			enough = questionService.hasEnoughQuestions(multiNum, exam.getGrade(), QuestionType.MULTI_SELECT);
			if (!enough) {
				ret.put("code", CList.Api.Client.LOGIC_ERROR);
				ret.put("question_type", QuestionType.MULTI_SELECT);
				ret.put("desc", "has no enough questions in this questionType");
				return ret;
			}
			types.add(QuestionType.MULTI_SELECT);
		}
		if (null != shortNum && shortNum > 0) {
			enough = questionService.hasEnoughQuestions(shortNum, exam.getGrade(), QuestionType.SHORT_ANSWER);
			if (!enough) {
				ret.put("code", CList.Api.Client.LOGIC_ERROR);
				ret.put("question_type", QuestionType.SHORT_ANSWER);
				ret.put("desc", "has no enough questions in this SHORT_ANSWER");
				return ret;
			}
			types.add(QuestionType.SHORT_ANSWER);
		}
		Map<String, Integer> knowledges = Maps.newHashMap();
		if (null != knowledge && knowledge.length >= 1) {
			for (String k : knowledge) {
				Integer num = questionService.getQuestionsNum(exam.getGrade(), k, types);
				if (num < 1) {
					ret.put("code", CList.Api.Client.LOGIC_ERROR);
					ret.put("knowledge", k);
					ret.put("desc", "has no questions in this knowledge");
					return ret;
				} else {
					knowledges.put(k, num);
				}
			}
		}
		String examPaperId = examPaperQuestionService.auto(examId, exam.getGrade(), title, score, resolvedTime,
				singleNum, singleScore, multiNum, multiScore, shortNum, shortScore, diffculty, knowledges, types);
		if (Strings.isNullOrEmpty(examPaperId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "auto create papaer error");
		} else {
			ret.put("code", CList.Api.Client.OK);
			ret.put("exam_paper_id", examPaperId);
		}
		System.out.println("---ret:" + ret);
		return ret;
	}

	/**
	 * 自动组卷（表演）
	 * @param examId
	 * @param title
	 * @param score
	 * @param resolvedTime
	 * @param singNum
	 * @param singScore
	 * @param behindBackSingNum
	 * @param behindBackSingScore
	 * @param behindBackPerformanceNum
	 * @param behindBackPerformanceScore
	 * @param performanceNum
	 * @param performanceScore
	 * @param sightSingingNum
	 * @param sightSingingScore
	 * @param diffculty
	 * @param knowledge
	 * @return
	 */
	@PostMapping(value = "/exam_paper/auto/act")
	public Map<String, Object> autoAct(String examId, String title, Integer score, Integer resolvedTime,
			Integer singNum, Integer singScore, Integer behindBackSingNum, Integer behindBackSingScore,
			Integer behindBackPerformanceNum, Integer behindBackPerformanceScore, Integer performanceNum,
			Integer performanceScore, Integer sightSingingNum, Integer sightSingingScore, Float diffculty,
			String[] knowledge) {
		Map<String, Object> ret = Maps.newHashMap();
		ExamPaper temp = examPaperService.getByExamAndPaperType(examId, ExamPaperType.ACT);
		if (null != temp) {
			ret.put("code", CList.Api.Client.LOGIC_ERROR);
			ret.put("desc", "has this type examPaper");
			return ret;
		}
		if (Strings.isNullOrEmpty(examId) || Strings.isNullOrEmpty(title) || null == score || score <= 0
				|| null == resolvedTime || resolvedTime <= 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "params is error");
			return ret;
		}
		Exam exam = examService.findById(examId);
		if (null == exam) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "exam is null");
			return ret;
		}
		if ((null == singNum || singNum <= 0 || null == singScore || singScore <= 0)
				&& (null == behindBackSingNum || behindBackSingNum <= 0 || null == behindBackSingScore
						|| behindBackSingScore <= 0)
				&& (null == behindBackPerformanceNum || behindBackPerformanceNum <= 0
						|| null == behindBackPerformanceScore || behindBackPerformanceScore <= 0)
				&& (null == performanceNum || performanceNum <= 0 || null == performanceScore || performanceScore <= 0)
				&& (null == sightSingingNum || sightSingingNum <= 0 || null == sightSingingScore
						|| sightSingingScore <= 0)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "params is error");
			return ret;
		}
		List<QuestionType> types = Lists.newArrayList();
		boolean enough = false;
		if (null != singNum && singNum > 0) {
			enough = questionService.hasEnoughQuestions(singNum, exam.getGrade(), QuestionType.SING);
			if (!enough) {
				ret.put("code", CList.Api.Client.LOGIC_ERROR);
				ret.put("question_type", QuestionType.SING);
				ret.put("desc", "has no enough questions in this questionType");
				return ret;
			}
			types.add(QuestionType.SING);
		}
		if (null != behindBackSingNum && behindBackSingNum > 0) {
			enough = questionService.hasEnoughQuestions(behindBackSingNum, exam.getGrade(),
					QuestionType.BEHIND_BACK_SING);
			if (!enough) {
				ret.put("code", CList.Api.Client.LOGIC_ERROR);
				ret.put("question_type", QuestionType.BEHIND_BACK_SING);
				ret.put("desc", "has no enough questions in this questionType");
				return ret;
			}
			types.add(QuestionType.BEHIND_BACK_SING);
		}
		if (null != behindBackPerformanceNum && behindBackPerformanceNum > 0) {
			enough = questionService.hasEnoughQuestions(behindBackPerformanceNum, exam.getGrade(),
					QuestionType.BEHIND_BACK_PERFORMANCE);
			if (!enough) {
				ret.put("code", CList.Api.Client.LOGIC_ERROR);
				ret.put("question_type", QuestionType.BEHIND_BACK_PERFORMANCE);
				ret.put("desc", "has no enough questions in this questionType");
				return ret;
			}
			types.add(QuestionType.BEHIND_BACK_PERFORMANCE);
		}
		if (null != performanceNum && performanceNum > 0) {
			enough = questionService.hasEnoughQuestions(performanceNum, exam.getGrade(), QuestionType.PERFORMANCE);
			if (!enough) {
				ret.put("code", CList.Api.Client.LOGIC_ERROR);
				ret.put("question_type", QuestionType.PERFORMANCE);
				ret.put("desc", "has no enough questions in this questionType");
				return ret;
			}
			types.add(QuestionType.PERFORMANCE);
		}
		if (null != sightSingingNum && sightSingingNum > 0) {
			enough = questionService.hasEnoughQuestions(sightSingingNum, exam.getGrade(), QuestionType.SIGHT_SINGING);
			if (!enough) {
				ret.put("code", CList.Api.Client.LOGIC_ERROR);
				ret.put("question_type", QuestionType.SIGHT_SINGING);
				ret.put("desc", "has no enough questions in this questionType");
				return ret;
			}
			types.add(QuestionType.SIGHT_SINGING);
		}
		Map<String, Integer> knowledges = Maps.newHashMap();
		if (null != knowledge && knowledge.length >= 1) {
			for (String k : knowledge) {
				Integer num = questionService.getQuestionsNum(exam.getGrade(), k, types);
				if (num < 1) {
					ret.put("code", CList.Api.Client.LOGIC_ERROR);
					ret.put("knowledge", k);
					ret.put("desc", "has no questions in this knowledge");
					return ret;
				} else {
					knowledges.put(k, num);
				}
			}
		}
		String examPaperId = examPaperQuestionService.auto(examId, exam.getGrade(), title, score, resolvedTime, singNum,
				singScore, behindBackSingNum, behindBackSingScore, behindBackPerformanceNum, behindBackPerformanceScore,
				performanceNum, performanceScore, sightSingingNum, sightSingingScore, diffculty, knowledges, types);
		if (Strings.isNullOrEmpty(examPaperId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "auto create papaer error");
		} else {
			ret.put("code", CList.Api.Client.OK);
			ret.put("exam_paper_id", examPaperId);
		}
		System.out.println("---ret:" + ret);
		return ret;
	}

	/**
	 * 下载试卷
	 * @param pid
	 * @param examId
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@GetMapping(value = "/exam_paper/{pid}", produces = JSON_PRODUCES)
	public Map<String, Object> get(@PathVariable String pid, @RequestParam(value = "exam_id") String examId,
			HttpServletResponse response) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(pid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examPaper id is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(examId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examId id is null");
			return ret;
		}
		Exam exam = examService.findById(examId);
		if (null == exam) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "exam is null");
			return ret;
		}
		ExamPaper examPaper = this.examPaperService.get(pid);
		if (null == examPaper) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examPaper is null");
			return ret;
		}
		List<Question> questions = examPaperQuestionService.getQuestionByExamPaper(examPaper.getId());
		if (null == questions || questions.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no questions be found");
			return ret;
		}
		String examPaperFolderPath = zipPath + File.separator + pid;
		String zipName = pid + ".zip";
		String zipFilePath = zipPath + File.separator + zipName;
		Path path = Paths.get(zipFilePath);
		File zipFile = new File(zipFilePath);
		Long updateAt = examPaper.getUpdateAt();
		lock.lock();
		try {
			if (zipFile.exists()) {
				FileTime updateAtTime = (FileTime) java.nio.file.Files.getAttribute(path, ATTR);
				if (null == updateAtTime || Long.valueOf(updateAt).longValue() != updateAtTime.toMillis()) {
					zipFile.delete();
					createJsonFile(examPaperFolderPath, exam, examPaper, questions);
					createFiles(examPaperFolderPath, questions);
					zip(zipFile, examPaperFolderPath, path, updateAt);
				}
			} else {
				createJsonFile(examPaperFolderPath, exam, examPaper, questions);
				createFiles(examPaperFolderPath, questions);
				zip(zipFile, examPaperFolderPath, path, updateAt);
			}
		} finally {
			lock.unlock();
		}
		download(zipFile, zipName, response);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@GetMapping(value = "/exam_paper/{pid}/has_answer")
	public Map<String, Object> hasAnswer(@PathVariable String pid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(pid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "exam_paper_id is null");
			return ret;
		}
		boolean hasAnswer = answerRecordService.hasAnswer(pid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("has_answer", hasAnswer);
		return ret;
	}

	@GetMapping(value = "/exam_paper/{pid}/qids")
	public Map<String, Object> getQuestionIds(@PathVariable String pid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(pid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "exam_paper_id is null");
			return ret;
		}
		List<Question> questions = examPaperQuestionService.getQuestionByExamPaper(pid);
		ret.put("code", CList.Api.Client.OK);
		if (null == questions || questions.isEmpty()) {
			return ret;
		}
		List<String> ids = questions.stream().map(question -> question.getId()).collect(Collectors.toList());
		ret.put("ids", ids);
		return ret;
	}

	@GetMapping(value = "/exam_paper/{pid}/answer_user_num", produces = JSON_PRODUCES)
	public Map<String, Object> getAnswerNum(@PathVariable String pid, String classId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(pid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "pid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(classId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classId is null");
			return ret;
		}
		Integer num = answerRecordService.getAnswerUserNum(pid, classId);
		ret.put("code", CList.Api.Client.OK);
		ret.put("num", num);
		return ret;
	}

	@GetMapping(value = "/exam_paper/{pid}/average_score", produces = JSON_PRODUCES)
	public Map<String, Object> getAverageScore(@PathVariable String pid, String classId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(pid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "pid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(classId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classId is null");
			return ret;
		}
		Integer score = answerRecordService.getAverageScore(pid, classId);
		ret.put("code", CList.Api.Client.OK);
		ret.put("score", score);
		return ret;
	}

	private void createJsonFile(String examPaperFolderPath, Exam exam, ExamPaper examPaper, List<Question> questions) {
		File jsonFile = new File(examPaperFolderPath + File.separator + "json.json");
		Map<String, Object> ret = Maps.newHashMap();
		List<Map<String, Object>> questionsList = Lists.newArrayList();
		ret.put("exam_id", exam.getId());
		ret.put("exam_type", exam.getExamType());
		ret.put("exam_paper_id", examPaper.getId());
		ret.put("exam_paper_title", examPaper.getTitle());
		ret.put("resolved_time", examPaper.getResolvedTime());
		ret.put("exam_paper_score", examPaper.getScore());
		ret.put("exam_questions", questionsList);
		if (null != questions && !questions.isEmpty()) {
			for (Question question : questions) {
				Map<String, Object> jsonMap = this.getQuestionJson(question, examPaper);
				questionsList.add(jsonMap);
			}
		}
		try {
			Files.createParentDirs(jsonFile);
			Files.write(gson.toJson(ret), jsonFile, Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, Object> getQuestionJson(Question question, ExamPaper examPaper) {
		Map<String, Object> jsonMap = questionToJsonUtil.toJosn(question);
		jsonMap.put("score", examPaperQuestionService.getScore(examPaper.getId(), question.getId()));
		return jsonMap;
	}

	private void createFiles(String examPaperFolderPath, List<Question> questions) {
		if (null != questions && !questions.isEmpty()) {
			for (Question question : questions) {
				String questionFolderPath = examPaperFolderPath + File.separator + question.getId();
				List<String> urls = Lists.newArrayList();
				String titleImageUrl = question.getTitleImage();
				if (StringUtils.isNotBlank(titleImageUrl)) {
					urls.add(titleImageUrl);
				}
				String titleAudioUrl = question.getTitleAudio();
				if (StringUtils.isNotBlank(titleAudioUrl)) {
					urls.add(titleAudioUrl);
				}
				String xmlPathUrl = question.getXmlPath();
				if (StringUtils.isNotBlank(xmlPathUrl)) {
					urls.add(xmlPathUrl);
				}
				String templatePath = question.getTemplatePath();
				if (StringUtils.isNotBlank(templatePath)) {
					urls.add(templatePath);
				}
				List<Option> options = optionService.getByQuestion(question.getId());
				for (Option option : options) {
					String optionImagePath = option.getOptionImage();
					if (StringUtils.isNotBlank(optionImagePath)) {
						urls.add(optionImagePath);
					}
					String xmlPath = option.getXmlPath();
					if (StringUtils.isNotBlank(xmlPath)) {
						urls.add(xmlPath);
					}
				}
				copyFile(questionFolderPath, urls);
			}
		}
	}

	private void zip(File zipFile, String examPaperFolderPath, Path path, Long updateAt) throws IOException {
		zipFileUtil.zipFile(zipFile, examPaperFolderPath);
		FileTime fileTime = FileTime.fromMillis(updateAt);
		java.nio.file.Files.setAttribute(path, ATTR, fileTime);
		FileUtils.deleteDirectory(new File(examPaperFolderPath));
	}

	private void download(File zipFile, String zipName, HttpServletResponse response) {
		if (null == zipFile || !zipFile.exists()) {
			return;
		}
		OutputStream to = null;
		try {
			response.setContentType("application/zip;charset=UTF-8");
			response.setHeader("Content-disposition", "filename=" + zipName);
			response.setContentLengthLong(zipFile.length());
			to = response.getOutputStream();
			FileUtils.copyFile(zipFile, to);
			to.flush();
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (to != null) {
				try {
					to.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void copyFile(String questionFolderPath, List<String> rids) {
		if (null == rids || rids.isEmpty()) {
			return;
		}
		rids.forEach(rid -> {
			if (Strings.isNullOrEmpty(rid)) {
				return;
			}
			Map<String, Object> ret = cloudFileUtil.getResource(rid);
			if (null == ret || ret.isEmpty()) {
				return;
			}
			String ext = (String) ret.getOrDefault("ext", "");
			File toFile = new File(questionFolderPath + File.separator + rid + "." + ext);
			if (toFile.exists()) {
				return;
			}
			InputStream is = cloudFileUtil.download(rid, ext);
			if (null == is) {
				return;
			}
			try {
				FileUtils.copyInputStreamToFile(is, toFile);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != is) {
					try {
						is.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
