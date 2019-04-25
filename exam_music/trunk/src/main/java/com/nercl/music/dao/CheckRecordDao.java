package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.CheckRecord;

public interface CheckRecordDao extends BaseDao<CheckRecord, String> {

	List<CheckRecord> getRecordsById(String id);

	String getUnpassReason(String id, String type);

}
