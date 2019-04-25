package com.nercl.music.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.dao.ExamPaperQuestionDao;
import com.nercl.music.entity.CheckRecord;
import com.nercl.music.entity.Exam;
import com.nercl.music.entity.ExamPaper;
import com.nercl.music.entity.ExamPaperQuestion;
import com.nercl.music.entity.ExamQuestion;
import com.nercl.music.service.ExamExamPaperService;
import com.nercl.music.service.ExamPaperQuestionService;
import com.nercl.music.service.ExamPaperService;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.ExamService;

@Service
@Transactional
public class ExamPaperQuestionServiceImpl implements ExamPaperQuestionService {

	@Autowired
	private ExamPaperQuestionDao examPaperQuestionDao;

	@Autowired
	private ExamQuestionService examQuestionService;

	@Autowired
	private ExamService examService;

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private ExamExamPaperService examExamPaperService;

	@Override
	public void setQuestionsToExamPaper(String examPaperId, String[] examQuestionIds, Integer score) {
		for (String examQuestionId : examQuestionIds) {
			if (StringUtils.isNotBlank(examQuestionId)) {
				ExamPaperQuestion examPaperQuestion = this.getByExamPaperAndQuestion(examPaperId, examQuestionId);
				if (null == examPaperQuestion) {
					ExamPaperQuestion epq = new ExamPaperQuestion();
					epq.setExamPaperId(examPaperId);
					epq.setExamQuestionId(examQuestionId);
					epq.setScore(score);
					this.examPaperQuestionDao.save(epq);
				}
			}
		}
	}

	@Override
	public void removeQuestions(String examPaperId, String examQuestionId) {
		ExamPaperQuestion examPaperQuestion = this.getByExamPaperAndQuestion(examPaperId, examQuestionId);
		if (null != examPaperQuestion) {
			this.examPaperQuestionDao.delete(examPaperQuestion);
		}
	}

	@Override
	public ExamPaperQuestion getByExamPaperAndQuestion(String examPaperId, String questionId) {
		return this.examPaperQuestionDao.getByExamPaperAndQuestion(examPaperId, questionId);
	}

	@Override
	public boolean setScore(String examPaperId, String questionId, Integer score) {
		ExamPaper examPaper = this.examPaperService.get(examPaperId);
		ExamPaperQuestion examPaperQuestion = this.getByExamPaperAndQuestion(examPaperId, questionId);
		if (null != examPaper && null != examPaperQuestion) {
			examPaperQuestion.setScore(score);
			this.examPaperQuestionDao.update(examPaperQuestion);
			return true;
		}
		return false;
	}

	@Override
	public boolean auto(String title, Integer score, Integer resolvedTime, Integer subjectType, Integer singleNum,
	        Integer singleScore, Integer multiNum, Integer multiScore, Integer shortNum, Integer shortScore,
	        Integer lookSingNum, Integer lookSingScore) {
		singleNum = null == singleNum ? 0 : singleNum;
		singleScore = null == singleScore ? 0 : singleScore;
		multiNum = null == multiNum ? 0 : multiNum;
		multiScore = null == multiScore ? 0 : multiScore;
		shortNum = null == shortNum ? 0 : shortNum;
		shortScore = null == shortScore ? 0 : shortScore;
		lookSingNum = null == lookSingNum ? 0 : lookSingNum;
		lookSingScore = null == lookSingScore ? 0 : lookSingScore;
		Integer total = singleNum * singleScore + multiNum * multiScore + shortNum * shortScore
		        + lookSingNum * lookSingScore;
		if (!total.equals(score)) {
			return false;
		}
		ExamPaper examPaper = new ExamPaper();
		examPaper.setTitle(title);
		examPaper.setScore(score);
		examPaper.setResolvedTime(resolvedTime);
		examPaper.setSubjectType(subjectType);
		examPaper.setYear(LocalDate.now().getYear());
		examPaper.setCheckStatus(CheckRecord.Status.FOR_CHECKED);
		examPaper.setQuestionNum(singleNum + multiNum + shortNum + lookSingNum);
		this.examPaperService.save(examPaper);
		if (singleNum > 0 && singleScore > 0) {
			List<ExamQuestion> singleQuestions = this.examQuestionService.random(CList.Api.QuestionType.SINGLE_SELECT,
			        singleNum);
			List<String> ids = singleQuestions.stream().map(question -> {
				return question.getId();
			}).collect(Collectors.toList());
			this.setQuestionsToExamPaper(examPaper.getId(), ids.stream().toArray(size -> new String[size]),
			        singleScore);
		}
		if (multiNum > 0 && multiScore > 0) {
			List<ExamQuestion> multiQuestions = this.examQuestionService.random(CList.Api.QuestionType.MULTI_SELECT,
			        multiNum);
			List<String> ids = multiQuestions.stream().map(question -> {
				return question.getId();
			}).collect(Collectors.toList());
			this.setQuestionsToExamPaper(examPaper.getId(), ids.stream().toArray(size -> new String[size]), multiScore);
		}
		if (shortNum > 0 && shortScore > 0) {
			List<ExamQuestion> shortQuestions = this.examQuestionService.random(CList.Api.QuestionType.SHORT_ANSWER,
			        shortNum);
			List<String> ids = shortQuestions.stream().map(question -> {
				return question.getId();
			}).collect(Collectors.toList());
			this.setQuestionsToExamPaper(examPaper.getId(), ids.stream().toArray(size -> new String[size]), shortScore);
		}
		if (lookSingNum > 0 && lookSingScore > 0) {
			List<ExamQuestion> lookSingQuestions = this.examQuestionService.random(CList.Api.QuestionType.LOOK_SING,
			        lookSingNum);
			List<String> ids = lookSingQuestions.stream().map(question -> {
				return question.getId();
			}).collect(Collectors.toList());
			this.setQuestionsToExamPaper(examPaper.getId(), ids.stream().toArray(size -> new String[size]),
			        lookSingScore);
		}
		return true;
	}

