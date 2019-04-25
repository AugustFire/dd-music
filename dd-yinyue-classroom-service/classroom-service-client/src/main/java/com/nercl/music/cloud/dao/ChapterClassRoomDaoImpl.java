package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.classroom.ChapterClassRoom;

@Repository
public class ChapterClassRoomDaoImpl extends AbstractBaseDaoImpl<ChapterClassRoom, String>
		implements ChapterClassRoomDao {

	@Override
	public ChapterClassRoom get(String classRoomId , String chapterId) {
		String jpql = "from ChapterClassRoom ccr where ccr.chapterId = ?1 and ccr.classRoomId = ?2 and ccr.deleted = ?3";
		List<ChapterClassRoom> ccrs = this.executeQueryWithoutPaging(jpql, chapterId, classRoomId, false);
		return null == ccrs || ccrs.isEmpty() ? null : ccrs.get(0);
	}

	@Override
	public List<ChapterClassRoom> get(String classRoomId) {
		String jpql = "from ChapterClassRoom ccr where ccr.classRoomId = ?1 and ccr.deleted = ?2";
		List<ChapterClassRoom> ccrs = this.executeQueryWithoutPaging(jpql, classRoomId, false);
		return ccrs;
	}

	@Override
	public List<ChapterClassRoom> getParentChapters(String classRoomId) {
		String jpql = "select ccr from ChapterClassRoom ccr inner join ccr.chapter c where ccr.classRoomId = ?1 and c.parentId is null and ccr.deleted = ?2";
		List<ChapterClassRoom> ccrs = this.executeQueryWithoutPaging(jpql, classRoomId, false);
		return ccrs;
	}

	@Override
	public List<ChapterClassRoom> getChildrenChapters(String classRoomId, String chapterId) {
		String jpql = "select ccr from ChapterClassRoom ccr inner join ccr.chapter c where ccr.classRoomId = ?1 and c.parentId = ?2 and ccr.deleted = ?3";
		List<ChapterClassRoom> ccrs = this.executeQueryWithoutPaging(jpql, classRoomId, chapterId, false);
		return ccrs;
	}

}
