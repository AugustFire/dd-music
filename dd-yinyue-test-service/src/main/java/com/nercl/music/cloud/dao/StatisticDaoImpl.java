package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.nercl.music.cloud.entity.ability.Statistic;

@Repository
public class StatisticDaoImpl extends AbstractBaseDaoImpl<Statistic, String> implements StatisticDao {

	@Override
	public Statistic getClassKnowledgeStatistics(String knowledge, String classId, long end) {
		String jpql = null;
		List<Statistic> statistics = null;
		if (Strings.isNullOrEmpty(knowledge)) {
			jpql = "from ClassKnowledgeStatistic ar where ar.classId = ?1 and ar.endAt <= ?2 order by ar.endAt desc";
			statistics = this.executeQueryWithoutPaging(jpql, classId, end);
			return null == statistics || statistics.isEmpty() ? null : statistics.get(0);
		} else {
			jpql = "from ClassKnowledgeStatistic ar where ar.knowledgeNo = ?1 and ar.classId = ?2 and ar.endAt <= ?3 order by ar.endAt desc";
			statistics = this.executeQueryWithoutPaging(jpql, knowledge, classId, end);
			return null == statistics || statistics.isEmpty() ? null : statistics.get(0);
		}
	}

	@Override
	public Statistic getClassCompositeAbilityStatistics(String compositeAbility, String classId, long end) {
		String jpql = null;
		List<Statistic> statistics = null;
		if (Strings.isNullOrEmpty(compositeAbility)) {
			jpql = "from ClassCompositeAbilityStatistic ar where ar.classId = ?1 and ar.endAt <= ?2 order by ar.endAt desc";
			statistics = this.executeQueryWithoutPaging(jpql, classId, end);
			return null == statistics || statistics.isEmpty() ? null : statistics.get(0);
		} else {
			jpql = "from ClassCompositeAbilityStatistic ar where ar.compositeAbility = ?1 and ar.classId = ?2 and ar.endAt <= ?3 order by ar.endAt desc";
			statistics = this.executeQueryWithoutPaging(jpql, compositeAbility, classId, end);
			return null == statistics || statistics.isEmpty() ? null : statistics.get(0);
		}
	}

	@Override
	public Statistic getGradeCompositeAbilityStatistics(String compositeAbility, String gid, String sid, long end) {
		String jpql = null;
		List<Statistic> statistics = null;
		if (Strings.isNullOrEmpty(compositeAbility)) {
			jpql = "from GradeCompositeAbilityStatistic ar where ar.gradeId = ?1 and ar.schoolId = ?2 and ar.endAt <= ?3 order by ar.endAt desc";
			statistics = this.executeQueryWithoutPaging(jpql, gid, sid, end);
			return null == statistics || statistics.isEmpty() ? null : statistics.get(0);
		} else {
			jpql = "from GradeCompositeAbilityStatistic ar where ar.compositeAbility = ?1 and ar.gradeId = ?2 and ar.schoolId = ?3 and ar.endAt <= ?4 order by ar.endAt desc";
			statistics = this.executeQueryWithoutPaging(jpql, compositeAbility, gid, sid, end);
			return null == statistics || statistics.isEmpty() ? null : statistics.get(0);
		}
	}

	@Override
	public Statistic getGradeKnowledgeStatistics(String knowledge, String gid, String sid, long end) {
		String jpql = null;
		List<Statistic> statistics = null;
		if (Strings.isNullOrEmpty(knowledge)) {
			jpql = "from GradeKnowledgeStatistic ar where ar.gradeId = ?1 and ar.schoolId = ?2 and ar.endAt <= ?3 order by ar.endAt desc";
			statistics = this.executeQueryWithoutPaging(jpql, gid, sid, end);
			return null == statistics || statistics.isEmpty() ? null : statistics.get(0);
		} else {
			jpql = "from GradeKnowledgeStatistic ar where ar.knowledgeNo = ?1 and ar.gradeId = ?2 and ar.schoolId = ?3 and ar.endAt <= ?4 order by ar.endAt desc";
			statistics = this.executeQueryWithoutPaging(jpql, knowledge, gid, sid, end);
			return null == statistics || statistics.isEmpty() ? null : statistics.get(0);
		}
	}

	@Override
	public Statistic getSchoolCompositeAbilityStatistics(String compositeAbility, String sid, long end) {
		String jpql = null;
		List<Statistic> statistics = null;
		if (Strings.isNullOrEmpty(compositeAbility)) {
			jpql = "from SchoolCompositeAbilityStatistic ar where ar.schoolId = ?1 and ar.endAt <= ?2 order by ar.endAt desc";
			statistics = this.executeQueryWithoutPaging(jpql, sid, end);
			return null == statistics || statistics.isEmpty() ? null : statistics.get(0);
		} else {
			jpql = "from SchoolCompositeAbilityStatistic ar where ar.compositeAbility = ?1 and ar.schoolId = ?2 and ar.endAt <= ?3 order by ar.endAt desc";
			statistics = this.executeQueryWithoutPaging(jpql, compositeAbility, sid, end);
			return null == statistics || statistics.isEmpty() ? null : statistics.get(0);
		}
	}

	@Override
	public Statistic getSchoolKnowledgeStatistics(String knowledge, String sid, long end) {
		String jpql = null;
		List<Statistic> statistics = null;
		if (Strings.isNullOrEmpty(knowledge)) {
			jpql = "from SchoolKnowledgeStatistic ar where ar.schoolId = ?1 and ar.endAt <= ?2 order by ar.endAt desc";
			statistics = this.executeQueryWithoutPaging(jpql, sid, end);
			return null == statistics || statistics.isEmpty() ? null : statistics.get(0);
		} else {
			jpql = "from SchoolKnowledgeStatistic ar where ar.knowledgeNo = ?1 and ar.schoolId = ?2 and ar.endAt <= ?3 order by ar.endAt desc";
			statistics = this.executeQueryWithoutPaging(jpql, knowledge, sid, end);
			return null == statistics || statistics.isEmpty() ? null : statistics.get(0);
		}
	}

}
