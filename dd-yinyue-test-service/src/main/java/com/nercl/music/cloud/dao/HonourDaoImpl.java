package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.nercl.music.cloud.entity.ActivityType;
import com.nercl.music.cloud.entity.AwardLevel;
import com.nercl.music.cloud.entity.Honour;
import com.nercl.music.util.page.PaginateSupportArray;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class HonourDaoImpl extends AbstractBaseDaoImpl<Honour, String> implements HonourDao {

	@Override
	public PaginateSupportArray<Honour> findAllHonours(String sid, ActivityType type, AwardLevel level, int page,
			int pageSize) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("from Honour ans where userRole = 'SCHOOL_MASTER' and schoolId = ?1").append(" ");
		List<Object> params = Lists.newLinkedList();
		params.add(sid);
		int i = 1;
		if (type != null) {
			i++;
			sbf.append("and activityType=?").append(i).append(" ");
			params.add(type);
		}
		if (level != null) {
			i++;
			sbf.append("and awardLevel=?").append(i).append(" ");
			params.add(level);
		}
		sbf.append(" order by honourTime Desc");
		int count = this.executeCountQuery("select count(*) " + sbf.toString(), params.stream().toArray(Object[]::new));
		List<Honour> listHonour = this.executeQueryWithPaging(sbf.toString(), page, pageSize,
				params.stream().toArray(Object[]::new));
		return PaginateSupportUtil.pagingList(listHonour, page, pageSize, count);
	}

}
