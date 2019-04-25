package com.nercl.music.cloud.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.base.Grade;
import com.nercl.music.cloud.entity.base.LearnStage;
import com.nercl.music.cloud.entity.base.School;
import com.nercl.music.cloud.service.GradeService;
import com.nercl.music.cloud.service.LearnStageService;
import com.nercl.music.cloud.service.SchoolService;
import com.nercl.music.constant.CList;
import com.nercl.music.util.CommonUtils;

@RestController
public class GradeController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private GradeService gradeService;

	@Autowired
	private LearnStageService learnStageService;

	@Autowired
	private SchoolService schoolService;

	/**
	 * 新增年级
	 * 
	 * @param lid
	 *            学段Id
	 * @param name
	 *            年级名称
	 * @param schoolId
	 *            学校Id
	 */
	// @PostMapping(value = "/grade", produces = JSON_PRODUCES)
	public Map<String, Object> save(String lid, String name) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(name)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "name is null");
			return ret;
		}
		LearnStage learnStage = learnStageService.findById(lid);
		if (null == learnStage) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "learnStage not found");
			return ret;
		}
		String code = CommonUtils.getRandomString(8);
		while (null != gradeService.findByCode(code)) {
			code = CommonUtils.getRandomString(8);
		}
		Grade grade = new Grade();
		grade.setCode(code);
		grade.setName(name);
		grade.setLearnStageId(lid);
		gradeService.save(grade);
		ret.put("code", CList.Api.Client.OK);
		ret.put("grade", grade);
		return ret;
	}

	/**
	 * 根据Id删除年级
	 * 
	 * @param id
	 *            年级Id
	 */
	// @DeleteMapping(value = "/grade/{id}", produces = JSON_PRODUCES)
	public Map<String, Object> deleteById(@PathVariable String id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
			return ret;
		}
		try {
			gradeService.deleteById(id);
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
	 * 修改年级
	 * 
	 * @param gid
	 *            年级Id
	 * @param lid
	 *            修改的年级对应的学段
	 * @param name
	 *            年级名称
	 */
	// @PutMapping(value = "/grade/{gid}", produces = JSON_PRODUCES)
	public Map<String, Object> update(@PathVariable String gid, String name, String lid) {
		Map<String, Object> ret = Maps.newHashMap();
		Grade grade = gradeService.findById(gid);
		if (null == grade) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no such grade");
			return ret;
		}
		if (!Strings.isNullOrEmpty(name)) {
			grade.setName(name);
		}
		if (!Strings.isNullOrEmpty(lid)) {
			grade.setLearnStageId(lid);
		}
		gradeService.update(grade);
		ret.put("code", CList.Api.Client.OK);
		ret.put("grade", grade);
		return ret;
	}

	/**
	 * 根据条件查询年级
	 */
	@GetMapping(value = "/grades", produces = JSON_PRODUCES)
	public Map<String, Object> getGradeByLearnStage(Grade grade) {
		Map<String, Object> ret = Maps.newHashMap();
		try {
			ret.put("code", CList.Api.Client.OK);
			ret.put("gradeList", gradeService.findByConditions(grade));
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "inner exception");
		}
		return ret;
	}

	/**
	 * 根据学校id查询年级列表
	 */
	@GetMapping(value = "/grades/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> getGradeByLearnStage(@PathVariable String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		try {
			School school = schoolService.findById(sid);
			if (school == null) {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "school not exist");
				return ret;
			}
			Grade grade = new Grade();
			grade.setLearnStageId(school.getLearnStageId());
			ret.put("code", CList.Api.Client.OK);
			ret.put("gradeList", gradeService.findByConditions(grade));
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "inner exception");
		}
		return ret;
	}
}
