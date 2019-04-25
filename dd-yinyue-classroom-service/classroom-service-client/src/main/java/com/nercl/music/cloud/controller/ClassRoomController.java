package com.nercl.music.cloud.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.base.ClassUser;
import com.nercl.music.cloud.entity.base.Classes;
import com.nercl.music.cloud.entity.base.Grade;
import com.nercl.music.cloud.entity.classroom.Book;
import com.nercl.music.cloud.entity.classroom.Chapter;
import com.nercl.music.cloud.entity.classroom.ChapterClassRoom;
import com.nercl.music.cloud.entity.classroom.ClassRoom;
import com.nercl.music.cloud.entity.classroom.Notice;
import com.nercl.music.cloud.entity.classroom.VersionDesc;
import com.nercl.music.cloud.entity.template.ClassroomTemplateChapterStatus;
import com.nercl.music.cloud.entity.template.TemplateChapterResource;
import com.nercl.music.cloud.entity.template.TemplateParentChapterBookClassRoom;
import com.nercl.music.cloud.service.BookService;
import com.nercl.music.cloud.service.ChapterClassRoomService;
import com.nercl.music.cloud.service.ChapterService;
import com.nercl.music.cloud.service.ClassRoomService;
import com.nercl.music.cloud.service.ClassRoomUserRelationService;
import com.nercl.music.cloud.service.ClassUserService;
import com.nercl.music.cloud.service.ClassesService;
import com.nercl.music.cloud.service.ClassroomTemplateChapterStatusService;
import com.nercl.music.cloud.service.GradeService;
import com.nercl.music.cloud.service.NoticeService;
import com.nercl.music.cloud.service.TemplateChapterResourceService;
import com.nercl.music.cloud.service.TemplateParentChapterBookClassRoomService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;
import com.nercl.music.util.CloudFileUtil;
import com.nercl.music.util.CommonUtils;
import com.nercl.music.websocket.WebSocketSessionManager;

