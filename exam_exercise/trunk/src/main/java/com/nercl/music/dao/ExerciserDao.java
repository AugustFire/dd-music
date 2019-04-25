package com.nercl.music.dao;

import com.nercl.music.entity.user.Exerciser;

import java.util.List;

public interface ExerciserDao extends BaseDao<Exerciser, String> {

	Exerciser getByPerson(String personId);

	List<Exerciser> list(int page, String name, String email);

}
