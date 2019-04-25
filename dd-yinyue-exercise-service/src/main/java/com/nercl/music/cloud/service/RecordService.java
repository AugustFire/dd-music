package com.nercl.music.cloud.service;

public interface RecordService {

	boolean save(String userId, String songId, String rid, Integer tempo);

}
