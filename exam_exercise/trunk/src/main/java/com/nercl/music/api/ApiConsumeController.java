package com.nercl.music.api;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.service.ConsumeService;

@RestController
public class ApiConsumeController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ConsumeService consumeService;

	@PostMapping(value = "/api/consume/end", produces = JSON_PRODUCES)
	public Map<String, Object> end(String exerciserId, String consumeRecordId) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(exerciserId) || StringUtils.isBlank(consumeRecordId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "练习者id或者消费记录id为空");
			return ret;
		}
		boolean success = consumeService.end(exerciserId, consumeRecordId);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "结束消费失败");
		}
		return ret;
	}

}
