package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.CompositeAbility;

public interface ClassStatisicService2 {

	Map<String, Object> getClassMusicTendency(String classId, long start, long end);

	Map<String, Object> getClassesMusicAbility(String[] cids, long start, long end);

	Map<String, Object> getClassAbilityStatistics(String classId, long start, long end);

	Map<String, Object> getClassesAbilityStatistics(String[] cids, long start, long end);

	Map<String, Object> getClassKnowledgeStatistics(String classId, long start, long end);

	Map<String, Object> getClassesKnowledgeStatistics(String[] cids, long start, long end);

	List<Map<String, Object>> getClassStudentsMusicScore(String cid, long start, long end);

	List<Map<String, Object>> getClassMusicExamStatData(String cid, long start, long end);

	List<Map<String, Object>> getExamUserScore(String cid, String eid);

	Map<String, Object> getQuestionStatData(String cid, long start, long end);

	Map<String, Object> getActQuestionNoteStatData(String cid, long start, long end);

	List<Map<String, Object>> getActSongs(String cid, String question_type, String answer_source, long start, long end);

	List<Map<String, Object>> getActStudents(String cid, String sid, String answer_source, long start, long end);

	Map<String, Object> getHotChart(String cid, String sid, String uid, long start, long end);

	Map<String, Integer> getClassSubAbilityStatistics(String cid, CompositeAbility ca, long start, long end);

	Map<String, Integer> getClassSubKnowledgeStatistics(String cid, String knowledge, long start, long end);

	List<Map<String, Object>> getQuestionsByKnowledge(String cid, String knowledgeNo, AnswerSource source, long start,
			long end);

	List<Map<String, Object>> getQuestionsByAbility(String cid, CompositeAbility ca, AnswerSource source, long start,
			long end);

	Map<String, Object> getAnswerPeopleAccuracy(String classId, String questionId, long start, long end);

}
