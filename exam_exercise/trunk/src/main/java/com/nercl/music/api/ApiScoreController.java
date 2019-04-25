package com.nercl.music.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.question.ExamQuestion;
import com.nercl.music.entity.question.ExerciserAnswerRecord;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.ExerciserAnswerRecordService;
import com.nercl.music.service.MFileService;
import com.nercl.music.util.StaffUtil;

@RestController
public class ApiScoreController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private MFileService mfileService;

	@Value("${music_exercise.answer.looksing}")
	private String lookSingPath;

	@Autowired
	private ExerciserAnswerRecordService answerRecordService;

	@Autowired
	private StaffUtil staffUtil;

	@Autowired
	private ExamQuestionService questionService;

	@PostMapping(value = "/api/looksing/score", produces = JSON_PRODUCES)
	public Map<String, Object> setScore(String topic_id, String exerciser_id, String question_id, Integer score,
	        HttpServletRequest request) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(topic_id) || Strings.isNullOrEmpty(exerciser_id) || Strings.isNullOrEmpty(question_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "专题id为空或者练习者id为空或者题目id为空");
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
						return ret;
					}
					name = uuid + "." + ext;
					File file = new File(this.lookSingPath + File.separator + name);
					Files.createParentDirs(file);
					FileUtils.copyToFile(in, file);
					this.mfileService.save(originalName, name, uuid, ext, file.getPath(),
					        CList.Api.FileResType.EXERCISER_ANSWER_AUDIO);
					ExerciserAnswerRecord record = answerRecordService.save(topic_id, exerciser_id, question_id, null,
					        uuid);
					if (null != record) {
						new Thread(() -> {
							String audioPath = this.mfileService.getPath(record.getResPath());
							ExamQuestion question = questionService.get(question_id);
							String standardAnswerPath = this.mfileService.getPath(question.getXmlPath());
							Integer accuracy = this.staffUtil.getLookSingData(record.getId(), standardAnswerPath,
						            audioPath);
							record.setScore(accuracy);
							answerRecordService.update(record);
						}).start();

						ret.put("code", CList.Api.Client.OK);
						ret.put("path", uuid);
						ret.put("record_id", record.getId());
					} else {
						ret.put("code", CList.Api.Client.PROCESSING_FAILED);
						ret.put("desc", "分数提交失败");
					}
				}
			} else {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "没有视唱录音文件");
				return ret;
			}
		} catch (IOException e) {
			e.printStackTrace();
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "分数提交失败");
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
