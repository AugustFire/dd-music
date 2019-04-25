package com.nercl.music.cloud.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public abstract class AbstractBaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID> {

	static final int DEFAULT_PAGESIZE = 10;

	@PersistenceContext
	EntityManager entityManager;

	Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public AbstractBaseDaoImpl() {
		entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void save(T t) {
		entityManager.persist(t);
	}

	public T merge(T t) {
		return entityManager.merge(t);
	}

	public void update(T t) {
		entityManager.merge(t);
	}

	public void deleteById(ID id) {
		final Class<T> pclass = this.entityClass;
		T t = entityManager.find(pclass, id);
		if (null != t) {
			entityManager.remove(t);
		}
	}

	public void deleteByIds(ID[] ids) {
		for (ID id : ids) {
			deleteById(id);
		}
	}

	public void delete(T t) {
		if (null != t) {
			entityManager.remove(t);
		}
	}

	public void delete(String jpql) {
		entityManager.createQuery(jpql).executeUpdate();
	}

	public T findByID(ID id) {
		final Class<T> pclass = this.entityClass;
		return entityManager.find(pclass, id);
	}

	protected int executeCountQuery(String queryString, Object... restricts) {
		Query query = entityManager.createQuery(queryString);
		for (int i = 0; i < restricts.length; i++) {
			query.setParameter(i + 1, restricts[i]);
		}
		Number singleResult = null;
		try {
			singleResult = (Number) query.getSingleResult();
		} catch (NoResultException e) {
		}
		return null == singleResult ? 0 : singleResult.intValue();
	}

	@SuppressWarnings("unchecked")
	protected List<T> executeQueryWithPaging(String queryString, int page, int pageSize, Object... restricts) {
		page = (page < 1) ? 1 : page;
		Query query = entityManager.createQuery(queryString);
		for (int i = 0; i < restricts.length; i++) {
			query.setParameter(i + 1, restricts[i]);
		}
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	protected List<T> executeQueryWithoutPaging(String queryString, Object... restricts) {
		Query query = entityManager.createQuery(queryString);
		for (int i = 0; i < restricts.length; i++) {
			query.setParameter(i + 1, restricts[i]);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	protected List<T> executeQueryWithoutPagingForMap(String queryString, Map<String, ?> keyAndValues) {
		Query query = entityManager.createQuery(queryString);
		for (Map.Entry<String, ?> entry : keyAndValues.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	protected T findTop(int top, String queryString, Object... restricts) {
		Query query = entityManager.createQuery(queryString);
		for (int i = 0; i < restricts.length; i++) {
			query.setParameter(i + 1, restricts[i]);
		}
		query.setFirstResult(top - 1);
		query.setMaxResults(1);
		List<T> res = query.getResultList();
		if (res != null && !res.isEmpty()) {
			return res.get(0);
		}
		return null;
	}

	/**
	 * 根据实体中的属性值进行查询
	 * */
	@SuppressWarnings("unchecked")
	public List<T> findByConditions(T t) throws Exception{
		StringBuffer sbf = new StringBuffer();
		Class<? extends Object> class1 = t.getClass();
		String className = class1.getName();
		sbf.append("from "+className+" cn where 1=1 ");
		Field[] fields = class1.getDeclaredFields();
		int param = 1;
		LinkedList<Object> list = new LinkedList<>(); 
		for(Field f:fields){
			String fieldName = f.getName();
			Class<?> fieldType = f.getType();
			if (!fieldName.equals("serialVersionUID")) {
				String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method method = class1.getMethod(methodName);
				Object invoke = method.invoke(t);
				if (null != invoke && !"".equals(invoke)) {
					if (fieldType.getName().equals("java.lang.Boolean")) {
						sbf.append(" and cn." + fieldName + " is ?" + param);
					} else {
						sbf.append(" and cn." + fieldName + " = ?" + param);
					}
					param++;
					list.add(invoke);
				}
			}
		}
		Query query = entityManager.createQuery(sbf.toString());
		entityManager.createQuery(sbf.toString());
		for(int i=0;i<param-1;i++){
			Object object = list.get(i);
			query.setParameter(i+1, object);
		}
		return query.getResultList();
	}
}
