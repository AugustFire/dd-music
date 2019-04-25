package com.nercl.music.cloud.dao;

import com.nercl.music.cloud.entity.ability.Statistic;

public interface StatisticDao extends BaseDao<Statistic, String> {

	Statistic getClassKnowledgeStatistics(String knowledge, String classId, long end);

	Statistic getClassCompositeAbilityStatistics(String compositeAbility, String classId, long end);

	Statistic getGradeCompositeAbilityStatistics(String compositeAbility, String gid, String sid, long end);

	Statistic getGradeKnowledgeStatistics(String knowledge, String gid, String sid, long end);

	Statistic getSchoolCompositeAbilityStatistics(String compositeAbility, String sid, long end);

	Statistic getSchoolKnowledgeStatistics(String knowledge, String sid, long end);

}
