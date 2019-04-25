package com.nercl.music.api.mobile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.Answer;
import com.nercl.music.entity.ExamQuestion;
import com.nercl.music.entity.ExerciserAnswerRecord;
import com.nercl.music.service.AnswerService;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.ExerciserAnswerRecordService;
import com.nercl.music.service.MFileService;
import com.nercl.music.util.DESCryption;
import com.nercl.music.util.StaffUtil;

@RestController
public class ApiMobileAnswerController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ExerciserAnswerRecordService exerciserAnswerRecordService;

	@Autowired
	private MFileService mfileService;

	@Value("${exam_music.answer.exerciser.looksing}")
	private String lookSingPath;

	@Value("${exam_music.answer.exerciser.xml}")
	private String answerXmlPath;

	@Value("${exam_music.domain}")
	private String domain;

	@Value("${exam_music.looksing.data.exerciser.table}")
	private String exerciserLooksingDataTablePath;

	@Value("${exam_music.looksing.data.exerciser.curve}")
	private String exerciserLooksingDataCurvePath;

	@Autowired
	private StaffUtil staffUtil;

	@Autowired
	private ExamQuestionService questionService;

	@Autowired
	private AnswerService answerService;

	@Autowired
	private DESCryption desCryption;

	@PostMapping(value = "/api/mobile/upload_looksing_file", produces = JSON_PRODUCES)
	public Map<String, Object> uploadLookSingFile(String exam_id, String exam_paper_id, String exerciser_id,
	        String exam_question_id, HttpServletRequest request) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(exerciser_id) || StringUtils.isBlank(exam_question_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "练习者id或者考题id为空");
			return ret;
		}
		InputStream in = null;
		String name = null;
		String uuid = UUID.randomUUID().toString();
		try {
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
				MultipartFile mfile = mreq.getFile("file");
				if (mfile != null) {
					in = mfile.getInputStream();
					String originalName = mfile.getOriginalFilename();
					String ext = Files.getFileExtension(originalName);
					if (StringUtils.isBlank(originalName) || StringUtils.isBlank(ext)) {
						ret.put("code", CList.Api.Client.PROCESSING_FAILED);
						ret.put("desc", "文件为空");
					}
					name = uuid + "." + ext;
					File file = new File(this.lookSingPath + File.separator + name);
					Files.createParentDirs(file);
					FileUtils.copyToFile(in, file);
					this.mfileService.save(originalName, name, uuid, ext, file.getPath(),
					        CList.Api.FileResType.EXERCISER_ANSWER_AUDIO);
					String path = this.domain + "/file/" + uuid;
					ExerciserAnswerRecord record = this.exerciserAnswerRecordService.save(exam_id, exam_paper_id,
					        exerciser_id, exam_question_id, null, path);
					if (null != record) {
						ret.put("code", CList.Api.Client.OK);
						ret.put("path", path);
						ret.put("answer_record_id", record.getId());
						// this.setAccuracy(ret, record);
					} else {
						ret.put("code", CList.Api.Client.PROCESSING_FAILED);
						ret.put("desc", "保存答案出错");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "保存答案出错");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
		return ret;
	}

	@PostMapping(value = "/api/mobile/answer_record/xml", produces = JSON_PRODUCES)
	public Map<String, Object> saveAnswerRecordXml(String exam_id, String exam_paper_id, String exerciser_id,
	        String exam_question_id, HttpServletRequest request) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(exerciser_id) || StringUtils.isBlank(exam_question_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "练习者id或者问题id为空");
			return ret;
		}
		InputStream in = null;
		String name = null;
		String uuid = UUID.randomUUID().toString();
		try {
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
				MultipartFile mfile = mreq.getFile("file");
				if (mfile != null) {
					in = mfile.getInputStream();
					String originalName = mfile.getOriginalFilename();
					String ext = Files.getFileExtension(originalName);
					if (StringUtils.isBlank(originalName) && StringUtils.isBlank(ext)) {
						ExerciserAnswerRecord record = this.exerciserAnswerRecordService.save(exam_id, exam_paper_id,
						        exerciser_id, exam_question_id, null, null);
						if (null != record) {
							ret.put("code", CList.Api.Client.OK);
							ret.put("path", "");
						} else {
							ret.put("code", CList.Api.Client.PROCESSING_FAILED);
							ret.put("desc", "保存答案出错");
						}
					} else {
						name = uuid + "." + ext;
						File file = new File(this.answerXmlPath + File.separator + name);
						Files.createParentDirs(file);
						FileUtils.copyToFile(in, file);
						this.mfileService.save(originalName, name, uuid, ext, file.getPath(),
						        CList.Api.FileResType.EXERCISER_ANSWER_XML);
						String path = this.domain + "/file/" + uuid;
						ExerciserAnswerRecord record = this.exerciserAnswerRecordService.save(exam_id, exam_paper_id,
						        exerciser_id, exam_question_id, null, path);
						this.staffUtil.createStaffPic(file);
						if (null != record) {
							ret.put("code", CList.Api.Client.OK);
							ret.put("path", path);
							this.setAccuracy(ret, record);
						} else {
							ret.put("code", CList.Api.Client.OK);
							ret.put("desc", "保存答案出错");
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "上传失败");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
		return ret;
	}

	private void setAccuracy(Map<String, Object> ret, ExerciserAnswerRecord record) {
		ExamQuestion question = this.questionService.get(record.getExamQuestionId());
		Answer answer = this.answerService.getByQuestion(record.getExamQuestionId());
		if (null == question || null == answer) {
			ret.put("accuracy", 0);
		}
		Integer questionType = question.getQuestionType();
		Integer accuracy = 0;
		if (CList.Api.QuestionType.LOOK_SING.equals(questionType)) {
			String audioPath = this.mfileService.getPath(record.getResPath());
			String standardAnswerPath = this.mfileService.getPath(question.getXmlPath());
			accuracy = this.staffUtil.getLookSingData(record.getId(), standardAnswerPath, audioPath);
			ret.put("accuracy", accuracy);
		} else if (CList.Api.QuestionType.SHORT_ANSWER.equals(questionType)) {
			if (CList.Api.ExamField.MELODY_WRITING.equals(question.getExamField())) {
				accuracy = this.getAccuracy(answer.getContent(), record.getContent());
				accuracy = accuracy.equals(100) ? accuracy : 0;
				ret.put("accuracy", accuracy);
			} else if (CList.Api.ExamField.INTERVAL_NATURE.equals(question.getExamField())
			        || CList.Api.ExamField.CHORD_NATURE.equals(question.getExamField())) {
				accuracy = this.getAccuracy(answer.getContent(), record.getContent());
				ret.put("accuracy", accuracy);
			} else {
				String exerciserAnswerPath = this.mfileService.getPath(record.getResPath());
				String answerPath = this.mfileService.getPath(answer.getXmlPath());
				accuracy = this.staffUtil.getShortAnswerScore(exerciserAnswerPath, answerPath, question.getExamField());
				ret.put("accuracy", accuracy);
			}
		}
	}

	private Integer getAccuracy(String standarAnswer, String exerciserAnswer) {
		if (StringUtils.isBlank(standarAnswer) || StringUtils.isBlank(exerciserAnswer)) {
			return 0;
		}
		List<String> standarList = Splitter.on(",").splitToList(this.desCryption.decode(standarAnswer));
		List<String> examineeList = Splitter.on(",").splitToList(exerciserAnswer);
		int count = 0;
		for (int i = 0; i < standarList.size(); i++) {
			if (standarList.get(i).equals(examineeList.get(i))) {
				count++;
			}
		}
		Integer accuracy = (int) ((double) count / (double) standarList.size() * 100);
		return accuracy;
	}

	@PostMapping(value = "/api/mobile/answer_record", produces = JSON_PRODUCES)
	public Map<String, Object> saveAnswerRecord(String answer, String exam_id, String exam_paper_id,
	        String exam_question_id, String exerciser_id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(exerciser_id) || StringUtils.isBlank(exam_question_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "练习者id，或者考题id为空");
			return ret;
		}
		if (Strings.isNullOrEmpty(answer)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "答案为空");
			return ret;
		}
		exerciserAnswerRecordService.save(exam_id, exam_paper_id, exerciser_id, exam_question_id, answer, null);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@GetMapping(value = "/api/mobile/looksing/result/wave", produces = JSON_PRODUCES)
	public Object getLookSingWaveResult(String exam_id, String exam_paper_id, String exam_question_id,
	        String exerciser_id, String answer_record_id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(exerciser_id) || StringUtils.isBlank(exam_question_id)
		        || StringUtils.isBlank(answer_record_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "练习者id，或者考题id，或者答案id为空");
			return ret;
		}
		List<ExerciserAnswerRecord> answers = exerciserAnswerRecordService.get(exam_id, exam_paper_id, exerciser_id,
		        exam_question_id);
		if (null == answers || answers.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "答案为空");
			return ret;
		}
		ExamQuestion question = this.questionService.get(exam_question_id);
		if (null == question) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "考题为空");
			return ret;
		}
		ExerciserAnswerRecord answerRecord = answers.get(0);
		String audioPath = this.mfileService.getPath(answerRecord.getResPath());
		String standardAnswerPath = this.mfileService.getPath(question.getXmlPath());
		this.staffUtil.getExerciserLookSingData(answerRecord.getId(), standardAnswerPath, audioPath);
		File curveFile = new File(
		        this.exerciserLooksingDataCurvePath + File.separator + answerRecord.getId() + ".json");
		if (null != curveFile && curveFile.exists()) {
			return this.getData(curveFile.getPath());
		}
		ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		ret.put("desc", "答案为空");
		return ret;
	}

	@GetMapping(value = "/api/mobile/looksing/result/table", produces = JSON_PRODUCES)
	public Object getLookSingTableResult(String exam_id, String exam_paper_id, String exam_question_id,
	        String exerciser_id, String answer_record_id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(exerciser_id) || StringUtils.isBlank(exam_question_id)
		        || StringUtils.isBlank(answer_record_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "练习者id，或者考题id，或者答案id为空");
			return ret;
		}
		List<ExerciserAnswerRecord> answers = exerciserAnswerRecordService.get(exam_id, exam_paper_id, exerciser_id,
		        exam_question_id);
		if (null == answers || answers.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "答案为空");
			return ret;
		}
		ExamQuestion question = this.questionService.get(exam_question_id);
		if (null == question) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "考题为空");
			return ret;
		}
		ExerciserAnswerRecord answerRecord = answers.get(0);
		String audioPath = this.mfileService.getPath(answerRecord.getResPath());
		String standardAnswerPath = this.mfileService.getPath(question.getXmlPath());
		this.staffUtil.getExerciserLookSingData(answerRecord.getId(), standardAnswerPath, audioPath);
		File tableFile = new File(
		        this.exerciserLooksingDataTablePath + File.separator + answerRecord.getId() + ".json");
		if (null != tableFile && tableFile.exists()) {
			return this.getData(tableFile.getPath());
		}
		ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		ret.put("desc", "答案为空");
		return ret;
	}

	private String getData(String filePath) {
		StringBuffer sb = new StringBuffer();
		InputStreamReader read = null;
		BufferedReader br = null;
		try {
			read = new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8");
			br = new BufferedReader(read);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != br) {
					br.close();
				}
				if (null != read) {
					read.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
