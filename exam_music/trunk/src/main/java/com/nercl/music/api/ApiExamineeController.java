package com.nercl.music.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.service.ExamineeService;

@RestController
public class ApiExamineeController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ExamineeService examineeService;

	@GetMapping(value = "/api/examinees", produces = JSON_PRODUCES)
	public Map<String, Object> getExaminees(HttpServletResponse response) {
		Map<String, Object> ret = Maps.newHashMap();
		List<Map<String, String>> idName = this.examineeService.getAllUUIDAndName();
		ret.put("code", CList.Api.Client.OK);
		ret.put("id_names", idName);
		return ret;
	}

}
