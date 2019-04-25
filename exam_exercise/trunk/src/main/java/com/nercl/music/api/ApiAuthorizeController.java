package com.nercl.music.api;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.service.AuthorizeService;

@RestController
public class ApiAuthorizeController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private AuthorizeService authorizeService;

	@GetMapping(value = "/api/authorize", produces = JSON_PRODUCES)
	public Map<String, Object> authorize(String topic_id, String exerciser_id) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(topic_id) || StringUtils.isBlank(exerciser_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "专题id或者练习者id为空");
			return ret;
		}
		boolean hasAuthorize = authorizeService.hasAuthorize(exerciser_id, topic_id);
		ret.put("code", CList.Api.Client.OK);
		ret.put("topic_id", topic_id);
		ret.put("has_authorize", hasAuthorize);
		return ret;
	}

}
