package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.question.MFile;

public interface MFileDao extends BaseDao<MFile, String> {

	List<MFile> getAll();

}
