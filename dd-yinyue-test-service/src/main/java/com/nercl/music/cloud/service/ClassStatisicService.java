package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.CompositeAbility;

public interface ClassStatisicService {

	Map<String, Integer> getClassMusicAbilityTendency(String classId, long start, long end);

	Map<String, Object> getClassAbilityStatistics(String classId, long start, long end);

	Map<String, Integer> getClassSubAbilityStatistics(String cid, CompositeAbility ca, long start, long end);

	Map<String, Object> getClassKnowledgeStatistics(String classId, long start, long end);

	Map<String, Integer> getClassSubKnowledgeStatistics(String cid, String knowledge, long start, long end);

	Map<String, Map<String, Integer>> getClassMusicAbilityTendency(String[] cids, long start, long end);

	Map<String, Object> getClassesAbilityStatistics(String[] cids, long start, long end);

	Map<String, Integer> getClassesMusicAbility(String[] cids, long start, long end);

	Map<String, Object> getClassesKnowledgeStatistics(String[] cids, long start, long end);

	List<Map<String, Object>> getQuestionsByAbility(String cid, CompositeAbility ca, AnswerSource source, long start,
			long end);

	List<Map<String, Object>> getQuestionsByKnowledge(String cid, String knowledgeNo, AnswerSource source, long start,
			long end);

	Map<String, Object> getAnswerPeopleAccuracy(String classId, String questionId, long start, long end);

	Map<Object, Object> getQuestionAnswerDetails(String cid, String qid, long start, long end);

	Map<String, Map<String, Object>> getTeacherMusicAbility(String sid, String gid, long start, long end);

	String getClassRanking(String cid, long start, long end);
}
