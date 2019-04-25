package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.user.Expert;

public interface ExpertDao extends BaseDao<Expert, String> {

	List<Expert> list(int page);
	
	List<Expert> listByAttributes(int page,String name,String jobTitle,String unit,String email,String phone);

	Expert get(String id);

	List<Expert> query(String key, int page);

	Expert getByPerson(String personId);

}
