package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.nercl.music.cloud.dao.ExamPaperQuestionDao;
import com.nercl.music.cloud.entity.ExamPaper;
import com.nercl.music.cloud.entity.ExamPaperQuestion;
import com.nercl.music.cloud.entity.ExamPaperType;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.QuestionType;

@Service
@Transactional
public class ExamPaperQuestionServiceImpl implements ExamPaperQuestionService {

	@Autowired
	private ExamPaperQuestionDao examPaperQuestionDao;

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private QuestionService questionService;

	@Override
	public void setQuestionsToExamPaper(String examPaperId, List<String> qids, Integer score) {
		if (null == qids || qids.isEmpty()) {
			return;
		}
		qids.stream().filter(id -> !Strings.isNullOrEmpty(id)).forEach(id -> {
			ExamPaperQuestion examPaperQuestion = getByExamPaperAndQuestion(examPaperId, id);
			if (null == examPaperQuestion) {
				ExamPaperQuestion epq = new ExamPaperQuestion();
				epq.setExamPaperId(examPaperId);
				epq.setQuestionId(id);
				epq.setScore(null == score ? 0 : score); // 默认给0分
				this.examPaperQuestionDao.save(epq);
			}
		});
		ExamPaper examPaper = examPaperService.get(examPaperId);
		if (null != examPaper) {
			Integer questionNum = examPaper.getQuestionNum();
			questionNum = null == questionNum || questionNum < 0 ? 0 : questionNum;
			examPaper.setQuestionNum(questionNum + qids.size());
			examPaperService.update(examPaper);
		}
	}

	@Override
	public boolean removeQuestion(String examPaperId, String examQuestionId) {
		ExamPaperQuestion examPaperQuestion = this.getByExamPaperAndQuestion(examPaperId, examQuestionId);
		if (null != examPaperQuestion) {
			this.examPaperQuestionDao.delete(examPaperQuestion);
			ExamPaper examPaper = examPaperService.get(examPaperId);
			if (null != examPaper) {
				Integer questionNum = examPaper.getQuestionNum();
				questionNum = null == questionNum || questionNum <= 1 ? 0 : questionNum - 1;
				examPaper.setQuestionNum(questionNum);

				Integer score = null == examPaper.getScore() ? 0 : examPaper.getScore();
				Integer tscore = null == examPaperQuestion.getScore() ? 0 : examPaperQuestion.getScore();
				examPaper.setScore(score - tscore <= 0 ? 0 : score - tscore);
				examPaperService.update(examPaper);
			}
		}
		return true;
	}

	@Override
	public boolean removeQuestions(String examPaperId, QuestionType questionType) {
		List<ExamPaperQuestion> epqs = this.getByExamPaper(examPaperId);
		if (null == epqs || epqs.isEmpty()) {
			return true;
		}
		epqs.stream().filter(epq -> questionType == epq.getQuestion().getQuestionType())
				.forEach(epq -> examPaperQuestionDao.delete(epq));
		ExamPaper examPaper = examPaperService.get(examPaperId);
		if (null != examPaper) {
			examPaperService.update(examPaper);
		}
		return true;
	}

	@Override
	public ExamPaperQuestion getByExamPaperAndQuestion(String examPaperId, String questionId) {
		return this.examPaperQuestionDao.getByExamPaperAndQuestion(examPaperId, questionId);
	}

	@Override
	public List<ExamPaperQuestion> getByExamPaper(String examPaperId) {
		return this.examPaperQuestionDao.getByExamPaper(examPaperId);
	}

	@Override
	public boolean setScore(String examPaperId, String questionId, Integer score) {
		ExamPaper examPaper = this.examPaperService.get(examPaperId);
		ExamPaperQuestion examPaperQuestion = this.getByExamPaperAndQuestion(examPaperId, questionId);
		if (null != examPaper && null != examPaperQuestion) {
			examPaperQuestion.setScore(score);
			examPaperQuestionDao.update(examPaperQuestion);
			examPaperService.update(examPaper);
			return true;
		}
		return false;
	}

	@Override
	public Map<String, Integer> getScore(String examPaperId) {
		List<ExamPaperQuestion> examPaperQuestions = examPaperQuestionDao.getByExamPaper(examPaperId);
		if (null != examPaperQuestions && !examPaperQuestions.isEmpty()) {
			Map<String, Integer> ret = Maps.newHashMap();
			examPaperQuestions.forEach(question -> {
				// ret.put(question.getExamQuestionId(), question.getScore());
			});
			return ret;
		}
		return null;
	}

