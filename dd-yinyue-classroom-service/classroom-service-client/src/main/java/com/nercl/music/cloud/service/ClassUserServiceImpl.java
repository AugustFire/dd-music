package com.nercl.music.cloud.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.nercl.music.cloud.dao.ClassUserDao;
import com.nercl.music.cloud.entity.base.ClassUser;
import com.nercl.music.cloud.entity.base.Classes;
import com.nercl.music.cloud.entity.base.Grade;
import com.nercl.music.constant.ApiClient;

@Service
@Transactional
public class ClassUserServiceImpl implements ClassUserService {

	@Autowired
	private ClassUserDao classUserDao;

	@Autowired
	private ClassesService classesService;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public void save(String uid, String name, String classId, String gradeId, String schoolId, boolean isTeacher) {
		ClassUser cu = new ClassUser();
		cu.setUserId(uid);
		cu.setUserName(name);
		cu.setClassId(classId);
		cu.setGradeId(gradeId);
		cu.setSchoolId(schoolId);
		cu.setIsTeacher(isTeacher);
		classUserDao.save(cu);
	}

	@Override
	public List<ClassUser> findByCondionts(ClassUser cs) throws Exception {
		return classUserDao.findByConditions(cs);
	}

	@Override
	public int getStudentNum(String cid) {
		return classUserDao.getStudentNum(cid);
	}

	@Override
	public Map<String, Integer> getManAndWomanStudentNum(String cid) {
		List<ClassUser> users = classUserDao.getStudents(cid);
		if (null == users || users.isEmpty()) {
			return null;
		}
		List<String> ids = users.parallelStream().map(user -> user.getUserId()).collect(Collectors.toList());
		System.out.println("------------ids:" + ids);
		@SuppressWarnings("unchecked")
		Map<String, Object> res = restTemplate.getForObject(ApiClient.GET_USERS_GENDER, Map.class,
				Joiner.on(",").join(ids));
		System.out.println("------------res:" + res);
		if (null != res && null != res.get("MAN")) {
			Map<String, Integer> num = Maps.newHashMap();
			num.put("MAN", (Integer) res.get("MAN"));
			num.put("WOMAN", (Integer) res.get("WOMAN"));
			return num;
		}
		return null;
	}

	@Override
	public int getSchoolStudentNum(String sid) {
		List<ClassUser> cus = classUserDao.getSchoolStudent(sid);
		if (null == cus || cus.isEmpty()) {
			return 0;
		}
		int now = LocalDate.now().getYear();

		long count = cus.stream().filter(cu -> {
			Classes cl = cu.getClasses();
			return null != cl && (now == cl.getStartYear().intValue() || now == cl.getEndYear().intValue());
		}).count();
		return count <= 0 ? 0 : Long.valueOf(count).intValue();
	}

	@Override
	public List<Grade> getGradesByTeacher(String tid) {
		List<Classes> classes = classesService.getClassByTeacher(tid);
		if (null == classes || classes.isEmpty()) {
			return null;
		}
		Set<Grade> grades = Sets.newHashSet();
		int now = LocalDate.now().getYear();
		classes.parallelStream().filter(cl -> now == cl.getStartYear().intValue() || now == cl.getEndYear().intValue())
				.forEach(c -> {
					grades.add(c.getGrade());
				});
		return grades.stream().collect(Collectors.toList());
	}

	@Override
	public List<Grade> getGradesBySchool(String sid) {
		List<Classes> classes = classesService.getClassBySchool(sid);
		if (null == classes || classes.isEmpty()) {
			return null;
		}
		Set<Grade> grades = Sets.newHashSet();
		int now = LocalDate.now().getYear();
		classes.parallelStream().filter(cl -> now == cl.getStartYear().intValue() || now == cl.getEndYear().intValue())
				.forEach(c -> {
					grades.add(c.getGrade());
				});
		return grades.stream().collect(Collectors.toList());
	}

	@Override
	public List<Classes> getClassByTeacherGrade(String tid, String gid) {
		List<Classes> classes = classesService.getClassByTeacherGrade(tid, gid);
		if (null == classes || classes.isEmpty()) {
			return null;
		}
		int now = LocalDate.now().getYear();
		List<Classes> cls = classes.parallelStream()
				.filter(cl -> now == cl.getStartYear().intValue() || now == cl.getEndYear().intValue())
				.collect(Collectors.toList());
		return cls;
	}

