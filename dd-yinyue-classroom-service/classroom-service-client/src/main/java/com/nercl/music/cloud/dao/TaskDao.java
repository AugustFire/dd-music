package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.Task;
import com.nercl.music.util.page.PaginateSupportArray;

public interface TaskDao extends BaseDao<Task, String> {

	/**
	 * 根据课堂Id和章节Id查询作业列表
	 */
	PaginateSupportArray<Task> get(String classroomId, String chapterId, int page, int pageSize);

	List<Task> getTasks(String[] roomIds);

	List<Task> get(long beginAt, long endAt);

	List<Task> getTasksByClasses(String[] cids);

}
