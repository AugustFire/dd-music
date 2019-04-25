package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.nercl.music.cloud.entity.Activity;
import com.nercl.music.cloud.entity.ActivityType;
import com.nercl.music.cloud.entity.CheckStatus;
import com.nercl.music.cloud.entity.UserRole;
import com.nercl.music.util.page.PaginateSupportArray;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class ActivityDaoImpl extends AbstractBaseDaoImpl<Activity, String> implements ActivityDao {

	@Override
	public PaginateSupportArray<Activity> findByUserId(String userId, int page, int pageSize) {
		String jpql = "from Activity ans where userId = ?1 order by createTime Desc";
		int count = this.executeCountQuery("select count(*) " + jpql, userId);
		List<Activity> listActivity = this.executeQueryWithPaging(jpql, page, pageSize, userId);
		return PaginateSupportUtil.pagingList(listActivity, page, pageSize, count);
	}

	@Override
	public PaginateSupportArray<Activity> findByStudentIds(List<String> studentIds, CheckStatus checkStatus,
			ActivityType activityType, int page, int pageSize) {
		StringBuffer jpql = new StringBuffer();
		jpql.append("from Activity ans where userId in ?1").append(" ");
		List<Object> params = Lists.newLinkedList();
		params.add(studentIds);
		int i = 1;
		if (checkStatus != null) {
			i++;
			jpql.append("and checkStatus = ?").append(i).append(" ");
			params.add(checkStatus);
		}
		if (activityType != null) {
			i++;
			jpql.append("and activityType = ?").append(i).append(" ");
			params.add(activityType);
		}
		int count = this.executeCountQuery("select count(*) " + jpql.toString(),
				params.stream().toArray(Object[]::new));
		List<Activity> listArtSpecialty = this.executeQueryWithPaging(jpql.toString(), page, pageSize,
				params.stream().toArray(Object[]::new));
		return PaginateSupportUtil.pagingList(listArtSpecialty, page, pageSize, count);
	}

	@Override
	public List<Activity> findByType(ActivityType activityType, String awardLevel, UserRole userRole) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("from Activity  where userRole=?1").append(" ");
		int i = 1;
		List<Object> params = Lists.newLinkedList();
		params.add(userRole);
		if (activityType != null) {
			i++;
			sbf.append("and activityType = ?").append(i).append(" ");
			params.add(activityType);
		}
		if (!Strings.isNullOrEmpty(awardLevel)) {
			i++;
			sbf.append("and awardLevel = ?").append(i).append(" ");
			params.add(awardLevel);
		}
		sbf.append("order by createTime Desc");
		return this.executeQueryWithoutPaging(sbf.toString(), params.stream().toArray(Object[]::new));
	}

}
