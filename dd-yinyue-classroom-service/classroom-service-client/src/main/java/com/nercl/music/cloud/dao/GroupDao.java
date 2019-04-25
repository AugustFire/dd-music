package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.group.Group;

public interface GroupDao extends BaseDao<Group, String> {

	List<Group> get(String classRoomId);

}
