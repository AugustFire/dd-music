package com.nercl.music.dao;

import com.nercl.music.entity.behavior.LogoutRecord;

import java.util.List;

public interface LogoutRecordDao extends BaseDao<LogoutRecord, String> {

	List<LogoutRecord> get(String name, String email, int page);
}
