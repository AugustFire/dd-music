package com.nercl.music.cloud.service;

import com.nercl.music.cloud.entity.ActivityType;
import com.nercl.music.cloud.entity.AwardLevel;
import com.nercl.music.cloud.entity.Honour;
import com.nercl.music.util.page.PaginateSupportArray;

public interface HonourService {

	/**
	 * 查询所有学校获得的荣誉
	 * @param sid
	 *            学校id
	 * @param page
	 *            页码
	 * @param pageSize
	 *            页面大小
	 */
	PaginateSupportArray<Honour> findAllHonours(String sid, ActivityType activityType, AwardLevel awardLevel, int page,
			int pageSize);

	/**
	 * 新增荣誉
	 * 
	 * @param honour
	 *            学校获得的荣誉
	 */
	String save(Honour honour);

	/**
	 * 根据活动id删除荣誉
	 */
	void deleteById(String id);

	/**
	 * 根据id查询荣誉
	 */
	Honour findById(String id);

	/**
	 * 更新荣誉
	 */
	void update(Honour activity);

}
