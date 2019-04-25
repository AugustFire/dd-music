package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.group.GroupUser;

@Repository
public class GroupUserDaoImpl extends AbstractBaseDaoImpl<GroupUser, String> implements GroupUserDao {

	@Override
	public List<GroupUser> getStudents(String groupId) {
		String jpql = "from GroupUser gu where gu.groupId = ?1";
		List<GroupUser> groupUsers = this.executeQueryWithoutPaging(jpql, groupId);
		return groupUsers;
	}

	@Override
	public GroupUser get(String userId, String groupId) {
		String jpql = "from GroupUser gu where gu.userId = ?1 and gu.groupId = ?2";
		List<GroupUser> groupUsers = this.executeQueryWithoutPaging(jpql, userId, groupId);
		return null != groupUsers && !groupUsers.isEmpty() ? groupUsers.get(0) : null;
	}

	@Override
	public GroupUser getLeader(String groupId) {
		String jpql = "from GroupUser gu where gu.groupId = ?1 and gu.isLeader = ?2";
		List<GroupUser> groupUsers = this.executeQueryWithoutPaging(jpql, groupId, true);
		return null == groupUsers || groupUsers.isEmpty() ? null : groupUsers.get(0);
	}

}
