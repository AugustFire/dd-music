package com.nercl.music.cloud.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.base.School;
import com.nercl.music.cloud.service.SchoolService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;
import com.nercl.music.util.CommonUtils;

@RestController
public class SchoolController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private SchoolService schoolService;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 新增学校
	 * 
	 * @param name
	 *            班级名称
	 * @param cityId
	 *            学校所在市Id
	 * @param regionId
	 *            学校所在区域Id
	 */
//	@PostMapping(value = "/school", produces = JSON_PRODUCES)
	public Map<String, Object> save(String cityId, String regionId, String name) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(name)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "name or code is null");
			return ret;
		}
		String code = CommonUtils.getRandomString(8);
		while (null != schoolService.findByCode(code)) {
			code = CommonUtils.getRandomString(8);
		}
		School school = new School();
		school.setName(name);
		school.setCode(code);
		school.setCityId(cityId);
		school.setRegionId(regionId);
		schoolService.save(school);
		ret.put("code", CList.Api.Client.OK);
		ret.put("school", school);
		return ret;
	}

	/**
	 * 根据Id删除学校
	 * 
	 * @param sid
	 *            学校Id
	 */
//	@DeleteMapping(value = "/school/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> deleteById(@PathVariable String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
			return ret;
		}
		try {
			schoolService.deleteById(sid);
			ret.put("code", CList.Api.Client.OK);
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				ret.put("desc", e.getMessage());
			} else {
				ret.put("desc", "delete fail");
			}
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		}
		return ret;
	}

	/**
	 * 修改学校信息
	 * 
	 * @param sid
	 *            学校Id
	 * @param name
	 *            班级名称
	 * @param cityId
	 *            学校所在城市Id
	 * @param regionId
	 *            学校所在区域Id
	 */
//	@PutMapping(value = "/school/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> update(@PathVariable String sid, String cityId, String regionId, String name) {
		Map<String, Object> ret = Maps.newHashMap();
		School school = schoolService.findById(sid);
		if (null == school) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no such school");
			return ret;
		}
		if (!Strings.isNullOrEmpty(cityId)) {
			school.setCityId(cityId);
		}
		if (!Strings.isNullOrEmpty(name)) {
			school.setName(name);
		}
		if (!Strings.isNullOrEmpty(regionId)) {
			school.setRegionId(regionId);
		}
		schoolService.update(school);
		ret.put("code", CList.Api.Client.OK);
		ret.put("school", school);
		return ret;
	}

	/**
	 * 根据条件查询学校
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/school/schools", produces = JSON_PRODUCES)
	public Map<String, Object> getSchoolByConditions(School school) {
		Map<String, Object> ret = Maps.newHashMap();
		List<Map<String, Object>> listMap = Lists.newArrayList();
		try {
			List<School> schoolList = schoolService.findByConditions(school);
			schoolList.forEach(sl -> {
				Map<String, Object> map = Maps.newHashMap();
				map.put("school", sl);
				String url = "";
				if (!Strings.isNullOrEmpty(sl.getRegionId())) {
					url = ApiClient.GET_USER_DISTRICTS_REGION + "?id=" + sl.getRegionId();
				} else {
					url = ApiClient.GET_USER_DISTRICTS_CITY + "?id=" + sl.getCityId();
				}
				Map<String, Object> districts = restTemplate.getForObject(url, Map.class);
				if (null != districts && (int) districts.get("code") == 200) {
					map.put("district", districts.get("district"));
				} else {
					map.put("district", null);
				}
				listMap.add(map);
			});
			ret.put("code", CList.Api.Client.OK);
			ret.put("school_list", listMap);
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		}
		return ret;
	}
}
