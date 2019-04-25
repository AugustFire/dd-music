package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.TaskTFileDao;
import com.nercl.music.cloud.entity.classroom.TaskTFile;

@Service
@Transactional
public class TaskTFileServiceImpl implements TaskTFileService {

	@Autowired
	private TaskTFileDao taskTFileDao;
	
	@Override
	public List<TaskTFile> findTaskTFilesByTaskId(String taskId) {
		return taskTFileDao.findTaskTFilesByTaskId(taskId);
	}

}
