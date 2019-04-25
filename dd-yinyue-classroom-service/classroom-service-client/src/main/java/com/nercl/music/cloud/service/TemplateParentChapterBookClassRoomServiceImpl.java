package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.nercl.music.cloud.dao.TemplateParentChapterBookClassRoomDao;
import com.nercl.music.cloud.entity.template.TemplateParentChapterBookClassRoom;

@Service
@Transactional
public class TemplateParentChapterBookClassRoomServiceImpl implements TemplateParentChapterBookClassRoomService {

	@Autowired
	private TemplateParentChapterBookClassRoomDao templateParentChapterBookClassRoomDao;

	@Override
	public TemplateParentChapterBookClassRoom getBookTemplate(String bookId, String chapterId) {
		return templateParentChapterBookClassRoomDao.getBookTemplateWithBook(bookId, chapterId);
	}

	@Override
	public List<TemplateParentChapterBookClassRoom> getBookTemplate(String bookId) {
		return templateParentChapterBookClassRoomDao.getBookTemplate(bookId);
	}

	@Override
	public boolean hasBookTemplate(String bookId, String chapterId, String templateId) {
		return null != templateParentChapterBookClassRoomDao.getBookTemplate(bookId, chapterId, templateId);
	}

	@Override
	public List<TemplateParentChapterBookClassRoom> getClassRoomTemplate(String classRoomId, String chapterId) {
		return templateParentChapterBookClassRoomDao.getClassRoomTemplate(classRoomId, chapterId);
	}

	@Override
	public boolean saveBookTemplate(String bookId, String chapterId, String templateId) {
		boolean hasBookTemplate = hasBookTemplate(bookId, chapterId, templateId);
		if (hasBookTemplate) {
			return false;
		}
		TemplateParentChapterBookClassRoom tpcbc = new TemplateParentChapterBookClassRoom();
		tpcbc.setBookId(bookId);
		tpcbc.setChapterId(chapterId);
		tpcbc.setTemplateId(templateId);
		templateParentChapterBookClassRoomDao.save(tpcbc);
		return !Strings.isNullOrEmpty(tpcbc.getId());
	}

	@Override
	public boolean saveClassRoomTemplate(String classRoomId, String chapterId, String templateId) {
		List<TemplateParentChapterBookClassRoom> tpcbc = getClassRoomTemplate(classRoomId, chapterId);
		if (null != tpcbc && !tpcbc.isEmpty()) {
			tpcbc.forEach(tp -> {
				templateParentChapterBookClassRoomDao.delete(tp);
			});
		}
		TemplateParentChapterBookClassRoom ntpcbc = new TemplateParentChapterBookClassRoom();
		ntpcbc.setClassRoomId(classRoomId);
		ntpcbc.setChapterId(chapterId);
		ntpcbc.setTemplateId(templateId);
		templateParentChapterBookClassRoomDao.save(ntpcbc);
		return !Strings.isNullOrEmpty(ntpcbc.getId());
	}

	@Override
	public boolean hasClassRoomTemplate(String classRoomId, String chapterId, String templateId) {
		return null != templateParentChapterBookClassRoomDao.getClassRoomTemplate(classRoomId, chapterId, templateId);
	}

	@Override
	public void save(TemplateParentChapterBookClassRoom tpcbc) {
		templateParentChapterBookClassRoomDao.save(tpcbc);

	}

	@Override
	public void deleteByClassroom(String roomId) {
		List<TemplateParentChapterBookClassRoom> tpcbcrs = templateParentChapterBookClassRoomDao.getByClassroom(roomId);
		if (null == tpcbcrs || tpcbcrs.isEmpty()) {
			return;
		}
		tpcbcrs.forEach(tpcbcr -> {
			templateParentChapterBookClassRoomDao.deleteById(tpcbcr.getId());
		});
	}

}