	@Override
	public Map<String, Integer> getScore(String examPaperId) {
		List<ExamPaperQuestion> examPaperQuestions = this.examPaperQuestionDao.getByExamPaper(examPaperId);
		if (null != examPaperQuestions && !examPaperQuestions.isEmpty()) {
			Map<String, Integer> ret = Maps.newHashMap();
			examPaperQuestions.forEach(question -> {
				ret.put(question.getExamQuestionId(), question.getScore());
			});
			return ret;
		}
		return null;
	}

	@Override
	public boolean auto(String examId, String yueliTitle, Integer yueliScore, Integer yueliResolvedTime,
	        Integer yueliSingleTotalScore, Integer yueliSingleNum, Integer yueliSingleScore,
	        Integer yueliMultiTotalScore, Integer yueliMultiNum, Integer yueliMultiScore, Integer yueliShortTotalScore,
	        Integer yueliShortNum, Integer yueliShortScore, String tingyinTitle, Integer tingyinResolvedTime,
	        Integer tingyinShortTotalScore, Integer tingyinShortNum, Integer tingyinScore, String lookSingTitle,
	        Integer lookSingResolvedTime, Integer lookSingTotalScore, Integer lookSingNum, Integer lookSingScore) {
		Exam exam = this.examService.get(examId);
		this.autoCreateYueliPaper(exam, yueliTitle, yueliScore, yueliResolvedTime, yueliSingleTotalScore,
		        yueliSingleNum, yueliSingleScore, yueliMultiTotalScore, yueliMultiNum, yueliMultiScore,
		        yueliShortTotalScore, yueliShortNum, yueliShortScore);
		this.autoCreateTingyinPaper(exam, tingyinTitle, tingyinResolvedTime, tingyinShortTotalScore, tingyinShortNum,
		        tingyinScore);
		this.autoCreateLooksingPaper(exam, lookSingTitle, lookSingResolvedTime, lookSingTotalScore, lookSingNum,
		        lookSingScore);
		return false;
	}

	private void autoCreateYueliPaper(Exam exam, String yueliTitle, Integer yueliScore, Integer yueliResolvedTime,
	        Integer yueliSingleTotalScore, Integer yueliSingleNum, Integer yueliSingleScore,
	        Integer yueliMultiTotalScore, Integer yueliMultiNum, Integer yueliMultiScore, Integer yueliShortTotalScore,
	        Integer yueliShortNum, Integer yueliShortScore) {
		if (null != yueliScore
		        && !yueliScore.equals(yueliSingleTotalScore + yueliMultiTotalScore + yueliShortTotalScore)) {
			return;
		}
		yueliSingleNum = null == yueliSingleNum ? 0 : yueliSingleNum;
		yueliSingleScore = null == yueliSingleScore ? 0 : yueliSingleScore;
		yueliMultiNum = null == yueliMultiNum ? 0 : yueliMultiNum;
		yueliMultiScore = null == yueliMultiScore ? 0 : yueliMultiScore;
		yueliShortNum = null == yueliShortNum ? 0 : yueliShortNum;
		yueliShortScore = null == yueliShortScore ? 0 : yueliShortScore;
		Integer total = yueliSingleNum * yueliSingleScore + yueliMultiNum * yueliMultiScore
		        + yueliShortNum * yueliShortScore;
		if (!total.equals(yueliScore)) {
			return;
		}
		ExamPaper examPaper = new ExamPaper();
		examPaper.setTitle(yueliTitle);
		examPaper.setScore(yueliScore);
		examPaper.setResolvedTime(yueliResolvedTime);
		examPaper.setSubjectType(CList.Api.SubjectType.YUE_LI);
		examPaper.setYear(LocalDate.now().getYear());
		examPaper.setCheckStatus(CheckRecord.Status.PASSED);
		examPaper.setQuestionNum(yueliSingleNum + yueliMultiNum + yueliShortNum);
		this.examPaperService.save(examPaper);
		this.examExamPaperService.save(exam.getId(), examPaper.getId(), null);
		if (yueliSingleNum > 0 && yueliSingleScore > 0) {
			List<ExamQuestion> singleQuestions = this.examQuestionService.random(CList.Api.QuestionType.SINGLE_SELECT,
			        yueliSingleNum);
			List<String> ids = singleQuestions.stream().map(question -> {
				return question.getId();
			}).collect(Collectors.toList());
			this.setQuestionsToExamPaper(examPaper.getId(), ids.stream().toArray(size -> new String[size]),
			        yueliSingleScore);
		}
		if (yueliMultiNum > 0 && yueliMultiScore > 0) {
			List<ExamQuestion> multiQuestions = this.examQuestionService.random(CList.Api.QuestionType.MULTI_SELECT,
			        yueliMultiNum);
			List<String> ids = multiQuestions.stream().map(question -> {
				return question.getId();
			}).collect(Collectors.toList());
			this.setQuestionsToExamPaper(examPaper.getId(), ids.stream().toArray(size -> new String[size]),
			        yueliMultiScore);
		}
		if (yueliShortNum > 0 && yueliShortScore > 0) {
			List<ExamQuestion> shortQuestions = this.examQuestionService.random(CList.Api.QuestionType.SHORT_ANSWER,
			        yueliShortNum);
			List<String> ids = shortQuestions.stream().map(question -> {
				return question.getId();
			}).collect(Collectors.toList());
			this.setQuestionsToExamPaper(examPaper.getId(), ids.stream().toArray(size -> new String[size]),
			        yueliShortScore);
		}

	}

