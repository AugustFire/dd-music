package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.template.ClassroomTemplateChapterStatus;

public interface ClassroomTemplateChapterStatusDao extends BaseDao<ClassroomTemplateChapterStatus, String> {

	List<ClassroomTemplateChapterStatus> get(String rid, String tid);

	ClassroomTemplateChapterStatus get(String rid, String tid, String cid);

	List<ClassroomTemplateChapterStatus> getByParentChapter(String rid, String tid, String cid);

}
