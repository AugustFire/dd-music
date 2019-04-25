package com.nercl.music.cloud.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.nercl.music.cloud.dao.ChapterClassRoomDao;
import com.nercl.music.cloud.entity.classroom.Chapter;
import com.nercl.music.cloud.entity.classroom.ChapterClassRoom;

@Service
@Transactional
public class ChapterClassRoomServiceImpl implements ChapterClassRoomService {

	@Autowired
	private ChapterClassRoomDao chapterClassRoomDao;

	@Override
	public boolean save(String chapterId, String classRoomId) {
		if (isExsit(chapterId, classRoomId)) {
			return false;
		} else {
			ChapterClassRoom ccr = new ChapterClassRoom();
			ccr.setChapterId(chapterId);
			ccr.setClassRoomId(classRoomId);
			ccr.setDeleted(false);
			chapterClassRoomDao.save(ccr);
			return !Strings.isNullOrEmpty(ccr.getId());
		}
	}

	@Override
	public boolean isExsit(String chapterId, String classRoomId) {
		return null != chapterClassRoomDao.get(chapterId, classRoomId);
	}

	@Override
	public boolean delete(String chapterId, String classRoomId) {
		ChapterClassRoom chapterClassRoom = chapterClassRoomDao.get(chapterId, classRoomId);
		if (null == chapterClassRoom) {
			return false;
		}
		chapterClassRoom.setDeleted(true);
		chapterClassRoomDao.update(chapterClassRoom);
		return true;
	}

	@Override
	public List<ChapterClassRoom> get(String classRoomId) {
		List<ChapterClassRoom> ccrs = chapterClassRoomDao.get(classRoomId);
		return ccrs;
	}

	@Override
	public ChapterClassRoom get(String classRoomId, String chapterId) {
		return chapterClassRoomDao.get(classRoomId, chapterId);
	}

	@Override
	@Cacheable(value = "chapters", key = "'chapters_rid_'.concat(#rid)")
	public List<Chapter> getParentChapters(String rid) {
		List<ChapterClassRoom> chapterClassRooms = chapterClassRoomDao.getParentChapters(rid);
		if (null == chapterClassRooms) {
			return null;
		}
		List<Chapter> chapters = chapterClassRooms.stream().map(chapterClassRoom -> {
			return chapterClassRoom.getChapter();
		}).collect(Collectors.toList());
		return chapters;
	}

	@Override
	public Chapter getChapter(String rid, String cid) {
		ChapterClassRoom chapterClassRoom = get(rid, cid);
		if (null == chapterClassRoom) {
			return null;
		}
		return chapterClassRoom.getChapter();
	}

	@Override
	public List<Chapter> getChildrenChapters(String classRoomId, String chapterId) {
		List<ChapterClassRoom> chapterClassRooms = chapterClassRoomDao.getChildrenChapters(classRoomId, chapterId);
		if (null == chapterClassRooms) {
			return null;
		}
		List<Chapter> chapters = chapterClassRooms.stream().map(chapterClassRoom -> {
			return chapterClassRoom.getChapter();
		}).collect(Collectors.toList());
		return chapters;
	}

	@Override
	public int deleteClassroomChapters(String id) {
		List<ChapterClassRoom> list = chapterClassRoomDao.get(id);
		if(null == list || list.isEmpty()){
			return 0;
		}else{
			list.forEach(l->{
				chapterClassRoomDao.deleteById(l.getId());
			});
			return list.size();
		}
	}

}
