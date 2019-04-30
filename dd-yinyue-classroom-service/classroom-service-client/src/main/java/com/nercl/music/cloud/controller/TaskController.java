package com.nercl.music.cloud.controller;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.base.ClassUser;
import com.nercl.music.cloud.entity.classroom.Chapter;
import com.nercl.music.cloud.entity.classroom.ClassRoom;
import com.nercl.music.cloud.entity.classroom.Task;
import com.nercl.music.cloud.entity.classroom.Task.TaskType;
import com.nercl.music.cloud.entity.classroom.TaskComment;
import com.nercl.music.cloud.entity.classroom.TaskComment.EunmStar;
import com.nercl.music.cloud.entity.classroom.TaskQuestion;
import com.nercl.music.cloud.service.ChapterService;
import com.nercl.music.cloud.service.ClassRoomService;
import com.nercl.music.cloud.service.ClassRoomUserRelationService;
import com.nercl.music.cloud.service.ClassUserService;
import com.nercl.music.cloud.service.TaskCommentService;
import com.nercl.music.cloud.service.TaskQuestionService;
import com.nercl.music.cloud.service.TaskService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;
import com.nercl.music.util.CommonUtils;
import com.nercl.music.util.page.PaginateSupportArray;

@RestController
public class TaskController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ClassRoomService classRoomService;

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskQuestionService taskQuestionService;

	@Autowired
	private ClassUserService classUserService;

	@Autowired
	private TaskCommentService taskCommentService;

	@Autowired
	private ClassRoomUserRelationService classRoomUserRelationService;

	@GetMapping(value = "/task/{tid}", produces = JSON_PRODUCES)
	public Map<String, Object> get(@PathVariable String tid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "tid is null");
			return ret;
		}
		Task task = taskService.get(tid);
		ret.put("code", CList.Api.Client.OK);
		if (null == task) {
			return ret;
		}
		ret.put("tid", task.getId());
		ret.put("title", task.getTitle());
		ret.put("task_type", task.getTaskType());
		ret.put("create_at", task.getCreateAt());
		ret.put("dendline", task.getDendline());
		ret.put("class_room_id", task.getClassRoomId());
		ret.put("chapter_id", task.getChapterId());
		return ret;
	}

	/**
	 * 布置作业
	 */
	@PostMapping(value = "/task", produces = JSON_PRODUCES)
	public Map<String, Object> add(String title, String classRoomId, String chapterId, Long dendline, String[] qids,
			String taskType) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(classRoomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroomId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(chapterId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapterId is null");
			return ret;
		}
		ClassRoom room = classRoomService.findById(classRoomId);
		if (null == room) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "ClassRoom not exist");
			return ret;
		}
		Chapter chapter = chapterService.findById(chapterId);
		if (null == chapter) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapter not exist");
			return ret;
		}
		taskType = Strings.isNullOrEmpty(taskType) ? "IN_CLASSROOM" : taskType;
		TaskType valueOfTaskType = null;
		try {
			valueOfTaskType = TaskType.valueOf(taskType);
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", " No enum constant" + taskType);
			return ret;
		}
		Task task = new Task();
		task.setTitle(title);
		task.setChapterId(chapterId);
		task.setClassRoomId(classRoomId);
		task.setTaskType(valueOfTaskType);
		task.setDendline(dendline);
		task.setCreateAt(Instant.now().toEpochMilli());
		String taskId = taskService.save(task, qids);
		ret.put("code", CList.Api.Client.OK);
		ret.put("taskId", taskId);
		return ret;
	}

	/**
	 * 新增题目
	 */
	@PostMapping(value = "/task/{tid}/questions", produces = JSON_PRODUCES)
	public Map<String, Object> addQuestions(@PathVariable String tid, String[] qids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == qids || qids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "qids is null");
			return ret;
		}
		Task task = taskService.findById(tid);
		if (null == task) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "task not exist");
			return ret;
		}
		Stream.of(qids).forEach(qid -> {
			TaskQuestion tq = new TaskQuestion();
			tq.setTaskId(tid);
			tq.setQuestionId(qid);
			taskQuestionService.save(tq);
		});
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 删除题目
	 * 
	 * @param qid
	 *            题目 Id
	 * @param taskId
	 *            作业Id
	 */
	@DeleteMapping(value = "/task/{tid}/questions", produces = JSON_PRODUCES)
	public Map<String, Object> deleteQuestions(@PathVariable String tid, String[] qids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == qids || qids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "qids is null");
			return ret;
		}
		try {
			taskQuestionService.deleteTaskQuestions(tid, qids);
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "delete fail");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 删除作业
	 * 
	 * @param qid
	 *            题目 Id
	 * @param taskId
	 *            作业Id
	 */
	@DeleteMapping(value = "/task/{tid}", produces = JSON_PRODUCES)
	public Map<String, Object> delete(@PathVariable String tid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "taskId is null");
			return ret;
		}
		try {
			taskService.delete(tid);
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "delete fail");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 修改作业
	 */
	@PutMapping(value = "/task/{tid}", produces = JSON_PRODUCES)
	public Map<String, Object> update(@PathVariable String tid, Task task) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "taskId is null");
			return ret;
		}
		Task tsk = taskService.findById(tid);
		if (null == tsk) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "task not exist");
			return ret;
		}
		try {
			CommonUtils.copyProperties(task, tsk, Lists.newArrayList("title", "taskType", "dendline"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		tsk.setCreateAt(Instant.now().toEpochMilli());
		taskService.update(tsk);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/tasks", produces = JSON_PRODUCES)
	public Map<String, Object> get(@RequestParam(value = "classroomId") String classroomId, String chapterId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(classroomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroomId is null");
			return ret;
		}
		List<Task> tasks = taskService.get(classroomId, chapterId, 1, 999999);
		ret.put("code", CList.Api.Client.OK);
		if (null == tasks || tasks.isEmpty()) {
			return ret;
		}
		List<Map<String, Object>> ts = tasks.stream().map(task -> {
			Map<String, Object> t = Maps.newHashMap();
			t.put("id", task.getId());
			t.put("title", task.getTitle());
			t.put("task_type", task.getTaskType());
			t.put("create_at", task.getCreateAt());
			t.put("dend_line", task.getDendline());
			t.put("chapter_id", task.getChapterId());
			t.put("chapter_title", task.getChapter().getTitle());

			List<TaskQuestion> tqs = taskQuestionService.get(task.getId());
			List<String> qids = tqs.stream().map(TaskQuestion::getQuestionId).collect(Collectors.toList());
			if (null != qids && !qids.isEmpty()) {
				Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_QUESTIONS, Map.class,
						Joiner.on(",").join(qids));
				if (null != res && null != res.get("questions")) {
					t.put("questions", res.get("questions"));
				}
			}
			return t;
		}).collect(Collectors.toList());
		ret.put("tasks", ts);
		return ret;
	}

	/**
	 * 查询某个学作业中的所有题目及答案
	 * 
	 * @param userId
	 *            学生id
	 * @param taskId
	 *            作业id
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping(value = "/task_answers/{taskId}", produces = JSON_PRODUCES)
	public Map<String, Object> queryQeustionAnswers(@PathVariable String taskId, String userId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(taskId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "taskId is null");
			return ret;
		}
		Task task = taskService.findById(taskId);
		if (null == task) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "task is not exist");
			return ret;
		}
		List<Map<String, Object>> results = Lists.newArrayList();
		try {
			TaskQuestion tq = new TaskQuestion();
			tq.setTaskId(taskId);
			// 查询task下的所有题目
			List<TaskQuestion> list = taskQuestionService.findByConditions(tq);
			list.forEach(li -> {
				Map<String, Object> questionAnswerMap = Maps.newHashMap();
				// 根据问题Id
				Map map = restTemplate.getForObject(ApiClient.GET_USER_QUESTION, Map.class, li.getQuestionId(), userId);
				map.remove("code");
				questionAnswerMap.put("question", map);
				try {
					Map<String, Object> answers = restTemplate.getForObject(ApiClient.GET_USER_ANSWER, Map.class,
							li.getQuestionId(), userId,taskId);
					if (null != answers) {
						Map<String, Object> an = (Map<String, Object>) answers.get("answer");
						if (null != an) {
							an.remove("question");
							questionAnswerMap.put("answer", an);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				results.add(questionAnswerMap);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.put("taskAnswer", results);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 给学生作答点评
	 * 
	 * @param score
	 *            评分分数
	 * @param comment
	 *            点评内容
	 */
	@PutMapping(value = "/task/mark/{answerRecordId}", produces = JSON_PRODUCES)
	public Map<String, Object> markTaskAnswer(@PathVariable String answerRecordId, Float score, String comment) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(answerRecordId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "answerRecordId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(comment)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "comment is null");
			return ret;
		}
		HttpHeaders headers = new HttpHeaders(); // http请求头
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8); // 请求头设置属性
		HttpEntity<String> requestEntity = new HttpEntity<String>(comment, headers); // comment字段内容可能很长（可能超出url长度限制）,所以一定要放在http请求的请求体里面
		restTemplate.put(ApiClient.MARK_ANSWER, requestEntity, answerRecordId, score); // score字段比较短,可以放在url上面
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 学生答题
	 * 
	 * @param uid
	 *            学生id
	 * @param answers
	 *            <p>
	 *            answers这个map中包含2个key-value对，key为“taskId”的字符串和key为“answers”的
	 *            {@link java.util.List List}。
	 *            taskId对应的值是作业id，而answers对应的值是此作业中所有题目及其相应的答案。
	 *            </p>
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/task/commit", produces = JSON_PRODUCES)
	public Map<String, Object> commitAnswers(@RequestBody Map<String, Object> answers) {
		Map<String, Object> ret = Maps.newHashMap();
		String uid = (String) answers.get("uid");
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "userId is null");
			return ret;
		}
		String taskId = (String) answers.get("taskId");
		if (Strings.isNullOrEmpty(taskId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "taskId is null");
			return ret;
		}
		Task task = taskService.findById(taskId);
		if (null == task) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "task not exist");
			return ret;
		}
		Long now = Instant.now().toEpochMilli();
		// 过了最后期限就不能提交作业了
		if (now > task.getDendline()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "task is out of date");
			return ret;
		}
		taskService.update(task);
		List<Map<String, Object>> answersList = (List<Map<String, Object>>) answers.get("answers");
		if (null == answersList || answersList.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "answers is null");
			return ret;
		}
		List<ClassUser> classUser = Lists.newArrayList();
		ClassUser cs = new ClassUser();
		cs.setUserId(uid);
		cs.setIsTeacher(false);
		try {
			classUser = classUserService.findByCondionts(cs);
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "ClassUser is null");
			return ret;
		}
		// 提交答案
		HttpHeaders headers = new HttpHeaders(); // http请求头
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8); // 请求头设置属性
		Map<String, Object> body = Maps.newHashMap();
		body.put("answersList", answersList);
		body.put("classUser", classUser);
		body.put("answerSource", "TASK");
		body.put("fullScore", "100");
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(body, headers);
		restTemplate.postForObject(ApiClient.COMMIT_ANSWER, requestEntity, Map.class, taskId, uid);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 根据课堂和章节查询作业概要信息列表
	 * 
	 * @param classroomId
	 *            课堂Id
	 * @param chapterId
	 *            章节Id，可以传空，传空查询所有课堂内的作业
	 * @param page
	 *            页码
	 * @param pageSize
	 *            页面大小
	 * @return 返回作业列表信息
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/task_list", produces = JSON_PRODUCES)
	public Map<String, Object> taskList(String classroomId, String chapterId, int page,
			@RequestParam(value = "page_size") int pageSize) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(classroomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroomId is null");
			return ret;
		}
		PaginateSupportArray<Task> taskList = taskService.get(classroomId, chapterId, page, pageSize);
		ClassRoom room = classRoomService.findById(classroomId);
		int studentNum = classUserService.getStudentNum(room.getClassesId()); // 课堂中的学生人数
		List<Map<String, Object>> taskInfoList = Lists.newArrayList();
		taskList.forEach(task -> {
			Map<String, Object> taskInfoMap = Maps.newHashMap();
			taskInfoMap.put("total_amount", studentNum);
			Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_TASK_AMOUNT, Map.class, task.getId());
			if (null != res && null != res.get("commit_amount")) {
				taskInfoMap.put("commit_amount", (Integer) res.get("commit_amount"));
			}
			Integer markedAmount = taskCommentService.getMarkedAmountByTaskId(task.getId());
			taskInfoMap.put("marked_amount", markedAmount);
			taskInfoMap.put("task_id", task.getId());
			taskInfoMap.put("task_title", task.getTitle());
			taskInfoMap.put("chapter_id", task.getChapterId());
			taskInfoMap.put("classroom_id", task.getClassRoomId());
			taskInfoMap.put("task_type", task.getTaskType());
			taskInfoMap.put("dendline", task.getDendline());
			taskInfoMap.put("create_at", task.getCreateAt());
			taskInfoList.add(taskInfoMap);
		});
		ret.put("page", taskList.getPage());
		ret.put("page_size", taskList.getPageSize());
		ret.put("total", taskList.getTotal());
		ret.put("task_info_list", taskInfoList);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/task/completion", produces = JSON_PRODUCES)
	public Map<String, Object> taskListStudent(String classroomId, String chapterId, String studentId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(classroomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroomId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(studentId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "studentId is null");
			return ret;
		}
		List<Task> tasks = taskService.get(classroomId, chapterId, 1, 999999);
		ret.put("code", CList.Api.Client.OK);
		if (null == tasks || tasks.isEmpty()) {
			ret.put("tasks", null);
			return ret;
		}
		List<Map<String, Object>> ts = tasks.stream().map(task -> {
			Map<String, Object> t = Maps.newHashMap();
			t.put("id", task.getId());
			t.put("title", task.getTitle());
			t.put("task_type", task.getTaskType());
			t.put("create_at", task.getCreateAt());
			t.put("dend_line", task.getDendline());
			TaskComment comment = taskCommentService.get(task.getId(), studentId);
			if (null != comment) {
				t.put("is_marked", true);
				t.put("comment", comment.getComment());
				t.put("star", comment.getEunmStar());
			} else {
				t.put("is_marked", false);
			}
			Map<String, Object> answers = restTemplate.getForObject(ApiClient.GET_TASK_ANSWERS, Map.class, task.getId(),
					studentId);
			if (null != answers && null != answers.get("is_commited")) {
				t.put("is_commited", answers.get("is_commited"));
			}
			t.put("chapter_id", task.getChapterId());
			t.put("chapter_title", task.getChapter().getTitle());
			return t;
		}).collect(Collectors.toList());
		ret.put("tasks", ts);
		return ret;
	}

	/**
	 * 根据taskId查询问题详情
	 * 
	 * @param taskId
	 *            试题Id
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/task_questions/{taskId}", produces = JSON_PRODUCES)
	public Map<String, Object> taskQuestionList(@PathVariable String taskId) {
		Map<String, Object> ret = Maps.newHashMap();
		// 查询对应的Task
		Task task = taskService.findById(taskId);
		if (null == task) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "task not exist");
			return ret;
		}
		try {
			TaskQuestion tq = new TaskQuestion();
			tq.setTaskId(taskId);
			List<TaskQuestion> taskQuestionList = taskQuestionService.findByConditions(tq);
			List<Map<String, Object>> questionList = Lists.newArrayList();
			taskQuestionList.forEach(t -> {
				Map<String, Object> map = restTemplate.getForObject(ApiClient.GET_QUESTION, Map.class,
						t.getQuestionId());
				questionList.add(map);
			});
			ret.put("code", CList.Api.Client.OK);
			ret.put("questions", questionList);
			return ret;
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "inner exception");
			return ret;
		}
	}

	/**
	 * <p>
	 * 教师一键批阅某作业中的所有选择题
	 * </p>
	 * 
	 * @param taskId
	 *            作业id
	 */
	@PutMapping(value = "/task_mark/{taskId}", produces = JSON_PRODUCES)
	public Map<String, Object> taskMark(@PathVariable String taskId) {
		Map<String, Object> ret = Maps.newHashMap();
		Task task = taskService.findById(taskId);
		if (null == task) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "task not exist");
			return ret;
		}
		taskService.autoMarkSelectQuestions(task);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@GetMapping(value = "/tasks2", produces = JSON_PRODUCES)
	public Map<String, Object> getTasks(String[] rids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == rids || rids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rids is null");
			return ret;
		}
		List<Task> tasks = taskService.getTasks(rids);
		if (null == tasks || tasks.isEmpty()) {
			return ret;
		}
		List<Map<String, Object>> ts = tasks.stream().map(task -> {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", task.getId());
			map.put("title", task.getTitle());
			map.put("chapter_id", task.getChapterId());
			map.put("dendline", task.getDendline());
			return map;
		}).collect(Collectors.toList());
		ret.put("tasks", ts);
		return ret;
	}

	/**
	 * 给某作业评价和评星
	 */
	@PostMapping(value = "/task/{tid}/comment", produces = JSON_PRODUCES)
	public Map<String, Object> postTaskComment(@PathVariable String tid, String comment, String star, String studentId,
			String teacherId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "tid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(studentId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "studentId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(teacherId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "teacherId is null");
			return ret;
		}
		EunmStar eunmStar = null;
		if (!Strings.isNullOrEmpty(star)) {
			if (!EunmStar.isDefined(star)) {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "star is error");
				return ret;
			}
			eunmStar = EunmStar.valueOf(star);
		}
		// 是否批阅过
		TaskComment taskComment = taskCommentService.get(tid, studentId);
		if (taskComment == null) { // 没有批阅则批阅
			TaskComment tc = new TaskComment();
			tc.setComment(Strings.nullToEmpty(comment));
			tc.setStudentId(studentId);
			tc.setTeacherId(teacherId);
			tc.setTaskId(tid);
			tc.setEunmStar(eunmStar);
			taskCommentService.save(tc);
		} else { // 批阅过则更新
			taskComment.setComment(Strings.nullToEmpty(comment));
			taskComment.setEunmStar(eunmStar);
			taskCommentService.update(taskComment);
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@GetMapping(value = "/task/{tid}/comment", produces = JSON_PRODUCES)
	public Map<String, Object> getTaskComment(@PathVariable String tid, String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "tid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		TaskComment tc = taskCommentService.get(tid, sid);
		ret.put("code", CList.Api.Client.OK);
		if (null == tc) {
			return ret;
		}
		ret.put("comment", tc.getComment());
		ret.put("eunm_star", tc.getEunmStar());
		return ret;
	}

	/**
	 * 根据课堂和章节查询章节下的作业数量
	 * 
	 * @param classroomId
	 *            课堂Id
	 * @param chapterId
	 *            章节Id，可以传空，传空查询所有课堂内的作业数量
	 */
	@GetMapping(value = "/task/amount", produces = JSON_PRODUCES)
	public Map<String, Object> taskAmount(String classroomId, String chapterId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(classroomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroomId is null");
			return ret;
		}
		List<Task> tasks = taskService.get(classroomId, chapterId, 1, 999999);
		if (tasks == null || tasks.isEmpty()) {
			ret.put("amount", 0);
		} else {
			ret.put("amount", tasks.size());
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 查询成员的作业提交情况
	 * 
	 * @param taskId
	 *            作业Id
	 * @return 返回作业成员及其作业的提交/批阅情况
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/task_student_details", produces = JSON_PRODUCES)
	public Map<String, Object> taskStudentDetails(String taskId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(taskId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "taskId is null");
			return ret;
		}
		Task task = taskService.findById(taskId);
		ClassRoom classRoom = task.getClassRoom();
		List<Map<String, Object>> students = classRoomUserRelationService.getJoinedStudents(classRoom.getId());
		if (students == null || students.isEmpty()) {
			ret.put("users", null);
		} else {
			students.forEach(user -> {
				String uid = (String) user.get("id");
				Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_TASK_ANSWERS, Map.class, taskId, uid);
				if (null != res && null != res.get("is_commited")) {
					user.put("is_commited", (Boolean) res.get("is_commited"));
				}
				user.put("is_marked", null != taskCommentService.get(taskId, uid));
			});
			ret.put("users", students);
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@GetMapping(value = "/task/questions", produces = JSON_PRODUCES)
	public Map<String, Object> getTaskQuestions(String cid, Long beginAt, Long endAt) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		if (null == beginAt || beginAt <= 0 || null == endAt || endAt <= 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "beginAt or endAt is null");
			return ret;
		}
		List<String> qids = taskService.getTaskQuestions(cid, beginAt, endAt);
		ret.put("code", CList.Api.Client.OK);
		if (qids == null || qids.isEmpty()) {
			return ret;
		}
		ret.put("qids", qids);
		return ret;
	}

	@GetMapping(value = "/tasks", params = { "cids" }, produces = JSON_PRODUCES)
	public Map<String, Object> getTasksByClasses(String[] cids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == cids || cids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cids is null");
			return ret;
		}
		List<Task> tasks = taskService.getTasksByClasses(cids);
		ret.put("code", CList.Api.Client.OK);
		if (null == tasks || tasks.isEmpty()) {
			return ret;
		}
		List<Map<String, Object>> ts = tasks.parallelStream().map(task -> {
			Map<String, Object> m = Maps.newHashMap();
			m.put("id", task.getId());
			m.put("title", task.getTitle());
			return m;
		}).collect(Collectors.toList());
		ret.put("tasks", ts);
		return ret;
	}

	@GetMapping(value = "/task_comments", params = { "cids" }, produces = JSON_PRODUCES)
	public Map<String, Object> getTaskComments(String[] cids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == cids || cids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cids is null");
			return ret;
		}
		List<TaskComment> comments = taskCommentService.getTaskComments(cids);
		ret.put("code", CList.Api.Client.OK);
		if (null == comments || comments.isEmpty()) {
			return ret;
		}
		List<Map<String, Object>> cms = comments.parallelStream().map(comment -> {
			Map<String, Object> m = Maps.newHashMap();
			m.put("id", comment.getId());
			m.put("comment", comment.getComment());
			return m;
		}).collect(Collectors.toList());
		ret.put("comments", cms);
		return ret;
	}

	@GetMapping(value = "/task/questions", params = { "cids" }, produces = JSON_PRODUCES)
	public Map<String, Object> getTaskQuestions(String[] cids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == cids || cids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cids is null");
			return ret;
		}
		List<String> qids = taskService.getTaskQuestions(cids);
		ret.put("code", CList.Api.Client.OK);
		if (null == qids || qids.isEmpty()) {
			return ret;
		}
		ret.put("qids", qids);
		return ret;
	}
}