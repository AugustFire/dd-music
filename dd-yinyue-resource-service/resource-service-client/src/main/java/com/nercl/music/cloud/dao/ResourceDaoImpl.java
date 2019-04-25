package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.resource.Resource;

@Repository
public class ResourceDaoImpl extends AbstractBaseDaoImpl<Resource, String> implements ResourceDao {

	@Override
	public List<Resource> getByCloudId(String cloudId) {
		String jpql = "from Resource rs where rs.cloudId = ?1";
		return this.executeQueryWithoutPaging(jpql, cloudId);
	}

	@Override
	public List<Resource> fuzzyQueryResources(String condition) {
		String jpql = "from Resource rs";
		jpql += " where rs.name like ?" + 1 + " or rs.ext like ?" + 2;
		return this.executeQueryWithoutPaging(jpql, "%" + condition + "%", "%" + condition + "%");
	}

	@Override
	public List<Resource> fuzzyQueryFolderResources(String uid, String condition) {
		String jpql = "select frr.resource from FolderResourceRelation frr "
				+ "left join frr.folder on frr.folderId = frr.folder.id "
				+ "left join frr.resource on frr.resourceId = frr.resource.id "
				+ "where frr.isDeleted is false and frr.folder.userId=?1 and (frr.resource.name like ?2 or frr.resource.ext like ?3)";
		return this.executeQueryWithoutPaging(jpql, uid, "%" + condition + "%", "%" + condition + "%");
	}

}