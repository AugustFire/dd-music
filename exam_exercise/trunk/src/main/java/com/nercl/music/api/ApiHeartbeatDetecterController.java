package com.nercl.music.api;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;

@RestController
public class ApiHeartbeatDetecterController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private HeartbeatDetecterManager heartbeatDetecterManager;

	@GetMapping(value = "/api/heartbeat_detecter", produces = JSON_PRODUCES)
	public Map<String, Object> detect(String exerciserId) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(exerciserId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "练习者id为空");
			return ret;
		}
		heartbeatDetecterManager.incitement(exerciserId);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

}
