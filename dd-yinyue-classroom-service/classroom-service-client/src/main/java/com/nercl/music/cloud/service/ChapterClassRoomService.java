package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.Chapter;
import com.nercl.music.cloud.entity.classroom.ChapterClassRoom;

public interface ChapterClassRoomService {

	boolean save(String chapterId, String classRoomId);

	boolean isExsit(String chapterId, String classRoomId);

	boolean delete(String chapterId, String classRoomId);

	List<ChapterClassRoom> get(String classRoomId);

	ChapterClassRoom get(String classRoomId, String chapterId);

	List<Chapter> getParentChapters(String rid);

	Chapter getChapter(String rid, String cid);
	
	List<Chapter> getChildrenChapters(String classRoomId, String chapterId);

	/**
	 * 根据课堂id,删除课堂章节关系
	 * */
	int deleteClassroomChapters(String id);

}
