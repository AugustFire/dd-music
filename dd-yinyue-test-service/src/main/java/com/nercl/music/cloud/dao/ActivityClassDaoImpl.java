package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.nercl.music.cloud.entity.ActivityClass;
import com.nercl.music.cloud.entity.ActivityType;
import com.nercl.music.cloud.entity.AwardLevel;
import com.nercl.music.cloud.entity.UserRole;
import com.nercl.music.util.page.PaginateSupportArray;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class ActivityClassDaoImpl extends AbstractBaseDaoImpl<ActivityClass, String> implements ActivityClassDao {

	@Override
	public PaginateSupportArray<ActivityClass> getActivityClassesByConditions(String schoolId, String gradeId,
			String classId, String activityId, int page, int pageSize) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("from ActivityClass where 1 = 1").append(" ");
		int i = 0;
		List<Object> params = Lists.newLinkedList();
		if (!Strings.isNullOrEmpty(schoolId)) {
			i++;
			params.add(schoolId);
			sbf.append("and schoolId=?").append(i).append(" ");
		}
		if (!Strings.isNullOrEmpty(gradeId)) {
			i++;
			params.add(gradeId);
			sbf.append("and gradeId=?").append(i).append(" ");
		}
		if (!Strings.isNullOrEmpty(classId)) {
			i++;
			params.add(classId);
			sbf.append("and classId=?").append(i).append(" ");
		}
		if (!Strings.isNullOrEmpty(activityId)) {
			i++;
			params.add(activityId);
			sbf.append("and activityId=?").append(i).append(" ");
		}
		int count = this.executeCountQuery("select count(*) " + sbf.toString(), params.stream().toArray(Object[]::new));
		List<ActivityClass> activityClassList = this.executeQueryWithPaging(sbf.toString(), page, pageSize,
				params.stream().toArray(Object[]::new));
		return PaginateSupportUtil.pagingList(activityClassList, page, pageSize, count);
	}

	@Override
	public List<ActivityClass> getActivityClassesByActivityConditions(ActivityType activityType, AwardLevel awardLevel,
			UserRole userRole, String schoolId) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("from ActivityClass ac inner join fetch ac.activity where 1=1").append(" ");
		int i = 0;
		List<Object> params = Lists.newLinkedList();
		if (activityType != null) {
			i++;
			params.add(activityType);
			sbf.append("and ac.activity.activityType=?").append(i).append(" ");
		}
		if (awardLevel != null) {
			i++;
			params.add(awardLevel);
			sbf.append("and ac.activity.awardLevel=?").append(i).append(" ");
		}
		if (userRole != null) {
			i++;
			params.add(userRole);
			sbf.append("and ac.activity.userRole=?").append(i).append(" ");
		}
		if (!Strings.isNullOrEmpty(schoolId)) {
			i++;
			params.add(schoolId);
			sbf.append("and ac.schoolId=?").append(i).append(" ");
		}
		return this.executeQueryWithoutPaging(sbf.toString(), params.stream().toArray(Object[]::new));
	}

	@Override
	public List<ActivityClass> getActivityClassByActivityId(String activityId) {
		String jpql = "from ActivityClass where activityId = ?1";
		return this.executeQueryWithoutPaging(jpql, activityId);
	}

}
