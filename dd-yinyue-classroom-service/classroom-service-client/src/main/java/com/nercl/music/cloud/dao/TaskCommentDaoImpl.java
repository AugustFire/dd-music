package com.nercl.music.cloud.dao;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.classroom.TaskComment;

@Repository
public class TaskCommentDaoImpl extends AbstractBaseDaoImpl<TaskComment, String> implements TaskCommentDao {

	@Override
	public TaskComment get(String tid, String sid) {
		String jpql = "from TaskComment tc where tc.taskId = ?1 and tc.studentId = ?2";
		List<TaskComment> tcs = this.executeQueryWithoutPaging(jpql, tid, sid);
		return null == tcs || tcs.isEmpty() ? null : tcs.get(0);
	}

	@Override
	public List<TaskComment> getByTaskId(String taskId) {
		String jpql = "from TaskComment tc where tc.taskId = ?1";
		return this.executeQueryWithoutPaging(jpql, taskId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaskComment> getTaskComments(String[] cids) {
		String jpql = "from TaskComment tc where tc.task.classRoom.classesId in ?1";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1, Stream.of(cids).collect(Collectors.toList()));
		return query.getResultList();
	}

}
