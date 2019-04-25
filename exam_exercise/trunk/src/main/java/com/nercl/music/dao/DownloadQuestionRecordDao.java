package com.nercl.music.dao;

import com.nercl.music.entity.behavior.DownloadQuestionRecord;

import java.util.List;

public interface DownloadQuestionRecordDao extends BaseDao<DownloadQuestionRecord, String> {

	List<DownloadQuestionRecord> get(String name, String email, int page);

}
