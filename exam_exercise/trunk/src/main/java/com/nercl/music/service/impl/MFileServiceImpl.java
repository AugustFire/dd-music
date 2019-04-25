package com.nercl.music.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.nercl.music.dao.MFileDao;
import com.nercl.music.entity.question.MFile;
import com.nercl.music.service.MFileService;

@Service
@Transactional
public class MFileServiceImpl implements MFileService {

	@Autowired
	private MFileDao mfileDao;

	@Override
	public boolean save(String originName, String name, String uuid) {
		MFile mfile = new MFile();
		mfile.setId(uuid);
		mfile.setOriginName(originName);
		mfile.setName(name);
		this.mfileDao.save(mfile);
		return true;
	}

	@Override
	public MFile get(String uuid) {
		return this.mfileDao.findByID(uuid);
	}

	@Override
	public boolean save(String originName, String name, String uuid, String ext, String path, Integer fileResType) {
		MFile mfile = new MFile();
		mfile.setId(uuid);
		mfile.setOriginName(originName);
		mfile.setName(name);
		mfile.setExt(ext);
		mfile.setPath(path);
		mfile.setFileResType(fileResType);
		this.mfileDao.save(mfile);
		return true;
	}

	@Override
	public String getPath(String url) {
		if (StringUtils.isNotBlank(url)) {
			String[] strs = url.split("/");
			String uuid = strs[strs.length - 1];
			MFile mfile = this.get(uuid);
			return null != mfile ? mfile.getPath() : "";
		}
		return "";
	}

	@Override
	public MFile getByUrl(String url) {
		MFile mfile = null;
		if (StringUtils.isNotBlank(url)) {
			String[] strs = url.split("/");
			String uuid = strs[strs.length - 1];
			mfile = this.get(uuid);
		}
		return mfile;
	}

	@Override
	public void updatePath() {
		List<MFile> mfiles = this.mfileDao.getAll();
		if (null == mfiles || mfiles.isEmpty()) {
			return;
		}
		mfiles.forEach(mfile -> {
			String path = mfile.getPath();
			if (!Strings.isNullOrEmpty(path)) {
				path = "d" + path.substring(1, path.length());
				mfile.setPath(path);
				this.mfileDao.update(mfile);
			}
		});
	}

}
