package com.nercl.music.dao.impl;

import com.google.common.collect.Lists;
import com.nercl.music.dao.LoginRecordDao;
import com.nercl.music.entity.behavior.LoginRecord;
import com.nercl.music.util.page.PaginateSupportUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LoginRecordDaoImpl extends AbstractBaseDaoImpl<LoginRecord, String> implements LoginRecordDao {

	@Override
	public LoginRecord getNewest(String personId) {
		String jpql = "from LoginRecord r where r.personId = ?1 order by r.createAt desc ";
		List<LoginRecord> records = executeQueryWithoutPaging(jpql, personId);
		return null != records && !records.isEmpty() ? records.get(0) : null;
	}

	@Override
	public List<LoginRecord> get(String name, String email, int page) {
		String jpql = "from LoginRecord r where 1=1 ";
		List<Object> params = Lists.newArrayList();
		int i = 0;
		if (StringUtils.isNotBlank(name)) {
			jpql += " and r.person.name like ?" + (++i);
			params.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(email)) {
			jpql += " and r.person.email like ?" + (++i);
			params.add("%" + email + "%");
		}
		jpql += " order by r.createAt desc";

		int count = this.executeCountQuery("select count(*) " + jpql, params.toArray());
		List<LoginRecord> records = executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE, params.toArray());
		return PaginateSupportUtil.pagingList(records, page, DEFAULT_PAGESIZE, count);
	}
}
