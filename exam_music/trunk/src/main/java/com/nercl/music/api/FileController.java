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

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.nercl.music.entity.ExamQuestion;
import com.nercl.music.entity.MFile;
import com.nercl.music.service.MFileService;
import com.nercl.music.util.DESCryption;
import com.nercl.music.util.ZipFileUtil;

public abstract class FileController {

	@Autowired
	private Gson gson;

	@Autowired
	private ZipFileUtil zipFileUtil;

	@Autowired
	private MFileService mfileService;

	@Autowired
	private DESCryption desCryption;

	protected void createJsonFile(String josnFilePath, ExamQuestion question, String examineeId, String fileName) {
		Map<String, Object> jsonMap = this.getQuestionJson(question, examineeId, fileName);
		File jsonFile = new File(josnFilePath);
		try {
			Files.createParentDirs(jsonFile);
			Files.write(gson.toJson(jsonMap), jsonFile, Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected Map<String, Object> getQuestionJson(ExamQuestion question, String examineeId, String fileName) {
		Map<String, Object> jsonMap = Maps.newHashMap();
		Integer questionType = question.getQuestionType();
		String questionId = question.getId();
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
		jsonMap.put("examinee_id", examineeId);
		jsonMap.put("file_name", fileName);
		return jsonMap;
	}

	protected void downloadZipFile(String examPaperFolderPath, String zipFilePath, HttpServletResponse response) {
		OutputStream to = null;
		File zipFile = null;
		try {
			zipFile = new File(zipFilePath);
			this.zipFileUtil.zipFile(zipFile, examPaperFolderPath);
			response.setContentType("application/zip;charset=UTF-8");
			response.setHeader("Content-disposition", "filename=" + zipFile.getName());
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

	protected void createFiles(String examPaperFolderPath, String subFolder, List<ExamQuestion> examQuestions) {
		if (null != examQuestions && !examQuestions.isEmpty()) {
			for (ExamQuestion question : examQuestions) {
				String folderPath = examPaperFolderPath + File.separator + subFolder;

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

				this.copyFile(folderPath, urls);
			}
		}
	}

	private void copyFile(String folderPath, List<String> fileUrls) {
		for (String fileUrl : fileUrls) {
			if (!Strings.isNullOrEmpty(fileUrl)) {
				String[] strs = fileUrl.split("/");
				String uuid = strs[strs.length - 1];
				System.out.println("uuid:" + uuid);
				MFile mfile = this.mfileService.get(uuid);
				if (null != mfile) {
					String fileName = mfile.getName();
					File file = new File(mfile.getPath());
					File toFile = new File(folderPath + File.separator + fileName);
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

}
