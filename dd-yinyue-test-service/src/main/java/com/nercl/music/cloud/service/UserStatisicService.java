package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.CompositeAbility;

public interface UserStatisicService {

	Map<String, Integer> getUserMusicAbility(String userId, long start, long end);

	Map<String, Object> getUserAbilityStatistics(String uid, long start, long end);

	Map<String, Integer> getUserSubAbilityStatistics(String uid, CompositeAbility parent, long start, long end);

	Map<String, Object> getUserKnowledgeStatistics(String uid, long start, long end);

	Map<String, Integer> getUserSubKnowledgeStatistics(String uid, String knowledge, long start, long end);

	List<Map<String, Object>> getErrorQuestions(String uid, AnswerSource source, CompositeAbility ca, String knowledge,
			long start, long end);

	List<Map<String, Object>> getQuestionsByAbility(String uid, CompositeAbility ca, AnswerSource source, long start,
			long end);

	List<Map<String, Object>> getQuestionsByKnowledge(String uid, String knowledgeNo, AnswerSource source, long start,
			long end);

	String getUserRanking(String uid, long start, long end);

}
