package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.Activity;
import com.nercl.music.cloud.entity.ActivityMember;
import com.nercl.music.cloud.entity.ActivityType;
import com.nercl.music.cloud.entity.CheckStatus;
import com.nercl.music.cloud.entity.UserRole;
import com.nercl.music.util.page.PaginateSupportArray;

public interface ActivityService {

	/**
	 * 根据用户id查询用户创建的活动列表
	 * 
	 * @param userId
	 *            用户id
	 * @param page
	 *            页码
	 * @param pageSize
	 *            页面大小
	 */
	PaginateSupportArray<Activity> findByUserId(String userId, int page, int pageSize);

	/**
	 * 新增活动
	 * 
	 * @param activity
	 *            活动
	 * @param pictures
	 *            活动中上传的图片id列表
	 * @param resources
	 *            活动中上传的其他文件id列表
	 * @param activityMemberList
	 *            活动参与的学生
	 */
	String save(Activity activity, List<String> pictures, List<String> resources,
			List<ActivityMember> activityMemberList);

	/**
	 * 根据活动id删除活动
	 */
	void deleteById(String id);

	/**
	 * 根据id查询活动
	 */
	Activity findById(String id);

	/**
	 * 根据学生id列表查询学生提交的活动
	 * 
	 * @param studentIds
	 *            学生id列表
	 * @param checkStatus
	 *            审核状态
	 * @param activityType
	 *            活动类型
	 * @param page
	 *            页码
	 * @param pageSize
	 *            页面大小
	 */
	PaginateSupportArray<Activity> findByStudentIds(List<String> studentIds, CheckStatus checkStatus,
			ActivityType activityType, int page, int pageSize);

	/**
	 * 更新活动
	 */
	void update(Activity activity);

	/**
	 * @param activityType
	 *            活动类型
	 * @param awardLevel
	 *            活动级别
	 * @param userRole
	 *            用户角色
	 */
	List<Activity> findByType(ActivityType activityType, String awardLevel, UserRole userRole);

	List<Activity> findByCondition(Activity activity) throws Exception;

}
