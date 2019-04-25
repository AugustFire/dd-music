package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.ClassroomTemplateChapterStatusDao;
import com.nercl.music.cloud.entity.template.ClassroomTemplateChapterStatus;
import com.nercl.music.cloud.entity.template.ClassroomTemplateChapterStatus.ChapterResourceStatus;

@Service
@Transactional
public class ClassroomTemplateChapterStatusServiceImpl implements ClassroomTemplateChapterStatusService {

	@Autowired
	private ClassroomTemplateChapterStatusDao classroomTemplateChapterStatusDao;

	@Override
	public boolean setStatus(String rid, String tid, String cid, ChapterResourceStatus status) {
		ClassroomTemplateChapterStatus chapterStatus = get(rid, tid, cid);
		if (null == chapterStatus) {
			ClassroomTemplateChapterStatus cstatus = new ClassroomTemplateChapterStatus();
			cstatus.setClassroomId(rid);
			cstatus.setTemplateId(tid);
			cstatus.setChapterId(cid);
			cstatus.setStatus(status);
			classroomTemplateChapterStatusDao.save(cstatus);
		} else {
			chapterStatus.setStatus(status);
			classroomTemplateChapterStatusDao.update(chapterStatus);
		}
		return true;
	}

	@Override
	public List<ClassroomTemplateChapterStatus> get(String rid, String tid) {
		return classroomTemplateChapterStatusDao.get(rid, tid);
	}

	@Override
	public ClassroomTemplateChapterStatus get(String rid, String tid, String cid) {
		return classroomTemplateChapterStatusDao.get(rid, tid, cid);
	}

	@Override
	public List<ClassroomTemplateChapterStatus> getByParentChapter(String rid, String tid, String cid) {
		return classroomTemplateChapterStatusDao.getByParentChapter(rid, tid, cid);
	}

}
