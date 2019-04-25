package com.nercl.music.service.impl;

import com.google.common.base.Strings;
import com.nercl.music.dao.AuthorizeDao;
import com.nercl.music.entity.authorize.AuthorizeRecord;
import com.nercl.music.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthorizeServiceImpl implements AuthorizeService {

	@Autowired
	private AuthorizeDao authorizeDao;

	@Override
	public List<AuthorizeRecord> list(int page) {
		return authorizeDao.list(page);
	}

	@Override
	public List<AuthorizeRecord> list() {
		return authorizeDao.list();
	}

	@Override
	public List<AuthorizeRecord> list(String toAuthorizeId, int page) {
		return authorizeDao.list(toAuthorizeId, page);
	}

	@Override
	public List<AuthorizeRecord> list(String toAuthorizeId) {
		return authorizeDao.list(toAuthorizeId);
	}

	@Override
	public List<AuthorizeRecord> query(String title, Integer subjectType, String name, String login, int page) {
		return authorizeDao.query(title, subjectType, name, login, page);
	}

	@Override
	public boolean hasAuthorize(String exerciseId, String topicId) {
		List<AuthorizeRecord> authorizeRecord = authorizeDao.get(exerciseId, topicId);
		return null != authorizeRecord && !authorizeRecord.isEmpty();
	}

	@Override
	public boolean save(String topicId, String authorizerId, String toAuthorizerId) {
		boolean hasAuthorize = this.hasAuthorize(toAuthorizerId, topicId);
		if (!hasAuthorize) {
			AuthorizeRecord record = new AuthorizeRecord();
			record.setTopicId(topicId);
			record.setAuthorizerId(authorizerId);
			record.setToAuthorizerId(toAuthorizerId);
			record.setCreatAt(System.currentTimeMillis());
			authorizeDao.save(record);
			return !Strings.isNullOrEmpty(record.getId());
		}
		return false;
	}

}
