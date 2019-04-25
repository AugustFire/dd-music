package com.nercl.music.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.Answer;
import com.nercl.music.cloud.entity.Knowledge;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.QuestionType;
import com.nercl.music.cloud.service.AnswerService;
import com.nercl.music.cloud.service.KnowledgeService;
import com.nercl.music.cloud.service.OptionService;

@Component
public class QuestionToJsonUtil {

	@Autowired
	private OptionService optionService;

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private AnswerService answerService;

	public Map<String, Object> toJosn(Question question) {
		Map<String, Object> jsonMap = Maps.newHashMap();
		QuestionType questionType = question.getQuestionType();
		String questionId = question.getId();
		jsonMap.put("question_id", questionId);
		jsonMap.put("question_title", question.getTitle());
		jsonMap.put("title_image", question.getTitleImage());
		jsonMap.put("title_audio", question.getTitleAudio());
		jsonMap.put("explaine", question.getExplaine());
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
		jsonMap.put("is_show_time_signature", question.getIsShowTimeSignature());

		List<String> ks = Splitter.on(",").splitToList(Strings.nullToEmpty(question.getKnowledges()));
		List<Map<String, String>> knowledges = Lists.newArrayList();
		ks.forEach(k -> {
			Map<String, String> knw = Maps.newHashMap();
			Knowledge ke = knowledgeService.get(k);
			if (null != ke) {
				knw.put("no", k);
				knw.put("title", ke.getTitle());
				knowledges.add(knw);
			}
		});
		jsonMap.put("knowledges", knowledges);

		jsonMap.put("composite_abilitys", question.getCompositeAbilitys());
		jsonMap.put("template_path", question.getTemplatePath());
		jsonMap.put("version", question.getVersion());
		jsonMap.put("grade", question.getGrade());
		jsonMap.put("dimension", question.getDimension());
		jsonMap.put("is_numbered", question.getIsNumbered());
		jsonMap.put("present_type", question.getPresentType());
		jsonMap.put("song_id", question.getSongId());
		if (QuestionType.SINGLE_SELECT.equals(questionType) || QuestionType.MULTI_SELECT.equals(questionType)) {
			List<Map<String, Object>> options = this.optionService.list(questionId);
			jsonMap.put("options", options);
		}
		Answer answer = answerService.getByQuestion(questionId);
		if (null != answer) {
			jsonMap.put("standard_answer", answer.getContent());
			jsonMap.put("standard_answer_resource", answer.getResource());
		}
		return jsonMap;
	}

}
