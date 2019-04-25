package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.template.TemplateParentChapterBookClassRoom;

public interface TemplateParentChapterBookClassRoomService {

	TemplateParentChapterBookClassRoom getBookTemplate(String bookId, String chapterId);
	
	List<TemplateParentChapterBookClassRoom> getBookTemplate(String bookId);

	boolean hasBookTemplate(String bookId, String chapterId, String templateId);

	List<TemplateParentChapterBookClassRoom> getClassRoomTemplate(String classRoomId, String chapterId);

	boolean saveBookTemplate(String bookId, String chapterId, String templateId);

	boolean saveClassRoomTemplate(String classRoomId, String chapterId, String templateId);
	
	boolean hasClassRoomTemplate(String classRoomId, String chapterId, String templateId);

	void save(TemplateParentChapterBookClassRoom tpcbc);
	
	void deleteByClassroom(String roomId);
	
}
