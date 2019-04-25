package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.area.Region;

public interface RegionDao extends BaseDao<Region, String> {

	List<Region> getRegions(String cid);

}
