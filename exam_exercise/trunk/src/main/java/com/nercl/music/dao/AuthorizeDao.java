package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.authorize.AuthorizeRecord;

public interface AuthorizeDao extends BaseDao<AuthorizeRecord, String> {

	List<AuthorizeRecord> list(int page);

	List<AuthorizeRecord> list();

	List<AuthorizeRecord> list(String toAuthorizerId, int page);

	List<AuthorizeRecord> list(String toAuthorizerId);

	List<AuthorizeRecord> get(String exerciseId, String topicId);

	List<AuthorizeRecord> query(String title, Integer subjectType, String name, String login, int page);

}
