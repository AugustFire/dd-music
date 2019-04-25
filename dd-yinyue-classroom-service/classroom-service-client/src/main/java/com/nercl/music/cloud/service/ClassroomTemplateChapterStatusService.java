package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.template.ClassroomTemplateChapterStatus;

public interface ClassroomTemplateChapterStatusService {

	boolean setStatus(String rid, String tid, String cid, ClassroomTemplateChapterStatus.ChapterResourceStatus status);

	List<ClassroomTemplateChapterStatus> get(String rid, String tid);

	ClassroomTemplateChapterStatus get(String rid, String tid, String cid);

	List<ClassroomTemplateChapterStatus> getByParentChapter(String rid, String tid, String cid);

}
