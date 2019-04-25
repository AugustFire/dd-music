package com.nercl.music.api;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.Exam;
import com.nercl.music.entity.ExamPaper;
import com.nercl.music.entity.ExamQuestion;
import com.nercl.music.entity.MFile;
import com.nercl.music.entity.Option;
import com.nercl.music.service.ExamPaperQuestionService;
import com.nercl.music.service.ExamPaperService;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.ExamService;
import com.nercl.music.service.MFileService;
import com.nercl.music.service.OptionService;
import com.nercl.music.util.DESCryption;
import com.nercl.music.util.ZipFileUtil;

@RestController
public class ApiExamPaperController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private ExamQuestionService examQuestionService;

	@Autowired
	private OptionService optionService;

	@Autowired
	private MFileService mfileService;

	@Autowired
	private ExamService examService;

	@Autowired
	private ExamPaperQuestionService examPaperQuestionService;

	@Value("${exam_music.question.xml}")
	private String xmlPath;

	@Value("${exam_music.question.audio}")
	private String audioPath;

	@Value("${exam_music.question.img}")
	private String imgPath;

	@Value("${exam_music.question.zip}")
	private String zipPath;

	@Value("${exam_music.question.looksing.size}")
	private int size;

	@Autowired
	private ZipFileUtil zipFileUtil;

	@Autowired
	private DESCryption desCryption;

	@Autowired
	private Gson gson;

	private static final String XMl_EXT = "xml";

	private static final String NMl_EXT = "nml";

	private static final String WAV_EXT = "wav";

	private static final String MID_EXT = "mid";

	private static final String MIDI_EXT = "midi";

	@GetMapping(value = "/api/looksing/exam_paper", produces = JSON_PRODUCES)
	public void getLookSingQuestions(@RequestParam(value = "exam_id") String examId,
	        @RequestParam(value = "exam_paper_id") String examPaperId, HttpServletResponse response) {
		Exam exam = this.examService.get(examId);
		ExamPaper examPaper = this.examPaperService.get(examPaperId);
		if (null == exam || null == examPaper) {
			return;
		}
		List<ExamQuestion> examQuestions = this.examQuestionService.getLookSingQuestions(examId, examPaperId, size);
		if (null == examQuestions || examQuestions.isEmpty()) {
			return;
		}
		this.setScore(examPaperId, examQuestions);
		String examPaperFolderPath = this.zipPath + File.separator + examPaperId;
		this.createJsonFile(examPaperFolderPath, examPaper, examQuestions);

		this.createFiles(examPaperFolderPath, examQuestions);
		this.downloadZipFile(examPaperFolderPath, examPaperId, response);
	}

	@GetMapping(value = "/api/yueli/exam_paper", produces = JSON_PRODUCES)
	public void getYueLiQuestions(@RequestParam(value = "exam_id") String examId,
	        @RequestParam(value = "exam_paper_id") String examPaperId, HttpServletResponse response) {
		Exam exam = this.examService.get(examId);
		ExamPaper examPaper = this.examPaperService.get(examPaperId);
		if (null == exam || null == examPaper) {
			return;
		}
		List<ExamQuestion> examQuestions = this.examQuestionService.getYueLiQuestions(examId, examPaperId);
		if (null == examQuestions || examQuestions.isEmpty()) {
			return;
		}
		this.setScore(examPaperId, examQuestions);
		String examPaperFolderPath = this.zipPath + File.separator + examPaperId;
		this.createJsonFile(examPaperFolderPath, examPaper, examQuestions);

		this.createFiles(examPaperFolderPath, examQuestions);
		this.downloadZipFile(examPaperFolderPath, examPaperId, response);
	}

	@GetMapping(value = "/api/tingyin/exam_paper", produces = JSON_PRODUCES)
	public void getTingYinQuestions(@RequestParam(value = "exam_id") String examId,
	        @RequestParam(value = "exam_paper_id") String examPaperId, HttpServletResponse response) {
		Exam exam = this.examService.get(examId);
		ExamPaper examPaper = this.examPaperService.get(examPaperId);
		if (null == exam || null == examPaper) {
			return;
		}
		List<ExamQuestion> examQuestions = this.examQuestionService.getTingYinQuestions(examId, examPaperId);
		if (null == examQuestions || examQuestions.isEmpty()) {
			return;
		}
		this.setScore(examPaperId, examQuestions);
		String examPaperFolderPath = this.zipPath + File.separator + examPaperId;
		this.createJsonFile(examPaperFolderPath, examPaper, examQuestions);

		this.createFiles(examPaperFolderPath, examQuestions);
		this.downloadZipFile(examPaperFolderPath, examPaperId, response);
	}

	private void createFiles(String examPaperFolderPath, List<ExamQuestion> examQuestions) {
		if (null != examQuestions && !examQuestions.isEmpty()) {
			for (ExamQuestion question : examQuestions) {
				String questionFolderPath = examPaperFolderPath + File.separator + question.getId();

				List<String> urls = Lists.newArrayList();
				String titleImageUrl = question.getTitleImage();
				if (StringUtils.isNotBlank(titleImageUrl)) {
					titleImageUrl = desCryption.decode(titleImageUrl);
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
						optionImagePath = desCryption.decode(optionImagePath);
						urls.add(optionImagePath);
					}
					String xmlPath = option.getXmlPath();
					if (StringUtils.isNotBlank(xmlPath)) {
						xmlPath = desCryption.decode(xmlPath);
						urls.add(xmlPath);
					}
				}
				this.copyFile(questionFolderPath, urls);
			}
		}
	}

	private void createJsonFile(String examPaperFolderPath, ExamPaper examPaper, List<ExamQuestion> examQuestions) {
		Map<String, Object> ret = Maps.newHashMap();
		List<Map<String, Object>> examQuestionsList = Lists.newArrayList();
		ret.put("exam_paper_id", examPaper.getId());
		ret.put("exam_paper_title", examPaper.getTitle());
		ret.put("resolved_time", examPaper.getResolvedTime());
		ret.put("exam_paper_score", examPaper.getScore());
		ret.put("exam_questions", examQuestionsList);
		if (null != examQuestions && !examQuestions.isEmpty()) {
			for (ExamQuestion question : examQuestions) {
				Map<String, Object> jsonMap = this.getQuestionJson(question);
				examQuestionsList.add(jsonMap);
			}
		}
		File jsonFile = new File(examPaperFolderPath + File.separator + "json.json");
		try {
			Files.createParentDirs(jsonFile);
			Files.write(gson.toJson(ret), jsonFile, Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void downloadZipFile(String examPaperFolderPath, String examPaperId, HttpServletResponse response) {
		OutputStream to = null;
		File zipFile = null;
		try {
			String zipName = examPaperId + ".zip";
			zipFile = new File(this.zipPath + File.separator + zipName);
			this.zipFileUtil.zipFile(zipFile, examPaperFolderPath);
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
				FileUtils.deleteDirectory(new File(examPaperFolderPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Map<String, Object> getQuestionJson(ExamQuestion question) {
		Map<String, Object> jsonMap = Maps.newHashMap();
		Integer questionType = question.getQuestionType();
		String questionId = question.getId();
		jsonMap.put("question_id", questionId);
		jsonMap.put("question_title", question.getTitle());
		jsonMap.put("title_image", question.getTitleImage());
		jsonMap.put("title_audio", question.getTitleAudio());
		jsonMap.put("explaine", question.getExplaine());
		jsonMap.put("score", question.getScore2());
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
		return jsonMap;
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

	private String getPath(String ext) {
		if (XMl_EXT.equalsIgnoreCase(ext) || NMl_EXT.equalsIgnoreCase(ext)) {
			return this.xmlPath;
		} else if (WAV_EXT.equalsIgnoreCase(ext) || MID_EXT.equalsIgnoreCase(ext) || MIDI_EXT.equalsIgnoreCase(ext)) {
			return this.audioPath;
		} else {
			return this.imgPath;
		}
	}

	private void setScore(String examPaperId, List<ExamQuestion> questions) {
		if (null == questions || questions.isEmpty()) {
			return;
		}
		questions.forEach(question -> {
			question.setScore2(this.examPaperQuestionService.getScore(examPaperId, question.getId()));
		});
	}

}
