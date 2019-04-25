package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.template.TemplateParentChapterBookClassRoom;

public interface TemplateParentChapterBookClassRoomDao extends BaseDao<TemplateParentChapterBookClassRoom, String> {

	TemplateParentChapterBookClassRoom getBookTemplateWithBook(String bookId, String chapterId);

	TemplateParentChapterBookClassRoom getBookTemplateWithClassroom(String chapterId, String roomId);

	List<TemplateParentChapterBookClassRoom> getBookTemplate(String bookId);

	TemplateParentChapterBookClassRoom getBookTemplate(String bookId, String chapterId, String templateId);

	List<TemplateParentChapterBookClassRoom> getClassRoomTemplate(String classRoomId, String chapterId);

	TemplateParentChapterBookClassRoom getClassRoomTemplate(String classRoomId, String chapterId, String templateId);

	void deleteClassRoomTemplate(String classRoomId, String chapterId);
	
	List<TemplateParentChapterBookClassRoom> getByClassroom(String roomId);

}