	@Override
	public String auto(String examId, String grade, String title, Integer score, Integer resolvedTime,
			Integer singleNum, Integer singleScore, Integer multiNum, Integer multiScore, Integer shortNum,
			Integer shortScore, Float diffculty, Map<String, Integer> knowledges, List<QuestionType> types) {
		ExamPaper examPaper = new ExamPaper();
		examPaper.setTitle(title);
		examPaper.setScore(score);
		examPaper.setResolvedTime(resolvedTime);
		examPaper.setExamPaperType(ExamPaperType.MUSIC_ABILITY);
		singleNum = null == singleNum || singleNum < 0 ? 0 : singleNum;
		multiNum = null == multiNum || multiNum < 0 ? 0 : multiNum;
		shortNum = null == shortNum || shortNum < 0 ? 0 : shortNum;
		examPaper.setQuestionNum(singleNum + multiNum + shortNum);
		examPaper.setExamId(examId);
		examPaperService.save(examPaper);
		if (knowledges.isEmpty()) {
			if (singleNum > 0) {
				// 根据条件查询试卷问题Id
				List<Question> questions = questionService.random(QuestionType.SINGLE_SELECT, grade, singleNum, null);
				List<String> ids = null;
				if (null != questions && !questions.isEmpty()) {
					ids = questions.stream().map(question -> {
						return question.getId();
					}).collect(Collectors.toList());
				}
				setQuestionsToExamPaper(examPaper.getId(), ids, singleScore);
			}
			if (multiNum > 0) {
				List<Question> questions = questionService.random(QuestionType.MULTI_SELECT, grade, multiNum, null);
				List<String> ids = null;
				if (null != questions && !questions.isEmpty()) {
					ids = questions.stream().map(question -> {
						return question.getId();
					}).collect(Collectors.toList());
				}
				setQuestionsToExamPaper(examPaper.getId(), ids, multiScore);
			}
			if (shortNum > 0) {
				List<Question> questions = questionService.random(QuestionType.SHORT_ANSWER, grade, shortNum, null);
				List<String> ids = null;
				if (null != questions && !questions.isEmpty()) {
					ids = questions.stream().map(question -> {
						return question.getId();
					}).collect(Collectors.toList());
				}
				setQuestionsToExamPaper(examPaper.getId(), ids, shortScore);
			}
		} else {
			Set<Question> questions = Sets.newHashSet();
			knowledges.forEach((knowledge, count) -> {
				List<Question> qs = questionService.random(types, grade, 1, knowledge);
				questions.add(qs.get(0));
			});
			Map<QuestionType, List<Question>> group = questions.parallelStream()
					.collect(Collectors.groupingBy(question -> question.getQuestionType()));
			for (Map.Entry<QuestionType, List<Question>> entry : group.entrySet()) {
				List<String> qids = entry.getValue().stream().map(q -> q.getId()).collect(Collectors.toList());
				if (QuestionType.SINGLE_SELECT == entry.getKey()) {
					if (qids.size() >= singleNum) {
						setQuestionsToExamPaper(examPaper.getId(), qids, singleScore);
					} else {
						List<Question> qqs = questionService.random(QuestionType.SINGLE_SELECT, singleNum - qids.size(),
								qids, grade);
						List<String> ids = null;
						if (null != qqs && !qqs.isEmpty()) {
							ids = qqs.stream().map(question -> {
								return question.getId();
							}).collect(Collectors.toList());
							ids.addAll(qids);
						}
						setQuestionsToExamPaper(examPaper.getId(), ids, singleScore);
					}
				} else if (QuestionType.MULTI_SELECT == entry.getKey()) {
					if (qids.size() >= multiNum) {
						setQuestionsToExamPaper(examPaper.getId(), qids, multiScore);
					} else {
						List<Question> qqs = questionService.random(QuestionType.MULTI_SELECT, multiNum - qids.size(),
								qids, grade);
						List<String> ids = null;
						if (null != qqs && !qqs.isEmpty()) {
							ids = qqs.stream().map(question -> {
								return question.getId();
							}).collect(Collectors.toList());
							ids.addAll(qids);
						}
						setQuestionsToExamPaper(examPaper.getId(), ids, multiScore);
					}
				} else if (QuestionType.SHORT_ANSWER == entry.getKey()) {
					if (qids.size() >= shortNum) {
						setQuestionsToExamPaper(examPaper.getId(), qids, shortScore);
					} else {
						List<Question> qqs = questionService.random(QuestionType.SHORT_ANSWER, shortNum - qids.size(),
								qids, grade);
						List<String> ids = null;
						if (null != qqs && !qqs.isEmpty()) {
							ids = qqs.stream().map(question -> {
								return question.getId();
							}).collect(Collectors.toList());
							ids.addAll(qids);
						}
						setQuestionsToExamPaper(examPaper.getId(), ids, shortScore);
					}
				}
			}
		}
		return examPaper.getId();
	}

