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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.Activity;
import com.nercl.music.cloud.entity.ActivityClass;
import com.nercl.music.cloud.entity.ActivityMember;
import com.nercl.music.cloud.entity.ActivityResourceRelation;
import com.nercl.music.cloud.entity.ActivityResourceRelation.ResourceType;
import com.nercl.music.cloud.entity.ActivityType;
import com.nercl.music.cloud.entity.AwardLevel;
import com.nercl.music.cloud.entity.CheckStatus;
import com.nercl.music.cloud.entity.FileType;
import com.nercl.music.cloud.entity.UserRole;
import com.nercl.music.cloud.service.ActivityClassService;
import com.nercl.music.cloud.service.ActivityMemberService;
import com.nercl.music.cloud.service.ActivityResourceRelationService;
import com.nercl.music.cloud.service.ActivityService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;
import com.nercl.music.util.page.PaginateSupportArray;

@RestController
public class ActivityController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ActivityService activityService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ActivityResourceRelationService activityResourceRelationService;

	@Autowired
	private ActivityClassService activityClassService;

	@Autowired
	private ActivityMemberService activityMemberService;

	/**
	 * 根据用户id查询用户创建的所有活动
	 */
	@GetMapping(value = "/activities/{uid}", produces = JSON_PRODUCES)
	public Map<String, Object> getActivities(@PathVariable String uid, int page,
			@RequestParam(name = "page_size") int pageSize) {
		Map<String, Object> ret = Maps.newHashMap();
		PaginateSupportArray<Activity> activityList = activityService.findByUserId(uid, page, pageSize);
		List<Map<String, Object>> activities = Lists.newArrayList();
		activityList.forEach(al -> {
			Map<String, Object> map = Maps.newHashMap();
			List<ActivityClass> activityClasses = activityClassService.getActivityClassesByConditions(null, null, null,
					al.getId(), 1, 99999);
			List<String> schools = Lists.newArrayList();
			List<String> grades = Lists.newArrayList();
			List<String> classes = Lists.newArrayList();
			activityClasses.forEach(ac -> {
				classes.add(ac.getClassName());
				grades.add(ac.getGradeName());
				schools.add(ac.getSchoolName());
			});
			map.put("schools", schools.stream().distinct().collect(Collectors.joining(","))); // 多个学校用“,”分隔
			map.put("grades", grades.stream().distinct().collect(Collectors.joining(","))); // 多个年级用“,”分隔
			map.put("classes", classes.stream().collect(Collectors.joining(","))); // 多个班级用“,”分隔
			reForm(map, al);
			List<ActivityResourceRelation> resoures = activityResourceRelationService.getResoures(al.getId());
			if (resoures != null && !resoures.isEmpty()) {
				List<ActivityResourceRelation> pirctures = resoures.stream()
						.filter(res -> res.getResourceType().equals(ResourceType.picture)).collect(Collectors.toList());
				List<ActivityResourceRelation> files = resoures.stream()
						.filter(res -> res.getResourceType().equals(ResourceType.resouce)).collect(Collectors.toList());
				map.put("has_pircture", null != pirctures && !pirctures.isEmpty()); // 是否有图片
				map.put("has_other_files", null != files && !files.isEmpty()); // 是否有其他文件
			} else {
				map.put("has_pircture", false); // 是否有图片
				map.put("has_other_files", false); // 是否有其他文件
			}
			activities.add(map);
		});
		ret.put("code", CList.Api.Client.OK);
		ret.put("activity_list", activities);
		ret.put("page", activityList.getPage());
		ret.put("page_size", activityList.getPageSize());
		ret.put("total", activityList.getTotal());
		return ret;
	}

	/**
	 * 校长角色--根据活动类型和文件类型查询所有教师活动
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/activities/teacher_statistic/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> getActivitiesInType(@PathVariable String sid,
			@RequestParam(name = "activity_type", required = false) String activityType,
			@RequestParam(name = "award_level", required = false) String awardLevel) {
		Map<String, Object> ret = Maps.newHashMap();
		ActivityType type = null;
		if (!Strings.isNullOrEmpty(activityType) && !ActivityType.isDefined(activityType)) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("desc", "no enum constant【ActivityType】:" + activityType);
			return ret;
		} else {
			type = Strings.isNullOrEmpty(activityType) ? null : ActivityType.valueOf(activityType);
		}
		AwardLevel level = null;
		if (!Strings.isNullOrEmpty(awardLevel) && !AwardLevel.isDefined(awardLevel)) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("desc", "no enum constant【AwardLevel】:" + awardLevel);
			return ret;
		} else {
			level = Strings.isNullOrEmpty(awardLevel) ? null : AwardLevel.valueOf(awardLevel);
		}
		List<ActivityClass> activityClasses = activityClassService.getActivityClassesByActivityConditions(type, level,
				UserRole.TEACHER, sid);
		Map<String, List<ActivityClass>> activityGroup = activityClasses.parallelStream()
				.collect(Collectors.groupingBy(ActivityClass::getActivityId));
		List<Map<String, Object>> activities = Lists.newArrayList();
		activityGroup.forEach((activityId, acList) -> {
			Map<String, Object> activityMap = Maps.newHashMap();
			Activity activity = acList.get(0).getActivity();
			reForm(activityMap, activity);
			activityMap.put("classes",
					acList.parallelStream().map(ActivityClass::getClassName).collect(Collectors.joining(",")));
			activityMap.put("grades", acList.parallelStream().map(ActivityClass::getGradeName).distinct()
					.collect(Collectors.joining(",")));
			activityMap.put("schools", acList.parallelStream().map(ActivityClass::getSchoolName).distinct()
					.collect(Collectors.joining(",")));
			List<ActivityResourceRelation> resoures = activityResourceRelationService.getResoures(activity.getId());
			List<String> resoureIds = resoures.stream().map(ActivityResourceRelation::getResourceId)
					.collect(Collectors.toList());
			Map<String, Object> resourceMap = restTemplate.getForObject(ApiClient.GET_RESOURCES, Map.class,
					resoureIds.stream().collect(Collectors.joining(",")));
			List<Map<String, Object>> resList = (List<Map<String, Object>>) resourceMap.get("resourceList");
			activityMap.put("resoures", resList);
			activities.add(activityMap);
		});
		ret.put("code", CList.Api.Client.OK);
		ret.put("activity_list", activities);
		return ret;
	}

	/**
	 * 校长角色--学生艺术活动列表查询
	 */
	@GetMapping(value = "/activities/student_statistic/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> getStudentActivities(@PathVariable String sid,
			@RequestParam(name = "activity_type", required = false) String activityType) {
		Map<String, Object> ret = Maps.newHashMap();
		if (!Strings.isNullOrEmpty(activityType) && !ActivityType.isDefined(activityType)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no enum constant【ActivityType】:" + activityType);
			return ret;
		}
		List<ActivityClass> studentActivities = activityClassService.getActivityClassesByActivityConditions(
				Strings.isNullOrEmpty(activityType) ? null : ActivityType.valueOf(activityType), null, UserRole.STUDENT,
				sid);
		Map<String, List<ActivityClass>> activityClassGroup = studentActivities.parallelStream()
				.collect(Collectors.groupingBy(ActivityClass::getActivityId));
		List<Map<String, Object>> activities = Lists.newArrayList();
		activityClassGroup.forEach((activityId, acList) -> {
			Map<String, Object> activityMap = Maps.newHashMap();
			Activity activity = acList.get(0).getActivity();
			reForm(activityMap, activity);
			activityMap.put("classes",
					acList.parallelStream().map(ActivityClass::getClassName).collect(Collectors.joining(",")));
			activityMap.put("grades", acList.parallelStream().map(ActivityClass::getGradeName).distinct()
					.collect(Collectors.joining(",")));
			activityMap.put("schools", acList.parallelStream().map(ActivityClass::getSchoolName).distinct()
					.collect(Collectors.joining(",")));
			activities.add(activityMap);
		});
		ret.put("code", CList.Api.Client.OK);
		ret.put("activity_list", activities);
		return ret;
	}

	/**
	 * 校长角色--学生音乐考级分类统计
	 */
	@GetMapping(value = "/activities/item/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> getStudentActivitiesByItem(@PathVariable String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		List<ActivityClass> studentActivities = activityClassService.getActivityClassesByActivityConditions(
				ActivityType.STUDENT_GRADE_EXAMINATION, null, UserRole.STUDENT, sid);
		List<Activity> activityList = studentActivities.parallelStream().map(ActivityClass::getActivity).distinct()
				.collect(Collectors.toList());
		Map<String, Long> collect = activityList.stream()
				.collect(Collectors.groupingBy(al -> al.getItem(), Collectors.counting()));
		ret.put("code", CList.Api.Client.OK);
		ret.put("item", collect);
		return ret;
	}

	/**
	 * 校长角色--学生比赛获奖统计分析
	 */
	@GetMapping(value = "/activities/award_level/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> getStudentActivitiesByAwardLevel(@PathVariable String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		List<ActivityClass> studentActivities = activityClassService.getActivityClassesByActivityConditions(
				ActivityType.STUDENT_COMPETITION_AWARD, null, UserRole.STUDENT, sid);
		List<Activity> activityList = studentActivities.parallelStream().map(ActivityClass::getActivity).distinct()
				.collect(Collectors.toList());
		Map<AwardLevel, Long> collect = activityList.stream()
				.collect(Collectors.groupingBy(al -> al.getAwardLevel(), Collectors.counting()));
		AwardLevel[] values = AwardLevel.values();
		Map<String, String> levelMap = Maps.newHashMap();
		for (int i = 0; i < values.length; i++) {
			levelMap.put(values[i].toString(), values[i].getLevelName());
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("award_level", collect);
		ret.put("level_map", levelMap);
		return ret;
	}

	/**
	 * 创建活动
	 */
	@PostMapping(value = "/activity", produces = JSON_PRODUCES)
	public Map<String, Object> save(@RequestParam(name = "start_time", required = false) Long startTime, // 活动开始时间
			@RequestParam(name = "end_time", required = false) Long endTime, // 活动结束时间
			@RequestParam(name = "user_id") String userId, // 活动创建人id
			@RequestParam(name = "user_role") String userRole, // 活动创建人角色
			@RequestParam(name = "user_name") String userName, // 活动创建人姓名
			@RequestParam(required = false) String name, // 活动名称
			@RequestParam(required = false) List<String> pictures, // 活动图片
			@RequestParam(required = false) List<String> resources, // 活动其他文件
			@RequestParam(required = false) String item, // 活动项目
			@RequestParam(required = false) String award, // 活动获奖情况
			@RequestParam(required = false) String level, // 活动获奖等级
			@RequestBody List<ActivityMember> activityMemberList, // 活动参与学生
			@RequestParam(name = "activity_type", required = false) String activityType, // 活动类型
			@RequestParam(name = "award_level", required = false) String awardLevel // 活动获奖级别
	) {
		Map<String, Object> ret = Maps.newHashMap();
		Activity activity = new Activity();
		if (!Strings.isNullOrEmpty(activityType)) {
			if (ActivityType.isDefined(activityType)) {
				activity.setActivityType(ActivityType.valueOf(activityType));
			} else {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "no enum constant【ActivityType】:" + activityType);
				return ret;
			}
		}
		if (!Strings.isNullOrEmpty(awardLevel)) {
			if (AwardLevel.isDefined(awardLevel)) {
				activity.setAwardLevel(AwardLevel.valueOf(awardLevel));
			} else {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "no enum constant【AwardLevel】:" + awardLevel);
				return ret;
			}
		}
		if (!UserRole.isDefined(userRole)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no enum constant【UserRole】:" + userRole);
			return ret;
		}
		activity.setName(name);
		activity.setUserId(userId);
		activity.setUserRole(UserRole.valueOf(userRole));
		activity.setUserName(userName);
		activity.setStartTime(startTime);
		activity.setEndTime(endTime);
		activity.setCreateTime(Instant.now().toEpochMilli()); // 活动创建时间
		activity.setCheckStatus(CheckStatus.UN_CHECK); // 创建时状态为末审批
		activity.setAward(award);
		activity.setItem(item);
		activity.setLevel(level);
		String activityId = activityService.save(activity, pictures, resources, activityMemberList);
		ret.put("code", CList.Api.Client.OK);
		ret.put("activity_id", activityId);
		return ret;
	}

	/**
	 * 添加人员到活动
	 */
	@PostMapping(value = "/activity/{aid}/member", produces = JSON_PRODUCES)
	public Map<String, Object> save(@PathVariable String aid, // 活动id
			@RequestBody List<ActivityMember> activityMemberList // 活动参与学生
	) {
		Map<String, Object> ret = Maps.newHashMap();
		Activity activity = activityService.findById(aid);
		if (activity == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "expected activity not exist");
			return ret;
		}
		int saveNum = activityMemberService.save(aid, activityMemberList);
		ret.put("code", CList.Api.Client.OK);
		ret.put("save_num", saveNum);
		return ret;
	}

	/**
	 * 删除活动
	 */
	@DeleteMapping("/activity/{id}")
	public Map<String, Object> delete(@PathVariable String id) {
		Map<String, Object> ret = Maps.newHashMap();
		Activity activity = activityService.findById(id);
		if (activity == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "activity not exist");
			return ret;
		}
		activityService.deleteById(id);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 根据老师id查询老师所教授学生提交的活动列表
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/student_activities/{uid}", produces = JSON_PRODUCES)
	public Map<String, Object> getStudentActivities(@PathVariable String uid, int page,
			@RequestParam(name = "page_size") int pageSize, @RequestParam(name = "activity_type") String activityType,
			@RequestParam(name = "check_status", required = false) String checkStatus) {
		Map<String, Object> ret = Maps.newHashMap();
		if (!ActivityType.isDefined(activityType)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no enum constant【ActivityType】:" + activityType);
			return ret;
		}
		List<Map<String, Object>> activities = Lists.newArrayList();
		Map<String, Object> map = restTemplate.getForObject(ApiClient.GET_CLASS_STUDENTS, Map.class, uid);
		List<Map<String, Object>> classStudents = (List<Map<String, Object>>) map.get("class_students");
		if (classStudents == null || classStudents.isEmpty()) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("activity_list", null);
			return ret;
		}
		CheckStatus status = null;
		if (!Strings.isNullOrEmpty(checkStatus)) {
			if (!CheckStatus.isDefined(checkStatus)) {
				ret.put("code", CList.Api.Client.OK);
				ret.put("desc", "no enum constant:" + checkStatus);
				return ret;
			} else {
				status = CheckStatus.valueOf(checkStatus);
			}
		}
		ActivityType type = null;
		if (!Strings.isNullOrEmpty(activityType)) {
			if (!ActivityType.isDefined(activityType)) {
				ret.put("code", CList.Api.Client.OK);
				ret.put("desc", "no enum constant:" + activityType);
				return ret;
			} else {
				type = ActivityType.valueOf(activityType);
			}
		}
		List<String> studentIds = classStudents.stream().map(m -> (String) m.get("userId"))
				.collect(Collectors.toList());
		PaginateSupportArray<Activity> activityList = activityService.findByStudentIds(studentIds, status, type, page,
				pageSize);
		activityList.forEach(al -> {
			Map<String, Object> activity = Maps.newHashMap();
			reForm(activity, al);
			List<ActivityResourceRelation> resoures = activityResourceRelationService.getResoures(al.getId());
			if (resoures != null && !resoures.isEmpty()) {
				List<ActivityResourceRelation> pirctures = resoures.stream()
						.filter(res -> res.getResourceType().equals(ResourceType.picture)).collect(Collectors.toList());
				List<ActivityResourceRelation> files = resoures.stream()
						.filter(res -> res.getResourceType().equals(ResourceType.resouce)).collect(Collectors.toList());
				activity.put("has_pircture", null != pirctures && !pirctures.isEmpty()); // 是否有图片
				activity.put("has_other_files", null != files && !files.isEmpty()); // 是否有其他文件
			} else {
				activity.put("has_pircture", false); // 是否有图片
				activity.put("has_other_files", false); // 是否有其他文件
			}
			List<Map<String, Object>> students = classStudents.stream()
					.filter(mp -> al.getUserId().equals(mp.get("userId"))).collect(Collectors.toList());
			Map<String, Object> classStudent = students.get(0); // 每个学生最多只会有一条记录
			if (classStudent != null) {
				activity.put("user_name", (String) classStudent.get("userName")); // 活动创建人姓名
				Map<String, Object> classes = (Map<String, Object>) classStudent.get("classes");
				Map<String, Object> grade = (Map<String, Object>) classStudent.get("grade");
				activity.put("class_name", (String) classes.get("name")); // 活动创建人班级
				activity.put("grade_name", (String) grade.get("name")); // 活动创建人年级
				activities.add(activity);
			}
		});
		ret.put("code", CList.Api.Client.OK);
		ret.put("activity_list", activities);
		ret.put("page", activityList.getPage());
		ret.put("page_size", activityList.getPageSize());
		ret.put("total", activityList.getTotal());
		return ret;
	}

	/**
	 * 审批活动
	 */
	@PutMapping(value = "/approval/{id}", produces = JSON_PRODUCES)
	public Map<String, Object> approveActivity(@PathVariable String id,
			@RequestParam(name = "check_status") String checkStatus) {
		Map<String, Object> ret = Maps.newHashMap();
		Activity activity = activityService.findById(id);
		if (activity == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "activity not exist");
			return ret;
		}
		if (!CheckStatus.isDefined(checkStatus)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no enum constant:" + checkStatus);
			return ret;
		}
		activity.setCheckStatus(CheckStatus.valueOf(checkStatus));
		activityService.update(activity);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 根据活动id查询活动详情
	 */
	@SuppressWarnings({ "unchecked" })
	@GetMapping(value = "/activity/{id}", produces = JSON_PRODUCES)
	public Map<String, Object> getActivityById(@PathVariable String id) {
		Map<String, Object> ret = Maps.newHashMap();
		Activity activity = activityService.findById(id);
		if (activity == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "activity not exist");
			return ret;
		}
		reForm(ret, activity);
		List<ActivityResourceRelation> resoures = activityResourceRelationService.getResoures(id);
		if (resoures != null && !resoures.isEmpty()) {
			List<String> pictureIds = resoures.stream()
					.filter(res -> res.getResourceType().equals(ResourceType.picture)).map(rs -> rs.getResourceId())
					.collect(Collectors.toList());
			List<String> fileIds = resoures.stream().filter(res -> res.getResourceType().equals(ResourceType.resouce))
					.map(rs -> rs.getResourceId()).collect(Collectors.toList());
			Map<String, Object> resMap = restTemplate.getForObject(ApiClient.GET_RESOURCES, Map.class,
					fileIds.stream().collect(Collectors.joining(",")));
			List<Map<String, Object>> resList = (List<Map<String, Object>>) resMap.get("resourceList");
			if (resList != null && !resList.isEmpty()) {
				List<Map<String, Object>> otherFiles = resList.stream().filter(res -> res != null).map(resouce -> {
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", resouce.get("id"));
					map.put("name", resouce.get("name"));
					map.put("size", resouce.get("size"));
					map.put("create_at", resouce.get("createAt"));
					map.put("cloud_id", resouce.get("cloudId"));
					map.put("description", resouce.get("description"));
					return map;
				}).collect(Collectors.toList());
				ret.put("other_files", otherFiles);
			}
			ret.put("picture_ids", pictureIds);
		}
		List<ActivityMember> members = activityMemberService.findByActivityId(id);
		List<Map<String, Object>> listMember = Lists.newArrayList();
		members.forEach(m -> {
			Map<String, Object> member = Maps.newHashMap();
			member.put("student_id", m.getStudentId());
			member.put("student_name", m.getStudentName());
			member.put("class_id", m.getClassId());
			member.put("class_name", m.getClassName());
			member.put("grade_id", m.getGradeId());
			member.put("grade_name", m.getGradeName());
			member.put("school_id", m.getSchoolId());
			member.put("school_name", m.getSchoolName());
			listMember.add(member);
		});
		ret.put("activity_members", listMember);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 校长角色--首页--统计优秀演出剧目库中各种类型文件数量
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/activities/file_statistic/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> getActivityFiles(@PathVariable String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		List<ActivityClass> activities = activityClassService.getActivityClassesByActivityConditions(null, null,
				UserRole.TEACHER, sid); // 根据学校查询活动班级
		if (activities == null || activities.isEmpty()) { // 如果查询的活动班级为空，则各种类型文件数量都为0
			ret.put("code", CList.Api.Client.OK);
			return resetMap(ret);
		}
		List<Activity> activityList = activities.parallelStream().map(ActivityClass::getActivity).distinct()
				.collect(Collectors.toList()); // 筛选出活动
		List<ActivityResourceRelation> resoures = activityResourceRelationService
				.getResoures(activityList.parallelStream().map(Activity::getId).collect(Collectors.toList())); // 根据活动id查询活动对应的资源
		if (resoures == null || resoures.isEmpty()) { // 如果根据活动id查询出的资源为空，则各种类型文件数量都为0
			ret.put("code", CList.Api.Client.OK);
			return resetMap(ret);
		}
		List<String> resoureIds = resoures.stream().map(ActivityResourceRelation::getResourceId)
				.collect(Collectors.toList());
		// 查询资源详情
		Map<String, Object> resourceMap = restTemplate.getForObject(ApiClient.GET_RESOURCES, Map.class,
				resoureIds.stream().collect(Collectors.joining(",")));
		List<Map<String, Object>> resList = (List<Map<String, Object>>) resourceMap.get("resourceList");
		if (resList == null || resList.isEmpty()) { // 根据资源id查询对应的资源为空，则各种类型文件数量都为0
			ret.put("code", CList.Api.Client.OK);
			return resetMap(ret);
		}
		Map<String, Long> resourceExt = resList.parallelStream()
				.collect(Collectors.groupingBy(res -> (String) res.get("ext"), Collectors.counting()));
		List<Long> video = Lists.newArrayList();
		List<Long> audio = Lists.newArrayList();
		List<Long> document = Lists.newArrayList();
		List<Long> picture = Lists.newArrayList();
		// 将资源分成 视频、音频、图片、文档、其他 5大类来统计每个类型的资源数量
		resourceExt.forEach((ext, number) -> {
			if (FileType.VIDEO.getResourceTypeName().contains(ext)) {
				video.add(number);
			} else if (FileType.AUDIO.getResourceTypeName().contains(ext)) {
				audio.add(number);
			} else if (FileType.DOCUMENT.getResourceTypeName().contains(ext)) {
				document.add(number);
			} else if (FileType.PICTURE.getResourceTypeName().contains(ext)) {
				picture.add(number);
			}
		});
		Long videoNum = video.parallelStream().collect(Collectors.summarizingLong(v -> v)).getSum();
		Long audioNum = audio.parallelStream().collect(Collectors.summarizingLong(v -> v)).getSum();
		Long documentNum = document.parallelStream().collect(Collectors.summarizingLong(v -> v)).getSum();
		Long pictureNum = picture.parallelStream().collect(Collectors.summarizingLong(v -> v)).getSum();
		Long other = resList.size() - videoNum - audioNum - documentNum - pictureNum;
		ret.put("video", videoNum);
		ret.put("audio", audioNum);
		ret.put("document", documentNum);
		ret.put("picture", pictureNum);
		ret.put("other", other);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	private Map<String, Object> resetMap(Map<String, Object> ret) {
		ret.put("video", 0);
		ret.put("audio", 0);
		ret.put("document", 0);
		ret.put("picture", 0);
		ret.put("other", 0);
		return ret;
	}

	private void reForm(Map<String, Object> ret, Activity activity) {
		ret.put("id", activity.getId());
		ret.put("name", activity.getName());
		ret.put("user_id", activity.getUserId());
		ret.put("user_name", activity.getUserName());
		ret.put("user_role", activity.getUserRole());
		ret.put("create_time", activity.getCreateTime()); // 创建时间
		ret.put("start_time", activity.getStartTime()); // 开始时间
		ret.put("end_time", activity.getEndTime()); // 结束时间
		ret.put("check_status", activity.getCheckStatus()); // 审批状态
		ret.put("activity_type", activity.getActivityType()); // 艺术活动类型
		ret.put("item", activity.getItem()); // 考试/比赛项目
		ret.put("award", activity.getAward()); // 获奖情况
		ret.put("level", activity.getLevel()); // 获奖等级
		ret.put("award_level", activity.getAwardLevel()); // 获将级别
	}
}