package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.ResourceSongRelationDao;
import com.nercl.music.cloud.entity.song.ResourceSongRelation;

@Service
@Transactional
public class ResourceSongRelationServiceImpl implements ResourceSongRelationService {

	@Autowired
	private ResourceSongRelationDao resourceSongRelationDao;

	@Override
	public void deleteBySong(String sid) {
		List<ResourceSongRelation> relations = resourceSongRelationDao.getResourceSongs(sid);
		if (null == relations || relations.isEmpty()) {
			return;
		}
		relations.forEach(relation -> {
			resourceSongRelationDao.delete(relation);
		});
	}

	@Override
	public void save(ResourceSongRelation relation) {
		resourceSongRelationDao.save(relation);
	}

	@Override
	public void delete(ResourceSongRelation relation) {
		resourceSongRelationDao.delete(relation);
	}

	@Override
	public ResourceSongRelation getMainResourceSongBySong(String sid) {
		return resourceSongRelationDao.getMainResourceSongBySong(sid);
	}

	@Override
	public List<ResourceSongRelation> getResourceSongs(String sid) {
		return resourceSongRelationDao.getResourceSongs(sid);
	}

}
