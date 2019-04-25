package com.nercl.music.cloud.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.service.RecordService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;

@RestController
public class RecordController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private RecordService recordService;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping(value = "/songs", produces = JSON_PRODUCES)
	public Map<String, Object> getSongs(String uid, String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gradeId is null");
			return ret;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> songs = restTemplate.getForObject(ApiClient.GET_CLASSROOM_SONGS, Map.class, uid, gid);
		if(null == songs.get("desc")){
			ret.put("code", CList.Api.Client.OK);
			ret.put("uid", uid);
			ret.put("gid", gid);
			ret.put("songs", songs.get("songs"));
		}else{
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", songs.get("desc"));
		}
		return ret;
	}

	@PostMapping(value = "/record", produces = JSON_PRODUCES)
	public Map<String, Object> post(String uid, String sid, String rid, Integer tempo) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(rid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rid is null");
			return ret;
		}
		boolean success = recordService.save(uid, sid, rid, tempo);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("uid", uid);
			ret.put("sid", sid);
			ret.put("rid", rid);
			ret.put("tempo", tempo);
			return ret;
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "save record failed");
			return ret;
		}
	}

}
