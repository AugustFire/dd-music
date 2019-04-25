package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.song.ResourceSongRelation;

@Repository
public class ResourceSongRelationDaoImpl extends AbstractBaseDaoImpl<ResourceSongRelation, String>
		implements ResourceSongRelationDao {

	@Override
	public List<ResourceSongRelation> getResourceSongs(String sid) {
		String jpql = "from ResourceSongRelation rsr where rsr.songId = ?1  and rsr.isMain = ?2";
		return this.executeQueryWithoutPaging(jpql, sid, false);
	}

	@Override
	public ResourceSongRelation getMainResourceSongBySong(String songId) {
		String jpql = "from ResourceSongRelation rsr where rsr.songId = ?1 and rsr.isMain = ?2";
		List<ResourceSongRelation> relations = this.executeQueryWithoutPaging(jpql, songId, true);
		return null == relations || relations.isEmpty() ? null : relations.get(0);
	}

}
