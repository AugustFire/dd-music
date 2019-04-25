package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.group.Group;

@Repository
public class GroupDaoImpl extends AbstractBaseDaoImpl<Group, String> implements GroupDao {

	@Override
	public List<Group> get(String classRoomId) {
		String jpql = "from Group g where g.classRoomId = ?1";
		List<Group> groups = this.executeQueryWithoutPaging(jpql, classRoomId);
		return groups;
	}

}
