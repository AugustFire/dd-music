package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.area.City;

@Repository
public class CityDaoImpl extends AbstractBaseDaoImpl<City, String> implements CityDao {

	@Override
	public List<City> getCitys(String pid) {
		String jpql = "from City c where c.provinceId = ?1";
		List<City> citys = executeQueryWithoutPaging(jpql, pid);
		return citys;
	}

}
