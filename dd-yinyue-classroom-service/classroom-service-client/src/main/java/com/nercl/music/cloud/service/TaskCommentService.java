package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.TaskComment;

public interface TaskCommentService {

	void save(TaskComment tc);

	TaskComment get(String tid, String sid);

	/**
	 * 根据作业id查询作业批阅个数
	 */
	Integer getMarkedAmountByTaskId(String taskId);

	void update(TaskComment taskComment);

	List<TaskComment> getTaskComments(String[] cids);

}
