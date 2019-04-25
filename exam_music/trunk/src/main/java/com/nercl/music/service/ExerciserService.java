package com.nercl.music.service;

import com.nercl.music.entity.user.Exerciser;

public interface ExerciserService {

	boolean register(String phone, String password);

	Exerciser getByLogin(String login);

	boolean complete(String eid, String name, String email, String gender, Integer age, String school, String grade,
	        String photo);

	Exerciser getById(String id);

}
