package com.nercl.music.cloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.nercl.music.cloud.dao.RecordDao;
import com.nercl.music.cloud.entity.Record;

@Service
@Transactional
public class RecordServiceImpl implements RecordService {

	@Autowired
	private RecordDao recordDao;

	@Override
	public boolean save(String userId, String songId, String rid, Integer tempo) {
		Record record = new Record();
		record.setUserId(userId);
		record.setSongId(songId);
		record.setRid(rid);
		record.setTempo(tempo);
		record.setCreateAt(System.currentTimeMillis());
		recordDao.save(record);
		return !Strings.isNullOrEmpty(record.getId());
	}

}
