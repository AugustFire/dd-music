package com.nercl.music.cloud.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.CompositeAbility;
import com.nercl.music.cloud.entity.Knowledge;
import com.nercl.music.cloud.service.ClassStatisicService;
import com.nercl.music.cloud.service.KnowledgeService;
import com.nercl.music.cloud.service.UserStatisicService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;
import com.nercl.music.util.GraspValueUtil;

@RestController
public class UserStatisicController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private UserStatisicService userStatisicService;

	@Autowired
	private ClassStatisicService classStatisicService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private GraspValueUtil graspValueUtil;

	@GetMapping(value = "/user/music_ability/tendency", params = { "uid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getUserMusicAbilityTendency(@RequestParam(value = "uid") String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		String classId = getClass(uid);
		if (Strings.isNullOrEmpty(classId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classId is null");
			return ret;
		}
		Map<String, Integer> userResult = userStatisicService.getUserMusicAbility(uid, start, end);
		Map<String, Integer> classResult = classStatisicService.getClassMusicAbilityTendency(classId, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("uid", uid);
		ret.put("name", getUserName(uid));
		ret.put("start", start);
		ret.put("end", end);
		ret.put("user_result", userResult);
		ret.put("class_result", classResult);
		return ret;
	}

	@SuppressWarnings("unchecked")
	private String getClass(String uid) {
		Map<String, Object> classes = restTemplate.getForObject(ApiClient.GET_CLASS_USER, Map.class, uid);
		if (null == classes || null == classes.get("classes")) {
			return null;
		}
		List<Map<String, String>> cls = (List<Map<String, String>>) classes.get("classes");
		String cid = (String) cls.get(0).getOrDefault("class_id", "");
		return cid;
	}

	/**
	 * 音乐知识总览图
	 */
	@GetMapping(value = "/user/knowledge/statistics", params = { "uid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getUserKnowledgeStatistics(@RequestParam(value = "uid") String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Object> result = userStatisicService.getUserKnowledgeStatistics(uid, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("uid", uid);
		ret.put("name", getUserName(uid));
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		return ret;
	}

	@GetMapping(value = "/user/sub_knowledge/statistics", params = { "uid", "knowledge_no" }, produces = JSON_PRODUCES)
	public Map<String, Object> getUserSecondLevlKnowledgeanalysis(@RequestParam(value = "uid") String uid,
			@RequestParam(value = "knowledge_no") String knowledgeNo) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(knowledgeNo)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "knowledgeNo is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Integer> result = userStatisicService.getUserSubKnowledgeStatistics(uid, knowledgeNo, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("uid", uid);
		ret.put("name", getUserName(uid));
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		return ret;
	}

	@GetMapping(value = "/user/ability/statistics", params = { "uid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getUserAbilityStatistics(@RequestParam(value = "uid") String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Object> result = userStatisicService.getUserAbilityStatistics(uid, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("uid", uid);
		ret.put("name", getUserName(uid));
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		Map<String, Object> abilitys = Maps.newHashMap();
		Arrays.stream(CompositeAbility.values()).filter(ca -> null == ca.getParent()).forEach(ca -> {
			abilitys.put(ca.toString(), ca.getTitle());
		});
		ret.put("abilitys", abilitys);
		return ret;
	}

	@GetMapping(value = "/user/sub_ability/statistics", params = { "uid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getUserSubAbilityStatistics(@RequestParam(value = "uid") String uid, String ca) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		if (!CompositeAbility.isDefined(ca)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no enum constent:" + ca);
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Integer> result = userStatisicService.getUserSubAbilityStatistics(uid, CompositeAbility.valueOf(ca),
				start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("uid", uid);
		ret.put("name", getUserName(uid));
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		Map<String, Object> abilitys = Maps.newHashMap();
		Arrays.stream(CompositeAbility.values()).forEach(cc -> {
			abilitys.put(cc.toString(), cc.getTitle());
		});
		ret.put("abilitys", abilitys);
		return ret;
	}

	@GetMapping(value = "/user/error_questions", params = { "uid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getUserErrorQuestions(@RequestParam(value = "uid") String uid, String source,
			String skill, String knowledge) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		AnswerSource as = null;
		if (!Strings.isNullOrEmpty(source) && !AnswerSource.isDefined(source)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "source is error");
			return ret;
		}
		if (!Strings.isNullOrEmpty(source)) {
			as = AnswerSource.valueOf(source);
		}
		CompositeAbility ca = null;
		if (!Strings.isNullOrEmpty(skill) && !CompositeAbility.isDefined(skill)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "skill is error");
			return ret;
		}
		if (!Strings.isNullOrEmpty(skill)) {
			ca = CompositeAbility.valueOf(skill);
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<Map<String, Object>> result = userStatisicService.getErrorQuestions(uid, as, ca, knowledge, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("uid", uid);
		ret.put("name", getUserName(uid));
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		return ret;
	}

	@GetMapping(value = "/user/ability_questions", produces = JSON_PRODUCES)
	public Map<String, Object> getQuestionsByAbility(String uid, String source, String ca) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		if (!Strings.isNullOrEmpty(source) && !AnswerSource.isDefined(source)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "source is error");
			return ret;
		}
		AnswerSource as = Strings.isNullOrEmpty(source) ? null : AnswerSource.valueOf(source);
		if (!CompositeAbility.isDefined(ca)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "ca is error");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<Map<String, Object>> questions = userStatisicService.getQuestionsByAbility(uid,
				CompositeAbility.valueOf(ca), as, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("questions", questions);
		return ret;
	}

	@GetMapping(value = "/user/knowledge_questions", produces = JSON_PRODUCES)
	public Map<String, Object> getQuestionsByKnowledge(String uid, String source, String knowledgeNo) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		if (!AnswerSource.isDefined(source)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "source is error");
			return ret;
		}
		AnswerSource as = AnswerSource.valueOf(source);
		if (Strings.isNullOrEmpty(knowledgeNo)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "knowledgeNo is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<Map<String, Object>> questions = userStatisicService.getQuestionsByKnowledge(uid, knowledgeNo, as, start,
				end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("questions", questions);
		return ret;
	}

	@SuppressWarnings("unchecked")
	private String getUserName(String uid) {
		Map<String, Object> user = restTemplate.getForObject(ApiClient.GET_USER2, Map.class, uid);
		if (null == user || null == user.get("name")) {
			return "";
		}
		// Map<String, Object> person = (Map<String, Object>) user.get("name");
		return (String) user.getOrDefault("name", "");
	}

	/**
	 * 一级知识点
	 */
	@GetMapping(value = "/knowledge/top", produces = JSON_PRODUCES)
	public Map<String, Object> getTopKnowledge() {
		Map<String, Object> ret = Maps.newHashMap();
		List<Knowledge> top = knowledgeService.getTop();
		ret.put("code", CList.Api.Client.OK);
		ret.put("top", top);
		return ret;
	}

	/**
	 * 二级知识点
	 */
	@GetMapping(value = "/knowledge/second_level", produces = JSON_PRODUCES)
	public Map<String, Object> getSecondLevelKnowledge(@RequestParam(value = "knowledge_no") String knowledgeNo) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(knowledgeNo)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "knowledge_no is null");
			return ret;
		}
		List<Knowledge> second = knowledgeService.getChildren(knowledgeNo);
		ret.put("code", CList.Api.Client.OK);
		ret.put("second_level", second);
		return ret;
	}

	@GetMapping(value = "/user/ranking", params = { "uid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getUserRanking(@RequestParam(value = "uid") String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		String ranking = userStatisicService.getUserRanking(uid, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("uid", uid);
		ret.put("name", getUserName(uid));
		ret.put("start", start);
		ret.put("end", end);
		ret.put("ranking", ranking);
		return ret;
	}
}
