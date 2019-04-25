package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.CheckRecord;
import com.nercl.music.entity.user.Login;

public interface CheckRecordService {

	String getUnpassReason(String id, String type);

	void addRecord(CheckRecord checkRecord);

	List<CheckRecord> getRecordsById(String id);

	CheckRecord getCheckRecord(Login login, Object object, CheckRecord.Status status, String type, String unPassReason);

}
