package com.nercl.music.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.nercl.music.dao.GroupDao;
import com.nercl.music.entity.user.AbstractGroup;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class GroupDaoImpl extends AbstractBaseDaoImpl<AbstractGroup, String> implements GroupDao {

	@Override
	public List<AbstractGroup> getExamineeGroups() {
		String jpql = "from AbstractGroup ag where ag.class = 'ExamineeGroup'";
		List<AbstractGroup> groups = this.executeQueryWithoutPaging(jpql);
		return groups;
	}

	@Override
	public List<AbstractGroup> getExpertGroups() {
		String jpql = "from AbstractGroup ag where ag.class = 'ExpertGroup'";
		List<AbstractGroup> groups = this.executeQueryWithoutPaging(jpql);
		return groups;
	}

	@Override
	public List<AbstractGroup> query(String examId, String examRoom, boolean isExamineee, int page) {
		List<String> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from AbstractGroup ag where 1=1");
		if (StringUtils.isNotBlank(examId)) {
			jpql.append(" and ag.examId = ?" + (++paramCount));
			params.add(examId);
		}
		if (isExamineee) {
			jpql.append(" and ag.class = 'ExamineeGroup'");
		}else{
			jpql.append(" and ag.class = 'ExpertGroup'");
		}
		if (StringUtils.isNotBlank(examRoom)) {
		}
		int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
		List<AbstractGroup> groups = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE,
		        params.toArray());
		return PaginateSupportUtil.pagingList(groups, page, DEFAULT_PAGESIZE, count);
	}

}
