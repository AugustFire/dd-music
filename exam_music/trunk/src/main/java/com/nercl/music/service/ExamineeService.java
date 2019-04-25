package com.nercl.music.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.entity.user.Examinee;
import com.nercl.music.entity.user.ExamineeGroup;
import com.nercl.music.entity.user.Login;

public interface ExamineeService {

	List<Examinee> list(int page);

	List<Examinee> getByExam(String examId, int page);

	Login save(String login, String password, String name, String phone, String idcard, String examNo, String school,
	        Integer age, String email, String gender, String photo);

	Examinee getById(String id);

	boolean update(String id, String name, Integer age, String phone, String gender, String email, String school,
	        String idcard);

	Examinee getByLogin(String login);

	List<Examinee> list(String school, String idcard, String name, String phone, String email, String examNo, int page);

	void delete(String id);

	List<Map<String, String>> getAllUUIDAndName();

	boolean setGroup(String[] eid, String gid);

	List<Examinee> getByGroup(List<ExamineeGroup> groups);

}
