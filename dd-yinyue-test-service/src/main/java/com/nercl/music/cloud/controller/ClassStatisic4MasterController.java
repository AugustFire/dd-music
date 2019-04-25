package com.nercl.music.cloud.controller;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.CompositeAbility;
import com.nercl.music.cloud.entity.Knowledge;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.vo.BasicIndex;
import com.nercl.music.cloud.entity.vo.MusicAccomplishment;
import com.nercl.music.cloud.entity.vo.TestStatistic;
import com.nercl.music.cloud.service.AnswerRecordService;
import com.nercl.music.cloud.service.KnowledgeService;
import com.nercl.music.cloud.service.MasterStatisicService;
import com.nercl.music.constant.CList;
import com.nercl.music.util.GraspValueUtil;

@RestController
public class ClassStatisic4MasterController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private MasterStatisicService masterStatisicService;

	@Autowired
	private GraspValueUtil graspValueUtil;

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private AnswerRecordService answerRecordService;

	/**
	 * 校长角色---班级音乐素养综合查询
	 * 
	 * @param gid
	 *            年级id
	 * @param sid
	 *            学校id
	 */
	@GetMapping(value = "/class4master/music_accomplishment", produces = JSON_PRODUCES)
	public Map<String, Object> getAverageMusicAccomplishment(@RequestParam(required = true) String sid,
			@RequestParam(required = true) String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<MusicAccomplishment> accomplishment4Master = masterStatisicService.getAccomplishment4Master(sid, gid,
				start, end);
		ret.put("code", CList.Api.Client.OK);
		ret.put("accomplishment", accomplishment4Master);
		return ret;
	}

	/**
	 * 校长角色---基础指标数据查询
	 * 
	 * @param gid
	 *            年级id
	 * @param sid
	 *            学校id
	 */
	@GetMapping(value = "/class4master/basic_index", produces = JSON_PRODUCES)
	public Map<String, Object> getBasicIndex(@RequestParam(required = true) String sid,
			@RequestParam(required = true) String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<BasicIndex> basicIndex = Lists.newArrayList();
		try {
			basicIndex = masterStatisicService.getBasicIndex(sid, gid, start, end);
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "inner exception !");
			e.printStackTrace();
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("basicIndex", basicIndex);
		return ret;
	}

	/**
	 * 校长角色---知识维度统计分析
	 * 
	 * @param gid
	 *            年级id
	 * @param sid
	 *            学校id
	 */
	@GetMapping(value = "/class4master/knowledge", produces = JSON_PRODUCES)
	public Map<String, Object> knowledgeStatistics(@RequestParam(required = true) String sid,
			@RequestParam(required = true) String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		List<Knowledge> top = knowledgeService.getTop();
		List<AnswerRecord> records = answerRecordService.getGradeRecord(gid, sid, graspValueUtil.getStart(),
				graspValueUtil.getEnd());
		Map<String, List<AnswerRecord>> answerMap = records.parallelStream().filter(red -> null != red.getIsTrue())
				.collect(Collectors.groupingBy(red -> red.getClassId()));
		List<Map<String, Object>> list = Lists.newArrayList();
		List<Map<String, String>> classList = Lists.newArrayList();
		answerMap.forEach((classId, rds) -> {
			Map<String, Object> map = Maps.newHashMap();
			Map<String, String> classes = Maps.newHashMap();
			Iterator<Knowledge> iterator = top.iterator();
			Map<String, Object> result = Maps.newHashMap();
			while (iterator.hasNext()) {
				Knowledge knowledge = (Knowledge) iterator.next();
				String key = knowledge.getNo();
				List<AnswerRecord> collect = rds.stream()
						.filter(record -> record.getQuestion().getKnowledges().contains(key))
						.collect(Collectors.toList());
				result.put(key, graspValueUtil.getGraspValue(collect));
			}
			map.put(classId, result);
			classes.put(classId, rds.get(0).getClassName());
			list.add(map);
			classList.add(classes);
		});
		ret.put("code", CList.Api.Client.OK);
		ret.put("result", list);
		ret.put("classes", classList);
		ret.put("knowledges", top);
		return ret;
	}

	/**
	 * 校长角色---能力维度统计分析
	 * 
	 * @param gid
	 *            年级id
	 * @param sid
	 *            学校id
	 */
	@GetMapping(value = "/class4master/ability", produces = JSON_PRODUCES)
	public Map<String, Object> schoolAbilityStatistics(@RequestParam(required = true) String sid,
			@RequestParam(required = true) String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		List<AnswerRecord> records = answerRecordService.getGradeRecord(gid, sid, graspValueUtil.getStart(),
				graspValueUtil.getEnd());
		// 将答案按学校id分组
		Map<String, List<AnswerRecord>> answerMap = records.parallelStream().filter(red -> null != red.getIsTrue())
				.collect(Collectors.groupingBy(red -> red.getClassId()));
		List<CompositeAbility> parents = Arrays.stream(CompositeAbility.values()).filter(ca -> null == ca.getParent())
				.collect(Collectors.toList());
		Map<String, Object> results = Maps.newHashMap();
		List<Map<String, String>> listClasses = Lists.newArrayList();
		answerMap.forEach((classId, rds) -> {
			Map<String, Object> result = Maps.newHashMap();
			Map<String, String> classMap = Maps.newHashMap();
			parents.forEach(parent -> {
				// 在按班级分组的基础上再按能力维度分组并
				List<AnswerRecord> rs = rds.parallelStream().filter(record -> {
					Question question = record.getQuestion();
					return null != question
							&& Strings.nullToEmpty(question.getCompositeAbilitys()).contains(String.valueOf(parent));
				}).collect(Collectors.toList());
				// 计算小组内能力得分
				result.put(String.valueOf(parent), graspValueUtil.getGraspValue(rs));
			});
			results.put(classId, result);
			classMap.put(classId, rds.get(0).getClassName());
			listClasses.add(classMap);
		});
		Map<String, Object> abilitys = Maps.newHashMap();
		Arrays.stream(CompositeAbility.values()).filter(ca -> null == ca.getParent()).forEach(ca -> {
			abilitys.put(ca.toString(), ca.getTitle());
		});
		ret.put("abilitys", abilitys);
		ret.put("classes", listClasses);
		ret.put("result", results);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 校长角色---考试综合查询
	 * 
	 * @param gid
	 *            年级id
	 * @param sid
	 *            学校id
	 * @param testType
	 *            考试类型 （期中考试、期末考试）
	 */
	@GetMapping(value = "/class4master/test_statistics", produces = JSON_PRODUCES)
	public Map<String, Object> getTestStatistics(@RequestParam(required = true) String sid,
			@RequestParam(required = true) String gid,
			@RequestParam(value = "test_type", required = true) String testType) {
		Map<String, Object> ret = Maps.newHashMap();
		if (!AnswerSource.isDefined(testType)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no enum constant[AnswerSource]:" + testType);
			return ret;
		}
		AnswerSource examType = AnswerSource.valueOf(testType);
		Long start = graspValueUtil.getStart();
		Long end = graspValueUtil.getEnd();
		List<TestStatistic> testStatistics = masterStatisicService.getTestStatistics(sid, gid, examType, start, end);
		if (testStatistics == null || testStatistics.isEmpty()) {
			ret.put("statistics", null);
		} else {
			// 按总平均分降序排序
			testStatistics = testStatistics.parallelStream()
					.sorted((t1,
							t2) -> (t1.getTotalAverageScore() < t2.getTotalAverageScore() ? 1
									: (t1.getTotalAverageScore() > t2.getTotalAverageScore()) ? -1 : 0))
					.collect(Collectors.toList());
			testStatistics.get(0).setRank(1); // 将第一条记录的排名设置为1
			for (int i = 1; i < testStatistics.size(); i++) { // 从第二个开始，如果当前分数=上一个的分数则排名值与上一个设置成一样，否则设置成实际排名
				if (testStatistics.get(i).getTotalAverageScore()
						.equals(testStatistics.get(i - 1).getTotalAverageScore())) {
					testStatistics.get(i).setRank(i);
				} else {
					testStatistics.get(i).setRank(i + 1);
				}
			}
			ret.put("statistics", testStatistics);
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}
}
