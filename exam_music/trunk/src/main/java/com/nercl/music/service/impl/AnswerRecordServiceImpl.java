package com.nercl.music.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.dao.AnswerRecordDao;
import com.nercl.music.entity.Answer;
import com.nercl.music.entity.AnswerRecord;
import com.nercl.music.entity.ExamPaperQuestion;
import com.nercl.music.entity.ExamQuestion;
import com.nercl.music.entity.MFile;
import com.nercl.music.entity.Option;
import com.nercl.music.entity.user.Examinee;
import com.nercl.music.entity.user.ExamineeGroup;
import com.nercl.music.service.AnswerRecordService;
import com.nercl.music.service.AnswerService;
import com.nercl.music.service.ExamPaperQuestionService;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.ExamineeService;
import com.nercl.music.service.GroupService;
import com.nercl.music.service.MFileService;
import com.nercl.music.service.ResultService;
import com.nercl.music.util.DESCryption;
import com.nercl.music.util.MusicDataUtil;
import com.nercl.music.util.StaffUtil;
import com.nercl.music.util.page.PaginateSupportArray;
import com.nercl.music.util.page.PaginateSupportUtil;

@Service
@Transactional
public class AnswerRecordServiceImpl implements AnswerRecordService {

	@Autowired
	private AnswerRecordDao answerRecordDao;

	@Autowired
	private ExamQuestionService questionService;

	@Autowired
	private AnswerService answerService;

	@Autowired
	private StaffUtil staffUtil;

	@Autowired
	private MFileService mfileService;

	@Autowired
	private ResultService resultService;

	@Autowired
	private ExamPaperQuestionService examPaperQuestionService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private ExamineeService examineeService;

	@Autowired
	private DESCryption desCryption;

	@Autowired
	private MusicDataUtil musicDataUtil;

	@Value("${exam_music.looksing.data.table}")
	private String looksingDataTablePath;

	@Value("${exam_music.looksing.data.curve}")
	private String looksingDataCurvePath;

	private static final Integer RIGHT = 100;

	private static final Integer WRONG = 0;

	@Override
	public AnswerRecord get(String id) {
		return this.answerRecordDao.findByID(id);
	}

	@Override
	public AnswerRecord get(String examId, String examPaperId, String examineeId, String examQuestionId) {
		return this.answerRecordDao.get(examId, examPaperId, examineeId, examQuestionId);
	}

	@Override
	public List<AnswerRecord> list(String examineeId, int page) {
		return this.answerRecordDao.list(examineeId, page);
	}

	@Override
	public boolean save(String examId, String examPaperId, String examineeId, String examQuestionId, String content,
	        String resPath) {
		AnswerRecord record = this.get(examId, examPaperId, examineeId, examQuestionId);
		if (null != record) {
			return false;
		}
		record = new AnswerRecord();
		record.setCreatAt(System.currentTimeMillis());
		record.setExamId(examId);
		record.setExamPaperId(examPaperId);
		record.setExamineeId(examineeId);
		record.setExamQuestionId(examQuestionId);
		Integer questionType = this.questionService.get(examQuestionId).getQuestionType();
		record.setQuestionType(questionType);
		record.setContent(content);
		record.setResPath(resPath);
		this.answerRecordDao.save(record);
		return true;
	}

	@Override
	public List<AnswerRecord> list(String examId, String examPaperId, Integer questionType) {
		return this.answerRecordDao.list(examId, examPaperId, questionType);
	}

