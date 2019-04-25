package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.user.Exerciser;

public interface ExerciserService {

	Exerciser get(String id);

	Exerciser getByLogin(String login);

	List<Exerciser> list(int page, String name, String email);

	// Login save(String password, String name, String phone, String idcard,
	// String school, Integer age, String email,
	// String gender);

	String getToken(String eid);

	Exerciser getByPerson(String pid);

}
