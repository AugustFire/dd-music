package com.nercl.music.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.question.Answer;
import com.nercl.music.entity.question.ExamQuestion;
import com.nercl.music.entity.question.ExerciserAnswerRecord;
import com.nercl.music.entity.question.MFile;
import com.nercl.music.service.AnswerService;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.ExerciserAnswerRecordService;
import com.nercl.music.service.MFileService;
import com.nercl.music.util.PropertiesUtil;
import com.nercl.music.util.StaffUtil;
import com.nercl.music.xml.XmlParser;

@RestController
public class ApiAnswerController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private MFileService mfileService;

	@Value("${music_exercise.question.xml}")
	private String xmlPath;

	@Value("${music_exercise.answer.xml}")
	private String answerXmlPath;

	@Value("${music_exercise.answer.looksing}")
	private String lookSingPath;

	@Value("${music_exercise.domain}")
	private String domain;

	@Value("${music_exercise.answer.zip}")
	private String answerZipPath;

	@Value("${music_exercise.looksing.data.table}")
	private String tableLooksingDataPath;

	@Value("${music_exercise.looksing.data.curve}")
	private String curveLooksingDataPath;

	@Autowired
	private Gson gson;

	@Autowired
	private ExerciserAnswerRecordService answerRecordService;

	@Autowired
	private StaffUtil staffUtil;

	@Autowired
	private PropertiesUtil propertiesUtil;

	@Autowired
	private ExamQuestionService questionService;

	@Autowired
	private AnswerService answerService;

	@Autowired
	private XmlParser xmlParser;

	private static final String XMl_EXT = "xml";

	private static final String NMl_EXT = "nml";

	@GetMapping(value = "/api/answer_records", produces = JSON_PRODUCES)
	public Map<String, Object> getAnswerRecords(String topic_id, String exerciser_id, String question_id)
	        throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(topic_id) || Strings.isNullOrEmpty(exerciser_id)
		        || Strings.isNullOrEmpty(question_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "专题id或者练习类id或者题目id为空");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		List<ExerciserAnswerRecord> records = answerRecordService.get(topic_id, exerciser_id, question_id);
		if (null != records && !records.isEmpty()) {
			List<Map<String, Object>> scores = Lists.newArrayList();
			ret.put("records", scores);
			records.forEach(record -> {
				Map<String, Object> score = Maps.newHashMap();
				scores.add(score);
				score.put("record_id", record.getId());
				score.put("file_name", record.getResPath() + ".wav");
				score.put("date", record.getCreatAt());
				score.put("score", record.getScore());
			});
		}
		return ret;
	}

	@GetMapping(value = "/api/looksing/download", produces = JSON_PRODUCES)
	public Map<String, Object> downloadLookSingFile(String record_id, HttpServletResponse response) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(record_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "答案id为空");
			return ret;
		}
		ExerciserAnswerRecord record = answerRecordService.get(record_id);
		String uuid = record.getResPath();
		MFile mfile = mfileService.get(uuid);
		if (null == mfile) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "未找到答案");
			return ret;
		}
		String ext = mfile.getExt();
		String contentType = this.getContentType(ext);
		response.setContentType(contentType + ";charset=UTF-8");
		response.setHeader("Content-disposition",
		        "filename=" + Files.getNameWithoutExtension(mfile.getName()) + "." + ext);
		File file = new File(mfile.getPath());
		if (file.exists()) {
			response.setHeader("Content-Length", String.valueOf(file.length()));
		}
		OutputStream to = null;
		try {
			to = response.getOutputStream();
			FileUtils.copyFile(file, to);
			to.flush();
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "下载答案失败");
		} finally {
			if (to != null) {
				try {
					to.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	private String getContentType(String ext) {
		return this.propertiesUtil.get(ext);
	}

	@PostMapping(value = "/api/answer_record/xml", produces = JSON_PRODUCES)
	public Map<String, Object> saveAnswerRecordXml(String topic_id, String exerciser_id, String question_id,
	        HttpServletRequest request) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(topic_id) || StringUtils.isBlank(exerciser_id) || StringUtils.isBlank(question_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "专题id或者练习者id或者题目id为空");
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
						ExerciserAnswerRecord record = this.answerRecordService.save(topic_id, exerciser_id,
						        question_id, null, null);
						if (null != record) {
							ret.put("code", CList.Api.Client.OK);
							ret.put("path", "");
							ret.put("accuracy", 0);
						} else {
							ret.put("code", CList.Api.Client.PROCESSING_FAILED);
							ret.put("desc", "答案保存失败");
						}
					} else {
						name = uuid + "." + ext;
						File file = new File(this.answerXmlPath + File.separator + name);
						Files.createParentDirs(file);
						FileUtils.copyToFile(in, file);
						this.mfileService.save(originalName, name, uuid, ext, file.getPath(),
						        CList.Api.FileResType.EXERCISER_ANSWER_XML);

						ExamQuestion question = questionService.get(question_id);
						Answer answer = this.answerService.getByQuestion(question.getId());
						String answerPath = this.mfileService.getPath(answer.getXmlPath());
						int accuracy = this.staffUtil.getShortAnswerScore(file.getPath(), answerPath,
						        question.getExamField());

						ExerciserAnswerRecord record = this.answerRecordService.save(topic_id, exerciser_id,
						        question_id, null, uuid, accuracy);
						this.staffUtil.createStaffPic(file);

						if (null != record) {
							ret.put("code", CList.Api.Client.OK);
							ret.put("path", uuid);
							ret.put("accuracy", accuracy);
						} else {
							ret.put("code", CList.Api.Client.PROCESSING_FAILED);
							ret.put("desc", "答案保存失败");
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "答案保存失败");
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

	@PostMapping(value = "/api/answer_record", produces = JSON_PRODUCES)
	public Map<String, Object> saveAnswerRecord(String answers, String topic_id, String exerciser_id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(topic_id) || StringUtils.isBlank(exerciser_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "专题id或者练习者id或者题目id为空");
			return ret;
		}
		if (Strings.isNullOrEmpty(answers)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "答案为空");
			return ret;
		}
		System.out.println("answers:" + answers);
		@SuppressWarnings("unchecked")
		List<Map<String, String>> answerList = gson.fromJson(answers, List.class);
		if (null == answerList || answerList.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "答案为空");
			return ret;
		}
		for (Map<String, String> map : answerList) {
			String questionId = map.get("exam_question_id");
			String answer = map.get("answer");
			answerRecordService.save(topic_id, exerciser_id, questionId, answer, null);
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@PostMapping(value = "/api/exam_answer", produces = JSON_PRODUCES)
	public Map<String, Object> saveAnswer(HttpServletRequest request) {
		Map<String, Object> ret = Maps.newHashMap();
		InputStream in = null;
		String uuid = UUID.randomUUID().toString();
		try {
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
				in = mreq.getInputStream();
				MultipartFile file = mreq.getFile("file");
				if (file != null) {
					in = file.getInputStream();
					String filename = file.getOriginalFilename();
					if (StringUtils.isBlank(filename)) {
						ret.put("code", CList.Api.Client.PROCESSING_FAILED);
						ret.put("desc", "文件名称为空");
						return ret;
					}
					String ext = Files.getFileExtension(filename);
					if (!XMl_EXT.equalsIgnoreCase(ext) && !NMl_EXT.equalsIgnoreCase(ext)) {
						ret.put("code", CList.Api.Client.PROCESSING_FAILED);
						ret.put("desc", "非xml文件");
						return ret;
					}
					String name = uuid + "." + ext;
					File answerXmlFile = new File(xmlPath + File.separator + name);
					Files.createParentDirs(answerXmlFile);
					FileUtils.copyInputStreamToFile(in, answerXmlFile);
					boolean success = this.mfileService.save(filename, name, uuid, ext, answerXmlFile.getPath(),
					        CList.Api.FileResType.QUESTION_XML);
					if (success) {
						xmlParser.parseAnswer(answerXmlFile);
						ret.put("code", CList.Api.Client.OK);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "上传答案出错");
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

	@GetMapping(value = "/api/looksing/result/wave", produces = JSON_PRODUCES)
	public Object getLookSingWaveResult(String exam_question_id, String exerciser_id, String answer_record_id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(exerciser_id) || StringUtils.isBlank(exam_question_id)
		        || StringUtils.isBlank(answer_record_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "练习者id，或者题目id，或者答案id为空");
			return ret;
		}
		ExerciserAnswerRecord answer = answerRecordService.get(answer_record_id);
		if (null == answer) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "答案为空");
			return ret;
		}
		ExamQuestion question = this.questionService.get(exam_question_id);
		if (null == question) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "题目为空");
			return ret;
		}
		String audioPath = this.mfileService.getPath(answer.getResPath());
		String standardAnswerPath = this.mfileService.getPath(question.getXmlPath());
		this.staffUtil.getLookSingData(answer_record_id, standardAnswerPath, audioPath);
		File curveFile = new File(this.curveLooksingDataPath + File.separator + answer_record_id + ".json");
		if (null != curveFile && curveFile.exists()) {
			return this.getData(curveFile.getPath());
		}
		ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		ret.put("desc", "答案为空");
		return ret;
	}

	@GetMapping(value = "/api/looksing/result/table", produces = JSON_PRODUCES)
	public Object getLookSingTableResult(String exam_question_id, String exerciser_id, String answer_record_id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(exerciser_id) || StringUtils.isBlank(exam_question_id)
		        || StringUtils.isBlank(answer_record_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "练习者id，或者题目id，或者答案id为空");
			return ret;
		}
		ExerciserAnswerRecord answer = answerRecordService.get(answer_record_id);
		if (null == answer) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "答案为空");
			return ret;
		}
		ExamQuestion question = this.questionService.get(exam_question_id);
		if (null == question) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "题目为空");
			return ret;
		}
		String audioPath = this.mfileService.getPath(answer.getResPath());
		String standardAnswerPath = this.mfileService.getPath(question.getXmlPath());
		this.staffUtil.getLookSingData(answer_record_id, standardAnswerPath, audioPath);
		File tableFile = new File(this.tableLooksingDataPath + File.separator + answer_record_id + ".json");
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
