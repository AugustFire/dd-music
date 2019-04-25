package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.area.Region;

@Repository
public class RegionDaoImpl extends AbstractBaseDaoImpl<Region, String> implements RegionDao {

	@Override
	public List<Region> getRegions(String cid) {
		String jpql = "from Region r where r.cityId = ?1";
		List<Region> regions = executeQueryWithoutPaging(jpql, cid);
		return regions;
	}

}
