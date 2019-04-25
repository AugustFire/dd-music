package com.nercl.music.cloud.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.base.BasicFacility;
import com.nercl.music.cloud.service.BasicFacilityService;
import com.nercl.music.constant.CList;

@RestController
public class BasicFacilityController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private BasicFacilityService basicFacilityService;

	@PostMapping(value = "/basic_facility", produces = JSON_PRODUCES)
	public Map<String, Object> add(String title, Integer num, Boolean isInstrument, String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		if (null == num || num <= 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "num is error");
			return ret;
		}
		if (null == isInstrument) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "isInstrument is null");
			return ret;
		}
		boolean success = basicFacilityService.save(title, num, isInstrument, sid);
		if (!success) {
			ret.put("code", CList.Api.Client.LOGIC_ERROR);
			ret.put("desc", "已有该title的基础设施了");
		} else {
			ret.put("code", CList.Api.Client.OK);
		}
		return ret;
	}

	@GetMapping(value = "/basic_facilitys", produces = JSON_PRODUCES)
	public Map<String, Object> get(String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		List<BasicFacility> bfs = basicFacilityService.get(sid);
		ret.put("code", CList.Api.Client.OK);
		if (null == bfs || bfs.isEmpty()) {
			return ret;
		}
		List<Integer> nums = Lists.newArrayList();
		List<Map<String, Object>> maps = bfs.parallelStream().map(bf -> {
			Map<String, Object> m = Maps.newHashMap();
			m.put("id", bf.getId());
			m.put("title", bf.getTitle());
			m.put("num", bf.getNum());
			boolean isInstrument = bf.getIsInstrument();
			m.put("is_instrument", isInstrument);
			if (isInstrument) {
				nums.add(bf.getNum());
			}
			m.put("school_id", bf.getSchoolId());
			return m;
		}).collect(Collectors.toList());
		ret.put("bfs", maps);
		ret.put("instrument_num", nums.parallelStream().mapToInt(in -> in).sum());
		return ret;
	}

	@PutMapping(value = "/basic_facility/{id}", produces = JSON_PRODUCES)
	public Map<String, Object> edit(@PathVariable String id, String title, Integer num, Boolean isInstrument) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		if (null == num || num <= 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "num is error");
			return ret;
		}
		if (null == isInstrument) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "isInstrument is null");
			return ret;
		}
		boolean success = basicFacilityService.edit(id, title, num, isInstrument);
		if (!success) {
			ret.put("code", CList.Api.Client.LOGIC_ERROR);
			ret.put("desc", "已有该title的基础设施了");
		} else {
			ret.put("code", CList.Api.Client.OK);
		}
		return ret;
	}

	@DeleteMapping(value = "/basic_facility/{id}", produces = JSON_PRODUCES)
	public Map<String, Object> delete(@PathVariable String id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
			return ret;
		}
		basicFacilityService.delete(id);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

}
