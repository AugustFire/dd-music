package com.nercl.music.cloud.controller;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.ActivityType;
import com.nercl.music.cloud.entity.AwardLevel;
import com.nercl.music.cloud.entity.Honour;
import com.nercl.music.cloud.entity.UserRole;
import com.nercl.music.cloud.service.HonourService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;
import com.nercl.music.util.page.PaginateSupportArray;

@RestController
public class HonourController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private HonourService honourService;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 根据学校id查询学校的所有荣誉
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/honours/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> getHonours(@PathVariable String sid, int page,
			@RequestParam(name = "page_size") int pageSize,
			@RequestParam(name = "activity_type", required = false) String activityType,
			@RequestParam(name = "award_level", required = false) String awardLevel) {
		Map<String, Object> ret = Maps.newHashMap();
		ActivityType type = null;
		AwardLevel level = null;
		if (!Strings.isNullOrEmpty(activityType)) {
			if (ActivityType.isDefined(activityType)) {
				type = ActivityType.valueOf(activityType);
			} else {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "no enum constant【ActivityType】:" + activityType);
				return ret;
			}
		}
		if (!Strings.isNullOrEmpty(awardLevel)) {
			if (AwardLevel.isDefined(awardLevel)) {
				level = AwardLevel.valueOf(awardLevel);
			} else {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "no enum constant【AwardLevel】:" + awardLevel);
				return ret;
			}
		}
		PaginateSupportArray<Honour> honours = honourService.findAllHonours(sid, type, level, page, pageSize);
		List<Map<String, Object>> resultList = Lists.newArrayList();
		honours.forEach(hn -> {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", hn.getId());
			map.put("name", hn.getName());
			map.put("create_time", hn.getCreateTime());
			map.put("honour_time", hn.getHonourTime());
			map.put("activity_type", hn.getActivityType());
			map.put("award_level", hn.getAwardLevel());
			map.put("school_id", hn.getSchoolId());
			map.put("school_name", hn.getSchoolName());
			Map<String, Object> resMap = restTemplate.getForObject(ApiClient.GET_RESOURCE, Map.class,
					hn.getPictureId());
			resMap.remove("code");
			map.put("picture", resMap);
			resultList.add(map);
		});
		ret.put("code", CList.Api.Client.OK);
		ret.put("honour_list", resultList);
		ret.put("total", honours.getTotal());
		ret.put("page", honours.getPage());
		ret.put("page_size", honours.getPageSize());
		return ret;
	}

	/**
	 * 创建学校荣誉
	 */
	@PostMapping(value = "/honour", produces = JSON_PRODUCES)
	public Map<String, Object> save(String name, // 获得荣誉名称
			@RequestParam(name = "honour_time") Long honourTime, // 获得荣誉的时间
			@RequestParam(name = "user_id") String userId, // 提交人id
			@RequestParam(name = "activity_type") String activityType, // 获奖类型
			@RequestParam(name = "award_level") String awardLevel, // 获奖级别
			@RequestParam(name = "school_id") String schoolId, // 学校id
			@RequestParam(name = "school_name") String schoolName, // 学校名称
			@RequestParam(name = "picture_id", required = false) String pictureId) {
		Map<String, Object> ret = Maps.newHashMap();
		Honour honour = new Honour();
		if (!Strings.isNullOrEmpty(activityType)) {
			if (ActivityType.isDefined(activityType)) {
				honour.setActivityType(ActivityType.valueOf(activityType));
			} else {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "no enum constant【ActivityType】:" + activityType);
				return ret;
			}
		}
		if (!Strings.isNullOrEmpty(awardLevel)) {
			if (AwardLevel.isDefined(awardLevel)) {
				honour.setAwardLevel(AwardLevel.valueOf(awardLevel));
			} else {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "no enum constant【AwardLevel】:" + awardLevel);
				return ret;
			}
		}
		honour.setName(name);
		honour.setUserId(userId);
		honour.setCreateTime(Instant.now().toEpochMilli()); // 创建时间
		honour.setHonourTime(honourTime);
		honour.setPictureId(pictureId);
		honour.setUserRole(UserRole.SCHOOL_MASTER);
		honour.setSchoolId(schoolId);
		honour.setSchoolName(schoolName);
		String honourId = honourService.save(honour);
		ret.put("code", CList.Api.Client.OK);
		ret.put("honour_id", honourId);
		return ret;
	}

	/**
	 * 根据id删除学校荣誉
	 */
	@DeleteMapping(value = "/honour/{hid}", produces = JSON_PRODUCES)
	public Map<String, Object> deleteById(@PathVariable String hid) {
		Map<String, Object> ret = Maps.newHashMap();
		honourService.deleteById(hid);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 首页统计信息--按级别统计学校荣誉数量
	 */
	@GetMapping(value = "/honours/statistic/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> getHonoursStatistic(@PathVariable String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		List<Honour> honours = honourService.findAllHonours(sid, null, null, 1, 999999);
		Map<String, Long> honourStatistic = honours.parallelStream()
				.collect(Collectors.groupingBy(h -> h.getAwardLevel().toString(), Collectors.counting()));
		AwardLevel[] values = AwardLevel.values();
		Map<String, String> levelMap = Maps.newHashMap();
		for (int i = 0; i < values.length; i++) {
			levelMap.put(values[i].toString(), values[i].getLevelName());
		}
		ret.put("level_map", levelMap);
		ret.put("code", CList.Api.Client.OK);
		ret.put("honour_statistic", honourStatistic);
		return ret;
	}
}
