package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.Task;
import com.nercl.music.util.page.PaginateSupportArray;

public interface TaskService {

	Task get(String tid);

	/**
	 * 新增作业
	 * 
	 */
	int assignNewTask(List<Task> listTask);

	/**
	 * 保存一个task
	 */
	String save(Task task);

	/**
	 * 保存作业和作业对应的试题
	 * 
	 * @param task
	 *            作业信息
	 * @param questionIds
	 *            作业对应题目列表
	 */
	String save(Task task, String[] qids);

	/**
	 * 删除作业
	 * 
	 */
	void delete(String taskId) throws Exception;

	/**
	 * 更新作业
	 * 
	 */
	void update(Task task);

	/**
	 * 查询当前章节的作业列表
	 * 
	 * @param classroomId
	 *            课堂Id
	 * @param chapterId
	 *            章节Id
	 * @param page
	 *            页码
	 * @param pageSize
	 *            页面大小
	 */
	PaginateSupportArray<Task> get(String classroomId, String chapterId, int page, int pageSize);

	List<String> getTaskQuestions(String cid, long beginAt, long endAt);

	List<String> getTaskQuestions(String[] cids);

	List<Task> getTasksByClasses(String[] cids);

	/**
	 * 根据Id查询作业
	 * 
	 * @param taskId
	 *            作业Id
	 */
	Task findById(String taskId);

	/**
	 * 更新作业及对应的题目
	 * 
	 * @param task
	 *            作业信息
	 * @param questionIds
	 *            作业对应题目列表
	 */
	void update(Task task, List<String> questionIds);

	/**
	 * 自动批阅某作业中的选择题
	 * 
	 * @param task
	 *            作业信息
	 */
	Boolean autoMarkSelectQuestions(Task task);

	List<Task> getTasks(String[] roomIds);
}