	@Override
	public String auto(String examId, String grade, String title, Integer score, Integer resolvedTime, Integer singNum,
			Integer singScore, Integer behindBackSingNum, Integer behindBackSingScore, Integer behindBackPerformanceNum,
			Integer behindBackPerformanceScore, Integer performanceNum, Integer performanceScore,
			Integer sightSingingNum, Integer sightSingingScore, Float diffculty, Map<String, Integer> knowledges,
			List<QuestionType> types) {
		ExamPaper examPaper = new ExamPaper();
		examPaper.setTitle(title);
		examPaper.setScore(score);
		examPaper.setResolvedTime(resolvedTime);
		examPaper.setExamPaperType(ExamPaperType.ACT);
		singNum = null == singNum || singNum < 0 ? 0 : singNum;
		behindBackSingNum = null == behindBackSingNum || behindBackSingNum < 0 ? 0 : behindBackSingNum;
		behindBackPerformanceNum = null == behindBackPerformanceNum || behindBackPerformanceNum < 0 ? 0
				: behindBackPerformanceNum;
		performanceNum = null == performanceNum || performanceNum < 0 ? 0 : performanceNum;
		sightSingingNum = null == sightSingingNum || sightSingingNum < 0 ? 0 : sightSingingNum;
		examPaper.setQuestionNum(
				singNum + behindBackSingNum + behindBackPerformanceNum + performanceNum + sightSingingNum);
		examPaper.setExamId(examId);
		examPaperService.save(examPaper);
		if (knowledges.isEmpty()) {
			if (singNum > 0) {
				List<Question> questions = questionService.random(QuestionType.SING, grade, singNum, null);
				List<String> ids = null;
				if (null != questions && !questions.isEmpty()) {
					ids = questions.stream().map(question -> {
						return question.getId();
					}).collect(Collectors.toList());
				}
				setQuestionsToExamPaper(examPaper.getId(), ids, singScore);
			}
			if (behindBackSingNum > 0) {
				List<Question> questions = questionService.random(QuestionType.BEHIND_BACK_SING, grade,
						behindBackSingNum, null);
				List<String> ids = null;
				if (null != questions && !questions.isEmpty()) {
					ids = questions.stream().map(question -> {
						return question.getId();
					}).collect(Collectors.toList());
				}
				setQuestionsToExamPaper(examPaper.getId(), ids, behindBackSingScore);
			}
			if (behindBackPerformanceNum > 0) {
				List<Question> questions = questionService.random(QuestionType.BEHIND_BACK_PERFORMANCE, grade,
						behindBackPerformanceNum, null);
				List<String> ids = null;
				if (null != questions && !questions.isEmpty()) {
					ids = questions.stream().map(question -> {
						return question.getId();
					}).collect(Collectors.toList());
				}
				setQuestionsToExamPaper(examPaper.getId(), ids, behindBackPerformanceScore);
			}
			if (performanceNum > 0) {
				List<Question> questions = questionService.random(QuestionType.PERFORMANCE, grade, performanceNum,
						null);
				List<String> ids = null;
				if (null != questions && !questions.isEmpty()) {
					ids = questions.stream().map(question -> {
						return question.getId();
					}).collect(Collectors.toList());
				}
				setQuestionsToExamPaper(examPaper.getId(), ids, performanceScore);
			}
			if (sightSingingNum > 0) {
				List<Question> questions = questionService.random(QuestionType.SIGHT_SINGING, grade, sightSingingNum,
						null);
				List<String> ids = null;
				if (null != questions && !questions.isEmpty()) {
					ids = questions.stream().map(question -> {
						return question.getId();
					}).collect(Collectors.toList());
				}
				setQuestionsToExamPaper(examPaper.getId(), ids, sightSingingScore);
			}
		} else {
			Set<Question> questions = Sets.newHashSet();
			knowledges.forEach((knowledge, count) -> {
				List<Question> qs = questionService.random(types, grade, 1, knowledge);
				questions.add(qs.get(0));
			});
			Map<QuestionType, List<Question>> group = questions.parallelStream()
					.collect(Collectors.groupingBy(question -> question.getQuestionType()));
			for (Map.Entry<QuestionType, List<Question>> entry : group.entrySet()) {
				List<String> qids = entry.getValue().stream().map(q -> q.getId()).collect(Collectors.toList());
				if (QuestionType.SING == entry.getKey()) {
					if (qids.size() >= singNum) {
						setQuestionsToExamPaper(examPaper.getId(), qids, singScore);
					} else {
						List<Question> qqs = questionService.random(QuestionType.SING, singNum - qids.size(), qids,
								grade);
						List<String> ids = null;
						if (null != qqs && !qqs.isEmpty()) {
							ids = qqs.stream().map(question -> {
								return question.getId();
							}).collect(Collectors.toList());
							ids.addAll(qids);
						}
						setQuestionsToExamPaper(examPaper.getId(), ids, singScore);
					}
				} else if (QuestionType.BEHIND_BACK_SING == entry.getKey()) {
					if (qids.size() >= behindBackSingNum) {
						setQuestionsToExamPaper(examPaper.getId(), qids, behindBackSingScore);
					} else {
						List<Question> qqs = questionService.random(QuestionType.BEHIND_BACK_SING,
								behindBackSingNum - qids.size(), qids, grade);
						List<String> ids = null;
						if (null != qqs && !qqs.isEmpty()) {
							ids = qqs.stream().map(question -> {
								return question.getId();
							}).collect(Collectors.toList());
							ids.addAll(qids);
						}
						setQuestionsToExamPaper(examPaper.getId(), ids, behindBackSingScore);
					}
				} else if (QuestionType.BEHIND_BACK_PERFORMANCE == entry.getKey()) {
					if (qids.size() >= behindBackPerformanceNum) {
						setQuestionsToExamPaper(examPaper.getId(), qids, behindBackPerformanceScore);
					} else {
						List<Question> qqs = questionService.random(QuestionType.BEHIND_BACK_PERFORMANCE,
								behindBackPerformanceNum - qids.size(), qids, grade);
						List<String> ids = null;
						if (null != qqs && !qqs.isEmpty()) {
							ids = qqs.stream().map(question -> {
								return question.getId();
							}).collect(Collectors.toList());
							ids.addAll(qids);
						}
						setQuestionsToExamPaper(examPaper.getId(), ids, behindBackPerformanceScore);
					}
				} else if (QuestionType.PERFORMANCE == entry.getKey()) {
					if (qids.size() >= performanceNum) {
						setQuestionsToExamPaper(examPaper.getId(), qids, performanceScore);
					} else {
						List<Question> qqs = questionService.random(QuestionType.PERFORMANCE,
								performanceNum - qids.size(), qids, grade);
						List<String> ids = null;
						if (null != qqs && !qqs.isEmpty()) {
							ids = qqs.stream().map(question -> {
								return question.getId();
							}).collect(Collectors.toList());
							ids.addAll(qids);
						}
						setQuestionsToExamPaper(examPaper.getId(), ids, performanceScore);
					}
				} else if (QuestionType.SIGHT_SINGING == entry.getKey()) {
					if (qids.size() >= sightSingingNum) {
						setQuestionsToExamPaper(examPaper.getId(), qids, sightSingingScore);
					} else {
						List<Question> qqs = questionService.random(QuestionType.SIGHT_SINGING,
								sightSingingNum - qids.size(), qids, grade);
						List<String> ids = null;
						if (null != qqs && !qqs.isEmpty()) {
							ids = qqs.stream().map(question -> {
								return question.getId();
							}).collect(Collectors.toList());
							ids.addAll(qids);
						}
						setQuestionsToExamPaper(examPaper.getId(), ids, sightSingingScore);
					}
				}
			}
		}
		return examPaper.getId();
	}

	@Override
	public Integer getScore(String examPaperId, String examQuestionId) {
		ExamPaperQuestion examPaperQuestion = this.getByExamPaperAndQuestion(examPaperId, examQuestionId);
		return null == examPaperQuestion ? 0 : examPaperQuestion.getScore();
	}

	@Override
	public List<Question> getQuestionByExamPaper(String pid) {
		List<ExamPaperQuestion> epqs = examPaperQuestionDao.getByExamPaper(pid);
		return null == epqs || epqs.isEmpty() ? null
				: epqs.stream().map(ExamPaperQuestion::getQuestion).collect(Collectors.toList());
	}

	@Override
	public void deleteByExamPaper(String examPaperId) {
		List<ExamPaperQuestion> epqs = getByExamPaper(examPaperId);
		if (null == epqs || epqs.isEmpty()) {
			return;
		}
		epqs.forEach(epq -> examPaperQuestionDao.deleteById(epq.getId()));
		ExamPaper examPaper = examPaperService.get(examPaperId);
		if (null != examPaper) {
			examPaperService.update(examPaper);
		}
	}
}
