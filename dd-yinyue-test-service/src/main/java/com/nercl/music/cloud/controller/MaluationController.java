package com.nercl.music.cloud.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.AnswerRecordMaluation;
import com.nercl.music.cloud.entity.Maluation;
import com.nercl.music.cloud.entity.MaluationOption;
import com.nercl.music.cloud.entity.QuestionType;
import com.nercl.music.cloud.service.AnswerRecordMaluationService;
import com.nercl.music.constant.CList;

@RestController
public class MaluationController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private AnswerRecordMaluationService answerRecordMaluationService;

	@GetMapping(value = "/maluations", produces = JSON_PRODUCES)
	public Map<String, Object> getMaluations(@RequestParam(value = "question_type") String questionType) {
		Map<String, Object> ret = Maps.newHashMap();
		boolean correct = Arrays.stream(QuestionType.values())
				.anyMatch(type -> String.valueOf(type).equals(questionType));
		if (!correct) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "questionType is error");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("question_type", questionType);
		List<Maluation> ms = Arrays.stream(Maluation.values())
				.filter(m -> String.valueOf(m.getQuestionType()).equals(questionType)).collect(Collectors.toList());
		if (null == ms) {
			return ret;
		}
		List<Map<String, Object>> maluations = Lists.newArrayList();
		ms.forEach(m -> {
			Map<String, Object> maluation = Maps.newHashMap();
			maluation.put("title", m.getTitle());
			maluation.put("score", m.getScore());
			maluations.add(maluation);
			List<Map<String, Object>> options = Arrays.stream(MaluationOption.values())
					.filter(mo -> mo.getMaluation().equals(m)).map(mo -> {
						Map<String, Object> option = Maps.newHashMap();
						option.put("title", mo.getTitle());
						option.put("option", mo);
						option.put("low_score", mo.getLowScore());
						option.put("high_score", mo.getHighScore());
						return option;
					}).collect(Collectors.toList());
			maluation.put("options", options);
		});
		ret.put("maluations", maluations);
		return ret;
	}

	@PostMapping(value = "/maluation", produces = JSON_PRODUCES)
	public Map<String, Object> add(String json) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(json)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "json is null");
			return ret;
		}
		answerRecordMaluationService.save(json);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@GetMapping(value = "/maluation", produces = JSON_PRODUCES)
	public Map<String, Object> getMaluationScore(String rid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(rid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rid is null");
			return ret;
		}
		List<AnswerRecordMaluation> arms = answerRecordMaluationService.getByRecord(rid);
		ret.put("code", CList.Api.Client.OK);
		if (null == arms || arms.isEmpty()) {
			return ret;
		}
		List<Map<String, Object>> options = arms.stream().map(arm -> {
			Map<String, Object> option = Maps.newHashMap();
			option.put("option", arm.getMaluationOption());
			option.put("score", arm.getScore());
			return option;
		}).collect(Collectors.toList());
		ret.put("options", options);
		return ret;
	}

}
