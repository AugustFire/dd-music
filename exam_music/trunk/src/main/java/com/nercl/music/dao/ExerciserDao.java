package com.nercl.music.dao;

import com.nercl.music.entity.user.Exerciser;

public interface ExerciserDao extends BaseDao<Exerciser, String> {

	Exerciser getByPerson(String personId);

}
