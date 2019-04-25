package com.nercl.music.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.authorize.Subject;
import com.nercl.music.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class SubjectController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private SubjectService subjectService;

	@GetMapping(value = "/subjects/json", produces = JSON_PRODUCES)
	@ResponseBody
	public Map<String, Object> getTopics() {
		Map<String, Object> ret = Maps.newHashMap();
		List<Subject> subjects = subjectService.list();
		ret.put("code", CList.Api.Client.OK);
		if (null != subjects) {
			List<Map<String, Object>> list = Lists.newArrayList();
			ret.put("subjects", list);
			subjects.forEach(subject -> {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", subject.getId());
				map.put("title", subject.getTitle());
				list.add(map);
			});
		}
		return ret;
	}

	@PostMapping(value = "/subject", produces = JSON_PRODUCES)
	@ResponseBody
	public Map<String, Object> save(String title) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "主题为空");
			return ret;
		}
		Subject subject = subjectService.save(title);
		if (null != subject) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("id", subject.getId());
			ret.put("title", subject.getTitle());
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "重复的主题");
		}
		return ret;
	}

	@DeleteMapping(value = "/subject/{id}", produces = JSON_PRODUCES)
	@ResponseBody
	public Map<String, Object> delete(@PathVariable String id) {
		Map<String, Object> ret = Maps.newHashMap();
		boolean success = subjectService.delete(id);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "删除失败");
		}
		return ret;
	}

}
