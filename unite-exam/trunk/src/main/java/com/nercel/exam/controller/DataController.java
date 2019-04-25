package com.nercel.exam.controller;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.nercel.exam.constant.CList;

@RestController
public class DataController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Value("${unit-exam.data}")
	private String data;

	@GetMapping(value = "/{staff}/data", produces = JSON_PRODUCES)
	public Map<String, Object> get(@PathVariable String staff) {
		Map<String, Object> ret = Maps.newHashMap();
		File json = new File(data + File.separator + staff + ".json");
		if (!json.exists()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no json file be found");
			return ret;
		}
		String data = null;
		try {
			data = FileUtils.readFileToString(json, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Strings.isNullOrEmpty(data)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no json file be found");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("data", data);
		return ret;
	}

}
