package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.resource.Resource;

public interface ResourceDao extends BaseDao<Resource, String> {
	/**
	 * 根据资源的云Id查询
	 * 
	 * @param cloudId
	 *            资源对应的云Id
	 */
	List<Resource> getByCloudId(String cloudId);

	/**
	 * 根据条件模糊查询资源
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