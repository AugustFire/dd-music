package com.nercl.music.cloud.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.service.KnowledgeService;
import com.nercl.music.constant.CList;

@RestController
public class KnowledgeController {

	@Autowired
	private KnowledgeService knowledgeService;

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";
	
	@GetMapping(value = "/knowledge/knowledges", produces = JSON_PRODUCES)
	public Map<String, Object> get(String parentId) {
		Map<String, Object> ret = Maps.newHashMap();
		if(Strings.isNullOrEmpty(parentId)){
			
		}else{
			
		}
		return ret;
	}

	@PostMapping(value = "/knowledge", produces = JSON_PRODUCES)
	public Map<String, Object> add(String no, String title, Float difficulty, String parentId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		boolean success = knowledgeService.save(no, title, difficulty, parentId);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "add failed");
		}
		return ret;
	}

	@PutMapping(value = "/knowledge/{kid}", produces = JSON_PRODUCES)
	public Map<String, Object> update(@PathVariable String kid, String no, String title, Float difficulty,
			String parentId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(kid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "kid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		boolean success = knowledgeService.update(kid, no, title, difficulty, parentId);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "update failed");
		}
		return ret;
	}

	@DeleteMapping(value = "/knowledge/{kid}", produces = JSON_PRODUCES)
	public Map<String, Object> delete(@PathVariable String kid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(kid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "kid is null");
			return ret;
		}
		boolean success = knowledgeService.delete(kid);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "delete failed");
		}
		return ret;
	}

}
