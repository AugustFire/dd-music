package com.nercl.music.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.nercl.music.dao.ExamPointDao;
import com.nercl.music.entity.ExamPoint;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class ExamPointDaoImpl extends AbstractBaseDaoImpl<ExamPoint, String> implements ExamPointDao {

	@Override
	public List<ExamPoint> list(int page) {
		String jpql = "from ExamPoint ep";
		int count = this.executeCountQuery("select count(*) " + jpql);
		List<ExamPoint> examPoints = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE);
		return PaginateSupportUtil.pagingList(examPoints, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<ExamPoint> list() {
		String jpql = "from ExamPoint ep";
		return this.executeQueryWithoutPaging(jpql);
	}

	@Override
	public ExamPoint get(String examPoint) {
		String jpql = "from ExamPoint ep where ep.id = ?1";
		List<ExamPoint> examPoints = this.executeQueryWithoutPaging(jpql, examPoint);
		return null == examPoints || examPoints.isEmpty() ? null : examPoints.get(0);
	}

	@Override
	public ExamPoint getByName(String name) {
		String jpql = "from ExamPoint ep where ep.name = ?1";
		List<ExamPoint> examPoints = this.executeQueryWithoutPaging(jpql, name);
		return null == examPoints || examPoints.isEmpty() ? null : examPoints.get(0);
	}

	@Override
	public List<ExamPoint> listByAttributes(int page, String name, String address) {
		List<String> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from ExamPoint ep where 1=1");
		if (StringUtils.isNotBlank(name)) {
			jpql.append(" and ep.name like ?" + (++paramCount));
			params.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(address)) {
			jpql.append(" and ep.address like ?" + (++paramCount));
			params.add("%" + address + "%");
		}
		int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
		List<ExamPoint> examinees = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE,
		        params.toArray());
		return PaginateSupportUtil.pagingList(examinees, page, DEFAULT_PAGESIZE, count);
	}

}
