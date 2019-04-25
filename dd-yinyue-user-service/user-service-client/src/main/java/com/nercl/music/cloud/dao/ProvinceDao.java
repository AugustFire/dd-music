package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.area.Province;

public interface ProvinceDao extends BaseDao<Province, String> {

	List<Province> getProvinces();

}
