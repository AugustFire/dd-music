package com.nercl.music.cloud.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.RandomUtils;

import com.google.common.collect.Lists;
import com.nercl.music.util.CommonUtils;
import com.nercl.music.util.page.PaginateSupportUtil;

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
	protected List<Object[]> executeQuery(String queryString, Object... restricts) {
		Query query = entityManager.createQuery(queryString);
		for (int i = 0; i < restricts.length; i++) {
			query.setParameter(i + 1, restricts[i]);
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

	@SuppressWarnings("unchecked")
	protected List<T> findRandom(int size, int count, String queryString, Object... restricts) {
		if (size <= 0) {
			return null;
		}
		if (count <= 0) {
			return null;
		}
		Query query = entityManager.createQuery(queryString);
		for (int i = 0; i < restricts.length; i++) {
			query.setParameter(i + 1, restricts[i]);
		}
		if (size >= count) {
			return query.getResultList();
		}
		List<T> rusult = Lists.newArrayList();
		List<String> randoms = Lists.newArrayList();
		while (rusult.size() < size) {
			Integer random = Integer.valueOf(RandomUtils.nextInt(0, count));
			if (randoms.contains(String.valueOf(random))) {
				continue;
			}
			query.setFirstResult(random);
			query.setMaxResults(1);
			rusult.addAll(query.getResultList());
			randoms.add(String.valueOf(random));
		}
		return rusult;
	}

	/**
	 * 根据实体中的属性值进行查询
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByConditions(T t) throws Exception {
		StringBuffer sbf = new StringBuffer();
		Class<? extends Object> class1 = t.getClass();
		String className = class1.getName();
		sbf.append("from " + className + " cn where 1=1 ");
		Field[] fields = class1.getDeclaredFields();
		int param = 1;
		LinkedList<Object> list = new LinkedList<>();
		for (Field f : fields) {
			String fieldName = f.getName();
			Class<?> fieldType = f.getType();
			if (!fieldName.equals("serialVersionUID")) {
				String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method method = class1.getMethod(methodName);
				Object invoke = method.invoke(t);
				if (null != invoke && !"".equals(invoke)) { // 字段值不为空且不为null
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
		for (int i = 0; i < param - 1; i++) {
			Object object = list.get(i);
			query.setParameter(i + 1, object);
		}
		return query.getResultList();
	}

	/**
	 * 根据实体中的属性值进行查询
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByConditionsWithPaging(T t, int page) throws Exception {
		StringBuffer sbf = new StringBuffer();
		Class<? extends Object> class1 = t.getClass();
		String className = class1.getName();
		sbf.append("from " + className + " cn where 1=1 ");
		Field[] fields = class1.getDeclaredFields();
		int param = 1;
		LinkedList<Object> list = new LinkedList<>();
		for (Field f : fields) {
			String fieldName = f.getName();
			Class<?> fieldType = f.getType();
			if (!fieldName.equals("serialVersionUID")) {
				String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method method = class1.getMethod(methodName);
				Object invoke = method.invoke(t);
				if (null != invoke && !"".equals(invoke)) { // 字段值不为空且不为null
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
		int count = 0;
		if (list.isEmpty()) {
			count = this.executeCountQuery("select count(*) " + sbf.toString());
		} else {
			count = this.executeCountQuery("select count(*) " + sbf.toString(), list);
		}
		page = (page < 1) ? 1 : page;
		Query query = entityManager.createQuery(sbf.toString());
		for (int i = 0; i < param - 1; i++) {
			Object object = list.get(i);
			query.setParameter(i + 1, object);
		}
		query.setFirstResult((page - 1) * DEFAULT_PAGESIZE);
		query.setMaxResults(DEFAULT_PAGESIZE);
		List<T> results = query.getResultList();
		return PaginateSupportUtil.pagingList(results, page, DEFAULT_PAGESIZE, count);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<T> t) {
		String jpql = "from " + t.getName();
		return entityManager.createQuery(jpql).getResultList();
	}

	/**
	 * 在指定集合中查询指定数量的随机记录。此方法仅仅针对要取的随机数量不多的情况。
	 * 
	 * @param maxResults
	 *            查询结果集数量
	 * @param resultAmt
	 *            指定的查询数量
	 */
	@SuppressWarnings("unchecked")
	protected List<T> executeQueryRandom(String queryString, int maxResults, int resultAmt, Object... restricts) {
		Query query = entityManager.createQuery(queryString);
		for (int i = 0; i < restricts.length; i++) {
			query.setParameter(i + 1, restricts[i]);
		}
		List<T> resultList = Lists.newArrayList();
		// 查询满足条件的查询结果记录数，如果满足条件的记录数大于要取的随机记录的总数，则从满足条件的记录中随机取指定数量的记录
		if (maxResults > resultAmt) {
			// 取0到结果记录数中的resultAmt个随机数
			Set<Integer> randomIntSet = CommonUtils.getRandomInt(maxResults, resultAmt);
			Iterator<Integer> iterator = randomIntSet.iterator();
			// 此处根据取定的随机数循环查询结果，避免在在数据量很大时在sql中写rand()函数而拖慢速度
			while (iterator.hasNext()) {
				// 查询起始数为上面取的随机数
				query.setFirstResult((Integer) iterator.next());
				// 每次查询1条记录
				query.setMaxResults(1);
				resultList.addAll(query.getResultList());
			}
			return resultList;
		}
		// 如果满足条件的记录数不大于要取的随机记录的总数，则直接返回查询结果
		else {
			return query.getResultList();
		}
	}

	/**
	 * 查询指定条件的结果记录数
	 */
	protected int countQuery(String queryString, Object... restricts) {
		Query query = entityManager.createQuery(queryString);
		for (int i = 0; i < restricts.length; i++) {
			query.setParameter(i + 1, restricts[i]);
		}
		return query.getResultList().size();
	}
}