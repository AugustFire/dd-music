package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.area.City;
import com.nercl.music.cloud.entity.area.Province;
import com.nercl.music.cloud.entity.area.Region;

public interface AreaService {

	List<Province> getProvinces();

	List<City> getCitys(String pid);

	List<Region> getRegions(String cid);

	Province findProvinceById(String id);
	
	City findCityById(String id);
	
	Region findRegionById(String id);
}
