package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.cloud.entity.classroom.Chapter;

public interface ChapterService {

	boolean save(String rid, String title);

	boolean save(Chapter chapter);

	Chapter get(String id);

	List<Chapter> getChildrenChapters(String pid);

	List<Chapter> getByBook(String bid);

	void delete(String cid);

	/**
	 * 根据id删除章节及其子章节
	 * 
	 * @param id
	 *            章节id
	 */
	void deleteChapterWithChildren(String id);

	/**
	 * 更新实体
	 */
	void update(Chapter newChapter);

	/**
	 * 根据教材Id和父章节Id获取对应的章节
	 * 
	 * @param bid
	 *            教材Id不能送空
	 * @param parentId
	 *            父章节Id，可以送空，送空则查询顶层章节
	 */
	List<Chapter> getChapters(String bid, String parentId);

	List<Map<String, Object>> getChapters(String bid);

	String addSubChapter(String pid, String title);

	/**
	 * 根据Id查询Chapter
	 * */
	Chapter findById(String chapterId);

}
