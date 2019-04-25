package com.nercel.exam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercel.exam.dao.ResourceDao;
import com.nercel.exam.entity.Resource;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceDao resourceDao;

	@Override
	public String save(String name, long size, String ext) {
		Resource resource = new Resource();
		resource.setName(name);
		resource.setSize(size);
		resource.setExt(ext);
		resource.setCreateAt(System.currentTimeMillis());
		resourceDao.save(resource);
		return resource.getId();
	}

}
