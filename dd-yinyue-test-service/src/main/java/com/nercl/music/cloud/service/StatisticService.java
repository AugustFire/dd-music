package com.nercl.music.cloud.service;

import java.util.Map;

import com.nercl.music.cloud.entity.ability.Statistic;

public interface StatisticService {

	boolean save(Statistic statistic);

	Map<String, Float> getGradeMusicAbilityTendency(String gradeId, String schoolId, long start, long end);

	Integer getClassStatistics(String classId, long end);

	Integer getGradeStatistics(String gradeId, String schoolId, long end);

	Statistic getClassKnowledgeStatistics(String knowledge, String classId, long end);

	Statistic getClassCompositeAbilityStatistics(String compositeAbility, String classId, long end);

	Statistic getGradeKnowledgeStatistics(String knowledge, String gradeId, String schoolId, long end);

	Statistic getGradeCompositeAbilityStatistics(String compositeAbility, String gradeId, String schoolId, long end);

	Statistic getSchoolKnowledgeStatistics(String knowledge, String schoolId, long end);

	Statistic getSchoolCompositeAbilityStatistics(String compositeAbility, String schoolId, long end);

	void statisticClassMusicAbility(String classId, long start, long end);

	void statisticGradeMusicAbility(String gradeId, String schoolId, long start, long end);

	void statisticSchoolMusicAbility(String schoolId, long start, long end);

}
