package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.nercl.music.cloud.entity.ability.StatisticTimeSlice;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.util.GraspValueUtil;

@Component
public class StatisticTaskService {

	@Autowired
	private StatisticTimeSliceService statisticTimeSliceService;

	@Autowired
	private StatisticService statisticService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private GraspValueUtil graspValueUtil;

	@Value("#{'${schools}'.split(',')}")
	private List<String> schools;

	@Scheduled(cron = "0 0 23 * * ?")
//	@Scheduled(cron = "0/59 * * * * ?")
	public void statistics() {
		System.out.println("------------begin statistics------------");
		long now = System.currentTimeMillis();

		StatisticTimeSlice latestTimeSlice = statisticTimeSliceService.getLatest();
		Long start = null == latestTimeSlice ? graspValueUtil.getStart() : latestTimeSlice.getEndAt();
		statisticTimeSliceService.save(start, now);

		statisticClass(start, now);
		statisticGrade(start, now);
		statisticSchool(start, now);
	}

	@SuppressWarnings("unchecked")
	private void statisticClass(long start, long end) {
		schools.forEach(school -> {
			Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_GRADES, Map.class, school);
			if (null != ret && null != ret.get("grades")) {
				List<Map<String, String>> gs = (List<Map<String, String>>) ret.get("grades");
				List<String> grades = gs.stream().map(g -> g.getOrDefault("id", "")).collect(Collectors.toList());
				grades.stream().filter(grade -> !Strings.isNullOrEmpty(grade)).forEach(grade -> {
					Map<String, Object> classes = restTemplate.getForObject(ApiClient.GET_CLASSES, Map.class, school,
							grade);
					if (null != classes && null != classes.get("classes")) {
						List<Map<String, String>> cls = (List<Map<String, String>>) classes.get("classes");
						cls.forEach(cl -> {
							String id = cl.getOrDefault("id", "");
							statisticService.statisticClassMusicAbility(id, start, end);
						});
					}
				});
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void statisticGrade(long start, long end) {
		schools.forEach(school -> {
			Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_GRADES, Map.class, school);
			if (null != ret && null != ret.get("grades")) {
				List<Map<String, String>> gs = (List<Map<String, String>>) ret.get("grades");
				List<String> grades = gs.stream().map(g -> g.getOrDefault("id", "")).collect(Collectors.toList());
				grades.stream().filter(grade -> !Strings.isNullOrEmpty(grade)).forEach(grade -> {
					statisticService.statisticGradeMusicAbility(grade, school, start, end);
				});
			}
		});
	}

	private void statisticSchool(long start, long end) {
		schools.forEach(school -> {
			statisticService.statisticSchoolMusicAbility(school, start, end);
		});
	}
}