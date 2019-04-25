package com.nercl.music.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.dao.AnswerDao;
import com.nercl.music.dao.ExamPaperQuestionDao;
import com.nercl.music.dao.ExamQuestionDao;
import com.nercl.music.entity.Answer;
import com.nercl.music.entity.CheckRecord;
import com.nercl.music.entity.ExamPaperQuestion;
import com.nercl.music.entity.ExamQuestion;
import com.nercl.music.entity.MFile;
import com.nercl.music.entity.Option;
import com.nercl.music.entity.user.Login;
import com.nercl.music.service.CheckRecordService;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.MFileService;
import com.nercl.music.service.OptionService;
import com.nercl.music.util.DESCryption;

@Service
@Transactional
public class ExamQuestionServiceImpl implements ExamQuestionService {

	@Autowired
	private ExamQuestionDao examQuestionDao;

	@Autowired
	private AnswerDao answerDao;

	@Autowired
	private OptionService optionService;

	@Autowired
	private DESCryption desCryption;

	@Autowired
	private MFileService mfileService;

	@Autowired
	private CheckRecordService checkRecordService;

	@Autowired
	private ExamPaperQuestionDao examPaperQuestionDao;

	private static final List<String> ORDER = Lists.newArrayList("A. ", "B. ", "C. ", "D. ", "E. ", "F. ", "G. ", "H. ",
	        "I. ", "J. ");

