package com.nercl.music.cloud.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.nercl.music.cloud.entity.Answer;
import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.PresentType;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.QuestionType;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.util.CloudFileUtil;
import com.nercl.music.util.StaffUtil;

@Component
public class PendingScoredConsumer {

	@Autowired
	private StaffUtil staffUtil;

	@Autowired
	private CloudFileUtil cloudFileUtil;

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private AnswerService answerService;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${dd-yinyue.temp.standard.answer}")
	private String tempStandardAnswer;

	@Value("${dd-yinyue.temp.answer}")
	private String tempAnswer;

	@Value("${dd-yinyue.staff.img}")
	private String img;

	@Value("${dd-yinyue.act.data}")
	private String act;

	@Value("${dd-yinyue.creating.data}")
	private String creating;

	@JmsListener(destination = "short.pengding.scored.queue")
	public void receiveShortPengdingScoreQueue(String text) {
		System.out.println("-------------text:" + text);
		if (Strings.isNullOrEmpty(text)) {
			return;
		}
		AnswerRecord record = answerRecordService.findById(text);
		if (null == record) {
			System.out.println("-------------record is null");
			return;
		}
		Question question = record.getQuestion();
		if (null == question || QuestionType.SHORT_ANSWER != question.getQuestionType()) {
			return;
		}
		Answer answer = answerService.getByQuestion(record.getQuestionId());
		if (null == answer) {
			return;
		}
		PresentType presentType = record.getQuestion().getPresentType();
		String rid = answer.getResource();
		if (Strings.isNullOrEmpty(rid)) {
			return;
		}
		Map<String, Object> ret = cloudFileUtil.getResource(rid);
		if (null == ret || null == ret.get("ext")) {
			return;
		}
		String ext = (String) ret.get("ext");
		InputStream is = cloudFileUtil.download(rid, ext);
		if (null == is) {
			return;
		}
		File tempStandardAnswerFile = new File(tempStandardAnswer + File.separator + rid + "." + ext);
		try {
			FileUtils.copyInputStreamToFile(is, tempStandardAnswerFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!tempStandardAnswerFile.exists()) {
			return;
		}
		rid = record.getResourceId();
		if (Strings.isNullOrEmpty(rid)) {
			record.setScore(0f);
			answerRecordService.update(record);
			return;
		}
		ret = cloudFileUtil.getResource(rid);
		if (null == ret || null == ret.get("ext")) {
			return;
		}
		ext = (String) ret.get("ext");
		is = cloudFileUtil.download(rid, ext);
		if (null == is) {
			return;
		}
		File tempAnswerFile = new File(tempAnswer + File.separator + rid + "." + ext);
		try {
			FileUtils.copyInputStreamToFile(is, tempAnswerFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!tempAnswerFile.exists()) {
			return;
		}
		Float percent = staffUtil.getShortAnswerScore(tempStandardAnswerFile.getPath(), tempAnswerFile.getPath(),
				String.valueOf(presentType));
		System.out.println("------------percent:" + percent);
		Float fullScore = null == record.getFullScore() ? 0 : record.getFullScore();
		Float score = fullScore * percent / 100;
		record.setScore((float) Math.round(score));
		answerRecordService.update(record);

		File imgFile = new File(img + File.separator + text + ".png");
		if (!imgFile.exists()) {
			staffUtil.getStaffPic(tempStandardAnswerFile.getPath(), imgFile.getPath());
		}

		tempStandardAnswerFile.delete();
		tempAnswerFile.delete();

	}

	@SuppressWarnings("unchecked")
	@JmsListener(destination = "sing.pengding.scored.queue")
	public void receiveSingPengdingScoreQueue(String text) {
		if (Strings.isNullOrEmpty(text)) {
			return;
		}
		AnswerRecord record = answerRecordService.findById(text);
		if (null == record) {
			return;
		}
		File jsonFile = new File(act + File.separator + record.getId() + ".json");
		if (jsonFile.exists() && jsonFile.length() > 0) {
			return;
		}
		Question question = record.getQuestion();
		if (null == question || (QuestionType.SING != question.getQuestionType()
				&& QuestionType.BEHIND_BACK_SING != question.getQuestionType()
				&& QuestionType.PERFORMANCE != question.getQuestionType()
				&& QuestionType.BEHIND_BACK_PERFORMANCE != question.getQuestionType()
				&& QuestionType.SIGHT_SINGING != question.getQuestionType())) {
			return;
		}
		String rid = question.getXmlPath();
		if (Strings.isNullOrEmpty(rid)) {
			return;
		}
		Map<String, Object> ret = cloudFileUtil.getResource(rid);
		if (null == ret || null == ret.get("ext")) {
			return;
		}
		String ext = (String) ret.get("ext");
		InputStream is = cloudFileUtil.download(rid, ext);
		if (null == is) {
			return;
		}
		File tempStandardAnswerFile = new File(tempStandardAnswer + File.separator + rid + "." + ext);
		try {
			FileUtils.copyInputStreamToFile(is, tempStandardAnswerFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!tempStandardAnswerFile.exists()) {
			return;
		}
		rid = record.getResourceId();
		if (Strings.isNullOrEmpty(rid)) {
			record.setScore(0f);
			answerRecordService.update(record);
			return;
		}
		ret = cloudFileUtil.getResource(rid);
		if (null == ret || null == ret.get("ext")) {
			return;
		}
		ext = (String) ret.get("ext");
		is = cloudFileUtil.download(rid, ext);
		if (null == is) {
			return;
		}
		File tempAnswerFile = new File(tempAnswer + File.separator + rid + "." + ext);
		try {
			FileUtils.copyInputStreamToFile(is, tempAnswerFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!tempAnswerFile.exists()) {
			return;
		}
		Map<String, Object> user = restTemplate.getForObject(ApiClient.GET_USER2, Map.class, record.getUserId());
		if (null == user || null == user.get("man")) {
			return;
		}
		int age = (int) user.getOrDefault("age", 18);
		boolean isMan = (boolean) user.getOrDefault("man", true);

		jsonFile.delete();
		try {
			jsonFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Float percent = staffUtil.getActScore(tempStandardAnswerFile.getPath(), tempAnswerFile.getPath(),
				String.valueOf(record.getQuestion().getQuestionType()), isMan, age, record.getTempo(),
				jsonFile.getPath());
		Float fullScore = null == record.getFullScore() ? 0 : record.getFullScore();
		Float score = fullScore * percent / 100;
		record.setScore((float) Math.round(score));
		answerRecordService.update(record);

//		tempStandardAnswerFile.delete();
//		tempAnswerFile.delete();
	}

	@JmsListener(destination = "creating.pengding.audioed.queue")
	public void receiveCreatingPengdingAudioQueue(String text) {
		if (Strings.isNullOrEmpty(text)) {
			return;
		}
		AnswerRecord record = answerRecordService.findById(text);
		if (null == record) {
			return;
		}
		File audio = new File(creating + File.separator + record.getId() + ".mp3");
		if (audio.exists() && audio.length() > 0) {
			return;
		}
		Question question = record.getQuestion();
		if (null == question || (QuestionType.FILL_WORD_CREATING != question.getQuestionType()
				&& QuestionType.RHYTHM_CREATING != question.getQuestionType()
				&& QuestionType.MELODY_CREATING != question.getQuestionType()
				&& QuestionType.SONG_CREATING != question.getQuestionType()
				&& QuestionType.ASSIGN_MUSIC_CREATING != question.getQuestionType())) {
			return;
		}
		String rid = record.getResourceId();
		if (Strings.isNullOrEmpty(rid)) {
			return;
		}
		Map<String, Object> ret = cloudFileUtil.getResource(rid);
		if (null == ret || null == ret.get("ext")) {
			return;
		}
		String ext = (String) ret.get("ext");
		if (!"staff".equalsIgnoreCase(ext) && !"xml".equalsIgnoreCase(ext) && !"drum".equalsIgnoreCase(ext)) {
			return;
		}
		InputStream is = cloudFileUtil.download(rid, ext);
		if (null == is) {
			return;
		}
		File tempAnswerFile = new File(tempAnswer + File.separator + rid + "." + ext);
		try {
			FileUtils.copyInputStreamToFile(is, tempAnswerFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!tempAnswerFile.exists()) {
			return;
		}
		staffUtil.getCreatingAudio(tempAnswerFile.getPath(), audio.getPath());
		tempAnswerFile.delete();
	}

}