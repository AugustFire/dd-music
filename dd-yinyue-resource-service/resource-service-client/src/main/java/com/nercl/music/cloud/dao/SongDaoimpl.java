package com.nercl.music.cloud.dao;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.song.Song;

@Repository
public class SongDaoimpl extends AbstractBaseDaoImpl<Song, String> implements SongDao {

	@Override
	public List<Song> getByName(String name) {
		String jpql = "from Song song where song.name = ?1";
		return this.executeQueryWithoutPaging(jpql, name);
	}

	@Override
	public List<Song> getByLikeName(String name) {
		String jpql = "from Song song where song.name like ?1";
		return this.executeQueryWithoutPaging(jpql, "%" + name + "%");
	}

	@Override
	public List<Song> get(String[] sids) {
		if (sids.length == 1) {
			String jpql = "from Song song where song.id = ?1";
			List<Song> songs = this.executeQueryWithoutPaging(jpql, sids[0]);
			return songs;
		} else {
			String jpql = "from Song song where song.id = ?1";
			for (int i = 1; i < sids.length; i++) {
				jpql = jpql + " or song.id = ?" + (i + 1);
			}
			List<Song> songs = this.executeQueryWithoutPaging(jpql, Arrays.stream(sids).toArray());
			return songs;
		}
	}

}
