package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.song.ResourceSongRelation;

public interface ResourceSongRelationDao extends BaseDao<ResourceSongRelation, String> {

	List<ResourceSongRelation> getResourceSongs(String sid);

	ResourceSongRelation getMainResourceSongBySong(String songId);

}
