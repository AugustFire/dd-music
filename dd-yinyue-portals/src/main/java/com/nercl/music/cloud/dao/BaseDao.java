package com.nercl.music.cloud.dao;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T, ID extends Serializable> {

	void save(T t);

	T merge(T t);

	T findByID(ID id);

	void deleteById(ID id);

	void deleteByIds(ID[] ids);

	void delete(T t);

	void update(T t);

	List<T> findByConditions(T t) throws Exception;

	List<T> findByConditionsWithPaging(T t, int page) throws Exception;

	List<T> findAll(Class<T> t);
}
