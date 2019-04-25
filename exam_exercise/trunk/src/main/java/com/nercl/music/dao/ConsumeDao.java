package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.question.ConsumeRecord;

public interface ConsumeDao extends BaseDao<ConsumeRecord, String> {

	List<ConsumeRecord> list(String exerciserId, int page);

	List<ConsumeRecord> list(String exerciserId);

}
