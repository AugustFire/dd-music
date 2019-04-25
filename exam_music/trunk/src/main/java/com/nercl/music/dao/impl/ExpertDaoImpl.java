package com.nercl.music.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.nercl.music.dao.ExpertDao;
import com.nercl.music.entity.user.Expert;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class ExpertDaoImpl extends AbstractBaseDaoImpl<Expert, String> implements ExpertDao {

	@Override
	public List<Expert> list(int page) {
		String jpql = "from Expert ex";
		int count = this.executeCountQuery("select count(*) " + jpql);
		List<Expert> experts = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE);
		return PaginateSupportUtil.pagingList(experts, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public Expert get(String id) {
		String jpql = "from Expert ex where ex.id = ?1";
		List<Expert> experts = this.executeQueryWithoutPaging(jpql, id);
		return null == experts || experts.isEmpty() ? null : experts.get(0);
	}

	@Override
	public List<Expert> query(String key, int page) {
		String jpql = "from Expert ex where ex.name like ?1 or ex.email like ?2 or ex.phone like ?3 or ex.jobTitle like ?4 or ex.unit like ?5 or ex.intro like ?6";
		int count = this.executeCountQuery("select count(*) " + jpql, "%" + key + "%", "%" + key + "%", "%" + key + "%",
		        "%" + key + "%", "%" + key + "%", "%" + key + "%");
		List<Expert> experts = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE, "%" + key + "%",
		        "%" + key + "%", "%" + key + "%", "%" + key + "%", "%" + key + "%", "%" + key + "%");
		return PaginateSupportUtil.pagingList(experts, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public Expert getByPerson(String personId) {
		String jpql = "from Expert ex where ex.personId = ?1";
		List<Expert> experts = executeQueryWithoutPaging(jpql, personId);
		return null != experts && !experts.isEmpty() ? experts.get(0) : null;
	}

	@Override
	public List<Expert> listByAttributes(int page, String name, String jobTitle, String unit, String email,
			String phone) {
		List<String> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from Expert ex where 1=1");
		if (StringUtils.isNotBlank(name)) {
			jpql.append(" and ex.person.name like ?" + (++paramCount));
			params.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(jobTitle)) {
			jpql.append(" and ex.jobTitle like ?" + (++paramCount));
			params.add("%" + jobTitle + "%");
		}
		if (StringUtils.isNotBlank(unit)) {
			jpql.append(" and ex.unit like ?" + (++paramCount));
			params.add("%" + unit + "%");
		}
		if (StringUtils.isNotBlank(email)) {
			jpql.append(" and ex.person.email like ?" + (++paramCount));
			params.add("%" + email + "%");
		}
		if (StringUtils.isNotBlank(phone)) {
			jpql.append(" and ex.person.phone like ?" + (++paramCount));
			params.add("%" + phone + "%");
		}
		int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
		List<Expert> examinees = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE,
		        params.toArray());
		return PaginateSupportUtil.pagingList(examinees, page, DEFAULT_PAGESIZE, count);
	}

}
