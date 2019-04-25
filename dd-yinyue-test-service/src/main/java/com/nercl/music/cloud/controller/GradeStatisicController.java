package com.nercl.music.cloud.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.CompositeAbility;
import com.nercl.music.cloud.entity.Knowledge;
import com.nercl.music.cloud.service.KnowledgeService;
import com.nercl.music.cloud.service.StatisticService2;
import com.nercl.music.constant.CList;
import com.nercl.music.util.GraspValueUtil;

@RestController
public class GradeStatisicController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private StatisticService2 statisticService;

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private GraspValueUtil graspValueUtil;

	@GetMapping(value = "/v2/grade/music/tendency", produces = JSON_PRODUCES)
	public Map<String, Object> getGradeMusicTendency(String sid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Object> result = statisticService.getGradeMusicTendency(sid, gid, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("sid", sid);
		ret.put("gid", gid);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		return ret;
	}

	@GetMapping(value = "/v2/grade/ability/statistics", produces = JSON_PRODUCES)
	public Map<String, Object> getGradeAbilityAverageScore(String sid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Object> result = statisticService.getGradeAbilityAverageScore(sid, gid, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("gid", gid);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		ret.put("abilitys", getParentCompositeAbility());
		return ret;
	}

	@GetMapping(value = "/v2/grade/knowledge/statistics", produces = JSON_PRODUCES)
	public Map<String, Object> getGradeKnowledgeAverageScore(String sid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Object> result = statisticService.getGradeKnowledgeAverageScore(sid, gid, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("gid", gid);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		ret.put("knowledges", getParentKnowledges());
		return ret;
	}

	@GetMapping(value = "/v2/grade/exam/stat_data", produces = JSON_PRODUCES)
	public Map<String, Object> getGradeMusicExamStatData(String sid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<Map<String, Object>> result = statisticService.getGradeMusicExamStatData(sid, gid, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("sid", sid);
		ret.put("gid", gid);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		return ret;
	}

	@GetMapping(value = "/v2/teacher/basic_data", produces = JSON_PRODUCES)
	public Map<String, Object> getTeacherClassBasicData(String sid, String gcode) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gcode)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gcode is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<Map<String, Object>> teacherClassBasicData = statisticService.getTeacherClassBasicData(sid, gcode, start,
				end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("sid", sid);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("teacher_class_basic_data", teacherClassBasicData);
		return ret;
	}

	@GetMapping(value = "/v2/teacher/middle_term_exam/stat_data", produces = JSON_PRODUCES)
	public Map<String, Object> getTeacherMusicMiddleExamStatData(String sid, String gcode) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gcode)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gcode is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<Map<String, Object>> teacherClassExamData = statisticService.getTeacherMusicMiddleExamStatData(sid, gcode,
				start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("sid", sid);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("teacher_class_exam_data", teacherClassExamData);
		return ret;
	}

	@GetMapping(value = "/v2/teacher/final_term_exam/stat_data", produces = JSON_PRODUCES)
	public Map<String, Object> getTeacherMusicFinalExamStatData(String sid, String gcode) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gcode)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gcode is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<Map<String, Object>> teacherClassExamData = statisticService.getTeacherMusicMiddleExamStatData(sid, gcode,
				start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("sid", sid);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("teacher_class_exam_data", teacherClassExamData);
		return ret;
	}

	@GetMapping(value = "/v2/teacher/class/music/stat_data", produces = JSON_PRODUCES)
	public Map<String, Object> getTeacherClassMusicStatData(String sid, String gcode) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gcode)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gcode is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<Map<String, Object>> teacherClassMusicData = statisticService.getTeacherClassMusicStatData(sid, gcode,
				start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("sid", sid);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("teacher_class_music_data", teacherClassMusicData);
		return ret;
	}

	private Map<String, String> getParentCompositeAbility() {
		Map<String, String> abilitys = Maps.newHashMap();
		Arrays.stream(CompositeAbility.values()).filter(ca -> null == ca.getParent()).forEach(ca -> {
			abilitys.put(ca.toString(), ca.getTitle());
		});
		return abilitys;
	}

	private Map<String, String> getParentKnowledges() {
		Map<String, String> knowledges = Maps.newHashMap();
		List<Knowledge> ks = knowledgeService.getTop();
		ks.forEach(k -> {
			knowledges.put(k.getNo(), k.getTitle());
		});
		return knowledges;
	}

}
