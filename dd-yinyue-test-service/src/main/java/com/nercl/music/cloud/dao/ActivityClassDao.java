package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.ActivityClass;
import com.nercl.music.cloud.entity.ActivityType;
import com.nercl.music.cloud.entity.AwardLevel;
import com.nercl.music.cloud.entity.UserRole;
import com.nercl.music.util.page.PaginateSupportArray;

public interface ActivityClassDao extends BaseDao<ActivityClass, String> {

	/**
	 * 根据条件分页查询
	 * 
	 * @param schoolId
	 *            学校id
	 * @param gradeId
	 *            年级id
	 * @param classId
	 *            班级id
	 * @param activityId
	 *            活动id
	 * @param page
	 *            页码
	 * @param pageSize
	 *            页面大小
	 * @return
	 */
	PaginateSupportArray<ActivityClass> getActivityClassesByConditions(String schoolId, String gradeId, String classId,
			String activityId, int page, int pageSize);

	/**
	 * 根据条件查询
	 * 
	 * @param activityType
	 *            活动类型
	 * @param awardLevel
	 *            获奖级别
	 * @param userRole
	 *            用户角色
	 * @param schoolId
	 *            学校id
	 * @return
	 */
	List<ActivityClass> getActivityClassesByActivityConditions(ActivityType activityType, AwardLevel awardLevel,
			UserRole userRole, String schoolId);

	/**
	 * 根据活动id查询活动班级
	 * 
	 * @param activityId
	 *            活动id
	 */
	List<ActivityClass> getActivityClassByActivityId(String activityId);
}
