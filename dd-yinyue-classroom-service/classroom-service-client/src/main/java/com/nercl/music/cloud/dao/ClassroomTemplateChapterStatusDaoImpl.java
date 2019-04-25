package com.nercl.music.cloud.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.nercl.music.cloud.entity.classroom.Chapter;
import com.nercl.music.cloud.entity.template.ClassroomTemplateChapterStatus;

@Repository
public class ClassroomTemplateChapterStatusDaoImpl extends AbstractBaseDaoImpl<ClassroomTemplateChapterStatus, String>
		implements ClassroomTemplateChapterStatusDao {

	@Override
	public List<ClassroomTemplateChapterStatus> get(String rid, String tid) {
		String jpql = "from ClassroomTemplateChapterStatus ctcs where ctcs.classroomId = ?1 and crur.templateId = ?2";
		List<ClassroomTemplateChapterStatus> ctcss = this.executeQueryWithoutPaging(jpql, rid, tid);
		return ctcss;
	}

	@Override
	public ClassroomTemplateChapterStatus get(String rid, String tid, String cid) {
		String jpql = "from ClassroomTemplateChapterStatus ctcs where ctcs.classroomId = ?1 and ctcs.templateId = ?2 and ctcs.chapterId = ?3";
		List<ClassroomTemplateChapterStatus> ctcss = this.executeQueryWithoutPaging(jpql, rid, tid, cid);
		return null == ctcss || ctcss.isEmpty() ? null : ctcss.get(0);
	}

	@Override
	public List<ClassroomTemplateChapterStatus> getByParentChapter(String rid, String tid, String cid) {
		String jpql = "from ClassroomTemplateChapterStatus ctcs where ctcs.classroomId = ?1 and ctcs.templateId = ?2";
		List<ClassroomTemplateChapterStatus> ctcss = this.executeQueryWithoutPaging(jpql, rid, tid);
		if (null == ctcss || ctcss.isEmpty()) {
			return null;
		}
		return ctcss.parallelStream().filter(ctcs -> {
			Chapter chapter = ctcs.getChapter();
			return null != chapter && Strings.nullToEmpty(chapter.getParentId()).equals(cid);
		}).collect(Collectors.toList());
	}

}