@RestController
public class ClassRoomController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ClassRoomService classRoomService;

	@Autowired
	private ClassRoomUserRelationService classRoomUserRelationService;

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private WebSocketSessionManager sessionManager;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ChapterClassRoomService chapterClassRoomService;

	@Autowired
	private CloudFileUtil cloudFileUtil;

	@Autowired
	private ClassesService classesService;

	@Autowired
	private GradeService gradeService;

	@Autowired
	private BookService bookService;

	@Autowired
	private TemplateChapterResourceService templateChapterResourceService;

	@Autowired
	private ClassroomTemplateChapterStatusService classroomTemplateChapterStatusService;

	@Autowired
	private TemplateParentChapterBookClassRoomService templateParentChapterBookClassRoomService;

	@Autowired
	private ClassUserService classUserService;

	@Autowired
	private ChapterService chapterService;
	
	@GetMapping(value = "/test", produces = JSON_PRODUCES)
	public Map<String, Object> test() {
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.OK);
		File json = new File("E:\\unit-exam\\data\\1.json");
		try {
			ret.put("data", FileUtils.readFileToString(json, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	@PostMapping(value = "/", produces = JSON_PRODUCES)
	public Map<String, Object> found(String uid, String gradeId, String classId, String versionDesc,
			Boolean isFirstVolume) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gradeId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gradeId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(classId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(versionDesc)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "versionDesc is null");
			return ret;
		}
		VersionDesc version = null;
		try {
			version = VersionDesc.valueOf(versionDesc);
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "versionDesc is error");
			return ret;
		}
		if (null == isFirstVolume) {
			isFirstVolume = true;
		}
		ClassRoom room = classRoomService.found(uid, gradeId, version, classId, isFirstVolume);
		if (null != room) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("classroomId", room.getId());
			return ret;
		}
		ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		ret.put("desc", "found classroom failed");
		return ret;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/classrooms", params = { "uid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getClassRooms(@RequestParam(value = "uid") String uid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		List<ClassRoom> rooms = null;
		boolean isTeacher = false;
		Map<String, Object> user = restTemplate.getForObject(ApiClient.GET_USER, Map.class, uid);
		if (null != user && null != user.get("person")) {
			Map<String, Object> person = (Map<String, Object>) user.get("person");
			isTeacher = (Boolean) person.getOrDefault("isTeacher", false);
		}
		if (isTeacher) {
			rooms = classRoomService.getFoundedRooms(uid, gid);
		} else {
			rooms = classRoomService.getJoinedRooms(uid, gid);
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("uid", uid);
		ret.put("gid", gid);
		if (null == rooms || rooms.isEmpty()) {
			return ret;
		}
		ret.put("rooms", json(rooms));
		return ret;
	}

	private List<Map<String, Object>> json(List<ClassRoom> rooms) {
		List<Map<String, Object>> list = Lists.newArrayList();
		rooms.forEach(room -> {
			Map<String, Object> map = Maps.newHashMap();
			list.add(map);
			map.put("id", room.getId());
			map.put("title", room.getTitle());
			map.put("intro", room.getIntro());
			map.put("room_code", room.getCode());
			if (!Strings.isNullOrEmpty(room.getGradeId())) {
				Grade grade = gradeService.findById(room.getGradeId());
				if (null != grade) {
					map.put("grade", grade.getName());
					map.put("grade_code", grade.getCode());
				}
			}
			if (!Strings.isNullOrEmpty(room.getClassesId())) {
				Classes classes = classesService.findById(room.getClassesId());
				if (null != classes) {
					map.put("classes", classes.getName());
					map.put("trained", classes.getTrained());
				}
			}
			String tfileId = room.getImgTfileId();
			System.out.println("----tfileId:"+tfileId);
			if (!Strings.isNullOrEmpty(tfileId)) {
				Map<String, Object> tfile = cloudFileUtil.getResource(tfileId);
				if (null != tfile) {
					String ext = (String) tfile.get("ext");
					if (!Strings.isNullOrEmpty(ext)) {
						byte[] bytes = cloudFileUtil.downloadBytes(tfileId, ext);
						if (null != bytes) {
							map.put("img", Base64.getEncoder().encodeToString(bytes));
						}
					}
				}
			}
			List<Chapter> chapters = chapterClassRoomService.getParentChapters(room.getId());
			if (null != chapters) {
				List<Map<String, Object>> clist = Lists.newArrayList();
				Map<String, Integer> competition = Maps.newHashMap();
				map.put("chapters", clist);
				chapters.stream().forEach(chapter -> {
					Map<String, Object> m = Maps.newHashMap();
					m.put("cid", chapter.getId());
					m.put("ctitle", chapter.getTitle());
					m.put("order_by", chapter.getOrderBy());
					clist.add(m);
					TemplateParentChapterBookClassRoom tpcbc = templateParentChapterBookClassRoomService
							.getBookTemplate(room.getBookId(), chapter.getId());
					if (null == tpcbc) {
						return;
					}
					Map<String, Integer> comp = getClassroomCompetition(room.getId(), tpcbc.getTemplateId(),
							chapter.getId(), room.getBookId());
					competition.put("all", competition.getOrDefault("all", 0) + comp.getOrDefault("all", 0));
					competition.put("competition",
							competition.getOrDefault("competition", 0) + comp.getOrDefault("competition", 0));
				});
				map.put("competition",
						competition.getOrDefault("competition", 0) * 100 / competition.getOrDefault("all", 1));
			}
		});
		return list;
	}

	private Map<String, Integer> getClassroomCompetition(String rid, String tid, String pid, String bid) {
		Map<String, Integer> data = Maps.newHashMap();
		List<Chapter> children = chapterService.getChildrenChapters(pid);
		if (null == children || children.isEmpty()) {
			return data;
		}
		data.put("all", children.size());
		List<ClassroomTemplateChapterStatus> ctcss = classroomTemplateChapterStatusService.getByParentChapter(rid, tid,
				pid);
		if (null == ctcss || ctcss.isEmpty()) {
			return data;
		}
		long count = ctcss.parallelStream()
				.filter(ctcs -> ClassroomTemplateChapterStatus.ChapterResourceStatus.COMPLETION == ctcs.getStatus())
				.count();
		data.put("competition", Long.valueOf(count).intValue());
		return data;
	}

	@GetMapping(value = "/{rid}", produces = JSON_PRODUCES)
	public Map<String, Object> detail(@PathVariable String rid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(rid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rid is null");
			return ret;
		}
		ClassRoom room = classRoomService.get(rid);
		if (null == room) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroom is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("id", room.getId());
		ret.put("title", room.getTitle());
		ret.put("intro", room.getIntro());
		ret.put("room_code", room.getCode());
		ret.put("bookId", room.getBookId());
		String tfileId = room.getImgTfileId();
		Map<String, Object> tfile = cloudFileUtil.getResource(tfileId);
		if (null != tfile && !Strings.isNullOrEmpty((String) tfile.get("ext"))) {
			ret.put("img", Base64.getEncoder()
					.encodeToString(cloudFileUtil.downloadBytes(tfileId, (String) tfile.get("ext"))));
		}
		ret.put("teacher_id", room.getTeacherId());
		ret.put("teacher_name", room.getTeacherName());

		List<ChapterClassRoom> ccrs = chapterClassRoomService.get(room.getId());
		if (null == ccrs) {
			return ret;
		}
		List<Chapter> chapters = ccrs.stream().filter(ccr -> null == ccr.getChapter().getParent())
				.map(ccr -> ccr.getChapter()).collect(Collectors.toList());
		List<Map<String, Object>> list = Lists.newArrayList();
		chapters.forEach(chapter -> {
			Map<String, Object> c = Maps.newHashMap();
			c.put("cid", chapter.getId());
			c.put("title", chapter.getTitle());
			list.add(c);
		});
		ret.put("chapters", list);
		return ret;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/joined", produces = JSON_PRODUCES)
	public Map<String, Object> join(String uid, String code) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(code)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "code is null");
			return ret;
		}
		ClassRoom classRoom = classRoomService.getByCode(code);
		if (null == classRoom) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroom is null");
			return ret;
		}
