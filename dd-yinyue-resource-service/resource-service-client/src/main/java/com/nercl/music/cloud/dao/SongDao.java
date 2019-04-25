package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.song.Song;

public interface SongDao extends BaseDao<Song, String> {

	List<Song> getByName(String name);

	List<Song> getByLikeName(String name);

	List<Song> get(String[] sids);

}
