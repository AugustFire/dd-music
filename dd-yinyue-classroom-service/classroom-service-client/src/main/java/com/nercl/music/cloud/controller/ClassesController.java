package com.nercl.music.cloud.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.base.Classes;
import com.nercl.music.cloud.entity.base.Grade;
import com.nercl.music.cloud.service.ClassesService;
import com.nercl.music.cloud.service.GradeService;
import com.nercl.music.constant.CList;
import com.nercl.music.util.page.PaginateSupportArray;

@RestController
public class ClassesController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private GradeService gradeService;

	@Autowired
	private ClassesService classesService;

	/**
	 * 新增班级
	 * 
	 * @param gid
	 *            年级Id
	 * @param name
	 *            班级名称
	 * @param schoolId
	 *            学校Id
	 * @param startYear
	 *            开始年度
	 * @param endYear
	 *            结束年度
	 */
	// @PostMapping(value = "/class", produces = JSON_PRODUCES)
	public Map<String, Object> save(String gid, String name, String schoolId, int startYear, int endYear) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(name)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "name or code is null");
			return ret;
		}
		Grade grade = gradeService.findById(gid);
		if (null == grade) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "grade not found");
			return ret;
		}
		Classes classes = new Classes();
		classes.setName(name);
		classes.setGradeId(gid);
		classes.setSchoolId(schoolId);
		classes.setStartYear(startYear);
		classes.setEndYear(endYear);
		classesService.save(classes);
		ret.put("code", CList.Api.Client.OK);
		ret.put("class", classes);
		return ret;
	}

	/**
	 * 根据Id删除班级
	 * 
	 * @param cid
	 *            班级Id
	 */
	// @DeleteMapping(value = "/class/{cid}", produces = JSON_PRODUCES)
	public Map<String, Object> deleteById(@PathVariable String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
			return ret;
		}
		try {
			classesService.deleteById(cid);
			ret.put("code", CList.Api.Client.OK);
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				ret.put("desc", e.getMessage());
			} else {
				ret.put("desc", "delete fail");
			}
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		}
		return ret;
	}

	/**
	 * 修改班级
	 * 
	 * @param cid
	 *            班级id
	 * @param name
	 *            班级名称
	 */
	// @PutMapping(value = "/class/{cid}", produces = JSON_PRODUCES)
	public Map<String, Object> update(@PathVariable String cid, String name, String gradeId) {
		Map<String, Object> ret = Maps.newHashMap();
		Classes classes = classesService.findById(cid);
		if (null == classes) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no such class");
			return ret;
		}
		if (!Strings.isNullOrEmpty(name)) {
			classes.setName(name);
		}
		if (!Strings.isNullOrEmpty(gradeId)) {
			classes.setGradeId(gradeId);
		}
		classesService.update(classes);
		ret.put("code", CList.Api.Client.OK);
		ret.put("class", classes);
		return ret;
	}

	/**
	 * 根据条件查询班级
	 */
	@GetMapping(value = "/class/classes", produces = JSON_PRODUCES)
	public Map<String, Object> getGradeByConditions(Classes classes,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		Map<String, Object> ret = Maps.newHashMap();
		try {
			List<Classes> classList = classesService.findByConditions(classes, page);
			ret.put("code", CList.Api.Client.OK);
			ret.put("classList", classList);
			if (classList instanceof PaginateSupportArray) {
				PaginateSupportArray<Classes> psa = (PaginateSupportArray<Classes>) classList;
				ret.put("total", psa.getTotal());
				ret.put("page_size", psa.getPageSize());
				ret.put("current_page", psa.getPage());
			}
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		}
		return ret;
	}

	@GetMapping(value = "/class/{cid}", produces = JSON_PRODUCES)
	public Map<String, Object> get(@PathVariable String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		Classes classes = classesService.findById(cid);
		if (null == classes) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no such class");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("id", classes.getId());
		ret.put("name", classes.getName());
		ret.put("gid", classes.getGradeId());
		Grade grade = gradeService.findById(classes.getGradeId());
		if (null != grade) {
			ret.put("gcode", grade.getCode());
		}
		ret.put("sid", classes.getSchoolId());
		ret.put("start_year", classes.getStartYear());
		ret.put("end_year", classes.getEndYear());
		return ret;
	}

	@GetMapping(value = "/class/grades", produces = JSON_PRODUCES)
	public Map<String, Object> getGradesBySchool(String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		Set<Grade> grades = classesService.getGradesBySchool(sid);
		ret.put("code", CList.Api.Client.OK);
		List<Map<String, String>> gs = Lists.newArrayList();
		grades.forEach(grade -> {
			Map<String, String> g = Maps.newHashMap();
			g.put("id", grade.getId());
			g.put("name", grade.getName());
			gs.add(g);
		});
		ret.put("grades", gs);
		return ret;
	}

}
