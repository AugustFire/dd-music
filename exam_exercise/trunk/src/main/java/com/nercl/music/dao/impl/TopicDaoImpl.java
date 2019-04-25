package com.nercl.music.dao.impl;

import com.google.common.collect.Lists;
import com.nercl.music.dao.TopicDao;
import com.nercl.music.entity.authorize.Topic;
import com.nercl.music.util.page.PaginateSupportUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TopicDaoImpl extends AbstractBaseDaoImpl<Topic, String> implements TopicDao {

	@Override
	public List<Topic> list() {
		String jpql = "from Topic t";
		List<Topic> topics = executeQueryWithoutPaging(jpql);
		return topics;
	}

	@Override
	public List<Topic> list(int page) {
		String jpql = "from Topic t";
		int count = this.executeCountQuery("select count(*) " + jpql);
		List<Topic> topics = executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE);
		return PaginateSupportUtil.pagingList(topics, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<Topic> list(Integer subjectType) {
		String jpql = "from Topic t where t.subjectType = ?1";
		List<Topic> topics = executeQueryWithoutPaging(jpql, subjectType);
		return topics;
	}

	@Override
	public List<Topic> query(String key) {
		String jpql = "from Topic t where t.title like ?1";
		List<Topic> topics = executeQueryWithoutPaging(jpql, "%" + key + "%");
		return topics;
	}

	@Override
	public Topic getNewest(Integer year) {
		String jpql = "from Topic t where t.year = ?1 order by t.startAt desc ";
		List<Topic> topics = executeQueryWithoutPaging(jpql, year);
		return topics != null && !topics.isEmpty() ? topics.get(0) : null;
	}

	@Override
	public List<Topic> getByYear(Integer year) {
		String jpql = "from Topic t where t.year = ?1 order by t.startAt desc ";
		return executeQueryWithoutPaging(jpql, year);
	}

	@Override
	public List<Topic> getPayed(String exerciserId) {
		String jpql = "select ar.topic from AuthorizeRecord ar where ar.toAuthorizerId = ?1 order by ar.creatAt desc ";
		return executeQueryWithoutPaging(jpql, exerciserId);
	}

	@Override
	public List<Topic> getByTitle(Integer subjectType, String title, int page) {
		String jpql = "from Topic t where 1=1 ";
		List<Object> params = Lists.newArrayList();
		int i = 1;
		if (StringUtils.isNotBlank(title)) {
			jpql += " and t.title like ?" + i;
			params.add("%" + title + "%");
			i++;
		}
		if (null != subjectType && subjectType > 0) {
			jpql += " and t.subjectType = ?" + i;
			params.add(subjectType);
		}
		int count = this.executeCountQuery("select count(*) " + jpql, params.toArray());
		List<Topic> topics = executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE, params.toArray());
		return PaginateSupportUtil.pagingList(topics, page, DEFAULT_PAGESIZE, count);
	}
}
