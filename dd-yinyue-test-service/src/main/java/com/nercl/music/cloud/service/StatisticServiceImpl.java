package com.nercl.music.cloud.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.dao.StatisticDao;
import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.CompositeAbility;
import com.nercl.music.cloud.entity.Knowledge;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.ability.ClassCompositeAbilityStatistic;
import com.nercl.music.cloud.entity.ability.ClassKnowledgeStatistic;
import com.nercl.music.cloud.entity.ability.GradeCompositeAbilityStatistic;
import com.nercl.music.cloud.entity.ability.GradeKnowledgeStatistic;
import com.nercl.music.cloud.entity.ability.SchoolCompositeAbilityStatistic;
import com.nercl.music.cloud.entity.ability.SchoolKnowledgeStatistic;
import com.nercl.music.cloud.entity.ability.Statistic;
import com.nercl.music.util.GraspValueUtil;

@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private StatisticDao statisticDao;

	@Autowired
	private GraspValueUtil graspValueUtil;

	private final static long TWO_WEEK_MILLIS = 2 * 7 * 24 * 3600 * 1000;

	@Override
	public boolean save(Statistic statistic) {
		statisticDao.save(statistic);
		return !Strings.isNullOrEmpty(statistic.getId());
	}

	@Override
	public Map<String, Float> getGradeMusicAbilityTendency(String gradeId, String schoolId, long start, long end) {
		Map<String, Float> result = Maps.newLinkedHashMap();
		int slice = (int) ((end - start) / TWO_WEEK_MILLIS);
		slice = slice < 1 ? 1 : slice;
		IntStream.range(1, slice + 2).forEach(t -> {
			long begin = start + t * TWO_WEEK_MILLIS;
			Float grasp = 0F;
			Statistic ksta = getGradeKnowledgeStatistics(null, gradeId, schoolId, begin);
			Statistic csta = getGradeCompositeAbilityStatistics(null, gradeId, schoolId, begin);
			if (null != ksta && ksta.getStartAt() >= start) {
				grasp = grasp + ksta.getGrasp();
			}
			if (null != csta && csta.getStartAt() >= start) {
				grasp = grasp + csta.getGrasp();
			}
			result.put(String.valueOf(begin), grasp);
		});
		return result;
	}

	@Override
	public Integer getClassStatistics(String classId, long end) {
		Statistic ksta = getClassKnowledgeStatistics(null, classId, end);
		Statistic csta = getClassCompositeAbilityStatistics(null, classId, end);
		Integer value = null != ksta ? ksta.getGrasp() : 0;
		value = null != csta ? csta.getGrasp() : 0;
		return value / 2;
	}

	@Override
	public Integer getGradeStatistics(String gradeId, String schoolId, long end) {
		Statistic ksta = getGradeKnowledgeStatistics(null, gradeId, schoolId, end);
		Statistic csta = getGradeCompositeAbilityStatistics(null, gradeId, schoolId, end);
		Integer value = null != ksta ? ksta.getGrasp() : 0;
		value = null != csta ? csta.getGrasp() : 0;
		return value / 2;
	}

	@Override
	public Statistic getClassKnowledgeStatistics(String knowledge, String classId, long end) {
		return statisticDao.getClassKnowledgeStatistics(knowledge, classId, end);
	}

	@Override
	public Statistic getClassCompositeAbilityStatistics(String compositeAbility, String classId, long end) {
		return statisticDao.getClassCompositeAbilityStatistics(compositeAbility, classId, end);
	}

	@Override
	public Statistic getGradeKnowledgeStatistics(String knowledge, String gradeId, String schoolId, long end) {
		return statisticDao.getGradeKnowledgeStatistics(knowledge, gradeId, schoolId, end);
	}

	@Override
	public Statistic getGradeCompositeAbilityStatistics(String compositeAbility, String gradeId, String schoolId,
			long end) {
		return statisticDao.getGradeCompositeAbilityStatistics(compositeAbility, gradeId, schoolId, end);
	}

	@Override
	public Statistic getSchoolKnowledgeStatistics(String knowledge, String schoolId, long end) {
		return statisticDao.getSchoolKnowledgeStatistics(knowledge, schoolId, end);
	}

	@Override
	public Statistic getSchoolCompositeAbilityStatistics(String compositeAbility, String schoolId, long end) {
		return statisticDao.getSchoolCompositeAbilityStatistics(compositeAbility, schoolId, end);
	}

	@Override
	public void statisticClassMusicAbility(String classId, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getClassRecord(classId, start, end);
		if (null == records || records.isEmpty()) {
			return;
		}
		List<CompositeAbility> parents = Arrays.stream(CompositeAbility.values()).filter(ca -> null == ca.getParent())
				.collect(Collectors.toList());
		parents.stream().forEach(parent -> {
			List<AnswerRecord> rs = records.stream().filter(record -> {
				Question question = record.getQuestion();
				return null != question
						&& Strings.nullToEmpty(question.getCompositeAbilitys()).contains(String.valueOf(parent));
			}).collect(Collectors.toList());
			Integer grasp = graspValueUtil.getGraspValue(rs);
			Statistic statistic = getClassCompositeAbilityStatistics(String.valueOf(parent), classId, start);
			ClassCompositeAbilityStatistic classStatistic = null;
			if (null != statistic && statistic instanceof ClassCompositeAbilityStatistic) {
				ClassCompositeAbilityStatistic ccas = (ClassCompositeAbilityStatistic) statistic;
				grasp = grasp + ccas.getGrasp();
				classStatistic = new ClassCompositeAbilityStatistic(statistic.getStartAt(), end, classId, "", "", "",
						String.valueOf(parent), grasp);
			} else {
				classStatistic = new ClassCompositeAbilityStatistic(start, end, classId, "", "", "",
						String.valueOf(parent), grasp);
			}
			save(classStatistic);
		});

		List<Knowledge> top = knowledgeService.getTop();
		top.stream().forEach(t -> {
			List<AnswerRecord> rs = records.stream().filter(record -> {
				Question question = record.getQuestion();
				return null != question && Strings.nullToEmpty(question.getKnowledges()).contains(t.getNo());
			}).collect(Collectors.toList());
			Integer grasp = graspValueUtil.getGraspValue(rs);
			Statistic statistic = getClassKnowledgeStatistics(t.getNo(), classId, start);
			ClassKnowledgeStatistic classStatistic = null;
			if (null != statistic && statistic instanceof ClassKnowledgeStatistic) {
				ClassKnowledgeStatistic gks = (ClassKnowledgeStatistic) statistic;
				grasp = grasp + gks.getGrasp();
				classStatistic = new ClassKnowledgeStatistic(statistic.getStartAt(), end, classId, "", "", "", "",
						t.getNo(), grasp);
			} else {
				classStatistic = new ClassKnowledgeStatistic(start, end, classId, "", "", "", "", t.getNo(), grasp);
			}
			save(classStatistic);
		});
	}

	@Override
	public void statisticGradeMusicAbility(String gradeId, String schoolId, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getGradeRecord(gradeId, schoolId, start, end);
		if (null == records || records.isEmpty()) {
			return;
		}
		List<CompositeAbility> parents = Arrays.stream(CompositeAbility.values()).filter(ca -> null == ca.getParent())
				.collect(Collectors.toList());
		parents.stream().forEach(parent -> {
			List<AnswerRecord> rs = records.stream().filter(record -> {
				Question question = record.getQuestion();
				return null != question
						&& Strings.nullToEmpty(question.getCompositeAbilitys()).contains(String.valueOf(parent));
			}).collect(Collectors.toList());
			Integer grasp = graspValueUtil.getGraspValue(rs);
			Statistic statistic = getGradeCompositeAbilityStatistics(String.valueOf(parent), gradeId, schoolId, start);
			GradeCompositeAbilityStatistic gradeStatistic = null;
			if (null != statistic && statistic instanceof GradeCompositeAbilityStatistic) {
				GradeCompositeAbilityStatistic gcas = (GradeCompositeAbilityStatistic) statistic;
				grasp = grasp + gcas.getGrasp();
				gradeStatistic = new GradeCompositeAbilityStatistic(statistic.getStartAt(), end, gradeId, "", schoolId,
						"", String.valueOf(parent), grasp);
			} else {
				gradeStatistic = new GradeCompositeAbilityStatistic(start, end, gradeId, "", schoolId, "",
						String.valueOf(parent), grasp);
			}
			save(gradeStatistic);
		});

		List<Knowledge> top = knowledgeService.getTop();
		top.stream().forEach(t -> {
			List<AnswerRecord> rs = records.stream().filter(record -> {
				Question question = record.getQuestion();
				return null != question && Strings.nullToEmpty(question.getKnowledges()).contains(t.getNo());
			}).collect(Collectors.toList());
			Integer grasp = graspValueUtil.getGraspValue(rs);
			Statistic statistic = getGradeKnowledgeStatistics(t.getNo(), gradeId, schoolId, start);
			GradeKnowledgeStatistic gradeStatistic = null;
			if (null != statistic && statistic instanceof GradeKnowledgeStatistic) {
				GradeKnowledgeStatistic gks = (GradeKnowledgeStatistic) statistic;
				grasp = grasp + gks.getGrasp();
				gradeStatistic = new GradeKnowledgeStatistic(statistic.getStartAt(), end, gradeId, "", schoolId, "", "",
						t.getNo(), grasp);
			} else {
				gradeStatistic = new GradeKnowledgeStatistic(start, end, gradeId, "", schoolId, "", "", t.getNo(),
						grasp);
			}
			save(gradeStatistic);
		});

	}

	@Override
	public void statisticSchoolMusicAbility(String schoolId, long start, long end) {
		List<AnswerRecord> records = answerRecordService.getSchoolRecord(schoolId, start, end);
		if (null == records || records.isEmpty()) {
			return;
		}
		List<CompositeAbility> parents = Arrays.stream(CompositeAbility.values()).filter(ca -> null == ca.getParent())
				.collect(Collectors.toList());
		parents.stream().forEach(parent -> {
			List<AnswerRecord> rs = records.stream().filter(record -> {
				Question question = record.getQuestion();
				return null != question
						&& Strings.nullToEmpty(question.getCompositeAbilitys()).contains(String.valueOf(parent));
			}).collect(Collectors.toList());
			Integer grasp = graspValueUtil.getGraspValue(rs);
			Statistic statistic = getSchoolCompositeAbilityStatistics(String.valueOf(parent), schoolId, start);
			SchoolCompositeAbilityStatistic schoolStatistic = null;
			if (null != statistic && statistic instanceof SchoolCompositeAbilityStatistic) {
				SchoolCompositeAbilityStatistic scas = (SchoolCompositeAbilityStatistic) statistic;
				grasp = grasp + scas.getGrasp();
				schoolStatistic = new SchoolCompositeAbilityStatistic(statistic.getStartAt(), end, schoolId, "",
						String.valueOf(parent), grasp);
			} else {
				schoolStatistic = new SchoolCompositeAbilityStatistic(start, end, schoolId, "", String.valueOf(parent),
						grasp);
			}
			save(schoolStatistic);
		});

		List<Knowledge> top = knowledgeService.getTop();
		top.stream().forEach(t -> {
			List<AnswerRecord> rs = records.stream().filter(record -> {
				Question question = record.getQuestion();
				return null != question && Strings.nullToEmpty(question.getKnowledges()).contains(t.getNo());
			}).collect(Collectors.toList());
			Integer grasp = graspValueUtil.getGraspValue(rs);
			Statistic statistic = getSchoolKnowledgeStatistics(t.getNo(), schoolId, start);
			SchoolKnowledgeStatistic schoolStatistic = null;
			if (null != statistic && statistic instanceof SchoolKnowledgeStatistic) {
				SchoolKnowledgeStatistic gks = (SchoolKnowledgeStatistic) statistic;
				grasp = grasp + gks.getGrasp();
				schoolStatistic = new SchoolKnowledgeStatistic(statistic.getStartAt(), end, schoolId, "", "", t.getNo(),
						grasp);
			} else {
				schoolStatistic = new SchoolKnowledgeStatistic(start, end, schoolId, "", "", t.getNo(), grasp);
			}
			save(schoolStatistic);
		});
	}

}
