package com.nercl.music.api;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.question.Answer;
import com.nercl.music.entity.question.ExamQuestion;
import com.nercl.music.entity.question.MFile;
import com.nercl.music.entity.question.Option;
import com.nercl.music.service.*;
import com.nercl.music.util.DESCryption;
import com.nercl.music.util.PropertiesUtil;
import com.nercl.music.util.ZipFileUtil;
import com.nercl.music.xml.XmlParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class ApiQuestionController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ExamQuestionService examQuestionService;

	@Value("${music_exercise.question.xml}")
	private String xmlPath;

	@Value("${music_exercise.question.audio}")
	private String audioPath;

	@Value("${music_exercise.question.img}")
	private String imgPath;

	@Value("${music_exercise.question.zip}")
	private String zipPath;

	@Autowired
	private DESCryption desCryption;

	@Autowired
	private XmlParser xmlParser;

	@Autowired
	private MFileService mfileService;

	@Autowired
	private PropertiesUtil propertiesUtil;

	@Autowired
	private ZipFileUtil zipFileUtil;

	@Autowired
	private OptionService optionService;

	@Autowired
	private AnswerService answerService;

	@Autowired
	private BehaviorService behaviorService;

	@Autowired
	private Gson gson;

	private static final String XMl_EXT = "xml";

	private static final String NMl_EXT = "nml";

	private static final String WAV_EXT = "wav";

	private static final String MID_EXT = "mid";

	private static final String MIDI_EXT = "midi";

	@GetMapping(value = "/api/questions", produces = JSON_PRODUCES)
	public Map<String, Object> getQuestions(String topic_id, String exerciser_id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(topic_id) || Strings.isNullOrEmpty(exerciser_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "专题id为空或者练习者id为空");
		}
		List<Map<String, Object>> list = examQuestionService.getBytopic(topic_id, exerciser_id);
		System.out.println("list:" + list);
		ret.put("code", CList.Api.Client.OK);
		if (null != list) {
			ret.put("questions", list);
		}
		return ret;
	}

	@GetMapping(value = "/api/question/{id}", produces = JSON_PRODUCES)
	public Map<String, Object> get(@PathVariable String id, String exerciser_id, HttpServletResponse response) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(id)||Strings.isNullOrEmpty(exerciser_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "题目id或者练习者id为空");
			return ret;
		}
		ExamQuestion examQuestion = examQuestionService.get(id);
		if (null == examQuestion) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "题目为空");
			return ret;
		}
		List<ExamQuestion> questions = Lists.newArrayList(examQuestion);
		String uuid = UUID.randomUUID().toString();
		String exerciseQustionsFolderPath = this.zipPath + File.separator + uuid;
		this.createJsonFile(exerciseQustionsFolderPath, questions);

		this.createFiles(exerciseQustionsFolderPath, questions);
		this.downloadZipFile(uuid, exerciseQustionsFolderPath, response);

		//保存下载记录
		this.behaviorService.saveDownload(exerciser_id, id);

		return ret;
	}

	private void downloadZipFile(String uuid, String exerciseQustionsFolderPath, HttpServletResponse response) {
		OutputStream to = null;
		File zipFile = null;
		try {
			String zipName = uuid + ".zip";
			zipFile = new File(this.zipPath + File.separator + zipName);
			this.zipFileUtil.zipFile(zipFile, exerciseQustionsFolderPath);
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
			if (null != zipFile && zipFile.exists()) {
				zipFile.delete();
			}
			try {
				FileUtils.deleteDirectory(new File(exerciseQustionsFolderPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void createJsonFile(String exerciseQustionsFolderPath, List<ExamQuestion> examQuestions) {
		Map<String, Object> ret = Maps.newHashMap();
		List<Map<String, Object>> examQuestionsList = Lists.newArrayList();
		ret.put("exam_questions", examQuestionsList);
		if (null != examQuestions && !examQuestions.isEmpty()) {
			examQuestions.forEach(question -> {
				Map<String, Object> jsonMap = this.getQuestionJson(question);
				examQuestionsList.add(jsonMap);
			});
		}
		File jsonFile = new File(exerciseQustionsFolderPath + File.separator + "json.json");
		try {
			Files.createParentDirs(jsonFile);
			Files.write(gson.toJson(ret), jsonFile, Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createFiles(String exerciseQustionsFolderPath, List<ExamQuestion> examQuestions) {
		if (null != examQuestions && !examQuestions.isEmpty()) {
			for (ExamQuestion question : examQuestions) {
				String questionFolderPath = exerciseQustionsFolderPath + File.separator + question.getId();

				List<String> urls = Lists.newArrayList();
				String titleImageUrl = question.getTitleImage();
				if (StringUtils.isNotBlank(titleImageUrl)) {
//					titleImageUrl = desCryption.decode(titleImageUrl);
					urls.add(titleImageUrl);
				}

				String titleAudioUrl = question.getTitleAudio();
				if (StringUtils.isNotBlank(titleAudioUrl)) {
					titleAudioUrl = desCryption.decode(titleAudioUrl);
					urls.add(titleAudioUrl);
				}
				String xmlPathUrl = question.getXmlPath();
				urls.add(xmlPathUrl);

				List<Option> options = this.optionService.get(question.getId());
				for (Option option : options) {
					String optionImagePath = option.getOptionImage();
					if (StringUtils.isNotBlank(optionImagePath)) {
//						optionImagePath = desCryption.decode(optionImagePath);
						urls.add(optionImagePath);
					}
					String xmlPath = option.getXmlPath();
					if (StringUtils.isNotBlank(xmlPath)) {
						xmlPath = desCryption.decode(xmlPath);
						urls.add(xmlPath);
					}
				}

				Answer answer = question.getAnswer();
				if (null != answer) {
					MFile mfile = this.mfileService.getByUrl(answer.getXmlPath());
					if (null != mfile) {
						urls.add(mfile.getId());
					}
				}
				this.copyFile(questionFolderPath, urls);
			}
		}
	}

	private void copyFile(String questionFolderPath, List<String> fileUrls) {
		for (String fileUrl : fileUrls) {
			if (!Strings.isNullOrEmpty(fileUrl)) {
				String[] strs = fileUrl.split("/");
				String uuid = strs[strs.length - 1];
				System.out.println("uuid:" + uuid);
				MFile mfile = this.mfileService.get(uuid);
				if (null != mfile) {
					String fileName = mfile.getName();
					String ext = Files.getFileExtension(fileName);
					File file = new File(this.getPath(ext) + File.separator + fileName);
					File toFile = new File(questionFolderPath + File.separator + fileName);
					try {
						Files.createParentDirs(toFile);
						Files.copy(file, toFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private Map<String, Object> getQuestionJson(ExamQuestion question) {
		Map<String, Object> jsonMap = Maps.newHashMap();
		Integer questionType = question.getQuestionType();
		String questionId = question.getId();
		jsonMap.put("topic_id", "");
		jsonMap.put("question_id", questionId);
		jsonMap.put("question_title", question.getTitle());
		jsonMap.put("title_image", question.getTitleImage());
		jsonMap.put("title_audio", question.getTitleAudio());
		jsonMap.put("explaine", question.getExplaine());
		jsonMap.put("score", question.getScore());
		jsonMap.put("prepare_time", question.getPrepareTime());
		jsonMap.put("limit_time", question.getLimitTime());
		jsonMap.put("tune", question.getTune());
		jsonMap.put("tempo", question.getTempo());
		jsonMap.put("xml_path", question.getXmlPath());
		jsonMap.put("question_type", questionType);
		jsonMap.put("subject_type", question.getSubjectType());
		jsonMap.put("exam_field", question.getExamField());
		jsonMap.put("difficulty", question.getDifficulty());
		jsonMap.put("reliability", question.getReliability());
		jsonMap.put("validity", question.getValidity());
		jsonMap.put("discrimination", question.getDiscrimination());
		jsonMap.put("secret_level", question.getSecretLevel());
		jsonMap.put("is_open", question.getIsOpen());
		jsonMap.put("is_play_standard_note", question.getIsPlayStandardNote());
		jsonMap.put("is_play_repare_beat", question.getIsPlayRepareBeat());
		jsonMap.put("is_play_main_chord", question.getIsPlayMainChord());
		jsonMap.put("is_play_debug_pitch", question.getIsPlayDebugPitch());
		jsonMap.put("is_play_start_note", question.getIsPlayStartNote());
		jsonMap.put("groupnum", question.getGroupnum());
		jsonMap.put("playnum", question.getPlaynum());
		jsonMap.put("delay", question.getDelay());
		jsonMap.put("measurenum", question.getMeasurenum());
		jsonMap.put("numerator", question.getNumerator());
		jsonMap.put("denominator", question.getDenominator());
		jsonMap.put("knowledge", question.getKnowledge());
		if (CList.Api.QuestionType.SINGLE_SELECT.equals(questionType)
				|| CList.Api.QuestionType.MULTI_SELECT.equals(questionType)) {
			List<Map<String, Object>> options = this.optionService.list(questionId);
			jsonMap.put("options", options);
		}
		if (CList.Api.QuestionType.SHORT_ANSWER.equals(questionType)) {
			Answer answer = this.answerService.getByQuestion(question.getId());
			if (CList.Api.ExamField.MELODY_WRITING.equals(question.getExamField())
					|| CList.Api.ExamField.INTERVAL_NATURE.equals(question.getExamField())
					|| CList.Api.ExamField.CHORD_NATURE.equals(question.getExamField())) {
				jsonMap.put("answer_content", null != answer ? answer.getContent() : "");
			} else {
				MFile mfile = this.mfileService.getByUrl(answer.getXmlPath());
				jsonMap.put("answers", null != mfile ? mfile.getId() : "");
			}
		}
		return jsonMap;
	}

	@GetMapping(value = "/api/question/xml", produces = JSON_PRODUCES)
	public void downloadXml(String uuid, HttpServletResponse response) {
		MFile mfile = this.mfileService.get(uuid);
		if (null == mfile) {
			return;
		}
		String ext = Files.getFileExtension(mfile.getName());
		String contentType = this.getContentType(ext);
		response.setContentType(contentType + ";charset=UTF-8");
		response.setHeader("Content-disposition", "filename=" + mfile.getName());
		File file = new File(this.getPath(ext) + File.separator + mfile.getName());
		OutputStream to = null;
		try {
			to = response.getOutputStream();
			FileUtils.copyFile(file, to);
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

	private String getContentType(String ext) {
		return this.propertiesUtil.get(ext);
	}

	private String getPath(String ext) {
		if (XMl_EXT.equalsIgnoreCase(ext) || NMl_EXT.equalsIgnoreCase(ext)) {
			return this.xmlPath;
		} else if (WAV_EXT.equalsIgnoreCase(ext) || MID_EXT.equalsIgnoreCase(ext) || MIDI_EXT.equalsIgnoreCase(ext)) {
			return this.audioPath;
		} else {
			return this.imgPath;
		}
	}

	@PostMapping(value = "/api/exam_question", produces = JSON_PRODUCES)
	public Map<String, Object> saveQuestion(HttpServletRequest request) {
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
					File questionXmlFile = new File(xmlPath + File.separator + name);
					Files.createParentDirs(questionXmlFile);
					FileUtils.copyInputStreamToFile(in, questionXmlFile);
					boolean success = this.mfileService.save(filename, name, uuid, ext, questionXmlFile.getPath(),
							CList.Api.FileResType.QUESTION_XML);
					if (success) {
						xmlParser.parseQuestion(questionXmlFile);
						ret.put("code", CList.Api.Client.OK);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "上传题目出错");
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

}
