package com.nercl.music.service;

import com.nercl.music.entity.authorize.AuthorizeRecord;

import java.util.List;

public interface AuthorizeService {

	List<AuthorizeRecord> list(int page);

	List<AuthorizeRecord> list();

	List<AuthorizeRecord> list(String toAuthorizeId, int page);

	List<AuthorizeRecord> list(String toAuthorizeId);

	List<AuthorizeRecord> query(String title, Integer subjectType, String name, String login, int page);

	boolean hasAuthorize(String exerciseId, String topicId);

	boolean save(String topicId, String authorizerId, String toAuthorizerId);

}
