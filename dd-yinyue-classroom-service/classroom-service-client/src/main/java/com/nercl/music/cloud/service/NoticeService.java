package com.nercl.music.cloud.service;

import com.nercl.music.cloud.entity.classroom.Notice;

public interface NoticeService {

	Notice found(String rid, String title, String content);

}
