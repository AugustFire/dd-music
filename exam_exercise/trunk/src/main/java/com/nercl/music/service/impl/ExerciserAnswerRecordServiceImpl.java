package com.nercl.music.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.constant.CList.Api.ExamField;
import com.nercl.music.constant.CList.Api.QuestionType;
import com.nercl.music.dao.ExerciserAnswerRecordDao;
import com.nercl.music.entity.authorize.Topic;
import com.nercl.music.entity.question.Answer;
import com.nercl.music.entity.question.ExamQuestion;
import com.nercl.music.entity.question.ExerciserAnswerRecord;
import com.nercl.music.entity.question.MFile;
import com.nercl.music.entity.question.Option;
import com.nercl.music.entity.user.Person;
import com.nercl.music.service.AnswerService;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.ExerciserAnswerRecordService;
import com.nercl.music.service.MFileService;
import com.nercl.music.service.TopicService;
import com.nercl.music.util.DESCryption;
import com.nercl.music.util.MusicDataUtil;
import com.nercl.music.util.StaffUtil;
import com.nercl.music.util.page.PaginateSupportArray;
import com.nercl.music.util.page.PaginateSupportUtil;

@Service
@Transactional
public class ExerciserAnswerRecordServiceImpl implements ExerciserAnswerRecordService {

	@Autowired
	private ExerciserAnswerRecordDao exerciserAnswerRecordDao;

	@Autowired
	private ExamQuestionService questionService;

	@Autowired
	private AnswerService answerService;

	@Autowired
	private MFileService mfileService;

	@Autowired
	private TopicService topicService;

	@Autowired
	private DESCryption desCryption;

	@Autowired
	private StaffUtil staffUtil;

	@Autowired
	private MusicDataUtil musicDataUtil;

	private static final Integer RIGHT = 100;

	private static final Integer WRONG = 0;

	@Override
	public ExerciserAnswerRecord get(String id) {
		return this.exerciserAnswerRecordDao.findByID(id);
	}

	@Override
	public List<ExerciserAnswerRecord> get(String topicId, String exerciserId, String examQuestionId) {
		return this.exerciserAnswerRecordDao.get(exerciserId, examQuestionId);
	}

	@Override
	public ExerciserAnswerRecord save(String topicId, String personId, String examQuestionId, String content,
	        String resPath) {
		ExerciserAnswerRecord record = new ExerciserAnswerRecord();
		ExamQuestion question = this.questionService.get(examQuestionId);
		if (null == record || null == question) {
			return null;
		}
		record.setTopicId(topicId);
		record.setPersonId(personId);
		record.setExamQuestionId(examQuestionId);
		record.setCreatAt(System.currentTimeMillis());
		Integer questionType = question.getQuestionType();
		record.setQuestionType(questionType);
		record.setContent(content);
		record.setResPath(resPath);
		this.exerciserAnswerRecordDao.save(record);
		return record;
	}

	@Override
	public ExerciserAnswerRecord save(String topicId, String personId, String examQuestionId, String content,
	        String resPath, int accuracy) {
		ExerciserAnswerRecord record = new ExerciserAnswerRecord();
		record.setTopicId(topicId);
		record.setPersonId(personId);
		record.setExamQuestionId(examQuestionId);
		record.setCreatAt(System.currentTimeMillis());
		Integer questionType = this.questionService.get(examQuestionId).getQuestionType();
		record.setQuestionType(questionType);
		record.setContent(content);
		record.setResPath(resPath);
		record.setAccuracy(accuracy);
		this.exerciserAnswerRecordDao.save(record);
		return record;
	}