	private void autoCreateTingyinPaper(Exam exam, String tingyinTitle, Integer resolvedTime,
	        Integer tingyinShortTotalScore, Integer tingyinShortNum, Integer tingyinScore) {
		tingyinShortNum = null == tingyinShortNum ? 0 : tingyinShortNum;
		tingyinScore = null == tingyinScore ? 0 : tingyinScore;
		Integer total = tingyinShortNum * tingyinScore;
		if (!total.equals(tingyinShortTotalScore)) {
			return;
		}
		ExamPaper examPaper = new ExamPaper();
		examPaper.setTitle(tingyinTitle);
		examPaper.setScore(tingyinShortTotalScore);
		examPaper.setResolvedTime(resolvedTime);
		examPaper.setSubjectType(CList.Api.SubjectType.TING_YIN);
		examPaper.setYear(LocalDate.now().getYear());
		examPaper.setCheckStatus(CheckRecord.Status.PASSED);
		examPaper.setQuestionNum(tingyinShortNum);
		this.examPaperService.save(examPaper);
		this.examExamPaperService.save(exam.getId(), examPaper.getId(), null);
		if (tingyinShortNum > 0 && tingyinScore > 0) {
			List<ExamQuestion> shortQuestions = this.examQuestionService.random(CList.Api.QuestionType.SHORT_ANSWER,
			        tingyinShortNum);
			List<String> ids = shortQuestions.stream().map(question -> {
				return question.getId();
			}).collect(Collectors.toList());
			this.setQuestionsToExamPaper(examPaper.getId(), ids.stream().toArray(size -> new String[size]),
			        tingyinScore);
		}
	}

	private void autoCreateLooksingPaper(Exam exam, String lookSingTitle, Integer resolvedTime,
	        Integer lookSingTotalScore, Integer lookSingNum, Integer lookSingScore) {
		lookSingNum = null == lookSingNum ? 0 : lookSingNum;
		lookSingScore = null == lookSingScore ? 0 : lookSingScore;
		Integer total = lookSingNum * lookSingScore;
		if (!total.equals(lookSingTotalScore)) {
			return;
		}
		ExamPaper examPaper = new ExamPaper();
		examPaper.setTitle(lookSingTitle);
		examPaper.setScore(lookSingTotalScore);
		examPaper.setResolvedTime(resolvedTime);
		examPaper.setSubjectType(CList.Api.SubjectType.LOOK_SING);
		examPaper.setYear(LocalDate.now().getYear());
		examPaper.setCheckStatus(CheckRecord.Status.PASSED);
		examPaper.setQuestionNum(lookSingNum);
		this.examPaperService.save(examPaper);
		this.examExamPaperService.save(exam.getId(), examPaper.getId(), null);
		if (lookSingNum > 0 && lookSingScore > 0) {
			List<ExamQuestion> lookSingQuestions = this.examQuestionService.random(CList.Api.QuestionType.LOOK_SING,
			        lookSingNum);
			List<String> ids = lookSingQuestions.stream().map(question -> {
				return question.getId();
			}).collect(Collectors.toList());
			this.setQuestionsToExamPaper(examPaper.getId(), ids.stream().toArray(size -> new String[size]),
			        lookSingScore);
		}
	}

	@Override
	public Integer getScore(String examPaperId, String examQuestionId) {
		ExamPaperQuestion examPaperQuestion = this.getByExamPaperAndQuestion(examPaperId, examQuestionId);
		return null == examPaperQuestion ? 0 : examPaperQuestion.getScore();
	}

}
