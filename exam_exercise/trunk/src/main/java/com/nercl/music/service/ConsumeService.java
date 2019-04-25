package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.question.ConsumeRecord;

public interface ConsumeService {

	ConsumeRecord get(String consumeRecordId);

	List<ConsumeRecord> list(String exerciserId, int page);

	List<ConsumeRecord> list(String exerciserId);

	List<ConsumeRecord> query(String login, int page);

	ConsumeRecord start(String exerciserId);

	boolean end(String exerciserId, String consumeRecordId);

	boolean endConsume(String exerciserId);

}