	@Override
	public List<Map<String, Object>> list(String personId, String name, String topicId, Integer questionType,
	        int page) {
		List<ExerciserAnswerRecord> answerRecords = new ArrayList<>();
		if (StringUtils.isNotBlank(personId)) {
			answerRecords = this.exerciserAnswerRecordDao.listByPerson(personId, topicId, questionType, page);
		} else {
			answerRecords = this.exerciserAnswerRecordDao.list(name, topicId, questionType, page);
		}

		Topic topic = topicService.get(topicId);
		List<Map<String, Object>> list = Lists.newArrayList();
		for (ExerciserAnswerRecord record : answerRecords) {
			Map<String, Object> ret = Maps.newHashMap();
			list.add(ret);
			ExamQuestion question = record.getExamQuestion();
			if (null != question) {
				Person person = record.getPerson();
				ret.put("record_id", record.getId());
				ret.put("createAt", record.getCreatAt());
				ret.put("name", person.getName());
				ret.put("person_id", person.getId());
				ret.put("title", this.desCryption.decode(question.getTitle()));
				ret.put("question_id", question.getId());
				ret.put("topic_title", topic.getTitle());
				if ((QuestionType.LOOK_SING.equals(record.getQuestionType()))) {
					ret.put("lookSingAudio", record.getResPath());
				}
				if (QuestionType.SINGLE_SELECT.equals(questionType) || QuestionType.MULTI_SELECT.equals(questionType)) {
					List<String> values = Splitter.on(",").splitToList(Strings.nullToEmpty(record.getContent()));
					List<String> exerciserOptions = Lists.newArrayList();
					List<String> exerciserOptionsPath = Lists.newArrayList();
					ret.put("exerciser_options", exerciserOptions);
					ret.put("exerciser_options_path", exerciserOptionsPath);
					List<String> standardOptions = Lists.newArrayList();
					List<String> standardOptionsPath = Lists.newArrayList();
					ret.put("standard_options", standardOptions);
					ret.put("standard_options_path", standardOptionsPath);
					List<Option> options = question.getOptions();
					for (Option option : options) {
						if (StringUtils.isNotBlank(option.getContent())) {
							ret.put("is_option_content", true);
							String decodeOption = desCryption.decode(option.getContent());
							if (option.isTrue()) {
								standardOptions.add(decodeOption);
							}
							for (String v : values) {
								if (v.equals(option.getValue())) {
									exerciserOptions.add(decodeOption);
								}
							}
						} else if (StringUtils.isNotBlank(option.getXmlPath())) {
							ret.put("is_option_path", true);
							String xmlPath = this.desCryption.decode(option.getXmlPath());
							MFile mfile = this.mfileService.getByUrl(xmlPath);
							if (null != mfile) {
								if (option.isTrue()) {
									standardOptionsPath.add(mfile.getId());
								}
								for (String v : values) {
									if (v.equals(option.getValue())) {
										exerciserOptionsPath.add(mfile.getId());
									}
								}
							}
						}
					}
				} else if (QuestionType.SHORT_ANSWER.equals(questionType)) {
					Answer answer = this.answerService.getByQuestion(question.getId());
					if (null != answer) {
						if (StringUtils.isNotBlank(answer.getContent())) {
							ret.put("is_shortanswer_content", true);
							ret.put("standard_answer", this.desCryption.decode(answer.getContent()));
							ret.put("exerciser_answer", record.getContent());
						} else {
							ret.put("is_shortanswer_path", true);
							MFile mfile = this.mfileService.getByUrl(answer.getXmlPath());
							if (null != mfile) {
								ret.put("standard_path", mfile.getId());
							}
							mfile = this.mfileService.getByUrl(record.getResPath());
							if (null != mfile) {
								ret.put("exerciser_path", mfile.getId());
							}
						}
					}
				}
				this.setScore(record);
				ret.put("score", record.getScore());
			}
		}
		PaginateSupportArray<?> psa;
		if (answerRecords instanceof PaginateSupportArray) {
			psa = (PaginateSupportArray<?>) answerRecords;
			return PaginateSupportUtil.pagingList(list, page, psa.getPageSize(), psa.getTotal());
		}
		return list;
	}

