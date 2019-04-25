package com.nercl.music.cloud.controller;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.CompositeAbility;
import com.nercl.music.cloud.entity.Exam;
import com.nercl.music.cloud.entity.Knowledge;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.service.AnswerRecordService;
import com.nercl.music.cloud.service.ExamService;
import com.nercl.music.cloud.service.KnowledgeService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;
import com.nercl.music.util.GraspValueUtil;

@RestController
public class SchoolStatisicController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private GraspValueUtil graspValueUtil;

	@Autowired
	private KnowledgeService knowledgeService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ExamService examService;

	/**
	 * 学校综合素养
	 * 
	 * @param gradeId
	 *            年级id
	 * @param sids
	 *            学校id组
	 */
	@GetMapping(value = "/school/comprehensive/statistics", produces = JSON_PRODUCES)
	public Map<String, Object> getComprehensive(String gradeId, String[] sids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(gradeId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gradeId is null");
			return ret;
		}
		if (null == sids || sids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sids is null");
			return ret;
		}
		List<AnswerRecord> records = answerRecordService.getByGradeAndSchools(gradeId, sids, graspValueUtil.getStart(),
				graspValueUtil.getEnd());
		Map<String, List<AnswerRecord>> answerMap = records.parallelStream().filter(red -> null != red.getIsTrue())
				.collect(Collectors.groupingBy(red -> red.getSchoolId()));
		answerMap.forEach((schoolId, rs) -> {
			ret.put(schoolId, graspValueUtil.getGraspValue(rs));
		});
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 学校基础素养
	 * 
	 * @param gradeId
	 *            年级id
	 * @param sids
	 *            学校id组
	 */
	@GetMapping(value = "/school/knowledge/statistics", produces = JSON_PRODUCES)
	public Map<String, Object> schoolKnowledgeStatistics(String gradeId, String[] sids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(gradeId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gradeId is null");
			return ret;
		}
		if (null == sids || sids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sids is null");
			return ret;
		}
		List<Knowledge> top = knowledgeService.getTop();
		List<AnswerRecord> records = answerRecordService.getByGradeAndSchools(gradeId, sids, graspValueUtil.getStart(),
				graspValueUtil.getEnd());
		Map<String, List<AnswerRecord>> answerMap = records.parallelStream().filter(red -> null != red.getIsTrue())
				.collect(Collectors.groupingBy(red -> red.getSchoolId()));
		answerMap.forEach((schoolId, rds) -> {
			Iterator<Knowledge> iterator = top.iterator();
			while (iterator.hasNext()) {
				Map<String, Object> result = Maps.newHashMap();
				Knowledge knowledge = (Knowledge) iterator.next();
				String key = knowledge.getNo();
				List<AnswerRecord> collect = rds.stream()
						.filter(record -> record.getQuestion().getKnowledges().contains(key))
						.collect(Collectors.toList());
				result.put(key, graspValueUtil.getGraspValue(collect));
				ret.put(schoolId, result);
			}
		});
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 学校能力维度
	 * 
	 * @param gradeId
	 *            年级id
	 * @param sids
	 *            学校id组
	 */
	@GetMapping(value = "/school/ability/statistics", produces = JSON_PRODUCES)
	public Map<String, Object> schoolAbilityStatistics(String gradeId, String[] sids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(gradeId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gradeId is null");
			return ret;
		}
		if (null == sids || sids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sids is null");
			return ret;
		}
		// 查询所有指定年级的答案
		List<AnswerRecord> records = answerRecordService.getByGradeAndSchools(gradeId, sids, graspValueUtil.getStart(),
				graspValueUtil.getEnd());
		// 将答案按学校id分组
		Map<String, List<AnswerRecord>> answerMap = records.parallelStream().filter(red -> null != red.getIsTrue())
				.collect(Collectors.groupingBy(red -> red.getSchoolId()));
		List<CompositeAbility> parents = Arrays.stream(CompositeAbility.values()).filter(ca -> null == ca.getParent())
				.collect(Collectors.toList());
		Map<String, Object> results = Maps.newHashMap();
		answerMap.forEach((schoolId, rds) -> {
			Map<String, Object> result = Maps.newHashMap();
			parents.forEach(parent -> {
				// 在按学校分组的基础上再按能力维度分组并
				List<AnswerRecord> rs = rds.parallelStream().filter(record -> {
					Question question = record.getQuestion();
					return null != question
							&& Strings.nullToEmpty(question.getCompositeAbilitys()).contains(String.valueOf(parent));
				}).collect(Collectors.toList());
				// 计算小组内能力得分
				result.put(String.valueOf(parent), graspValueUtil.getGraspValue(rs));
			});
			results.put(schoolId, result);
		});
		Map<String, Object> abilitys = Maps.newHashMap();
		Arrays.stream(CompositeAbility.values()).filter(ca -> null == ca.getParent()).forEach(ca -> {
			abilitys.put(ca.toString(), ca.getTitle());
		});
		ret.put("abilitys", abilitys);
		ret.put("results", results);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}
	
	/**
	 * 根据学校id查询学校考试的统计数据
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/school_test_statistics/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> schoolMasterStatistics(@PathVariable String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		List<AnswerSource> answerSource = Lists.newArrayList();
		answerSource.add(AnswerSource.CHAPTER_TEST); // 单元测试
		answerSource.add(AnswerSource.MIDDLE_TERM_TEST); // 期中考试
		answerSource.add(AnswerSource.FINAL_TERM_TEST); // 期末考试
		answerSource.add(AnswerSource.OTHER); // 其他考试
		Integer studentAmount = answerRecordService.getStudentAmountBySchoolId(sid, answerSource); // 累计考试人数
		Integer answerTimes = answerRecordService.getAnswerTimesBySchoolId(sid, answerSource); // 累计考试人次
		Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_SCHOOL_STUDENTS, Map.class, sid);
		Integer students = (Integer) res.get("student_amount"); // 学校人数
		Float divide = graspValueUtil.divide(studentAmount, students); // 测试覆盖率
		List<AnswerRecord> schoolRecord = answerRecordService.getSchoolRecord(sid, 0, 9999999999999L);
		Map<String, List<AnswerRecord>> mapByGrade = schoolRecord.stream()
				.collect(Collectors.groupingBy(sr -> sr.getGradeName())); // 先按grade分组
		Map<String, Object> resultMap = Maps.newHashMap();
		mapByGrade.forEach((key, value) -> {
			Map<String, Integer> amountMap = Maps.newHashMap();
			Map<AnswerSource, List<AnswerRecord>> mapByAnswerSource = value.stream()
					.filter(ar -> ar.getAnswerSource().equals(AnswerSource.CHAPTER_TEST)
							|| ar.getAnswerSource().equals(AnswerSource.MIDDLE_TERM_TEST)
							|| ar.getAnswerSource().equals(AnswerSource.FINAL_TERM_TEST)
							|| ar.getAnswerSource().equals(AnswerSource.OTHER))
					.collect(Collectors.groupingBy(ar -> ar.getAnswerSource())); // 并将其按考试类型分组
			mapByAnswerSource.forEach((k, v) -> {
				amountMap.put(k.toString(), (v == null || v.isEmpty()) ? 0 : v.size());
			});
			resultMap.put(key, amountMap);
		});
		Exam exam = new Exam();
		exam.setSchoolId(sid);
		try {
			List<Exam> list = examService.findByCondition(exam);
			if (list != null && !list.isEmpty()) {
				ret.put("exam_amount", list.size()); // 考试数量
			} else {
				ret.put("exam_amount", 0);
			}
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "inner exception");
			return ret;
		}
		ret.put("test_amount", studentAmount); // 累计考试人数
		ret.put("answer_times", answerTimes); // 累计考试次数
		ret.put("cover_rate", divide); // 考试覆盖率
		ret.put("details", resultMap); // 考试人数详情
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}
}
