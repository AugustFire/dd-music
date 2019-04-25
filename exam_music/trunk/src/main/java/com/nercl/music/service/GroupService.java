package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.user.AbstractGroup;
import com.nercl.music.entity.user.ExamineeGroup;

public interface GroupService {

	AbstractGroup get(String id);

	List<AbstractGroup> getExamineeGroups();

	List<AbstractGroup> getExpertGroups();

	boolean save(String title, String examId, boolean isExamineeGroup);

	boolean relatedExamineeExpertGroups(String[] examineeGroupIds, String[] expertGroupIds);

	List<ExamineeGroup> getExamineeGroupsByExpertId(String expertId);

	boolean update(String id, String title);

	List<AbstractGroup> queryExamineeGroups(String examId, String examRoom, int page);

	List<AbstractGroup> queryExpertGroups(String examId, String examRoom, int page);

}
