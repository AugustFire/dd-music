package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.area.City;

public interface CityDao extends BaseDao<City, String> {

	List<City> getCitys(String pid);

}
