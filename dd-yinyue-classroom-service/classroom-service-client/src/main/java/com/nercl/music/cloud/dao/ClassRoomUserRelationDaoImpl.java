package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.classroom.ClassRoomUserRelation;

@Repository
public class ClassRoomUserRelationDaoImpl extends AbstractBaseDaoImpl<ClassRoomUserRelation, String>
		implements ClassRoomUserRelationDao {

	@Override
	public ClassRoomUserRelation get(String roomId, String uid) {
		String jpql = "from ClassRoomUserRelation crur where crur.classRoomId = ?1 and crur.studentId = ?2";
		List<ClassRoomUserRelation> crurs = this.executeQueryWithoutPaging(jpql, roomId, uid);
		return null != crurs && !crurs.isEmpty() ? crurs.get(0) : null;
	}

	@Override
	public List<ClassRoomUserRelation> get(String rid) {
		String jpql = "from ClassRoomUserRelation crur where crur.classRoomId = ?1";
		List<ClassRoomUserRelation> crurs = this.executeQueryWithoutPaging(jpql, rid);
		return crurs;
	}

	@Override
	public List<ClassRoomUserRelation> getUnGroupedUsers(String rid) {
		String jpql = " from ClassRoomUserRelation crur where crur.classRoomId = ?1 "
				+ "AND crur.studentId not in (select gu.userId from GroupUser gu where gu.classRoomId = ?2)";
		return this.executeQueryWithoutPaging(jpql, rid, rid);
	}
}
