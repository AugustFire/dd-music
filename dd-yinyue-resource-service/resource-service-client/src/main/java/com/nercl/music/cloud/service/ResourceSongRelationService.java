package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.song.ResourceSongRelation;

public interface ResourceSongRelationService {

	void deleteBySong(String sid);

	void save(ResourceSongRelation relation);

	void delete(ResourceSongRelation relation);

	ResourceSongRelation getMainResourceSongBySong(String sid);

	List<ResourceSongRelation> getResourceSongs(String sid);

}
