package com.nercl.music.cloud.dao;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.nercl.music.cloud.entity.classroom.Task;
import com.nercl.music.util.page.PaginateSupportArray;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class TaskDaoImpl extends AbstractBaseDaoImpl<Task, String> implements TaskDao {

	@Override
	public PaginateSupportArray<Task> get(String classroomId, String chapterId, int page, int pageSize) {
		if (Strings.isNullOrEmpty(chapterId)) {
			String jpql = "from Task t where t.classRoomId = ?1 order by t.createAt desc";
			int count = this.executeCountQuery("select count(*) " + jpql.toString(), classroomId);
			List<Task> listTasks = this.executeQueryWithPaging(jpql.toString(), page, pageSize, classroomId);
			return PaginateSupportUtil.pagingList(listTasks, page, pageSize, count);
		}
		String jpql = "from Task t where t.classRoomId = ?1 and t.chapterId = ?2 order by t.createAt desc";
		int count = this.executeCountQuery("select count(*) " + jpql.toString(), classroomId,chapterId);
		List<Task> listTasks = this.executeQueryWithPaging(jpql.toString(), page, pageSize, classroomId,chapterId);
		return PaginateSupportUtil.pagingList(listTasks, page, pageSize, count);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Task> getTasks(String[] roomIds) {
		String jpql = "from Task t where t.classRoomId in ?1";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1, Stream.of(roomIds).collect(Collectors.toList()));
		return query.getResultList();
	}

	@Override
	public List<Task> get(long beginAt, long endAt) {
		String jpql = "from Task t where t.createAt >= ?1 and t.dendline <= ?2";
		return this.executeQueryWithoutPaging(jpql, beginAt, endAt);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Task> getTasksByClasses(String[] cids) {
		String jpql = "from Task t where t.classRoom.classesId in ?1";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1, Stream.of(cids).collect(Collectors.toList()));
		return query.getResultList();
	}
}