	@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	@Override
	public List<ExamQuestion> getDefaultLookSingQuestions() {
		return this.examQuestionDao.getDefaultLookSingQuestions();
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	@Override
	public List<ExamQuestion> getDefaultYueLiQuestions() {
		return this.examQuestionDao.getDefaultYueLiQuestions();
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	@Override
	public List<ExamQuestion> getDefaultTingYinQuestions() {
		return this.examQuestionDao.getDefaultTingYinQuestions();
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	@Override
	public Map<Integer, List<ExamQuestion>> getByExamPaper(String examPaperId) {
		List<ExamQuestion> questions = this.examQuestionDao.getByExamPaper(examPaperId);
		questions.forEach(question -> {
			question.setTitle2(this.desCryption.decode(question.getTitle()));
			if (CList.Api.QuestionType.SINGLE_SELECT.equals(question.getQuestionType())
		            || CList.Api.QuestionType.MULTI_SELECT.equals(question.getQuestionType())) {
				List<Option> options = question.getOptions();
				int i = 0;
				for (Option option : options) {
					if (StringUtils.isNotBlank(option.getContent())) {
						option.setContent2(ORDER.get(i++) + desCryption.decode(option.getContent()));
					} else if (StringUtils.isNotBlank(option.getXmlPath())) {
						String xmlPath = this.desCryption.decode(option.getXmlPath());
						MFile mfile = this.mfileService.getByUrl(xmlPath);
						if (null != mfile) {
							option.setXmlPath2(mfile.getId());
						}
					}
				}
			} else if (CList.Api.QuestionType.SHORT_ANSWER.equals(question.getQuestionType())
		            || CList.Api.QuestionType.LOOK_SING.equals(question.getQuestionType())) {
				String xmlPath = question.getXmlPath();
				MFile mfile = this.mfileService.getByUrl(xmlPath);
				if (null != mfile) {
					question.setXmlPath2(mfile.getId());
				}
			}
		});
		Map<Integer, List<ExamQuestion>> map = questions.stream()
		        .collect(Collectors.groupingBy(ExamQuestion::getQuestionType));
		return map;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	@Override
	public List<ExamQuestion> getLookSingQuestions(String examId, String examPaperId, int size) {
		List<ExamQuestion> questions = this.examQuestionDao.getLookSingQuestions(examId, examPaperId, size);
		if (null == questions || questions.isEmpty()) {
			questions = this.getDefaultLookSingQuestions();
		}
		return questions;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	@Override
	public List<ExamQuestion> getTingYinQuestions(String examId, String examPaperId) {
		List<ExamQuestion> questions = this.examQuestionDao.getTingYinQuestions(examId, examPaperId);
		if (null == questions || questions.isEmpty()) {
			questions = this.getDefaultTingYinQuestions();
		}
		return questions;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	@Override
	public List<ExamQuestion> getYueLiQuestions(String examId, String examPaperId) {
		List<ExamQuestion> questions = this.examQuestionDao.getYueLiQuestions(examId, examPaperId);
		if (null == questions || questions.isEmpty()) {
			questions = this.getDefaultYueLiQuestions();
		}
		return questions;
	}

	@Override
	public ExamQuestion get(String id) {
		return this.examQuestionDao.findByID(id);
	}

	@Override
	public boolean save(ExamQuestion examQuestion) {
		this.examQuestionDao.save(examQuestion);
		return true;
	}

	@Override
	public boolean saveAnswer(Answer answer) {
		this.answerDao.save(answer);
		return true;
	}

	@Override
	public List<Map<String, Object>> json() {
		List<Option> options = this.optionService.list();
		List<Map<String, Object>> rets = Lists.newArrayList();
		options.forEach(option -> {
			if (StringUtils.isNotBlank(option.getContent())) {
				Map<String, Object> ret = Maps.newHashMap();
				ret.put("id", option.getId());
				ret.put("content", option.getContent());
				rets.add(ret);
			}
		});
		return rets;
	}

	@Override
	public List<ExamQuestion> list(Integer type, String title, Float difficulty, int page, String status) {
		List<ExamQuestion> questions = this.examQuestionDao.list(type, title, difficulty, page, status);
		questions.forEach(question -> {
			question.setTitle2(this.desCryption.decode(question.getTitle()));
			if (CList.Api.QuestionType.SINGLE_SELECT.equals(question.getQuestionType())
		            || CList.Api.QuestionType.MULTI_SELECT.equals(question.getQuestionType())) {
				List<Option> options = question.getOptions();
				int i = 0;
				for (Option option : options) {
					if (StringUtils.isNotBlank(option.getContent())) {
						option.setContent2(ORDER.get(i++) + desCryption.decode(option.getContent()));
					} else if (StringUtils.isNotBlank(option.getXmlPath())) {
						String xmlPath = this.desCryption.decode(option.getXmlPath());
						String[] strs = xmlPath.split("/");
						String uuid = strs[strs.length - 1];
						MFile mfile = this.mfileService.get(uuid);
						if (null != mfile) {
							option.setXmlPath2(mfile.getId());
						}
					}
				}
			} else {
				String xmlPath = question.getXmlPath();
				String[] strs = xmlPath.split("/");
				String uuid = strs[strs.length - 1];
				MFile mfile = this.mfileService.get(uuid);
				if (null != mfile) {
					question.setXmlPath2(mfile.getId());
				}
			}
			Answer answer = question.getAnswer();
			if (null != answer) {
				if (StringUtils.isNotBlank(answer.getContent())) {
					answer.setContent2(this.desCryption.decode(answer.getContent()));
				}
				if (StringUtils.isNotBlank(answer.getXmlPath())) {
					MFile mfile = this.mfileService.getByUrl(answer.getXmlPath());
					if (null != mfile) {
						answer.setXmlPath2(mfile.getId());
					}
				}
			}
		});
		return questions;
	}

	@Override
	public List<ExamQuestion> list(Integer type, int page) {
		List<ExamQuestion> questions = this.examQuestionDao.list(type, page);
		questions.forEach(question -> {
			question.setTitle2(this.desCryption.decode(question.getTitle()));
		});
		return questions;
	}

	@Override
	public List<ExamQuestion> get(String examId, String examPaperId) {
		List<ExamPaperQuestion> epqs = this.examPaperQuestionDao.getByExamPaper(examPaperId);
		return epqs.stream().map(epq -> {
			return epq.getExamQuestion();
		}).collect(Collectors.toList());
	}

	@Override
	public List<ExamPaperQuestion> getExamPaperQuestion(String examId, String examPaperId) {
		List<ExamPaperQuestion> epqs = this.examPaperQuestionDao.getByExamPaper(examPaperId);
		return epqs;
	}

	@Override
	public List<ExamQuestion> random(Integer type, int size) {
		return this.examQuestionDao.random(type, size);
	}

	@Override
	public List<ExamQuestion> random(Integer type, Integer subjectType, int size) {
		return this.examQuestionDao.random(type, subjectType, size);
	}

	@Override
	public List<ExamQuestion> query(Integer type, String word, int page) {
		List<ExamQuestion> questions = null;
		word = "";
		questions = this.examQuestionDao.query(type, word, page);
		questions.forEach(question -> {
			question.setTitle2(this.desCryption.decode(question.getTitle()));
		});
		return questions;
	}

	@Override
	public boolean pass(String qid, Login login) {
		if (!qid.contains(",")) {
			ExamQuestion examQuestion = this.examQuestionDao.findByID(qid);
			if (null != examQuestion) {
				examQuestion.setCheckStatus(CheckRecord.Status.PASSED);
				this.examQuestionDao.update(examQuestion);
				CheckRecord checkRecord = checkRecordService.getCheckRecord(login, examQuestion,
				        CheckRecord.Status.PASSED, "question", "");
				checkRecordService.addRecord(checkRecord);
				return true;
			}
		} else {
			String[] toPass = qid.split(",");
			for (String id : toPass) {
				this.pass(id, login);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean unpass(String qid, Login login, String reason) {

		if (!qid.contains(",")) {
			ExamQuestion examQuestion = this.examQuestionDao.findByID(qid);
			if (null != examQuestion) {
				examQuestion.setCheckStatus(CheckRecord.Status.UN_PASSED);
				examQuestion.setUnPassReason(reason);
				this.examQuestionDao.update(examQuestion);
				CheckRecord checkRecord = checkRecordService.getCheckRecord(login, examQuestion,
				        CheckRecord.Status.UN_PASSED, "question", reason);
				checkRecordService.addRecord(checkRecord);
				return true;
			}
		} else {
			String[] toUnPass = qid.split(",");
			for (String id : toUnPass) {
				this.unpass(id, login, reason);
			}
			return true;
		}
		return false;
	}

	@Override
	public void setCheckStatusDefault() {
		List<ExamQuestion> examQuestions = examQuestionDao.list();
		if (CollectionUtils.isNotEmpty(examQuestions)) {
			for (ExamQuestion examQuestion : examQuestions) {
				if (null == examQuestion.getCheckStatus()) {
					examQuestion.setCheckStatus(CheckRecord.Status.FOR_CHECKED);
					examQuestionDao.update(examQuestion);
				}
			}
		}
	}

	@Override
	public Integer getQuestionScore(String examPaperId, String questionId) {
		ExamPaperQuestion examPaperQuestion = this.examPaperQuestionDao.getByExamPaperAndQuestion(examPaperId,
		        questionId);
		return null != examPaperQuestion ? examPaperQuestion.getScore() : 0;
	}

	@Override
	public List<ExamQuestion> getTrialQuestions(Integer questionType) {
		List<ExamQuestion> examQuestions = examQuestionDao.getTrialQuestions(questionType);
		return examQuestions;
	}

}
