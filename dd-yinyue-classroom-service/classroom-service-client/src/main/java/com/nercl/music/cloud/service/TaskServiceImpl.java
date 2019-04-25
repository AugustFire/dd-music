package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.dao.TaskDao;
import com.nercl.music.cloud.entity.classroom.ClassRoom;
import com.nercl.music.cloud.entity.classroom.Task;
import com.nercl.music.cloud.entity.classroom.TaskQuestion;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.util.page.PaginateSupportArray;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private TaskQuestionService taskQuestionService;

	@Autowired
	private ClassRoomUserRelationService classRoomUserRelationService;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Task get(String tid) {
		return taskDao.findByID(tid);
	}

	@Override
	public int assignNewTask(List<Task> listTask) {
		Assert.notNull(listTask, "listTask is null");
		listTask.forEach(lt -> {
			taskDao.save(lt);
		});
		return listTask.size();
	}

	@Override
	public String save(Task task) {
		taskDao.save(task);
		return task.getId();
	}

	@Override
	public String save(Task task, String[] qids) {
		taskDao.save(task);
		if (null == qids || qids.length < 1) {
			return task.getId();
		}
		Stream.of(qids).forEach(qid -> {
			TaskQuestion taskQuestion = new TaskQuestion();
			taskQuestion.setQuestionId(qid);
			taskQuestion.setTaskId(task.getId());
			taskQuestionService.save(taskQuestion);
		});
		return task.getId();
	}

	@Override
	public void delete(String taskId) throws Exception {
		Task task = taskDao.findByID(taskId);
		TaskQuestion taskQuestion = new TaskQuestion();
		taskQuestion.setTaskId(taskId);
		List<TaskQuestion> list = taskQuestionService.findByConditions(taskQuestion);
		if (null != list && !list.isEmpty()) {
			list.forEach(l -> {
				taskQuestionService.delete(l.getId());
			});
		}
		taskDao.delete(task);
	}

	@Override
	public void update(Task task) {
		taskDao.update(task);
	}

	@Override
	public PaginateSupportArray<Task> get(String classroomId, String chapterId, int page, int pageSize) {
		return taskDao.get(classroomId, chapterId, page, pageSize);
	}

	@Override
	public Task findById(String taskId) {
		return taskDao.findByID(taskId);
	}

	@Override
	public void update(Task task, List<String> questionIds) {
		taskDao.update(task);
		questionIds.forEach(questionId -> {
			TaskQuestion taskQuestion = new TaskQuestion();
			taskQuestion.setQuestionId(questionId);
			taskQuestion.setTaskId(task.getId());
			taskQuestion.setTask(task);
			taskQuestionService.save(taskQuestion);
		});
	}

	@Override
	public Boolean autoMarkSelectQuestions(Task task) {
		// 更新作业状态为已批阅
		taskDao.update(task);

		// 批阅所有学生的作业中的选择题
		List<Map<String, Object>> students = classRoomUserRelationService.getJoinedStudents(task.getClassRoomId());
		HttpHeaders headers = new HttpHeaders(); // http请求头
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8); // 请求头设置属性
		Map<String, Object> body = Maps.newHashMap();
		body.put("students", students);
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(body, headers);
		restTemplate.postForObject(ApiClient.AUTO_MARK, requestEntity, Map.class, task.getId());

		if (null == students || students.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public List<Task> getTasks(String[] roomIds) {
		return taskDao.getTasks(roomIds);
	}

	@Override
	public List<String> getTaskQuestions(String cid, long beginAt, long endAt) {
		List<Task> tasks = taskDao.get(beginAt, endAt);
		if (null == tasks || tasks.isEmpty()) {
			return null;
		}
		List<String> qids = Lists.newArrayList();
		tasks.stream().filter(task -> {
			ClassRoom room = task.getClassRoom();
			return null != room && cid.equals(room.getClassesId());
		}).forEach(task -> {
			List<TaskQuestion> tqs = taskQuestionService.get(task.getId());
			if (null == tqs || tqs.isEmpty()) {
				return;
			}
			tqs.forEach(tq -> {
				qids.add(tq.getQuestionId());
			});
		});
		return qids;
	}

	@Override
	public List<String> getTaskQuestions(String[] cids) {
		if (null == cids || cids.length < 1) {
			return null;
		}
		List<Task> tasks = getTasksByClasses(cids);
		if (null == tasks || tasks.isEmpty()) {
			return null;
		}
		List<String> tids = tasks.parallelStream().map(task -> task.getId()).collect(Collectors.toList());
		List<TaskQuestion> tqs = taskQuestionService.get(tids);
		if (null == tqs || tqs.isEmpty()) {
			return null;
		}
		return tqs.parallelStream().map(tq -> tq.getQuestionId()).collect(Collectors.toList());
	}

	@Override
	public List<Task> getTasksByClasses(String[] cids) {
		if (null == cids || cids.length < 1) {
			return null;
		}
		return taskDao.getTasksByClasses(cids);
	}

}
