package com.nercl.music.cloud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.cloud.dao.CityDao;
import com.nercl.music.cloud.dao.ProvinceDao;
import com.nercl.music.cloud.dao.RegionDao;
import com.nercl.music.cloud.entity.area.City;
import com.nercl.music.cloud.entity.area.Province;
import com.nercl.music.cloud.entity.area.Region;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {

	@Autowired
	private ProvinceDao provinceDao;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private RegionDao regionDao;

	@Override
	public List<Province> getProvinces() {
		return provinceDao.getProvinces();
	}

	@Override
	public List<City> getCitys(String pid) {
		return cityDao.getCitys(pid);
	}

	@Override
	public List<Region> getRegions(String cid) {
		return regionDao.getRegions(cid);
	}
	
	@Override
	public Province findProvinceById(String id) {
		return provinceDao.findByID(id);
	}

	@Override
	public City findCityById(String id) {
		return cityDao.findByID(id);
	}

	@Override
	public Region findRegionById(String id) {
		return regionDao.findByID(id);
	}
}
