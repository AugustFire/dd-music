package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.user.AbstractGroup;

public interface GroupDao extends BaseDao<AbstractGroup, String> {

	List<AbstractGroup> getExamineeGroups();

	List<AbstractGroup> getExpertGroups();

	List<AbstractGroup> query(String examId, String examRoom, boolean isExamineee, int page);

}
