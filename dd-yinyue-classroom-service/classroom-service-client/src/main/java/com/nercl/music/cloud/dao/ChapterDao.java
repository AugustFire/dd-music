package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.Chapter;

public interface ChapterDao extends BaseDao<Chapter, String> {

	List<Chapter> getParentChaptersByClassRoom(String rid);

	List<Chapter> getChildrenChapters(String pid);

	List<Chapter> getByBook(String bid);

	/**
	 * 根据教材Id和父章节Id获取对应的章节
	 * 
	 * @param bid
	 *            教材Id不能送空
	 * @param parentId
	 *            父章节Id，可以送空，送空则查询顶层章节
	 */
	List<Chapter> getChapters(String bid, String parentId);

}
