package com.nercl.music.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.GroupDao;
import com.nercl.music.entity.user.AbstractGroup;
import com.nercl.music.entity.user.ExamineeGroup;
import com.nercl.music.entity.user.Expert;
import com.nercl.music.entity.user.ExpertGroup;
import com.nercl.music.service.ExpertService;
import com.nercl.music.service.GroupService;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private ExpertService expertService;

	@Override
	public AbstractGroup get(String id) {
		return this.groupDao.findByID(id);
	}

	@Override
	public List<AbstractGroup> getExamineeGroups() {
		return this.groupDao.getExamineeGroups();
	}

	@Override
	public List<AbstractGroup> getExpertGroups() {
		return this.groupDao.getExpertGroups();
	}

	@Override
	public boolean save(String title, String examId, boolean isExamineeGroup) {
		if (isExamineeGroup) {
			ExamineeGroup examineeGroup = new ExamineeGroup();
			examineeGroup.setTitle(title);
			examineeGroup.setExamId(examId);
			this.groupDao.save(examineeGroup);
			return true;
		} else {
			ExpertGroup expetGroup = new ExpertGroup();
			expetGroup.setTitle(title);
			expetGroup.setExamId(examId);
			this.groupDao.save(expetGroup);
			return true;
		}
	}

	@Override
	public boolean relatedExamineeExpertGroups(String[] examineeGroupIds, String[] expertGroupIds) {
		Arrays.stream(expertGroupIds).forEach(expertGroup -> {
			AbstractGroup egroup = this.get(expertGroup);
			if (null != egroup && egroup instanceof ExpertGroup) {
				ExpertGroup eGroup = (ExpertGroup) egroup;
				List<ExamineeGroup> egroups = Arrays.stream(examineeGroupIds)
		                .filter(examineeGroup -> StringUtils.isNotBlank(examineeGroup)).map(examineeGroup -> {
			                AbstractGroup agroup = this.get(examineeGroup);
			                if (null != agroup && agroup instanceof ExamineeGroup) {
				                return (ExamineeGroup) agroup;
			                }
			                return null;
		                }).collect(Collectors.toList());
				eGroup.setExamineeGroups(egroups);
				this.groupDao.update(eGroup);
			}
		});
		return true;
	}

	@Override
	public List<ExamineeGroup> getExamineeGroupsByExpertId(String expertId) {
		Expert expert = this.expertService.get(expertId);
		if (null != expert) {
			AbstractGroup group = expert.getGroup();
			if (null != group && group instanceof ExpertGroup) {
				ExpertGroup eGroup = (ExpertGroup) group;
				List<ExamineeGroup> groups = eGroup.getExamineeGroups();
				return groups;
			}
		}
		return null;
	}

	@Override
	public boolean update(String id, String title) {
		AbstractGroup agroup = this.get(id);
		if (null != agroup) {
			agroup.setTitle(title);
			this.groupDao.update(agroup);
			return true;
		}
		return false;
	}

	@Override
	public List<AbstractGroup> queryExamineeGroups(String examId, String examRoom, int page) {
		return this.groupDao.query(examId, examRoom, true, page);
	}

	@Override
	public List<AbstractGroup> queryExpertGroups(String examId, String examRoom, int page) {
		return this.groupDao.query(examId, examRoom, false, page);
	}

}
