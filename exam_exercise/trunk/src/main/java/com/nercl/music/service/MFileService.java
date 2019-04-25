package com.nercl.music.service;

import com.nercl.music.entity.question.MFile;

public interface MFileService {

	boolean save(String originName, String name, String uuid);

	boolean save(String originName, String name, String uuid, String ext, String path, Integer fileResType);

	MFile get(String uuid);

	String getPath(String url);

	MFile getByUrl(String url);

	void updatePath();

}
