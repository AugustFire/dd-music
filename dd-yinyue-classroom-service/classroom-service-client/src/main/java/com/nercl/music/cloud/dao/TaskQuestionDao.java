package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.TaskQuestion;

public interface TaskQuestionDao extends BaseDao<TaskQuestion, String> {

	List<TaskQuestion> get(String tid);

	List<TaskQuestion> get(List<String> tids);

}
