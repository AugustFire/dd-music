package com.nercl.music.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.AnswerRecord;
import com.nercl.music.entity.ExamQuestion;
import com.nercl.music.entity.MFile;
import com.nercl.music.service.AnswerRecordService;
import com.nercl.music.service.ExpertResultService;
import com.nercl.music.service.MFileService;
import com.nercl.music.service.MachineResultService;
import com.nercl.music.util.StaffUtil;
import com.nercl.music.xml.XmlParser;

@RestController
public class ApiAnswerController extends FileController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private MFileService mfileService;

	@Autowired
	private XmlParser xmlParser;

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private MachineResultService machineResultService;

	@Autowired
	private ExpertResultService expertResultService;

	@Autowired
	private Gson gson;

	@Autowired
	private StaffUtil staffUtil;

	@Value("${exam_music.question.xml}")
	private String xmlPath;

	@Value("${exam_music.answer.examinee.xml}")
	private String answerXmlPath;

	@Value("${exam_music.answer.examinee.looksing}")
	private String lookSingPath;

	@Value("${exam_music.domain}")
	private String domain;

	@Value("${exam_music.answer.zip}")
	private String answerZipPath;

	private static final String XMl_EXT = "xml";

	private static final String NMl_EXT = "nml";

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

	@PostMapping(value = "/api/upload_looksing_file", produces = JSON_PRODUCES)
	public Map<String, Object> uploadLookSingFile(@RequestParam(value = "exam_id", required = false) String examId,
	        @RequestParam(value = "exam_paper_id", required = false) String examPaperId,
	        @RequestParam(value = "examinee_id") String examineeId,
	        @RequestParam(value = "exam_question_id") String examQuestionId, HttpServletRequest request) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(examineeId) || StringUtils.isBlank(examQuestionId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "考生id或者考题id为空");
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
					        CList.Api.FileResType.EXAMINEE_ANSWER_AUDIO);
					String path = this.domain + "/file/" + uuid;
					boolean success = this.answerRecordService.save(examId, examPaperId, examineeId, examQuestionId,
					        null, path);
					if (success) {
						ret.put("code", CList.Api.Client.OK);
						ret.put("path", path);
					} else {
						ret.put("code", CList.Api.Client.PROCESSING_FAILED);
						ret.put("desc", "已有答案");
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

	@PostMapping(value = "/api/answer_record/xml", produces = JSON_PRODUCES)
	public Map<String, Object> saveAnswerRecordXml(String exam_id, String exam_paper_id, String examinee_id,
	        String exam_question_id, HttpServletRequest request) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(exam_id) || StringUtils.isBlank(exam_paper_id) || StringUtils.isBlank(examinee_id)
		        || StringUtils.isBlank(exam_question_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "考试id或者考卷id或者考题id或者考生id为空");
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
						boolean success = this.answerRecordService.save(exam_id, exam_paper_id, examinee_id,
						        exam_question_id, null, null);
						if (success) {
							ret.put("code", CList.Api.Client.OK);
							ret.put("path", "");
						} else {
							ret.put("code", CList.Api.Client.OK);
							ret.put("desc", "已存在答案");
						}
					} else {
						name = uuid + "." + ext;
						File file = new File(this.answerXmlPath + File.separator + name);
						Files.createParentDirs(file);
						FileUtils.copyToFile(in, file);
						this.mfileService.save(originalName, name, uuid, ext, file.getPath(),
						        CList.Api.FileResType.EXAMINEE_ANSWER_XML);
						String path = this.domain + "/file/" + uuid;
						boolean success = this.answerRecordService.save(exam_id, exam_paper_id, examinee_id,
						        exam_question_id, null, path);
						this.staffUtil.createStaffPic(file);
						if (success) {
							ret.put("code", CList.Api.Client.OK);
							ret.put("path", path);
						} else {
							ret.put("code", CList.Api.Client.OK);
							ret.put("desc", "已存在答案");
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

	@PostMapping(value = "/api/answer_record", produces = JSON_PRODUCES)
	public Map<String, Object> saveAnswerRecord(String answers, String exam_id, String exam_paper_id,
	        String examinee_id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(exam_id) || StringUtils.isBlank(exam_paper_id) || StringUtils.isBlank(examinee_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "考试id或者考卷id或者考生id为空");
			return ret;
		}
		if (null == answers || answers.isEmpty()) {
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
			answerRecordService.save(exam_id, exam_paper_id, examinee_id, questionId, answer, null);
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@PostMapping(value = "/api/looksing/score", produces = JSON_PRODUCES)
	public Map<String, Object> setScore(@RequestParam(value = "exam_id") String examId,
	        @RequestParam(value = "exam_paper_id") String examPaperId,
	        @RequestParam(value = "examinee_id") String examineeId,
	        @RequestParam(value = "exam_question_id") String examQuestionId, @RequestParam Integer score) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(examineeId) || StringUtils.isBlank(examQuestionId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "考生id或者考题id为空");
		}
		boolean success = this.machineResultService.save(examId, examPaperId, examineeId, examQuestionId, score);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("desc", "分数提交成功");
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "已有分数");
		}
		return ret;
	}

	@PostMapping(value = "/api/looksing/expert/score", produces = JSON_PRODUCES)
	public Map<String, Object> expertSetScore(@RequestParam(value = "exam_id") String examId,
	        @RequestParam(value = "exam_paper_id") String examPaperId,
	        @RequestParam(value = "examinee_id") String examineeId,
	        @RequestParam(value = "exam_question_id") String examQuestionId,
	        @RequestParam(value = "expert_id") String expertId, String comment, @RequestParam Integer score) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(examId) || StringUtils.isBlank(examPaperId) || StringUtils.isBlank(examineeId)
		        || StringUtils.isBlank(examQuestionId) || StringUtils.isBlank(expertId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "考试id或者考卷id考生id或者考题id或者专家id为空");
		}
		boolean success = this.expertResultService.save(examId, examPaperId, examineeId, examQuestionId, expertId,
		        score, comment);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("desc", "分数提交成功");
		}
		return ret;
	}

	@GetMapping(value = "/api/download/looksing_answer", produces = JSON_PRODUCES)
	public void downloadLookSingAnswer(@RequestParam(value = "exam_id") String examId,
	        @RequestParam(value = "exam_paper_id") String examPaperId,
	        @RequestParam(value = "expert_id", required = false) String expertId, HttpServletResponse response) {
		List<AnswerRecord> answers = null;
		if (StringUtils.isNotBlank(expertId)) {
			answers = this.answerRecordService.getByExamAndExpertGroup(examId, examPaperId, expertId);
		} else {
			answers = this.answerRecordService.list(examId, examPaperId, CList.Api.QuestionType.LOOK_SING);
		}
		if (null == answers || answers.isEmpty()) {
			return;
		}
		String examPaperFolderPath = this.answerZipPath + File.separator + examPaperId;
		for (AnswerRecord answer : answers) {
			ExamQuestion question = answer.getExamQuestion();
			String examineeId = answer.getExamineeId();

			this.createFiles(examPaperFolderPath, examineeId, Lists.newArrayList(question));
			String resPath = answer.getResPath();
			String[] str = resPath.split("/");
			String uuid = str[str.length - 1];
			File answerFile = this.getAnswerFile(uuid);
			String fileName = uuid + "." + Files.getFileExtension(answerFile.getName());
			String answerFilePath = examPaperFolderPath + File.separator + examineeId + File.separator
			        + question.getId() + File.separator + "answer" + File.separator + fileName;
			this.copyAnswerFile(answerFilePath, answerFile);

			String jsonFilePath = examPaperFolderPath + File.separator + examineeId + File.separator + question.getId()
			        + File.separator + "json.json";
			this.createJsonFile(jsonFilePath, question, examineeId, fileName);
		}
		String zipFilePath = this.answerZipPath + File.separator + examPaperId + ".zip";
		this.downloadZipFile(examPaperFolderPath, zipFilePath, response);
	}

	private File getAnswerFile(String uuid) {
		MFile mfile = this.mfileService.get(uuid);
		if (null == mfile) {
			return null;
		}
		return new File(mfile.getPath());
	}

	private void copyAnswerFile(String path, File file) {
		File newFile = new File(path);
		try {
			Files.createParentDirs(newFile);
			Files.copy(file, newFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
