package com.nercl.music.cloud.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.area.City;
import com.nercl.music.cloud.entity.area.Province;
import com.nercl.music.cloud.entity.area.Region;
import com.nercl.music.cloud.service.AreaService;
import com.nercl.music.constant.CList;

@RestController
public class AreaController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private AreaService areaService;

	@GetMapping(value = "/area/provinces", produces = JSON_PRODUCES)
	public Map<String, Object> getProvinces() {
		Map<String, Object> ret = Maps.newHashMap();
		List<Province> provinces = areaService.getProvinces();
		ret.put("code", CList.Api.Client.OK);
		List<Map<String, Object>> ps = Lists.newArrayList();
		ret.put("provinces", ps);
		if (null != provinces && !provinces.isEmpty()) {
			provinces.forEach(province -> {
				Map<String, Object> p = Maps.newHashMap();
				p.put("id", province.getId());
				p.put("code", province.getCode());
				p.put("name", province.getName());
				p.put("short_name", province.getShortName());
				ps.add(p);
			});
		}
		return ret;
	}

	@GetMapping(value = "/area/citys", produces = JSON_PRODUCES)
	public Map<String, Object> getCitys(String pid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(pid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "pid is null");
		}
		List<City> citys = areaService.getCitys(pid);
		ret.put("code", CList.Api.Client.OK);
		List<Map<String, Object>> cs = Lists.newArrayList();
		ret.put("citys", cs);
		if (null != citys && !citys.isEmpty()) {
			citys.forEach(city -> {
				Map<String, Object> c = Maps.newHashMap();
				c.put("id", city.getId());
				c.put("code", city.getCode());
				c.put("name", city.getName());
				cs.add(c);
			});
		}
		return ret;
	}

	@GetMapping(value = "/area/regions", produces = JSON_PRODUCES)
	public Map<String, Object> getRegions(String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
		}
		List<Region> regions = areaService.getRegions(cid);
		ret.put("code", CList.Api.Client.OK);
		List<Map<String, Object>> rs = Lists.newArrayList();
		ret.put("regions", rs);
		if (null != regions && !regions.isEmpty()) {
			regions.forEach(region -> {
				Map<String, Object> r = Maps.newHashMap();
				r.put("id", region.getId());
				r.put("name", region.getName());
				rs.add(r);
			});
		}
		return ret;
	}

	@GetMapping(value = "/area/district/province", produces = JSON_PRODUCES)
	public Map<String, Object> getDistrictProvince(String id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
		}
		Province province = areaService.findProvinceById(id);
		ret.put("code", CList.Api.Client.OK);
		ret.put("district", province);
		return ret;
	}

	@GetMapping(value = "/area/district/city", produces = JSON_PRODUCES)
	public Map<String, Object> getDistrictCity(String id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
		}
		City city = areaService.findCityById(id);
		ret.put("code", CList.Api.Client.OK);
		ret.put("district", city);
		return ret;
	}

	@GetMapping(value = "/area/district/region", produces = JSON_PRODUCES)
	public Map<String, Object> getDistrictRegion(String id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
		}
		Region region = areaService.findRegionById(id);
		ret.put("code", CList.Api.Client.OK);
		ret.put("district", region);
		return ret;
	}
}
