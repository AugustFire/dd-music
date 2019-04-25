package com.nercl.music.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.dao.*;
import com.nercl.music.entity.authorize.TopicQuestion;
import com.nercl.music.entity.question.*;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.ExerciserAnswerRecordService;
import com.nercl.music.service.MFileService;
import com.nercl.music.util.DESCryption;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ExamQuestionServiceImpl implements ExamQuestionService {

	@Autowired
	private ExamQuestionDao examQuestionDao;

	@Autowired
	private DESCryption desCryption;

	@Autowired
	private MFileService mfileService;

	@Autowired
	private TopicQuestionDao topicQuestionDao;

	@Autowired
	private AnswerDao answerDao;

	@Autowired
	private ExerciserAnswerRecordDao exerciserAnswerRecordDao;

	@Autowired
	private MachineResultDao machineResultDao;

	@Autowired
	private OptionDao optionDao;

	@Autowired
	private ExerciserAnswerRecordService exerciserAnswerRecordService;

	private static final List<String> ORDER = Lists.newArrayList("A. ", "B. ", "C. ", "D. ", "E. ", "F. ", "G. ", "H. ",
	        "I. ", "J. ");

	@Override
	public void save(ExamQuestion examQuestion) {
		examQuestion.setCommitedTime(System.currentTimeMillis());
		examQuestionDao.save(examQuestion);
	}

	@Override
	public ExamQuestion get(String id) {
		return examQuestionDao.findByID(id);
	}

	@Override
	public List<Map<String, Object>> getBySubjectType(Integer subjectType, int page) {
		List<ExamQuestion> examQuestions = examQuestionDao.getBySubjectType(subjectType, page);
		if (null == examQuestions) {
			return null;
		}
		List<Map<String, Object>> list = Lists.newArrayList();
		examQuestions.forEach(examQuestion -> {
			Map<String, Object> map = Maps.newHashMap();
			list.add(map);
			map.put("id", examQuestion.getId());
			String title = this.desCryption.decode(examQuestion.getTitle());
			map.put("title", title);

			if (examQuestion.getHasStaff()) {
				String xmlPath = examQuestion.getXmlPath();
				MFile mfile = this.mfileService.getByUrl(xmlPath);
				if (null != mfile) {
					map.put("xml_path", mfile.getId());
				}
			}
		});
		return list;
	}

	@Override
	public List<ExamQuestion> getQuestionsBySubjectType(Integer subjectType, int page) {
		List<ExamQuestion> questions = examQuestionDao.getBySubjectType(subjectType, page);
		questions.forEach(question -> {
			try {
				question.setTitle2(this.desCryption.decode(question.getTitle()));
			} catch (Exception e) {
				question.setTitle2(question.getTitle());
			}
			if (CList.Api.QuestionType.SINGLE_SELECT.equals(question.getQuestionType())
		            || CList.Api.QuestionType.MULTI_SELECT.equals(question.getQuestionType())) {
				List<Option> options = question.getOptions();
				for (Option option : options) {
					if (StringUtils.isNotBlank(option.getContent())) {
						try {
							option.setContent2(desCryption.decode(option.getContent()));
						} catch (Exception e) {
							option.setContent2(option.getContent());
						}
					} else if (StringUtils.isNotBlank(option.getXmlPath())) {
						String xmlPath = "";
						try {
							xmlPath = this.desCryption.decode(option.getXmlPath());
						} catch (Exception e) {
							xmlPath = option.getXmlPath();
						}
						String[] strs = xmlPath.split("/");
						String uuid = strs[strs.length - 1];
						MFile mfile = this.mfileService.get(uuid);
						if (null != mfile) {
							option.setXmlPath2(mfile.getId());
						}
					}
				}
				String xmlPath = question.getXmlPath();
				if(!Strings.isNullOrEmpty(xmlPath)){
					String[] strs = xmlPath.split("/");
					String uuid = strs[strs.length - 1];
					MFile mfile = this.mfileService.get(uuid);
					if (null != mfile) {
						question.setXmlPath2(mfile.getId());
					}
				}
			} else {
				String xmlPath = question.getXmlPath();
				if(!Strings.isNullOrEmpty(xmlPath)){
					String[] strs = xmlPath.split("/");
					String uuid = strs[strs.length - 1];
					MFile mfile = this.mfileService.get(uuid);
					if (null != mfile) {
						question.setXmlPath2(mfile.getId());
					}
				}
			}
			Answer answer = question.getAnswer();
			if (null != answer) {
				if (StringUtils.isNotBlank(answer.getContent())) {
					try {
						answer.setContent2(this.desCryption.decode(answer.getContent()));
					} catch (Exception e) {
						answer.setContent2(answer.getContent());
					}
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
	public List<Map<String, Object>> getLookSingQuestions(int year) {
		List<ExamQuestion> examQuestions = examQuestionDao.getLookSingQuestions(year);
		if (null == examQuestions) {
			return null;
		}
		List<Map<String, Object>> list = Lists.newArrayList();
		examQuestions.forEach(examQuestion -> {
			Map<String, Object> map = Maps.newHashMap();
			list.add(map);
			map.put("id", examQuestion.getId());
			String title = this.desCryption.decode(examQuestion.getTitle());
			map.put("title", title);
		});
		return list;
	}

	@Override
	public List<Map<String, Object>> getBytopic(String topicId, String exerciserId) {
		List<ExamQuestion> examQuestions = examQuestionDao.getBytopic(topicId);
		if (null == examQuestions) {
			return null;
		}
		List<Map<String, Object>> list = Lists.newArrayList();
		examQuestions.forEach(examQuestion -> {
			Map<String, Object> map = Maps.newHashMap();
			list.add(map);
			map.put("id", examQuestion.getId());
			try {
				map.put("title", this.desCryption.decode(examQuestion.getTitle()));
			} catch (Exception e) {
				map.put("title", examQuestion.getTitle());
			}
			map.put("exam_field", null == examQuestion.getExamField() ? 0 : examQuestion.getExamField());
			map.put("question_type", examQuestion.getQuestionType());
			map.put("subject_type", examQuestion.getSubjectType());
			boolean hasAnswers = exerciserAnswerRecordService.hasAnswers(exerciserId, topicId, examQuestion.getId());
			map.put("has_answers", hasAnswers);
		});
		return list;
	}

	@Override
	public List<Map<String, Object>> listLookSingQuestions() {
		List<ExamQuestion> examQuestions = examQuestionDao.listLookSingQuestions();
		if (null == examQuestions) {
			return null;
		}
		List<Map<String, Object>> list = Lists.newArrayList();
		examQuestions.forEach(examQuestion -> {
			Map<String, Object> map = Maps.newHashMap();
			list.add(map);
			map.put("id", examQuestion.getId());
			map.put("title", this.desCryption.decode(examQuestion.getTitle()));

			String xmlPath = examQuestion.getXmlPath();
			MFile mfile = this.mfileService.getByUrl(xmlPath);
			if (null != mfile) {
				map.put("xml_path", mfile.getId());
			}
		});
		return list;
	}

	@Override
	public List<ExamQuestion> list(Integer type, String title, Float difficulty, int page) {
		List<ExamQuestion> questions = this.examQuestionDao.list(type, title, difficulty, page);
		questions.forEach(question -> {
			try {
				question.setTitle2(this.desCryption.decode(question.getTitle()));
			} catch (Exception e) {
				question.setTitle2(question.getTitle());
			}
			if (CList.Api.QuestionType.SINGLE_SELECT.equals(question.getQuestionType())
		            || CList.Api.QuestionType.MULTI_SELECT.equals(question.getQuestionType())) {
				List<Option> options = question.getOptions();
				int i = 0;
				for (Option option : options) {
					if (StringUtils.isNotBlank(option.getContent())) {
						try {
							option.setContent2(ORDER.get(i++) + desCryption.decode(option.getContent()));
						} catch (Exception e) {
							option.setContent2(ORDER.get(i++) + option.getContent());
						}
					} else if (StringUtils.isNotBlank(option.getXmlPath())) {
						String xmlPath = "";
						try {
							xmlPath = this.desCryption.decode(option.getXmlPath());
						} catch (Exception e) {
							xmlPath = option.getXmlPath();
						}
						String[] strs = xmlPath.split("/");
						String uuid = strs[strs.length - 1];
						MFile mfile = this.mfileService.get(uuid);
						if (null != mfile) {
							option.setXmlPath2(mfile.getId());
						}
					}
				}
				String xmlPath = question.getXmlPath();
				if(!Strings.isNullOrEmpty(xmlPath)){
					String[] strs = xmlPath.split("/");
					String uuid = strs[strs.length - 1];
					MFile mfile = this.mfileService.get(uuid);
					if (null != mfile) {
						question.setXmlPath2(mfile.getId());
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
					try {
						answer.setContent2(this.desCryption.decode(answer.getContent()));
					} catch (Exception e) {
						answer.setContent2(answer.getContent());
					}
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
	public boolean delete(String[] ids) {
		Arrays.stream(ids).forEach(id -> {
			// 先删除关联数据
			Answer answer = answerDao.getByQuestion(id);
			answerDao.delete(answer);

			List<Option> options = optionDao.list(id);
			options.forEach(option -> optionDao.delete(option));

			List<TopicQuestion> topicQuestions = topicQuestionDao.list(id);
			topicQuestions.forEach(topicQuestion -> topicQuestionDao.delete(topicQuestion));

			List<ExerciserAnswerRecord> records = exerciserAnswerRecordDao.get(id);
			records.forEach(record -> exerciserAnswerRecordDao.delete(record));

			List<MachineResult> machineResults = machineResultDao.list(id);
			machineResults.forEach(machineResult -> machineResultDao.delete(machineResult));

			examQuestionDao.deleteById(id);
		});
		return true;
	}

	@Override
	public void update(ExamQuestion examQuestion) {
		examQuestionDao.update(examQuestion);
	}
}