//		String classId = classRoom.getClassesId();
//		if (!getClass(uid).equals(classId)) {
//			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
//			ret.put("desc", "your class mismatched the classroom");
//			return ret;
//		}
		ClassRoom room = classRoomService.joined(uid, code);
		if (null != room) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("rid", room.getId());

			Map<String, Object> user = restTemplate.getForObject(ApiClient.GET_USER, Map.class, uid);
			if (null != user) {
				Object person = user.get("person");
				if (null != person) {
					Map<String, Object> map = (Map<String, Object>) person;
					sessionManager.sendUserJoinedNotice(room.getCode(), room.getId(), uid,
							String.valueOf(map.getOrDefault("name", "")),
							String.valueOf(map.getOrDefault("gender", "")),
							String.valueOf(map.getOrDefault("photo", "")));
				}
			}
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "joined classroom fail");
		}
		return ret;
	}

	private String getClass(String uid) {
		int year = LocalDate.now().getYear();
		ClassUser cs = new ClassUser();
		cs.setUserId(uid);
		List<ClassUser> cusers = null;
		try {
			cusers = classUserService.findByCondionts(cs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<ClassUser> cus = cusers.stream().filter(cst -> year == cst.getClasses().getStartYear().intValue()
				|| year == cst.getClasses().getEndYear().intValue()).collect(Collectors.toList());
		if (null == cus || cus.isEmpty()) {
			return "";
		}
		return cusers.get(0).getClassId();
	}

	@SuppressWarnings("unchecked")
	@DeleteMapping(value = "/{rid}/user/{uid}/logout")
	public Map<String, Object> logout(@PathVariable String rid, @PathVariable String uid) {
		System.out.println("-------logout--------");
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(rid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		ClassRoom room = this.classRoomService.get(rid);
		if (null == room) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroom is null");
			return ret;
		}
		if (room.getTeacherId().equals(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "do not logout yourself classroom");
			return ret;
		}
		classRoomUserRelationService.removeJoinedStudent(rid, new String[] { uid });

		Map<String, Object> user = restTemplate.getForObject(ApiClient.GET_USER, Map.class, uid);
		if (null != user) {
			Object person = user.get("person");
			if (null != person) {
				Map<String, Object> map = (Map<String, Object>) person;
				sessionManager.sendUserLogoutNotice(room.getCode(), room.getId(), uid,
						String.valueOf(map.getOrDefault("name", "")));
			}
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@DeleteMapping(value = "/{rid}/user/{uid}/remove")
	public Map<String, Object> remove(@PathVariable String rid, @PathVariable String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(rid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		ClassRoom room = this.classRoomService.get(rid);
		if (null == room) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroom is null");
			return ret;
		}
		if (room.getTeacherId().equals(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "this classroom is not yourself");
			return ret;
		}
		sessionManager.sendRemoveUserNotice(room.getCode(), uid);
		classRoomUserRelationService.removeJoinedStudent(rid, new String[] { uid });
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@GetMapping(value = "/{rid}/students", produces = JSON_PRODUCES)
	public Map<String, Object> getStudents(@PathVariable String rid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(rid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rid is null");
			return ret;
		}
		ClassRoom room = classRoomService.get(rid);
		if (null == room) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroom is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		List<Map<String, Object>> students = classRoomUserRelationService.getJoinedStudents(rid);

		if (null != students) {
			students.forEach(student -> {
				boolean online = sessionManager.online(room.getCode(), (String) student.get("id"));
				student.put("online", online);
			});
		}

		ret.put("students", students);
		return ret;
	}

	@GetMapping(value = "/classrooms", params = { "teacher_name", "title" }, produces = JSON_PRODUCES)
	public Map<String, Object> query(@RequestParam("teacher_name") String teacherName,
			@RequestParam("title") String title) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(teacherName) && Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "query params is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		List<ClassRoom> rooms = classRoomService.query(teacherName, title);
		if (null == rooms) {
			return ret;
		}
		List<Map<String, Object>> lists = Lists.newArrayList();
		ret.put("rooms", lists);
		rooms.forEach(room -> {
			Map<String, Object> map = Maps.newHashMap();
			lists.add(map);
			map.put("id", room.getId());
			map.put("title", room.getTitle());
			map.put("intro", room.getIntro());
			map.put("room_code", room.getCode());
			map.put("grade", Strings.isNullOrEmpty(room.getGradeId()) ? "1" : room.getGradeId());
			map.put("classes", Strings.isNullOrEmpty(room.getClassesId()) ? "3" : room.getClassesId());
			map.put("teacher_id", room.getTeacherId());
			map.put("teacher_name", room.getTeacherName());

			String tfileId = room.getImgTfileId();
			if (!Strings.isNullOrEmpty(tfileId)) {
				Map<String, Object> tfile = cloudFileUtil.getResource(tfileId);
				String ext = (String) tfile.get("ext");
				if (!Strings.isNullOrEmpty(ext)) {
					map.put("img", Base64.getEncoder().encodeToString(cloudFileUtil.downloadBytes(tfileId, ext)));
				}
			}
			List<Chapter> chapters = chapterClassRoomService.getParentChapters(room.getId());
			if (null != chapters && !chapters.isEmpty()) {
				List<Map<String, Object>> list = Lists.newArrayList();
				map.put("chapters", list);
				chapters.forEach(chapter -> {
					Map<String, Object> m = Maps.newHashMap();
					m.put("cid", chapter.getId());
					m.put("ctitle", chapter.getTitle());
					list.add(m);
				});
			}
		});
		return ret;
	}

	@PostMapping(value = "/notice", produces = JSON_PRODUCES)
	public Map<String, Object> foundNotice(String rid, String title, String content) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(rid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		Notice notice = noticeService.found(rid, title, content);
		if (null != notice) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("rid", rid);
			ret.put("nid", notice.getId());
			ret.put("title", title);
			ret.put("content", content);
			return ret;
		}
		ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		ret.put("desc", "found notice failed");
		return ret;
	}

	@PutMapping(value = "/{id}", produces = JSON_PRODUCES)
	public Map<String, Object> update(@PathVariable String id, String title, String intro) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		ClassRoom classRoom = this.classRoomService.get(id);
		if (null == classRoom) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no such classroom");
			return ret;
		}
		this.classRoomService.update(id, title, intro);
		ret.put("code", CList.Api.Client.OK);
		ret.put("desc", "update completely");
		return ret;
	}

	@DeleteMapping(value = "/{id}", produces = JSON_PRODUCES)
	public Map<String, Object> delete(@PathVariable String id) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
			return ret;
		}
		this.classRoomService.deleteClassroom(id);
		ret.put("code", CList.Api.Client.OK);
		ret.put("desc", "delete completely");
		return ret;
	}

	@PostMapping(value = "/classroom", produces = JSON_PRODUCES)
	public Map<String, Object> newClassRoom(ClassRoom classroom) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == classroom) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroom is null");
			return ret;
		}
		// 班级Id
		if (Strings.isNullOrEmpty(classroom.getClassesId())) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classesId is null");
			return ret;
		}
		// 班级是否存在
		Classes classes = classesService.findById(classroom.getClassesId());
		if (null == classes) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classes not exist");
			return ret;
		}
		// 年级Id
		if (Strings.isNullOrEmpty(classroom.getGradeId())) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gradeId is null");
			return ret;
		}
		// 年级是否存在
		Grade grade = gradeService.findById(classroom.getGradeId());
		if (null == grade) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "grade not exist");
			return ret;
		}
		// 老师Id
		if (Strings.isNullOrEmpty(classroom.getTeacherId())) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "teacherId is null");
			return ret;
		}
		// 姓名
		if (Strings.isNullOrEmpty(classroom.getTeacherName())) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "teacherName is null");
			return ret;
		}
		// 标题
		if (Strings.isNullOrEmpty(classroom.getTitle())) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		// 教材
		if (Strings.isNullOrEmpty(classroom.getBookId())) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "bookId is null");
			return ret;
		}
		Book book = bookService.findById(classroom.getBookId());
		if (null == book) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "book not exist");
			return ret;
		}
		// 课堂代码随机生成
		String code = CommonUtils.getRandomString(8);
		while (null != classRoomService.getByCode(code)) {
			code = CommonUtils.getRandomString(8);
		}
		classroom.setCode(code);
		classroom.setCreateAt(System.currentTimeMillis()); // 当前时间
		ClassRoom newClassroom = classRoomService.newClassroom(classroom);
		if (null != newClassroom) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("classroom", classroom);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		}
		return ret;
	}

	@GetMapping(value = "/answer_notice", produces = JSON_PRODUCES)
	public Map<String, Object> notice(String roomId, String senderId, String answer) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(roomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "roomCode is null");
			return ret;
		}
		ClassRoom classRoom = classRoomService.get(roomId);
		if (null == classRoom) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classRoom is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(senderId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "senderId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(answer)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "answer is null");
			return ret;
		}
		sessionManager.sendSaveAnswerNotice(classRoom.getCode(), senderId, answer);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@GetMapping(value = "/my_classrooms", params = { "uid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getMyClassRooms(@RequestParam(value = "uid") String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		List<ClassRoom> rooms = classRoomService.getJoinedRooms(uid, "");
		ret.put("code", CList.Api.Client.OK);
		ret.put("uid", uid);
		if (null == rooms || rooms.isEmpty()) {
			return ret;
		}
		Map<String, String> map = Maps.newHashMap();
		rooms.forEach(room -> {
			map.put("id", room.getId());
			map.put("title", room.getTitle());
		});
		ret.put("rooms", map);
		return ret;
	}

	@GetMapping(value = "/songs", params = { "uid", "gid" }, produces = JSON_PRODUCES)
	public Map<String, Object> getMyClassRooms(@RequestParam(value = "uid") String uid,
			@RequestParam(value = "gid") String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		List<ClassRoom> rooms = classRoomService.getJoinedRooms(uid, "");
		if (null == rooms || rooms.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rooms is null");
			return ret;
		}
		Optional<ClassRoom> optional = rooms.stream().filter(room -> gid.equals(room.getGradeId())).findAny();
		if (!optional.isPresent()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rooms is null");
			return ret;
		}
		ClassRoom room = optional.get();
		List<ChapterClassRoom> ccrs = chapterClassRoomService.get(room.getId());
		if (null == ccrs || ccrs.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapters is null");
			return ret;
		}
		List<String> cids = ccrs.stream().filter(ccr -> !Strings.isNullOrEmpty(ccr.getChapter().getParentId()))
				.map(ccr -> ccr.getChapter().getId()).collect(Collectors.toList());
		if (null == cids || cids.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapters is null");
			return ret;
		}
		List<TemplateChapterResource> tcrs = templateChapterResourceService.getByChapters(cids);
		if (null == tcrs || tcrs.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapter resource is null");
			return ret;
		}
		List<String> sids = tcrs.stream().map(tcr -> tcr.getSongId()).collect(Collectors.toList());
		@SuppressWarnings("unchecked")
		Map<String, Object> songs = restTemplate.getForObject(ApiClient.GET_RESOURCE_SONGS, Map.class,
				Joiner.on(",").join(sids));
		ret.put("code", CList.Api.Client.OK);
		ret.put("songs", songs.get("songs"));
		return ret;
	}

}
