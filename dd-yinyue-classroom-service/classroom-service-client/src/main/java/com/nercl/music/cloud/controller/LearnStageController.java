package com.nercl.music.cloud.controller;

import java.util.List;
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
import com.nercl.music.cloud.entity.base.LearnStage;
import com.nercl.music.cloud.service.LearnStageService;
import com.nercl.music.constant.CList;
import com.nercl.music.util.CommonUtils;

@RestController
public class LearnStageController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private LearnStageService learnStageService;
	
	/**
	 * 新增学段
	 * */
	@PostMapping(value = "/learn_stage", produces = JSON_PRODUCES)
	public Map<String,Object> save(String title){
		Map<String,Object> ret = Maps.newHashMap();
		if(Strings.isNullOrEmpty(title)){
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc","title is null");
			return ret;
		}
		String code = CommonUtils.getRandomString(8);
		while(null != learnStageService.findByCode(code)){
			code = CommonUtils.getRandomString(8);
		}
		LearnStage learnStage = new LearnStage();
		learnStage.setCode(code);
		learnStage.setTitle(title);
		learnStageService.save(learnStage);
		ret.put("code", CList.Api.Client.OK);
		ret.put("learnStage", learnStage);
		return ret;
	}
	
	/**
	 * 根据Id删除学段
	 * */
	@DeleteMapping(value = "/learn_stage/{id}", produces = JSON_PRODUCES)
	public Map<String,Object> deleteById(@PathVariable String id){
		Map<String,Object> ret = Maps.newHashMap();
		if(Strings.isNullOrEmpty(id)){
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc","id is null");
			return ret;
		}
		learnStageService.deleteById(id);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}
	
	/**
	 * 修改学段
	 * */
	@PutMapping(value = "/learn_stage/{lid}", produces = JSON_PRODUCES)
	public Map<String,Object> update(@PathVariable String lid , String title){
		Map<String,Object> ret = Maps.newHashMap();
		if(Strings.isNullOrEmpty(title)){
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc","title is null");
			return ret;
		}
		LearnStage learnStage = learnStageService.findById(lid);
		if(null == learnStage){
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc","no such learnStage");
			return ret;
		}
		learnStage.setTitle(title);
		learnStageService.update(learnStage);
		ret.put("code", CList.Api.Client.OK);
		ret.put("learnStage",learnStage);
		return ret;
	}
	
	/**
	 * 根据Id查询学段
	 * */
	@GetMapping(value = "/learn_stage/{lid}", produces = JSON_PRODUCES)
	public Map<String,Object> getLearnStageById(@PathVariable String lid){
		Map<String,Object> ret = Maps.newHashMap();
		LearnStage learnStage = learnStageService.findById(lid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("learnStage", learnStage);
		return ret;
	}
	
	/**
	 * 查询所有学段
	 * */
	@GetMapping(value = "/learn_stages", produces = JSON_PRODUCES)
	public Map<String,Object> getLearnStages(){
		Map<String,Object> ret = Maps.newHashMap();
		List<LearnStage> list = learnStageService.getAllLearnStages();
		ret.put("code", CList.Api.Client.OK);
		ret.put("learnStages", list);
		return ret;
	}
}
