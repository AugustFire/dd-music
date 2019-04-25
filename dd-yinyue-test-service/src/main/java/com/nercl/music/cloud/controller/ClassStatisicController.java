package com.nercl.music.cloud.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.CompositeAbility;
import com.nercl.music.cloud.entity.Knowledge;
import com.nercl.music.cloud.entity.QuestionType;
import com.nercl.music.cloud.service.ClassStatisicService2;
import com.nercl.music.cloud.service.KnowledgeService;
import com.nercl.music.constant.CList;
import com.nercl.music.util.GraspValueUtil;

@RestController
public class ClassStatisicController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ClassStatisicService2 classStatisicService;

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private GraspValueUtil graspValueUtil;

	/**
	 * 获取某个班级的音乐素养走势分析
	 * @param cid
	 * @return
	 */
	@GetMapping(value = "/v2/class/music/tendency", produces = JSON_PRODUCES)
	public Map<String, Object> getClassMusicTendency(String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Object> result = classStatisicService.getClassMusicTendency(cid, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("cid", cid);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		return ret;
	}

	/**
	 * 获取某若干个班级的统计数据（综合音乐素养）
	 * @param cids
	 * @return
	 */
	@GetMapping(value = "/v2/classes/music/statistics", produces = JSON_PRODUCES)
	public Map<String, Object> getClassesMusicStatistics(String[] cids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == cids || cids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cids is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Object> result = classStatisicService.getClassesMusicAbility(cids, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("cids", Joiner.on(",").join(cids));
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		return ret;
	}

	/**
	 * 获取某若干个班级的统计数据（按音乐能力维度）
	 * @param cids
	 * @return
	 */
	@GetMapping(value = "/v2/classes/ability/statistics", produces = JSON_PRODUCES)
	public Map<String, Object> getClassesAbilityStatistics(String[] cids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == cids || cids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cids is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Object> result = classStatisicService.getClassesAbilityStatistics(cids, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("cids", Joiner.on(",").join(cids));
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		ret.put("abilitys", getParentCompositeAbility());
		return ret;
	}

	/**
	 * 获取某若干个班级的统计数据（按音乐知识维度）
	 * @param cids
	 * @return
	 */
	@GetMapping(value = "/v2/classes/knowledge/statistics", produces = JSON_PRODUCES)
	public Map<String, Object> getClassesKnowledgeStatistics(String[] cids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == cids || cids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cids is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Object> result = classStatisicService.getClassesKnowledgeStatistics(cids, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("cids", Joiner.on(",").join(cids));
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		ret.put("knowledges", getParentKnowledges());
		return ret;
	}

	/**
	 * 获取某个班级下所有的学生及音乐素养分数
	 * @param cid
	 * @return
	 */
	@GetMapping(value = "/v2/class/students_music_score", produces = JSON_PRODUCES)
	public Map<String, Object> getClassStudentsMusicScore(String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<Map<String, Object>> result = classStatisicService.getClassStudentsMusicScore(cid, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("cid", cid);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		return ret;
	}

	/**
	 * 获取某个班级的考试数据分析
	 * @param cid
	 * @return
	 */
	@GetMapping(value = "/v2/class/exam/stat_data", produces = JSON_PRODUCES)
	public Map<String, Object> getClassMusicExamStatData(String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<Map<String, Object>> result = classStatisicService.getClassMusicExamStatData(cid, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("cid", cid);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		return ret;
	}

	/**
	 * 获取某个班级中的所有学生在某张考卷下的分数
	 * @param cid
	 * @param eid
	 * @return
	 */
	@GetMapping(value = "/v2/class/exam/user_score", produces = JSON_PRODUCES)
	public Map<String, Object> getExamUserScore(String cid, String eid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(eid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "eid is null");
			return ret;
		}
		List<Map<String, Object>> result = classStatisicService.getExamUserScore(cid, eid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("cid", cid);
		ret.put("eid", eid);
		ret.put("result", result);
		return ret;
	}

	/**
	 * 获取某个班级的作业、练习、测试题目分布情况
	 * @param cid
	 * @return
	 */
	@GetMapping(value = "/v2/class/question/stat_data", produces = JSON_PRODUCES)
	public Map<String, Object> getQuestionStatData(String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Object> result = classStatisicService.getQuestionStatData(cid, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("cid", cid);
		ret.put("result", result);
		ret.put("abilitys", getParentCompositeAbility());
		return ret;
	}

	/**
	 * 获取某个班级的表演题的音高音长分析数据
	 * @param cid
	 * @return
	 */
	@GetMapping(value = "/v2/class/act_question/note/stat_data", produces = JSON_PRODUCES)
	public Map<String, Object> getActQuestionNoteStatData(String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Object> result = classStatisicService.getActQuestionNoteStatData(cid, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("cid", cid);
		ret.put("result", result);
		ret.put("act_types", actTypes());
		return ret;
	}

	/**
	 * 获取某个班级、某个测试来源、某个表演类型下的歌曲/曲目
	 * @param cid
	 * @param question_type
	 * @param answer_source
	 * @return
	 */
	@GetMapping(value = "/v2/class/act_songs", produces = JSON_PRODUCES)
	public Map<String, Object> getActSongs(String cid, String question_type, String answer_source) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		boolean valid = String.valueOf(QuestionType.SING).equals(question_type)
				|| String.valueOf(QuestionType.BEHIND_BACK_SING).equals(question_type)
				|| String.valueOf(QuestionType.BEHIND_BACK_PERFORMANCE).equals(question_type)
				|| String.valueOf(QuestionType.PERFORMANCE).equals(question_type)
				|| String.valueOf(QuestionType.SIGHT_SINGING).equals(question_type);
		if (!valid) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "question_type is wrong");
			return ret;
		}
		if (!AnswerSource.isDefined(answer_source)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "answer_source is wrong");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<Map<String, Object>> songs = classStatisicService.getActSongs(cid, question_type, answer_source, start,
				end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("cid", cid);
		ret.put("question_type", question_type);
		ret.put("answer_source", answer_source);
		ret.put("songs", songs);
		return ret;
	}

	/**
	 * 获取某个班级、某个测试来源、某个歌曲/曲目的所有演唱学生
	 * @param cid
	 * @param sid
	 * @param answer_source
	 * @return
	 */
	@GetMapping(value = "/v2/class/act_students", produces = JSON_PRODUCES)
	public Map<String, Object> getActStudents(String cid, String sid, String answer_source) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (AnswerSource.isDefined(answer_source)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "answer_source is wrong");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<Map<String, Object>> songs = classStatisicService.getActStudents(cid, sid, answer_source, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("cid", cid);
		ret.put("sid", sid);
		ret.put("answer_source", answer_source);
		ret.put("songs", songs);
		return ret;
	}

	/**
	 * 获取某个班级、某个学生、某个歌曲/曲目的演唱分析热力图数据
	 * @param cid
	 * @param sid
	 * @param uid
	 * @return
	 */
	@GetMapping(value = "/v2/class/act_question/hot_chart", params = { "cid", "sid", "uid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getHotChart(String cid, String sid, String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Object> result = classStatisicService.getHotChart(cid, sid, uid, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("cid", cid);
		ret.put("sid", sid);
		ret.put("uid", uid);
		ret.put("all_notes", getAllNotes());
		ret.put("result", result);
		return ret;
	}
	/**
	 * 获取某个班级的某个子综合能力的分析数据
	 * @param cid
	 * @param ca
	 * @return
	 */

	@GetMapping(value = "/v2/class/sub_ability/statistics", produces = JSON_PRODUCES)
	public Map<String, Object> getClassSubAbilityStatistics(String cid, String ca) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		if (!CompositeAbility.isDefined(ca)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "ca is error");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Integer> result = classStatisicService.getClassSubAbilityStatistics(cid,
				CompositeAbility.valueOf(ca), start, end);
		ret.put("code", CList.Api.Client.OK);
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

	/**
	 * 获取某个班级的某个子音乐知识的分析数据
	 * @param cid
	 * @param knowledge
	 * @return
	 */
	@GetMapping(value = "/v2/class/sub_knowledge/statistics", produces = JSON_PRODUCES)
	public Map<String, Object> getClassSubKnowledgeStatistics(String cid, String knowledge) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(knowledge)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "knowledge is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		Map<String, Integer> result = classStatisicService.getClassSubKnowledgeStatistics(cid, knowledge, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("result", result);
		return ret;
	}

	/**
	 * 根据班级、试题来源、次级综合能力获取试题
	 * @param cid
	 * @param source
	 * @param ca
	 * @return
	 */
	@GetMapping(value = "/v2/class/ability_questions", produces = JSON_PRODUCES)
	public Map<String, Object> getQuestionsByAbility(String cid, String source, String ca) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
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
		if (!CompositeAbility.isDefined(ca)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "ca is error");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<Map<String, Object>> questions = classStatisicService.getQuestionsByAbility(cid,
				CompositeAbility.valueOf(ca), as, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("questions", questions);
		return ret;
	}

	/**
	 * 根据班级、试题来源、次级音乐知识获取试题
	 * @param cid
	 * @param source
	 * @param knowledgeNo
	 * @return
	 */
	@GetMapping(value = "/v2/class/knowledge_questions", produces = JSON_PRODUCES)
	public Map<String, Object> getQuestionsByKnowledge(String cid, String source, String knowledgeNo) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
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
		List<Map<String, Object>> questions = classStatisicService.getQuestionsByKnowledge(cid, knowledgeNo, as, start,
				end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("start", start);
		ret.put("end", end);
		ret.put("questions", questions);
		return ret;
	}

	/***
	 * 统计某个题目在某个班级的作答人数及正确率
	 * @param cid
	 * @param qid
	 * @return
	 */
	@GetMapping(value = "/v2/class/answer_people_accuracy", produces = JSON_PRODUCES)
	public Map<String, Object> getAnswerPeopleAccuracy(String cid, String qid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(qid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "qid is null");
			return ret;
		}
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		if (null == start || null == end) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "start or end is null");
			return ret;
		}
		Map<String, Object> result = classStatisicService.getAnswerPeopleAccuracy(cid, qid, start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("cid", cid);
		ret.put("start", start);
		ret.put("end", end);
		ret.putAll(result);
		return ret;
	}

	private Map<String, String> getParentKnowledges() {
		Map<String, String> knowledges = Maps.newHashMap();
		List<Knowledge> ks = knowledgeService.getTop();
		ks.forEach(k -> {
			knowledges.put(k.getNo(), k.getTitle());
		});
		return knowledges;
	}

	private Map<String, String> getParentCompositeAbility() {
		Map<String, String> abilitys = Maps.newHashMap();
		Arrays.stream(CompositeAbility.values()).filter(ca -> null == ca.getParent()).forEach(ca -> {
			abilitys.put(ca.toString(), ca.getTitle());
		});
		return abilitys;
	}

	private Map<String, Object> actTypes() {
		Map<String, Object> actTypes = Maps.newHashMap();
		actTypes.put("SING", "演唱");
		actTypes.put("SIGHT_SINGING", "视唱");
		actTypes.put("BEHIND_BACK_SING", "背唱");
		actTypes.put("BEHIND_BACK_PERFORMANCE", "背奏");
		actTypes.put("PERFORMANCE", "演奏");
		return actTypes;
	}

	private List<String> getAllNotes() {
		return Lists.newArrayList("C3", "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3", "C4",
				"C#4", "D4", "D#4", "E4", "F4", "F#4", "G4", "G#4", "A4", "A#4", "B4", "C5", "C#5", "D5", "D#5", "E5",
				"F5", "F#5", "G5", "G#5", "A5", "A#5", "B5", "C6", "C#6", "D6", "D#6", "E6", "F6", "F#6", "G6", "G#6",
				"A6", "A#6", "B6");
	}
}
