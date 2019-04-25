package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.MFileDao;
import com.nercl.music.entity.MFile;

@Repository
public class MFileDaoImpl extends AbstractBaseDaoImpl<MFile, String> implements MFileDao {

	@Override
	public List<MFile> getAll() {
		String jpql = "from MFile ex";
		List<MFile> mfiles = executeQueryWithoutPaging(jpql);
		return mfiles;
	}

}
