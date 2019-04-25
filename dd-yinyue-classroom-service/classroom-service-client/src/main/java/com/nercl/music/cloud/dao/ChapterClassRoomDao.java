package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.ChapterClassRoom;

public interface ChapterClassRoomDao extends BaseDao<ChapterClassRoom, String> {

	ChapterClassRoom get(String chapterId, String classRoomId);

	List<ChapterClassRoom> get(String classRoomId);

	List<ChapterClassRoom> getParentChapters(String classRoomId);

	List<ChapterClassRoom> getChildrenChapters(String classRoomId, String chapterId);

}