	@Override
	public List<Map<String, Object>> list(String name, String examNo, String examId, String examPaperId,
	        Integer questionType, int page) {
		List<AnswerRecord> answerRecords = this.answerRecordDao.list(name, examNo, examId, examPaperId, questionType,
		        page);
		List<Map<String, Object>> list = Lists.newArrayList();
		for (AnswerRecord record : answerRecords) {
			Map<String, Object> ret = Maps.newHashMap();
			list.add(ret);
			ExamQuestion question = record.getExamQuestion();
			if (null != question) {
				Examinee examinee = record.getExaminee();
				if (null != examinee && null != examinee.getPerson()) {
					ret.put("name", examinee.getPerson().getName());
					ret.put("examno", examinee.getExamNo());
					ret.put("examinee_id", examinee.getId());
				}
				ret.put("title", this.desCryption.decode(question.getTitle()));
				ret.put("question_id", question.getId());
				if ((CList.Api.QuestionType.LOOK_SING.equals(record.getQuestionType()))) {
					if (!Strings.isNullOrEmpty(record.getResPath())) {
						ret.put("lookSingAudio", record.getResPath().split("/file/")[1]);
					}
				}
				if (CList.Api.QuestionType.SINGLE_SELECT.equals(questionType)
				        || CList.Api.QuestionType.MULTI_SELECT.equals(questionType)) {
					List<String> values = Splitter.on(",").splitToList(Strings.nullToEmpty(record.getContent()));
					List<String> examineeOptions = Lists.newArrayList();
					List<String> examineeOptionsPath = Lists.newArrayList();
					ret.put("examinee_options", examineeOptions);
					ret.put("examinee_options_path", examineeOptionsPath);
					List<String> standardOptions = Lists.newArrayList();
					List<String> standardOptionsPath = Lists.newArrayList();
					ret.put("standard_options", standardOptions);
					ret.put("standard_options_path", standardOptionsPath);
					List<Option> options = question.getOptions();
					for (Option option : options) {
						if (StringUtils.isNotBlank(option.getContent())) {
							ret.put("is_option_content", true);
							String decodeOption = null;
							decodeOption = desCryption.decode(option.getContent());
							if (option.isTrue()) {
								standardOptions.add(decodeOption);
							}
							for (String v : values) {
								if (v.equals(option.getValue())) {
									examineeOptions.add(decodeOption);
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
										examineeOptionsPath.add(mfile.getId());
									}
								}
							}
						}
					}
				} else if (CList.Api.QuestionType.SHORT_ANSWER.equals(questionType)) {
					Answer answer = this.answerService.getByQuestion(question.getId());
					if (null != answer) {
						if (StringUtils.isNotBlank(answer.getContent())) {
							ret.put("is_shortanswer_content", true);
							ret.put("standard_answer", this.desCryption.decode(answer.getContent()));
							ret.put("examinee_answer", record.getContent());
						} else {
							ret.put("is_shortanswer_path", true);
							MFile mfile = this.mfileService.getByUrl(answer.getXmlPath());
							if (null != mfile) {
								ret.put("standard_path", mfile.getId());
							}
							mfile = this.mfileService.getByUrl(record.getResPath());
							if (null != mfile) {
								ret.put("examinee_path", mfile.getId());
							}
						}
					}
				}
				this.setScore(record);
				ret.put("score", record.getScore());
			}
		}
		PaginateSupportArray<?> psa = null;
		if (answerRecords instanceof PaginateSupportArray) {
			psa = (PaginateSupportArray<?>) answerRecords;
		}
		return PaginateSupportUtil.pagingList(list, page, psa.getPageSize(), psa.getTotal());
	}

	private void setScore(AnswerRecord record) {
		if (null != record.getScore() && record.getScore() > 0) {
			return;
		}
		ExamQuestion question = record.getExamQuestion();
		if (null == question) {
			return;
		}
		Answer answer = this.answerService.getByQuestion(question.getId());
		Integer questionType = question.getQuestionType();
		if (null == answer && CList.Api.QuestionType.SHORT_ANSWER.equals(questionType)) {
			return;
		}
		if (CList.Api.QuestionType.SHORT_ANSWER.equals(questionType)) {
			if (CList.Api.ExamField.MELODY_WRITING.equals(question.getExamField())) {
				Integer accuracy = this.getAccuracy(answer.getContent(), record.getContent());
				accuracy = accuracy.equals(100) ? accuracy : 0;
				record.setAccuracy(accuracy);
			} else if (CList.Api.ExamField.INTERVAL_NATURE.equals(question.getExamField())
			        || CList.Api.ExamField.CHORD_NATURE.equals(question.getExamField())) {
				Integer accuracy = this.getAccuracy(answer.getContent(), record.getContent());
				record.setAccuracy(accuracy);
			} else {
				String examineeAnswerPath = this.mfileService.getPath(record.getResPath());
				String answerPath = this.mfileService.getPath(answer.getXmlPath());
				Integer accuracy = this.staffUtil.getShortAnswerScore(examineeAnswerPath, answerPath,
				        question.getExamField());
				record.setAccuracy(accuracy);
			}
		} else if (CList.Api.QuestionType.SINGLE_SELECT.equals(questionType)
		        || CList.Api.QuestionType.MULTI_SELECT.equals(questionType)) {
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
			boolean allRight = Arrays.stream(answers).allMatch(ansr -> {
				return options.stream().anyMatch(option -> option.isTrue() && ansr.equals(option.getValue()));
			});
			record.setAccuracy(allRight ? RIGHT : WRONG);
		} else if (CList.Api.QuestionType.LOOK_SING.equals(questionType)) {
			String audioPath = this.mfileService.getPath(record.getResPath());
			String standardAnswerPath = this.mfileService.getPath(question.getXmlPath());
			Integer accuracy = this.staffUtil.getLookSingData(record.getId(), standardAnswerPath, audioPath);
			record.setAccuracy(accuracy);
		}
		ExamPaperQuestion examPaperQuestion = this.examPaperQuestionService
		        .getByExamPaperAndQuestion(record.getExamPaperId(), question.getId());
		if (null == examPaperQuestion || null == examPaperQuestion.getScore()) {
			return;
		}
		Integer score = (int) ((double) record.getAccuracy() / 100 * examPaperQuestion.getScore());
		record.setScore(score);
		this.answerRecordDao.update(record);
	}

	@Override
	public boolean autoSetScore(String examId) {
		this.resultService.deleteAll();
		List<AnswerRecord> records = this.answerRecordDao.getByExam(examId);
		for (AnswerRecord record : records) {
			ExamQuestion question = record.getExamQuestion();
			if (null == question) {
				continue;
			}
			if (null != record.getScore() && record.getScore() > 0) {
				this.resultService.accumulate(record.getExamineeId(), record.getExamId(), record.getExamPaperId(),
				        record.getScore());
				continue;
			}
			Answer answer = this.answerService.getByQuestion(question.getId());
			Integer questionType = question.getQuestionType();
			if (null == answer && CList.Api.QuestionType.SHORT_ANSWER.equals(questionType)) {
				continue;
			}
			if (CList.Api.QuestionType.SHORT_ANSWER.equals(questionType)) {
				if (CList.Api.ExamField.MELODY_WRITING.equals(question.getExamField())) {
					Integer accuracy = this.getAccuracy(answer.getContent(), record.getContent());
					accuracy = accuracy.equals(100) ? accuracy : 0;
					record.setAccuracy(accuracy);
				} else if (CList.Api.ExamField.INTERVAL_NATURE.equals(question.getExamField())
				        || CList.Api.ExamField.CHORD_NATURE.equals(question.getExamField())) {
					Integer accuracy = this.getAccuracy(answer.getContent(), record.getContent());
					record.setAccuracy(accuracy);
				} else {
					String examineeAnswerPath = this.mfileService.getPath(record.getResPath());
					String answerPath = this.mfileService.getPath(answer.getXmlPath());
					Integer accuracy = this.staffUtil.getShortAnswerScore(examineeAnswerPath, answerPath,
					        question.getExamField());
					record.setAccuracy(accuracy);
				}
			} else if (CList.Api.QuestionType.SINGLE_SELECT.equals(questionType)
			        || CList.Api.QuestionType.MULTI_SELECT.equals(questionType)) {
				String answerContent = record.getContent();
				if (StringUtils.isBlank(answerContent)) {
					record.setAccuracy(WRONG);
					continue;
				}
				List<Option> options = question.getOptions();
				if (null == options || options.isEmpty()) {
					record.setAccuracy(WRONG);
					continue;
				}
				String[] answers = answerContent.split(",");
				boolean allRight = Arrays.stream(answers).allMatch(ansr -> {
					return options.stream().anyMatch(option -> option.isTrue() && ansr.equals(option.getValue()));
				});
				record.setAccuracy(allRight ? RIGHT : WRONG);
			} else if (CList.Api.QuestionType.LOOK_SING.equals(questionType)) {
				String audioPath = this.mfileService.getPath(record.getResPath());
				String standardAnswerPath = this.mfileService.getPath(question.getXmlPath());
				Integer accuracy = this.staffUtil.getLookSingData(record.getId(), standardAnswerPath, audioPath);
				record.setAccuracy(accuracy);
			}
			ExamPaperQuestion examPaperQuestion = this.examPaperQuestionService
			        .getByExamPaperAndQuestion(record.getExamPaperId(), question.getId());
			if (null == examPaperQuestion || null == examPaperQuestion.getScore()) {
				continue;
			}
			Integer score = (int) ((double) record.getAccuracy() / 100 * examPaperQuestion.getScore());
			record.setScore(score);
			this.answerRecordDao.update(record);
			this.resultService.accumulate(record.getExamineeId(), record.getExamId(), record.getExamPaperId(), score);
		}
		return true;
	}

	private Integer getAccuracy(String standarAnswer, String examineeAnswer) {
		if (StringUtils.isBlank(standarAnswer) || StringUtils.isBlank(examineeAnswer)) {
			return 0;
		}
		List<String> standarList = Splitter.on(",").splitToList(this.desCryption.decode(standarAnswer));
		List<String> examineeList = Splitter.on(",").splitToList(examineeAnswer);
		int count = 0;
		for (int i = 0; i < standarList.size(); i++) {
			if (standarList.get(i).equals(examineeList.get(i))) {
				count++;
			}
		}
		Integer accuracy = (int) ((double) count / (double) standarList.size() * 100);
		return accuracy;
	}

	@Override
	public List<AnswerRecord> getByExam(String examId) {
		return this.answerRecordDao.getByExam(examId);
	}

	@Override
	public List<AnswerRecord> getByExamAndExaminee(String examId, String examPaperId, String examineeId) {
		return this.answerRecordDao.getByExamAndExaminee(examId, examPaperId, examineeId);
	}

	@Override
	public List<AnswerRecord> getByExamPaper(String examId, String examPaperId) {
		return this.answerRecordDao.getByExamPaper(examId, examPaperId);
	}

	@Override
	public List<AnswerRecord> getByQuestion(String examId, String questionId) {
		return this.answerRecordDao.getByQuestion(examId, questionId);
	}

	@Override
	public List<AnswerRecord> getByExamAndExpertGroup(String examId, String examPaperId, String expertId) {
		List<ExamineeGroup> egroups = this.groupService.getExamineeGroupsByExpertId(expertId);
		List<Examinee> examinees = this.examineeService.getByGroup(egroups);
		if (null == examinees || examinees.isEmpty()) {
			return null;
		}
		return this.answerRecordDao.getByExamAndExaminees(examId, examPaperId, examinees);
	}

	@Override
	public String getLookSingData(String examId, String examPaperId, String examineeId, String questionId) {
		AnswerRecord answerRecord = this.get(examId, examPaperId, examineeId, questionId);
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
	public String getCurveData(String examId, String examPaperId, String examineeId, String questionId) {
		AnswerRecord answerRecord = this.get(examId, examPaperId, examineeId, questionId);
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
	public void getLookSingParser(String examId, String examPaperId, String examineeId, String questionId) {
		AnswerRecord answerRecord = this.get(examId, examPaperId, examineeId, questionId);
		if (null != answerRecord) {
			ExamQuestion examQuestion = answerRecord.getExamQuestion();
			if (null != examQuestion) {
				String path = examQuestion.getXmlPath();
				String standarAnswerPath = this.mfileService.getPath(path);
				String audioPath = this.mfileService.getPath(answerRecord.getResPath());
				this.staffUtil.getLookSingData(answerRecord.getId(), standarAnswerPath, audioPath);
			}
		}
	}

	@Override
	public Map<String, String> getRelations(Integer questionType) {
		Map<String, String> ret = Maps.newHashMap();
		List<AnswerRecord> records = answerRecordDao.list(questionType);
		System.out.println("records size:" + records.size());
		if (null != records) {
			records.forEach(record -> {
				ExamQuestion question = record.getExamQuestion();
				if (CList.Api.QuestionType.LOOK_SING.equals(question.getQuestionType())) {
					String standarAnswerPath = this.mfileService.getPath(question.getXmlPath());
					String audioPath = this.mfileService.getPath(record.getResPath());
					String[] audios = audioPath.split("\\\\");
					audioPath = audios[audios.length - 1];
					String[] answers = standarAnswerPath.split("\\\\");
					standarAnswerPath = answers[answers.length - 1];
					ret.put(audioPath, standarAnswerPath);
				}
			});
		}
		return ret;
	}

	@Override
	public boolean hasAnswerRecord(String examId, String examPaperId, String examineeId) {
		return answerRecordDao.hasAnswerRecord(examId, examPaperId, examineeId);
	}
}
