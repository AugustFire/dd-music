package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.TaskComment;

public interface TaskCommentDao extends BaseDao<TaskComment, String> {

	TaskComment get(String tid, String sid);

	List<TaskComment> getByTaskId(String taskId);

	List<TaskComment> getTaskComments(String[] cids);

}
