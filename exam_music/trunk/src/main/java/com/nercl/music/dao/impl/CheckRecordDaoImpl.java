package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.CheckRecordDao;
import com.nercl.music.entity.CheckRecord;

@Repository
public class CheckRecordDaoImpl extends AbstractBaseDaoImpl<CheckRecord, String> implements CheckRecordDao {

	@Override
	public List<CheckRecord> getRecordsById(String id) {
		String jpql = "from CheckRecord ch where ch.examQuestionId = ?1 or ch.examPaperId = ?2";
		List<CheckRecord> checkRecords = this.executeQueryWithoutPaging(jpql, id, id);
		return checkRecords;
	}

	@Override
	public String getUnpassReason(String id, String type) {
		String jpql = "";
		if (type.equals("question")) {
			jpql = "from CheckRecord ch where ch.examQuestionId = ?1 order by ch.checkAt DESC";
		} else if (type.equals("paper")) {
			jpql = "from CheckRecord ch where ch.examPaperId = ?1 order by ch.checkAt DESC";
		}
		List<CheckRecord> checkRecords = this.executeQueryWithoutPaging(jpql, id);
		return checkRecords != null ? checkRecords.get(0).getUnPassReason() : "";
	}
}
