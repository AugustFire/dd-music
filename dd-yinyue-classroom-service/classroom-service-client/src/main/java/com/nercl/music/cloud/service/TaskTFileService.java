package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.TaskTFile;

public interface TaskTFileService {
	
	/**
	 * 根据taskId查询task关联的文件列表
	 * */
	List<TaskTFile> findTaskTFilesByTaskId(String taskId);
}
