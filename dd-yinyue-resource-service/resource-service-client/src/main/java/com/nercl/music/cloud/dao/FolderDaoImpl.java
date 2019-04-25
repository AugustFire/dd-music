package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.folder.Folder;

@Repository
public class FolderDaoImpl extends AbstractBaseDaoImpl<Folder, String> implements FolderDao {

	@Override
	public List<Folder> getRootFolders(String userId) {
		String jpql = "from Folder fd where fd.parentId is null and fd.userId = ?1";
		List<Folder> rootFolders = this.executeQueryWithoutPaging(jpql, userId);
		if (!rootFolders.isEmpty()) {
			return rootFolders;
		}
		return null;
	}

	@Override
	public List<Folder> getChildFolders(String userId, Boolean isdelete, String parentId) {
		String jpql = "from Folder fd where fd.userId = ?1 and fd.isDeleted is ?2 and fd.parentId = ?3";
		return this.executeQueryWithoutPaging(jpql, userId, isdelete, parentId);
	}

	@Override
	public List<Folder> getDeletedFodlers(String uid) {
		String jpql = "select fd from Folder fd,Folder fd1 where fd.parentId=fd1.id "
				+ "and fd.isDeleted = true and fd1.isDeleted != true" + " and fd.userId = ?1";
		return this.executeQueryWithoutPaging(jpql, uid);
	}

	@Override
	public List<Folder> getAllDeletedFodlers(String uid) {
		String jpql = "from Folder fd  where fd.isDeleted is true and fd.userId = ?1";
		return this.executeQueryWithoutPaging(jpql, uid);
	}

	@Override
	public int deleteByIds(List<String> folderIds) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(" ( ");
		folderIds.forEach(folderId -> {
			sbf.append("'");
			sbf.append(folderId);
			sbf.append("'");
			sbf.append(",");
		});
		sbf.deleteCharAt(sbf.length() - 1); // 去掉最后的","
		sbf.append(" ) ");
		String jpql = "delete from Folder fd  where fd.id in" + sbf.toString();
		return entityManager.createQuery(jpql).executeUpdate();
	}

	@Override
	public List<Folder> getUserRoot(String uid) {
		String jpql = "from Folder fd  where fd.parentId is null and fd.userId = ?1";
		return this.executeQueryWithoutPaging(jpql, uid);
	}
}