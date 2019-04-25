package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.area.Province;

@Repository
public class ProvinceDaoImpl extends AbstractBaseDaoImpl<Province, String> implements ProvinceDao {

	@Override
	public List<Province> getProvinces() {
		String jpql = "from Province p";
		List<Province> provinces = executeQueryWithoutPaging(jpql);
		return provinces;
	}

}
