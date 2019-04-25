package com.nercl.music.cloud.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.nercl.music.cloud.entity.resource.Resource;
import com.nercl.music.cloud.entity.song.Song;

public interface SongService {

	Song get(String sid);

	List<Map<String, String>> get(String[] sids);

	List<Map<String, Object>> getByLikeName(String name);

	Song save(String name);

	boolean update(String sid, String name);

	boolean delete(String sid);

	boolean addResourceSongRelation(String sid, String rid, boolean isMain);

	boolean addResourceSongRelation(String name, String sid, InputStream is, boolean isMain);

	Resource getMainResource(String sid);

	List<Map<String, String>> getResourceSongs(String sid);

	void updateAttachments(String sid, List<InputStream> iss, List<String> names, List<String> exts) throws IOException;

	void deleteAttachments(String sid);

}
