package com.nercl.music.cloud.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.NoticeDao;
import com.nercl.music.cloud.entity.classroom.Notice;

@Service
@Transactional
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private NoticeDao noticeDao;

	@Override
	public Notice found(String rid, String title, String content) {
		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setClassRoomId(rid);
		noticeDao.save(notice);
		return notice;
	}

}
