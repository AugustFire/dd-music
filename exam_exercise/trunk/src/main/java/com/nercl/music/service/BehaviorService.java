package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.behavior.DownloadQuestionRecord;
import com.nercl.music.entity.behavior.LoginRecord;
import com.nercl.music.entity.behavior.LogoutRecord;

public interface BehaviorService {

	void saveLogin(String personId, String ip);

	void saveLogout(String personId);

	void saveDownload(String personId, String examQuestionId);

	List<LoginRecord> getLoginRecords(String name, String email, int page);

	List<LogoutRecord> getLogoutRecords(String name, String email, int page);

	List<DownloadQuestionRecord> getDownloadRecords(String name, String email, int page);
}
