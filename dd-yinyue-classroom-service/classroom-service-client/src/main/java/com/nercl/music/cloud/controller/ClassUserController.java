package com.nercl.music.cloud.controller;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.nercl.music.cloud.entity.base.ClassUser;
import com.nercl.music.cloud.entity.base.Classes;
import com.nercl.music.cloud.entity.base.Grade;
import com.nercl.music.cloud.service.ClassUserService;
import com.nercl.music.cloud.service.ClassesService;
import com.nercl.music.constant.CList;

@RestController
public class ClassUserController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ClassUserService classUserService;

	@Autowired
	private ClassesService classesService;

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/class_user/{uid}", produces = JSON_PRODUCES)
	public Map<String, Object> getClassUser(@PathVariable String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		int year = LocalDate.now().getYear();
		ClassUser cs = new ClassUser();
		cs.setUserId(uid);
		List<ClassUser> cusers;
		try {
			cusers = classUserService.findByCondionts(cs);
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		if (null == cusers) {
			return ret;
		}

		// 教育局,没有学校、年级、班级信息
		List<ClassUser> bureauofEducation = cusers.stream()
				.filter(cst -> null == cst.getSchoolId() && null == cst.getGradeId() && null == cst.getClassId())
				.collect(Collectors.toList());
		if (null != bureauofEducation && !bureauofEducation.isEmpty()) {
			ret.put("school_id", null);
			ret.put("school_name", null);
			ret.put("classes", null);
			ret.put("grades", null);
			return ret;
		}

		// 校长,没有年级、班级信息
		List<ClassUser> schoolMaster = cusers.stream()
				.filter(cst -> null == cst.getGradeId() && null == cst.getClassId()).collect(Collectors.toList());
		if (null != schoolMaster && !schoolMaster.isEmpty()) {
			ret.put("school_id", schoolMaster.get(0).getSchoolId());
			ret.put("school_name", schoolMaster.get(0).getSchool().getName());
			ret.put("classes", null);
			ret.put("grades", null);
			return ret;
		}

		// 年级主任 ,没有班级信息
		List<ClassUser> gradeMaster = cusers.stream().filter(cst -> null == cst.getClassId())
				.collect(Collectors.toList());
		if (null != gradeMaster && !gradeMaster.isEmpty()) {
			ret.put("school_id", gradeMaster.get(0).getSchoolId());
			ret.put("school_name", gradeMaster.get(0).getSchool().getName());
			List<Map<String, String>> grades = Lists.newArrayList();
			Set<String> gids = Sets.newHashSet();
			gradeMaster.forEach(gm -> {
				if (!Strings.isNullOrEmpty(gm.getGradeId()) && !gids.contains(gm.getGradeId())) {
					gids.add(gm.getGradeId());
					Map<String, String> map = Maps.newHashMap();
					map.put("grade_id", gm.getGradeId());
					map.put("grade_code", gm.getGrade().getCode());
					map.put("grade_name", gm.getGrade().getName());
					grades.add(map);
				}
			});
			List<Map<String, String>> grades1= grades.stream().sorted((g1,g2) -> g1.get("grade_code").compareTo(g2.get("grade_code"))).collect(Collectors.toList());
			ret.put("classes", null);
			ret.put("grades", grades1);
			return ret;
		}

		List<ClassUser> cus = cusers.stream()
				.filter(cst -> null != cst.getSchoolId() && null != cst.getGradeId() && null != cst.getClassId())
				.filter(cst -> year == cst.getClasses().getStartYear().intValue()
						|| year == cst.getClasses().getEndYear().intValue())
				.collect(Collectors.toList());
		if (null == cus || cus.isEmpty()) {
			return ret;
		}

		boolean isTeahcher = cusers.get(0).getIsTeacher();
		ret.put("is_teahcher", isTeahcher);
		if (isTeahcher) {
			ret.put("school_id", cusers.get(0).getSchoolId());
			ret.put("school_name", cusers.get(0).getSchool().getName());

			Set<String> cids = Sets.newHashSet();
			Set<String> gids = Sets.newHashSet();

			List<Map<String, String>> classes = Lists.newArrayList();
			List<Map<String, String>> grades = Lists.newArrayList();
			
			cus.forEach(cu -> {
				if (!Strings.isNullOrEmpty(cu.getClassId()) && !cids.contains(cu.getClassId())) {
					cids.add(cu.getClassId());
					Map<String, String> map = Maps.newHashMap();
					map.put("class_id", cu.getClassId());
					map.put("class_name", cu.getClasses().getName());
					map.put("order_by",cu.getClasses().getOrderBy());
					classes.add(map);
				}
				if (!Strings.isNullOrEmpty(cu.getGradeId()) && !gids.contains(cu.getGradeId())) {
					gids.add(cu.getGradeId());
					Map<String, String> map = Maps.newHashMap();
					map.put("grade_id", cu.getGradeId());
					map.put("grade_code", cu.getGrade().getCode());
					map.put("grade_name", cu.getGrade().getName());
					grades.add(map);
				}
			});
//			List<Map<String, String>> classes1= classes.stream().sorted((c1,c2) -> c1.get("order_by").compareTo(c2.get("order_by"))).collect(Collectors.toList());
			List<Map<String, String>> grades1= grades.stream().sorted((g1,g2) -> g1.get("grade_code").compareTo(g2.get("grade_code"))).collect(Collectors.toList());
			ret.put("classes", classes);
			ret.put("grades", grades1);
			return ret;
		} else {
			ClassUser cu = cus.get(0);
			ret.put("school_id", cu.getSchoolId());
			ret.put("school_name", cu.getSchool().getName());

			Map<String, String> classes = Maps.newHashMap();
			classes.put("class_id", cu.getClassId());
			classes.put("class_name", cu.getClasses().getName());
			ret.put("classes", Lists.newArrayList(classes));

			Map<String, String> grade = Maps.newHashMap();
			grade.put("grade_id", cu.getGrade().getId());
			grade.put("grade_code", cu.getGrade().getCode());
			grade.put("grade_name", cu.getGrade().getName());
			ret.put("grades", Lists.newArrayList(grade));
			return ret;
		}
	}

	@GetMapping(value = "/class_student/num", produces = JSON_PRODUCES)
	public Map<String, Object> getClassStudentNum(String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		int num = classUserService.getStudentNum(cid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("num", num);
		return ret;
	}

	@GetMapping(value = "/class_student/grade_student_num", produces = JSON_PRODUCES)
	public Map<String, Object> getGradeNum(String sid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		List<Classes> classes = classUserService.getClassBySchoolGrade(sid, gid);
		ret.put("code", CList.Api.Client.OK);
		if (null == classes) {
			return ret;
		}
		int count = classes.stream()
				.filter(cl -> LocalDate.now().getYear() == cl.getStartYear().intValue()
						|| LocalDate.now().getYear() == cl.getEndYear().intValue())
				.mapToInt(c -> classUserService.getStudentNum(c.getId())).sum();
		ret.put("num", count);
		return ret;
	}

	/**
	 * 获取某个教师所授课的年级
	 * @param tid
	 * @return
	 */
	@GetMapping(value = "/class_student/grades", produces = JSON_PRODUCES)
	public Map<String, Object> getGrades(String tid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "tid is null");
			return ret;
		}
		List<Grade> grades = classUserService.getGradesByTeacher(tid);
		ret.put("code", CList.Api.Client.OK);
		if (null == grades) {
			return ret;
		}
		List<Map<String, String>> gs = grades.stream().sorted((g1, g2) -> g1.getCode().compareTo(g2.getCode()))
				.map(grade -> {
					Map<String, String> g = Maps.newHashMap();
					g.put("id", grade.getId());
					g.put("name", grade.getName());
					return g;
				}).collect(Collectors.toList());
		ret.put("grades", gs);
		return ret;
	}

	@GetMapping(value = "/class_student/teacher/{tid}/classes", produces = JSON_PRODUCES)
	public Map<String, Object> getClassByTeacherGrade(@PathVariable String tid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(tid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "tid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		List<Classes> classes = classUserService.getClassByTeacherGrade(tid, gid);
		ret.put("code", CList.Api.Client.OK);
		if (null == classes) {
			return ret;
		}
		List<Map<String, String>> cls = classes.stream()
				.filter(cl -> LocalDate.now().getYear() == cl.getStartYear().intValue()
						|| LocalDate.now().getYear() == cl.getEndYear().intValue())
				.map(cl -> {
					Map<String, String> map = Maps.newHashMap();
					map.put("id", cl.getId());
					map.put("name", cl.getName());
					return map;
				}).sorted((cl1, cl2) -> cl1.get("name").compareTo(cl2.get("name"))).collect(Collectors.toList());

		ret.put("classes", cls);
		return ret;
	}

	@GetMapping(value = "/class_student/students", produces = JSON_PRODUCES)
	public Map<String, Object> getStudentsByClass(String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		List<Map<String, Object>> students = classUserService.getStudentsByClass(cid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("students", students);
		System.out.println("----------students:" + students);
		return ret;
	}

	@GetMapping(value = "/class_student/teachers_classes", produces = JSON_PRODUCES)
	public Map<String, Object> getTeachersClasses(String sid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		List<Classes> classes = classUserService.getClassBySchoolGrade(sid, gid);
		ret.put("code", CList.Api.Client.OK);
		if (null == classes) {
			return ret;
		}
		Map<String, List<Classes>> group = classes.stream()
				.filter(cl -> LocalDate.now().getYear() == cl.getStartYear().intValue()
						|| LocalDate.now().getYear() == cl.getEndYear().intValue())
				.collect(Collectors.groupingBy(cl -> cl.getTeacherId()));
		Map<String, List<Map<String, String>>> result = Maps.newHashMap();
		group.forEach((tid, cls) -> {
			if (null == cls) {
				return;
			}
			List<Map<String, String>> ms = cls.stream().map(cl -> {
				Map<String, String> m = Maps.newHashMap();
				m.put("id", cl.getId());
				m.put("name", cl.getName());
				return m;
			}).collect(Collectors.toList());
			result.put(tid, ms);
		});
		ret.put("teachers_classes", result);
		return ret;
	}

	@GetMapping(value = "/class_student/classes", produces = JSON_PRODUCES)
	public Map<String, Object> getClasses(String sid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		List<Classes> classes = classUserService.getClassBySchoolGrade(sid, gid);
		ret.put("code", CList.Api.Client.OK);
		if (null == classes || classes.isEmpty()) {
			return ret;
		}
		List<Map<String, String>> clss = classes.stream().map(cls -> {
			Map<String, String> c = Maps.newHashMap();
			c.put("id", cls.getId());
			c.put("name", cls.getName());
			c.put("teacher_name", cls.getTeacherName());
			return c;
		}).collect(Collectors.toList());
		ret.put("classes", clss);
		return ret;
	}

	@GetMapping(value = "/class_user/teachers", produces = JSON_PRODUCES)
	public Map<String, Object> getClassTeachers(String sid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		List<Map<String, String>> teachers = classUserService.getClassTeachers(sid, gid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("teachers", teachers);
		return ret;
	}

	@GetMapping(value = "/class_user/grades", produces = JSON_PRODUCES)
	public Map<String, Object> getGradesBySchool(String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		Set<Grade> grades = classesService.getGradesBySchool(sid);
		ret.put("code", CList.Api.Client.OK);
		if (null == grades || grades.isEmpty()) {
			return ret;
		}
		List<Map<String, String>> gs = grades.stream().map(grade -> {
			Map<String, String> map = Maps.newHashMap();
			map.put("id", grade.getId());
			map.put("name", grade.getName());
			return map;
		}).collect(Collectors.toList());
		ret.put("grades", gs);
		return ret;
	}

	/**
	 * 根据教师id查询教师所教授学生信息
	 */
	@GetMapping(value = "/class_user_students/{teacherId}", produces = JSON_PRODUCES)
	public Map<String, Object> getStudentsByTeacherId(@PathVariable String teacherId) {
		Map<String, Object> ret = Maps.newHashMap();
		List<ClassUser> classStudents = Lists.newArrayList();
		ClassUser cu = new ClassUser();
		cu.setUserId(teacherId);
		cu.setIsTeacher(true);
		try {
			List<ClassUser> classUsers = classUserService.findByCondionts(cu);
			if (classUsers != null && !classUsers.isEmpty()) {
				classUsers.forEach(cls -> {
					List<ClassUser> students = classUserService.getStudents(cls.getClassId());
					classStudents.addAll(students);
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.put("class_students", classStudents);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}
	
	/**
	 * 根据学校id查询所有学生数量
	 * 
	 * @param sid
	 *            学校id
	 */
	@GetMapping(value = "/school_students/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> getStudentsAmountBySchoolId(@PathVariable String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		int amount = classUserService.getStudentsAmountBySchoolId(sid);
		ret.put("student_amount", amount);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}
	
	/**
	 * 根据班级id查询班级任课老师
	 * 
	 * @param cid
	 *            班级id
	 */
	@GetMapping(value = "/class_teacher/{cid}", produces = JSON_PRODUCES)
	public Map<String, Object> getClassTeacherByClassId(@PathVariable String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		ClassUser cs = new ClassUser();
		cs.setClassId(cid);
		cs.setIsTeacher(true);
		List<ClassUser> classTeachers = Lists.newArrayList();
		try {
			classTeachers = classUserService.findByCondionts(cs);
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "inner exception !");
			return ret;
		}
		ret.put("class_teacher", classTeachers);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}
}