	private void setScore(ExerciserAnswerRecord record) {
		// 已评分，则返回
		if (null != record.getScore() && record.getScore() >= 0) {
			return;
		}
		ExamQuestion question = record.getExamQuestion();
		if (null == question) {
			return;
		}

		Answer answer = this.answerService.getByQuestion(question.getId());
		Integer questionType = question.getQuestionType();
		if (null == answer && QuestionType.SHORT_ANSWER.equals(questionType)) {
			return;
		}
		if (null != answer && QuestionType.SHORT_ANSWER.equals(questionType)) {
			if (ExamField.MELODY_WRITING.equals(question.getExamField())) {
				Integer accuracy = this.getAccuracy(answer.getContent(), record.getContent());
				accuracy = accuracy.equals(100) ? accuracy : 0;
				record.setAccuracy(accuracy);
			} else if (ExamField.INTERVAL_NATURE.equals(question.getExamField())
			        || ExamField.CHORD_NATURE.equals(question.getExamField())) {
				Integer accuracy = this.getAccuracy(answer.getContent(), record.getContent());
				record.setAccuracy(accuracy);
			} else {
				String exerciserAnswerPath = this.mfileService.getPath(record.getResPath());
				String answerPath = this.mfileService.getPath(answer.getXmlPath());
				Integer accuracy = this.staffUtil.getShortAnswerScore(exerciserAnswerPath, answerPath,
				        question.getExamField());
				record.setAccuracy(accuracy);
			}
		} else if (QuestionType.SINGLE_SELECT.equals(questionType) || QuestionType.MULTI_SELECT.equals(questionType)) {
			String answerContent = record.getContent();
			if (StringUtils.isBlank(answerContent)) {
				record.setAccuracy(WRONG);
				return;
			}
			List<Option> options = question.getOptions();
			if (null == options || options.isEmpty()) {
				record.setAccuracy(WRONG);
				return;
			}
			String[] answers = answerContent.split(",");
			boolean allRight = Arrays.stream(answers).allMatch(
			        ansr -> options.stream().anyMatch(option -> option.isTrue() && ansr.equals(option.getValue())));
			record.setAccuracy(allRight ? RIGHT : WRONG);
		} else if (QuestionType.LOOK_SING.equals(questionType)) {
			String audioPath = this.mfileService.getPath(record.getResPath());
			String standardAnswerPath = this.mfileService.getPath(question.getXmlPath());
			Integer accuracy = this.staffUtil.getLookSingData(record.getId(), standardAnswerPath, audioPath);
			System.out.println("----------------accuracy:" + accuracy);
			record.setAccuracy(accuracy);
		}

		Integer score = (null == question.getScore() || question.getScore() <= 0) ? record.getAccuracy()
		        : (int) ((double) record.getAccuracy() / 100 * question.getScore());
		record.setScore(score);
		this.exerciserAnswerRecordDao.update(record);
	}

	private Integer getAccuracy(String standarAnswer, String exerciserAnswer) {
		if (StringUtils.isBlank(standarAnswer) || StringUtils.isBlank(exerciserAnswer)) {
			return 0;
		}
		List<String> standardList = Splitter.on(",").splitToList(this.desCryption.decode(standarAnswer));
		List<String> exerciserList = Splitter.on(",").splitToList(exerciserAnswer);
		int count = 0;
		for (int i = 0; i < standardList.size(); i++) {
			if (standardList.get(i).equals(exerciserList.get(i))) {
				count++;
			}
		}
		return (int) ((double) count / (double) standardList.size() * 100);
	}

	@Override
	public String getLookSingData(String recordId) {
		ExerciserAnswerRecord answerRecord = this.exerciserAnswerRecordDao.findByID(recordId);
		String data = "";
		if (null != answerRecord) {
			ExamQuestion examQuestion = answerRecord.getExamQuestion();
			if (null != examQuestion) {
				String path = examQuestion.getXmlPath();
				String standarAnswerPath = this.mfileService.getPath(path);
				String audioPath = this.mfileService.getPath(answerRecord.getResPath());
				this.staffUtil.getLookSingData(answerRecord.getId(), standarAnswerPath, audioPath);
				data = this.musicDataUtil.getSourceAndVoiceData(answerRecord.getId());
			}
		}
		return data;

	}

	@Override
	public String getCurveData(String recordId) {
		ExerciserAnswerRecord answerRecord = this.exerciserAnswerRecordDao.findByID(recordId);
		String data = "";
		if (null != answerRecord) {
			ExamQuestion examQuestion = answerRecord.getExamQuestion();
			if (null != examQuestion) {
				String path = examQuestion.getXmlPath();
				String standarAnswerPath = this.mfileService.getPath(path);
				String audioPath = this.mfileService.getPath(answerRecord.getResPath());
				this.staffUtil.getLookSingData(answerRecord.getId(), standarAnswerPath, audioPath);
				data = this.musicDataUtil.getCurveData(answerRecord.getId());
			}
		}
		return data;
	}

	@Override
	public boolean hasAnswers(String personId, String topicId, String examQuestionId) {
		return exerciserAnswerRecordDao.hasAnswers(personId, topicId, examQuestionId);
	}

	@Override
	public void update(ExerciserAnswerRecord record) {
		exerciserAnswerRecordDao.update(record);
	}
}
