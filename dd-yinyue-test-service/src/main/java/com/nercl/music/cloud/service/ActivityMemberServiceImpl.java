package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.ActivityMemberDao;
import com.nercl.music.cloud.entity.ActivityMember;

@Service
@Transactional
public class ActivityMemberServiceImpl implements ActivityMemberService {

	@Autowired
	private ActivityMemberDao activityMemberDao;
	
	@Override
	public int save(String activityId, List<ActivityMember> activityMemberList) {
		activityMemberList.forEach(am->{
			ActivityMember activityMember = new ActivityMember();
			activityMember.setActivityId(activityId);
			activityMember.setClassId(am.getClassId());
			activityMember.setClassName(am.getClassName());
			activityMember.setGradeId(am.getGradeId());
			activityMember.setGradeName(am.getGradeName());
			activityMember.setSchoolId(am.getSchoolId());
			activityMember.setSchoolName(am.getSchoolName());
			activityMember.setStudentId(am.getStudentId());
			activityMember.setStudentName(am.getStudentName());
			activityMember.setJoined(true);
			activityMemberDao.save(activityMember);
		});
		return activityMemberList.size();
	}

	@Override
	public String save(ActivityMember activityMember) {
		activityMemberDao.save(activityMember);
		return activityMember.getId();
	}

	@Override
	public List<ActivityMember> findByActivityId(String id) {
		return activityMemberDao.findByActivityId(id);
	}

	
}
