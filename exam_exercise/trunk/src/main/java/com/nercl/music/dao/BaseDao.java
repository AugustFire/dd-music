package com.nercl.music.dao;

import java.io.Serializable;

public interface BaseDao<T, ID extends Serializable> {

	void save(T t);

	T merge(T t);

	T findByID(ID id);

	void deleteById(ID id);

	void deleteByIds(ID[] ids);

	void delete(T t);

	void update(T t);
}
