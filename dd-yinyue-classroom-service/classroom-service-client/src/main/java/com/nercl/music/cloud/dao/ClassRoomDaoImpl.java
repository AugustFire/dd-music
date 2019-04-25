package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.nercl.music.cloud.entity.classroom.ClassRoom;

@Repository
public class ClassRoomDaoImpl extends AbstractBaseDaoImpl<ClassRoom, String> implements ClassRoomDao {

	@Override
	public ClassRoom getByCode(String roomCode) {
		String jpql = "from ClassRoom room where room.code = ?1";
		List<ClassRoom> rooms = this.executeQueryWithoutPaging(jpql, roomCode);
		return null == rooms || rooms.isEmpty() ? null : rooms.get(0);
	}

	@Override
	public List<ClassRoom> getFoundedRooms(String uid) {
		String jpql = "from ClassRoom room where room.teacherId = ?1 order by room.createAt desc";
		List<ClassRoom> rooms = this.executeQueryWithoutPaging(jpql, uid);
		return rooms;
	}

	@Override
	public List<ClassRoom> getJoinedRooms(String uid) {
		String jpql = "select relation.classRoom from ClassRoomUserRelation relation where relation.studentId = ?1 order by relation.joinedAt desc";
		List<ClassRoom> rooms = this.executeQueryWithoutPaging(jpql, uid);
		return rooms;
	}

	@Override
	public List<ClassRoom> query(String teacherName, String title) {
		StringBuilder sb = new StringBuilder("from ClassRoom room where 1=1");
		List<Object> params = Lists.newArrayList();
		int i = 0;
		if (!Strings.isNullOrEmpty(teacherName)) {
			sb.append(" and room.teacherName like ?" + (++i));
			params.add("%" + teacherName + "%");
		}
		if (!Strings.isNullOrEmpty(title)) {
			sb.append(" and room.title like ?" + (++i));
			params.add("%" + title + "%");
		}
		sb.append(" order by room.createAt desc");
		List<ClassRoom> rooms = this.executeQueryWithoutPaging(sb.toString(), params.toArray());
		return rooms;
	}

}
