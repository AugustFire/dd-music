package com.nercl.music.cloud.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.nercl.music.cloud.entity.resource.Resource;

public interface ResourceService {

	/**
	 * 保存资源
	 */
	void save(Resource resource);

	/**
	 * 保存资源
	 */
	String save(String json, InputStream in, String name, String ext) throws IOException;

	/**
	 * 根据资源的uuid查询
	 * 
	 * @param id
	 *            资源Id
	 */
	Resource getByID(String id);

	/**
	 * 根据资源的云Id查询
	 * 
	 * @param cloudId
	 *            资源对应的云Id
	 */
	Resource getByCloudId(String cloudId);

	/**
	 * 根据Id删除资源
	 * 
	 * @param rid
	 *            资源id
	 */
	void deleteById(String rid);

	/**
	 * 根据条件查询对应资源
	 * 
	 * @param conditions
	 */
	List<Resource> getByConditions(Resource conditions) throws Exception;

	/**
	 * 根据条件查询对应资源
	 * 
	 * @param condition
	 *            条件可以是“资源名称”、“资源后缀”
	 */
	List<Resource> fuzzyQueryResources(String condition);

	/**
	 * 根据条件查询对应资源目录下的资源
	 * 
	 * @param uid
	 *            用户id
	 * @param condition
	 *            条件可以是“资源名称”、“资源后缀”
	 */
	List<Resource> fuzzyQueryFolderResources(String uid, String condition);
}
