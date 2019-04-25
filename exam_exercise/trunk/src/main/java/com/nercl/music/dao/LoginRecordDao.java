package com.nercl.music.dao;

import com.nercl.music.entity.behavior.LoginRecord;

import java.util.List;

public interface LoginRecordDao extends BaseDao<LoginRecord, String> {
	LoginRecord getNewest(String personId);

	List<LoginRecord> get(String name, String email, int page);
}
