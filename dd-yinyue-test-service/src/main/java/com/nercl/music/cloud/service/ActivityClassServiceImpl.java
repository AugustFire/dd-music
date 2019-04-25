package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.ActivityClassDao;
import com.nercl.music.cloud.entity.ActivityClass;
import com.nercl.music.cloud.entity.ActivityType;
import com.nercl.music.cloud.entity.AwardLevel;
import com.nercl.music.cloud.entity.UserRole;
import com.nercl.music.util.page.PaginateSupportArray;

@Service
@Transactional
public class ActivityClassServiceImpl implements ActivityClassService {

	@Autowired
	private ActivityClassDao activityClassDao;

	@Override
	public String save(ActivityClass ac) {
		activityClassDao.save(ac);
		return ac.getId();
	}

	@Override
	public PaginateSupportArray<ActivityClass> getActivityClassesByConditions(String schoolId, String gradeId,
			String classId, String activityId, int page, int pageSize) {
		return activityClassDao.getActivityClassesByConditions(schoolId, gradeId, classId, activityId, page, pageSize);
	}

	@Override
	public List<ActivityClass> getActivityClassesByActivityConditions(ActivityType activityType, AwardLevel awardLevel,
			UserRole userRole, String schoolId) {
		return activityClassDao.getActivityClassesByActivityConditions(activityType, awardLevel, userRole, schoolId);
	}

	@Override
	public List<ActivityClass> getActivityClassByActivityId(String activityId) {
		return activityClassDao.getActivityClassByActivityId(activityId);
	}

}