	@Override
	public List<Classes> getClassBySchoolGrade(String sid, String gid) {
		List<Classes> classes = classesService.getClassBySchoolGrade(sid, gid);
		if (null == classes || classes.isEmpty()) {
			return null;
		}
		int now = LocalDate.now().getYear();
		List<Classes> cls = classes.stream()
				.filter(cl -> now == cl.getStartYear().intValue() || now == cl.getEndYear().intValue())
				.collect(Collectors.toList());
		return cls;
	}

	@Override
	public List<Classes> getClassBySchool(String sid) {
		List<Classes> classes = classesService.getClassBySchool(sid);
		if (null == classes || classes.isEmpty()) {
			return null;
		}
		int now = LocalDate.now().getYear();
		List<Classes> cls = classes.parallelStream()
				.filter(cl -> now == cl.getStartYear().intValue() || now == cl.getEndYear().intValue())
				.collect(Collectors.toList());
		return cls;
	}

	@Override
	public List<Classes> getClassByTeacher(String tid) {
		List<Classes> classes = classesService.getClassByTeacher(tid);
		if (null == classes || classes.isEmpty()) {
			return null;
		}
		int now = LocalDate.now().getYear();
		List<Classes> cls = classes.parallelStream()
				.filter(cl -> now == cl.getStartYear().intValue() || now == cl.getEndYear().intValue())
				.collect(Collectors.toList());
		return cls;
	}

	@Override
	public List<Map<String, Object>> getStudentsByClass(String cid) {
		List<ClassUser> cus = classUserDao.getStudents(cid);
		if (null == cus || cus.isEmpty()) {
			return null;
		}
		return cus.stream().map(cu -> {
			Map<String, Object> student = Maps.newHashMap();
			student.put("id", cu.getUserId());
			student.put("name", cu.getUserName());
			return student;
		}).collect(Collectors.toList());
	}

	@Override
	public List<Map<String, String>> getClassTeachers(String sid, String gid) {
		List<ClassUser> cus = classUserDao.getClassTeachers(sid, gid);
		if (null == cus || cus.isEmpty()) {
			return null;
		}
		Set<String> uids = Sets.newHashSet();
		List<Map<String, String>> teachers = Lists.newArrayList();
		cus.forEach(cu -> {
			String uid = cu.getUserId();
			if (!uids.contains(uid)) {
				Map<String, String> teacher = Maps.newHashMap();
				teacher.put("id", cu.getUserId());
				teacher.put("name", cu.getUserName());
				teachers.add(teacher);
			}
		});
		return teachers;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getSchoolTeachers(String sid) {
		List<ClassUser> cus = classUserDao.getSchoolTeachers(sid);
		if (null == cus || cus.isEmpty()) {
			return null;
		}
		Set<String> uids = Sets.newHashSet();
		List<Map<String, Object>> teachers = Lists.newArrayList();
		int now = LocalDate.now().getYear();
		cus.parallelStream().filter(cu -> {
			Classes cl = cu.getClasses();
			return null != cl && (now == cl.getStartYear().intValue() || now == cl.getEndYear().intValue());
		}).forEach(cu -> {
			String uid = cu.getUserId();
			if (!uids.contains(uid)) {
				uids.add(uid);
				Map<String, Object> teacher = Maps.newHashMap();
				Map<String, Object> user = restTemplate.getForObject(ApiClient.GET_USER2, Map.class, uid);
				if (null == user) {
					return;
				}
				teacher.put("id", cu.getUserId());
				teacher.put("name", cu.getUserName());
				teacher.put("gid", cu.getGradeId());
				teacher.put("gcode", cu.getGrade().getCode());

				teacher.put("gender", (String) user.getOrDefault("gender", "MAN"));
				teacher.put("degree", (String) user.getOrDefault("degree", "BEN_KE"));
				teacher.put("age", (Integer) user.getOrDefault("age", 18));
				teacher.put("graduate_school", (String) user.getOrDefault("graduate_school", "武汉大学"));
				teacher.put("worked_years", LocalDate.now().getYear() - (Integer) user.getOrDefault("work_year", 20));
				teacher.put("is_grade_master", (Boolean) user.getOrDefault("GRADE_MASTER", false));
				teachers.add(teacher);
			}
		});
		return teachers;
	}

	@Override
	public List<ClassUser> getStudents(String classId) {
		return classUserDao.getStudents(classId);
	}

	@Override
	public int getStudentsAmountBySchoolId(String sid) {
		return classUserDao.getStudentsAmountBySchoolId(sid);
	}

}
