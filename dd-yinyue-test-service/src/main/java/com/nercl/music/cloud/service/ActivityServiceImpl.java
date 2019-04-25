package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.ActivityClassDao;
import com.nercl.music.cloud.dao.ActivityDao;
import com.nercl.music.cloud.dao.ActivityMemberDao;
import com.nercl.music.cloud.dao.ActivityResourceRelationDao;
import com.nercl.music.cloud.entity.Activity;
import com.nercl.music.cloud.entity.ActivityClass;
import com.nercl.music.cloud.entity.ActivityMember;
import com.nercl.music.cloud.entity.ActivityResourceRelation;
import com.nercl.music.cloud.entity.ActivityResourceRelation.ResourceType;
import com.nercl.music.cloud.entity.ActivityType;
import com.nercl.music.cloud.entity.CheckStatus;
import com.nercl.music.cloud.entity.UserRole;
import com.nercl.music.util.page.PaginateSupportArray;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityDao activityDao;

	@Autowired
	private ActivityResourceRelationDao activityResourceRelationDao;

	@Autowired
	private ActivityClassService activityClassService;

	@Autowired
	private ActivityMemberService activityMemberService;

	@Autowired
	private ActivityClassDao activityClassDao;

	@Autowired
	private ActivityMemberDao activityMemberDao;
	
	@Override
	public PaginateSupportArray<Activity> findByUserId(String userId, int page, int pageSize) {
		return activityDao.findByUserId(userId, page, pageSize);
	}

	@Override
	public String save(Activity activity, List<String> pictures, List<String> resources,
			List<ActivityMember> activityMemberList) {
		activityDao.save(activity);
		if (pictures != null && !pictures.isEmpty()) {
			pictures.forEach(pic -> { // 保存活动对应的图片
				ActivityResourceRelation arr = new ActivityResourceRelation();
				arr.setActivityId(activity.getId());
				arr.setResourceId(pic);
				arr.setResourceType(ResourceType.picture);
				activityResourceRelationDao.save(arr);
			});
		}
		if (resources != null && !resources.isEmpty()) {
			resources.forEach(res -> { // 保存活动对应的其他文件
				ActivityResourceRelation arr = new ActivityResourceRelation();
				arr.setActivityId(activity.getId());
				arr.setResourceId(res);
				arr.setResourceType(ResourceType.resouce);
				activityResourceRelationDao.save(arr);
			});
		}
		Map<String, List<ActivityMember>> map = activityMemberList.parallelStream()
				.collect(Collectors.groupingBy(acs -> acs.getClassId()));
		map.forEach((k, v) -> { // 保存活动年级
			ActivityMember am = v.get(0);
			ActivityClass ac = new ActivityClass();
			ac.setActivityId(activity.getId());
			ac.setClassId(am.getClassId());
			ac.setClassName(am.getClassName());
			ac.setGradeId(am.getGradeId());
			ac.setGradeName(am.getGradeName());
			ac.setSchoolId(am.getSchoolId());
			ac.setSchoolName(am.getSchoolName());
			activityClassService.save(ac);
		});
		activityMemberService.save(activity.getId(), activityMemberList); // 保存活动成员
		return activity.getId();
	}

	@Override
	public void deleteById(String id) {
		List<ActivityResourceRelation> arrList = activityResourceRelationDao.getResoures(id);
		if (arrList != null && !arrList.isEmpty()) {
			arrList.forEach(al -> {
				activityResourceRelationDao.delete(al);
			});
		}
		List<ActivityClass> activityClasses = activityClassDao.getActivityClassByActivityId(id);
		if (activityClasses != null && !activityClasses.isEmpty()) {
			activityClasses.forEach(ac->{
				activityClassDao.delete(ac);
			});
		}
		List<ActivityMember> members  = activityMemberDao.findByActivityId(id);
		if (members != null && !members.isEmpty()) {
			members.forEach(m->{
				activityMemberDao.delete(m);
			});
		}
		activityDao.deleteById(id);
	}

	@Override
	public Activity findById(String id) {
		return activityDao.findByID(id);
	}

	@Override
	public PaginateSupportArray<Activity> findByStudentIds(List<String> studentIds, CheckStatus checkStatus,
			ActivityType activityType, int page, int pageSize) {
		return activityDao.findByStudentIds(studentIds, checkStatus, activityType, page, pageSize);
	}

	@Override
	public void update(Activity activity) {
		activityDao.update(activity);
	}

	@Override
	public List<Activity> findByType(ActivityType activityType, String awardLevel, UserRole userRole) {
		return activityDao.findByType(activityType, awardLevel, userRole);
	}

	@Override
	public List<Activity> findByCondition(Activity activity) throws Exception {
		return activityDao.findByConditions(activity);
	}
}
