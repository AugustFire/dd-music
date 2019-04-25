package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.user.Expert;

public interface ExpertService {

	List<Expert> list(int page);

	List<Expert> listByAttributes(int page, String name, String jobTitle, String unit, String email, String phone);

	Expert get(String id);

	List<Expert> query(String key, int page);

	boolean save(String login, String name, String jobTitle, String unit, String phone, String email, String intro);

	boolean update(String id, String login, String name, String jobTitle, String unit, String phone, String email,
	        String intro);

	Expert getByLogin(String login);

	String getLoginByExpertId(String expertId);

	void delete(String id);

	boolean setGroup(String[] eid, String gid);

}
