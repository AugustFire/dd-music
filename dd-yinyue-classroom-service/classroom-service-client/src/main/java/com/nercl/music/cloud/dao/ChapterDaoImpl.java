package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.nercl.music.cloud.entity.classroom.Chapter;

@Repository
public class ChapterDaoImpl extends AbstractBaseDaoImpl<Chapter, String> implements ChapterDao {

	@Override
	public List<Chapter> getParentChaptersByClassRoom(String rid) {
		String jpql = "from Chapter c where c.classRoomId = ?1 and c.parentId is null order by c.orderBy";
		List<Chapter> chapters = this.executeQueryWithoutPaging(jpql, rid);
		return chapters;
	}

	@Override
	public List<Chapter> getChildrenChapters(String pid) {
		String jpql = "from Chapter c where c.parentId = ?1 order by c.orderBy";
		List<Chapter> chapters = this.executeQueryWithoutPaging(jpql, pid);
		return chapters;
	}

	@Override
	public List<Chapter> getByBook(String bid) {
		String jpql = "from Chapter c where c.bookId = ?1";
		List<Chapter> chapters = this.executeQueryWithoutPaging(jpql, bid);
		return chapters;
	}

	@Override
	public List<Chapter> getChapters(String bid, String parentId) {
		if (Strings.isNullOrEmpty(parentId)) {
			String jpql = "from Chapter c where c.bookId = ?1 and c.parentId is null";
			List<Chapter> chapters = this.executeQueryWithoutPaging(jpql, bid);
			return chapters;
		} else {
			String jpql = "from Chapter c where c.bookId = ?1 and c.parentId = ?2";
			List<Chapter> chapters = this.executeQueryWithoutPaging(jpql, bid, parentId);
			return chapters;
		}
	}

}
