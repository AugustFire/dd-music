package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.template.TemplateParentChapterBookClassRoom;

@Repository
public class TemplateParentChapterBookClassRoomDaoImpl
		extends AbstractBaseDaoImpl<TemplateParentChapterBookClassRoom, String>
		implements TemplateParentChapterBookClassRoomDao {

	@Override
	public TemplateParentChapterBookClassRoom getBookTemplateWithBook(String bookId, String chapterId) {
		String jpql = "from TemplateParentChapterBookClassRoom tpcc where tpcc.bookId = ?1 and tpcc.chapterId = ?2";
		List<TemplateParentChapterBookClassRoom> tpccs = this.executeQueryWithoutPaging(jpql, bookId, chapterId);
		return null == tpccs || tpccs.isEmpty() ? null : tpccs.get(0);
	}

	@Override
	public TemplateParentChapterBookClassRoom getBookTemplateWithClassroom(String chapterId, String roomId) {
		String jpql = "from TemplateParentChapterBookClassRoom tpcc where tpcc.chapterId = ?1 and tpcc.classRoomId = ?2";
		List<TemplateParentChapterBookClassRoom> tpccs = this.executeQueryWithoutPaging(jpql, chapterId, roomId);
		return null == tpccs || tpccs.isEmpty() ? null : tpccs.get(0);
	}

	@Override
	public List<TemplateParentChapterBookClassRoom> getBookTemplate(String bookId) {
		String jpql = "from TemplateParentChapterBookClassRoom tpcc where tpcc.bookId = ?1";
		List<TemplateParentChapterBookClassRoom> tpccs = this.executeQueryWithoutPaging(jpql, bookId);
		return tpccs;
	}

	@Override
	public TemplateParentChapterBookClassRoom getBookTemplate(String bookId, String chapterId, String templateId) {
		String jpql = "from TemplateParentChapterBookClassRoom tpcc where tpcc.bookId = ?1 and tpcc.chapterId = ?2 and templateId = ?3";
		List<TemplateParentChapterBookClassRoom> tpccs = this.executeQueryWithoutPaging(jpql, bookId, chapterId,
				templateId);
		return null == tpccs || tpccs.isEmpty() ? null : tpccs.get(0);
	}

	@Override
	public List<TemplateParentChapterBookClassRoom> getClassRoomTemplate(String classRoomId, String chapterId) {
		String jpql = "from TemplateParentChapterBookClassRoom tpcc where tpcc.classRoomId = ?1 and tpcc.chapterId = ?2";
		return this.executeQueryWithoutPaging(jpql, classRoomId, chapterId);
	}

	@Override
	public TemplateParentChapterBookClassRoom getClassRoomTemplate(String classRoomId, String chapterId,
			String templateId) {
		String jpql = "from TemplateParentChapterBookClassRoom tpcc where tpcc.classRoomId = ?1 and tpcc.chapterId = ?2 and templateId = ?3";
		List<TemplateParentChapterBookClassRoom> tpccs = this.executeQueryWithoutPaging(jpql, classRoomId, chapterId,
				templateId);
		return null == tpccs || tpccs.isEmpty() ? null : tpccs.get(0);
	}

	@Override
	public void deleteClassRoomTemplate(String classRoomId, String chapterId) {
		String jpql = "delete from TemplateParentChapterBookClassRoom tpcc where tpcc.classRoomId = " + classRoomId
				+ " and tpcc.chapterId = " + chapterId;
		this.entityManager.createQuery(jpql).executeUpdate();
	}

	@Override
	public List<TemplateParentChapterBookClassRoom> getByClassroom(String roomId) {
		String jpql = "from TemplateParentChapterBookClassRoom tpcc where tpcc.classRoomId = ?1";
		List<TemplateParentChapterBookClassRoom> tpccs = this.executeQueryWithoutPaging(jpql, roomId);
		return tpccs;
	}
}
