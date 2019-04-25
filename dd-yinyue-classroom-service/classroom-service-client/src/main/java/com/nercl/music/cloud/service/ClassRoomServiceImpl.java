package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.nercl.music.cloud.dao.ClassRoomDao;
import com.nercl.music.cloud.entity.classroom.Book;
import com.nercl.music.cloud.entity.classroom.Chapter;
import com.nercl.music.cloud.entity.classroom.ClassRoom;
import com.nercl.music.cloud.entity.classroom.Task;
import com.nercl.music.cloud.entity.classroom.VersionDesc;
import com.nercl.music.constant.ApiClient;

@Service
@Transactional
public class ClassRoomServiceImpl implements ClassRoomService {

	@Autowired
	private ClassRoomDao classRoomDao;

	@Autowired
	private ChapterClassRoomService chapterClassRoomService;

	@Autowired
	private ClassRoomUserRelationService classRoomUserRelationService;

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private BookService bookService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private TemplateParentChapterBookClassRoomService templateParentChapterBookClassRoomService;

	@Override
	public ClassRoom get(String rid) {
		return classRoomDao.findByID(rid);
	}

	@Override
	public ClassRoom joined(String userId, String roomCode) {
		ClassRoom classRoom = this.getByCode(roomCode);
		if (null == classRoom) {
			return null;
		}
		if (userId.equals(classRoom.getTeacherId())) {
			return null;
		}
		boolean success = classRoomUserRelationService.save(classRoom.getId(), userId);
		return success ? classRoom : null;
	}

	@Override
	public ClassRoom getByCode(String roomCode) {
		return classRoomDao.getByCode(roomCode);
	}

	@Override
	public List<ClassRoom> getJoinedRooms(String uid, String gid) {
		List<ClassRoom> rooms = classRoomDao.getJoinedRooms(uid);
		if (null == rooms || rooms.isEmpty()) {
			return null;
		}
		if (Strings.isNullOrEmpty(gid)) {
			return rooms;
		}
		return rooms.stream().filter(r -> gid.equals(r.getGradeId())).collect(Collectors.toList());
	}

	@Override
	public List<ClassRoom> getFoundedRooms(String uid, String gid) {
		List<ClassRoom> rooms = classRoomDao.getFoundedRooms(uid);
		if (null == rooms || rooms.isEmpty()) {
			return null;
		}
		if (Strings.isNullOrEmpty(gid)) {
			return rooms;
		}
		return rooms.stream().filter(r -> gid.equals(r.getGradeId())).collect(Collectors.toList());
	}

	private String generateCode() {
		String code = null;
		do {
			StringBuffer sb = new StringBuffer();
			String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
			for (int i = 0; i < 8; i++) {
				sb.append(chars.charAt((int) (Math.random() * 36)));
			}
			code = sb.toString();
		} while (isExsitCode(code) || Strings.isNullOrEmpty(code));
		return code;
	}

	private boolean isExsitCode(String code) {
		ClassRoom classRoom = getByCode(code);
		return null != classRoom;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClassRoom found(String uid, String gradeId, VersionDesc version, String classId, boolean isFirstVolume) {
		Book book = bookService.getByGradeAndVersion(gradeId, version, isFirstVolume);
		if (null == book) {
			return null;
		}
		ClassRoom classRoom = new ClassRoom();
		classRoom.setTitle(book.getTitle());
		classRoom.setCode(generateCode());
		classRoom.setTeacherId(uid);
		classRoom.setIntro(book.getIntro());
		classRoom.setImgTfileId(book.getImgTfileId());
		classRoom.setCreateAt(System.currentTimeMillis());
		classRoom.setClassesId(classId);
		classRoom.setGradeId(gradeId);
		classRoom.setBookId(book.getId());

		Map<String, Object> user = restTemplate.getForObject(ApiClient.GET_USER, Map.class, uid);
		if (null != user) {
			Object person = user.get("person");
			if (null != person) {
				Map<String, Object> map = (Map<String, Object>) person;
				classRoom.setTeacherName(String.valueOf(map.getOrDefault("name", "")));
			}
		}

		classRoomDao.save(classRoom);
		List<Chapter> chapters = chapterService.getByBook(book.getId());
		if (null == chapters) {
			return classRoom;
		}
		chapters.forEach(chapter -> {
			chapterClassRoomService.save(chapter.getId(), classRoom.getId());
		});
		return classRoom;
	}

	@Override
	public void update(String id, String title, String intro) {
		ClassRoom classroom = this.classRoomDao.findByID(id);
		if (null != classroom) {
			classroom.setTitle(title);
			classroom.setIntro(intro);
			this.classRoomDao.update(classroom);
		}
	}

	@Override
	public void deleteClassroom(String id) {
		// 删除课堂中的成员
		classRoomUserRelationService.deleteClassroomUsers(id);

		// 删除课堂中的章节
		chapterClassRoomService.deleteClassroomChapters(id);

		// 删除课堂中的作业
		// 业务逻辑暂时写成,直接删除作业,后面可能会改成如果有作业就不能删除课堂
		String[] taskIds = { id };
		List<Task> tasks = taskService.getTasks(taskIds);
		tasks.forEach(t -> {
			try {
				taskService.delete(t.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		templateParentChapterBookClassRoomService.deleteByClassroom(id);

		// 删除课堂
		this.classRoomDao.deleteById(id);
	}

	@Override
	public List<ClassRoom> query(String teacherName, String title) {
		return classRoomDao.query(teacherName, title);
	}

	@Override
	public ClassRoom findById(String classRoomId) {
		return classRoomDao.findByID(classRoomId);
	}

	@Override
	public ClassRoom newClassroom(ClassRoom classroom) {
		classRoomDao.save(classroom);
		String bookId = classroom.getBookId();
		List<Chapter> chapters = chapterService.getByBook(bookId);
		if (null == chapters || chapters.isEmpty()) {
			return classroom;
		}
		chapters.forEach(chapter -> {
			chapterClassRoomService.save(chapter.getId(), classroom.getId());
		});
		return classroom;
	}
}
