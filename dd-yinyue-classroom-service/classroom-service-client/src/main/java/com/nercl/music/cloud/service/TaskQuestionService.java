package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.TaskQuestion;

public interface TaskQuestionService {

	/**
	 * 删除作业中的试题
	 * 
	 * @param taskId
	 *            作业Id
	 * @param questionId
	 *            作业中题目Id
	 */
	void deleteTaskQuestions(String tid, String[] qids) throws Exception;

	/**
	 * 根据条件查询对应的TaskQuestion
	 */
	List<TaskQuestion> findByConditions(TaskQuestion tq) throws Exception;

	/**
	 * 在指定作业中新增题目
	 */
	String save(TaskQuestion taskQuestion);

	List<TaskQuestion> get(String tid);

	List<TaskQuestion> get(List<String> tids);

	void delete(String id);

}
