package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.user.Examinee;
import com.nercl.music.entity.user.ExamineeGroup;

public interface ExamineeDao extends BaseDao<Examinee, String> {

	List<Examinee> list();

	List<Examinee> list(int page);

	List<Examinee> getByExam(String examId, int page);

	Examinee getByPerson(String personId);

	List<Examinee> list(String school, String idcard, String name, String phone, String email, String examNo, int page);

	List<Examinee> getByGroup(List<ExamineeGroup> groups);

}
