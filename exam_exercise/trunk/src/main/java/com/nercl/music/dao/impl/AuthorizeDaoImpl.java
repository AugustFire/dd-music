package com.nercl.music.dao.impl;

import com.google.common.collect.Lists;
import com.nercl.music.dao.AuthorizeDao;
import com.nercl.music.entity.authorize.AuthorizeRecord;
import com.nercl.music.util.page.PaginateSupportUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorizeDaoImpl extends AbstractBaseDaoImpl<AuthorizeRecord, String> implements AuthorizeDao {

	@Override
	public List<AuthorizeRecord> list(int page) {
		String jpql = "from AuthorizeRecord ar";
		int count = this.executeCountQuery("select count(*) " + jpql);
		List<AuthorizeRecord> authorizeRecords = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE);
		return PaginateSupportUtil.pagingList(authorizeRecords, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<AuthorizeRecord> list() {
		String jpql = "from AuthorizeRecord ar";
		List<AuthorizeRecord> authorizeRecords = this.executeQueryWithoutPaging(jpql);
		return authorizeRecords;
	}

	@Override
	public List<AuthorizeRecord> list(String toAuthorizerId, int page) {
		String jpql = "from AuthorizeRecord ar where ar.toAuthorizerId = ?1";
		int count = this.executeCountQuery("select count(*) " + jpql, toAuthorizerId);
		List<AuthorizeRecord> authorizeRecords = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE,
				toAuthorizerId);
		return PaginateSupportUtil.pagingList(authorizeRecords, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<AuthorizeRecord> list(String toAuthorizerId) {
		String jpql = "from AuthorizeRecord ar where ar.toAuthorizerId = ?1";
		List<AuthorizeRecord> authorizeRecords = this.executeQueryWithoutPaging(jpql, toAuthorizerId);
		return authorizeRecords;
	}

	@Override
	public List<AuthorizeRecord> get(String exerciseId, String topicId) {
		String jpql = "from AuthorizeRecord ar where ar.toAuthorizerId = ?1 and ar.topicId = ?2";
		List<AuthorizeRecord> authorizeRecords = this.executeQueryWithoutPaging(jpql, exerciseId, topicId);
		return authorizeRecords;
	}

	@Override
	public List<AuthorizeRecord> query(String title, Integer subjectType, String name, String login, int page) {
		List<Object> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from AuthorizeRecord ar where 1=1");
		if (StringUtils.isNotBlank(title)) {
			jpql.append(" and ar.topic.title like ?" + (++paramCount));
			params.add("%" + title + "%");
		}
		if (null != subjectType && subjectType > 0) {
			jpql.append(" and ar.topic.subjectType = ?" + (++paramCount));
			params.add(subjectType);
		}
		if (StringUtils.isNotBlank(name)) {
			jpql.append(" and ar.toAuthorizer.name like ?" + (++paramCount));
			params.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(login)) {
			jpql.append(" and ar.toAuthorizer.email like ?" + (++paramCount));
			params.add("%" + login + "%");
		}
		int count = this.executeCountQuery("select count(*) " + jpql, params.toArray());
		List<AuthorizeRecord> authorizeRecords = this.executeQueryWithPaging("select ar " + jpql.toString(), page,
				DEFAULT_PAGESIZE, params.toArray());
		return PaginateSupportUtil.pagingList(authorizeRecords, page, DEFAULT_PAGESIZE, count);
	}

}
