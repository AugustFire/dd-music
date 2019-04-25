package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.classroom.TaskTFile;

@Repository
public class TaskTFileDaoImpl extends AbstractBaseDaoImpl<TaskTFile, String> implements TaskTFileDao {

	@Override
	public List<TaskTFile> findTaskTFilesByTaskId(String taskId) {
		String jpql = "from TaskTFile ttf where ttf.taskId = ?1";
		return this.executeQueryWithoutPaging(jpql, taskId);
	}

}
