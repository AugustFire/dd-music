package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.TaskCommentDao;
import com.nercl.music.cloud.entity.classroom.TaskComment;

@Service
@Transactional
public class TaskCommentServiceImpl implements TaskCommentService {

	@Autowired
	private TaskCommentDao taskCommentDao;

	@Override
	public void save(TaskComment tc) {
		tc.setCreateAt(System.currentTimeMillis());
		taskCommentDao.save(tc);
	}

	@Override
	public TaskComment get(String tid, String sid) {
		return taskCommentDao.get(tid, sid);
	}

	@Override
	public Integer getMarkedAmountByTaskId(String taskId) {
		List<TaskComment> commens = taskCommentDao.getByTaskId(taskId);
		if (commens != null && !commens.isEmpty()) {
			return commens.size();
		}
		return 0;
	}

	@Override
	public void update(TaskComment taskComment) {
		taskCommentDao.update(taskComment);
	}

	@Override
	public List<TaskComment> getTaskComments(String[] cids) {
		return taskCommentDao.getTaskComments(cids);
	}

}
