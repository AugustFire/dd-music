package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.ConsumeDao;
import com.nercl.music.entity.question.ConsumeRecord;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class ConsumeDaoImpl extends AbstractBaseDaoImpl<ConsumeRecord, String> implements ConsumeDao {

	@Override
	public List<ConsumeRecord> list(String exerciserId, int page) {
		String jpql = "from ConsumeRecord cr where cr.exerciserId = ?1";
		int count = this.executeCountQuery("select count(*) " + jpql, exerciserId);
		List<ConsumeRecord> consumeRecords = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE, exerciserId);
		return PaginateSupportUtil.pagingList(consumeRecords, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<ConsumeRecord> list(String exerciserId) {
		String jpql = "from ConsumeRecord cr where cr.exerciserId = ?1";
		List<ConsumeRecord> consumeRecords = this.executeQueryWithoutPaging(jpql, exerciserId);
		return consumeRecords;
	}

}
