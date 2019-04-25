package com.nercl.music.cloud.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.FolderResourceRelationDao;
import com.nercl.music.cloud.dao.ResourceDao;
import com.nercl.music.cloud.entity.folder.FolderResourceRelation;
import com.nercl.music.cloud.entity.resource.Resource;
import com.nercl.music.util.CommUtils;

@Service
@Transactional
public class FolderResourceRelationServiceImpl implements FolderResourceRelationService {

	@Autowired
	private FolderResourceRelationDao folderResourceRelationDao;
	
	@Autowired
	private ResourceDao resourceDao;

	@Override
	public void save(FolderResourceRelation relation) {
		folderResourceRelationDao.save(relation);
	}

	@Override
	public void delete(String relationId) {
		folderResourceRelationDao.deleteById(relationId);
	}

	@Override
	public void update(FolderResourceRelation relation) {
		folderResourceRelationDao.update(relation);
	}

	@Override
	public FolderResourceRelation getByID(String id) {
		return folderResourceRelationDao.findByID(id);
	}

	@Override
	public List<FolderResourceRelation> getRelations(String folderId, Boolean isDeleted) {
		return folderResourceRelationDao.getRelations(folderId, isDeleted);
	}

	@Override
	public FolderResourceRelation getRelationsByConditions(FolderResourceRelation relation) throws Exception {
		FolderResourceRelation ralation = new FolderResourceRelation();
		ralation.setFolderId(relation.getFolderId());
		ralation.setResourceId(relation.getResourceId());
		List<FolderResourceRelation> list = folderResourceRelationDao.findByConditions(ralation );
		// 根据folderId和resourceId可以定位唯一一条关系
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Resource> getFavoriteList(String uid) {
		return folderResourceRelationDao.getFavoriteList(uid);
	}

	@Override
	public List<FolderResourceRelation> getDeletedResourceList(String uid) {
		return folderResourceRelationDao.getDeletedResourceList(uid);
	}

	@Override
	public void restoreRerource(String uid, String resourceId) {
		List<FolderResourceRelation> relations = folderResourceRelationDao.restoreRerource(uid, resourceId);
		if (!relations.isEmpty()) {
			// 根据userId和resourceId 可能决定唯一一条FolderResourceRelation
			FolderResourceRelation folderResourceRelation = relations.get(0);
			folderResourceRelation.setIsDeleted(false);
			folderResourceRelation.setIsRestored(true);
			folderResourceRelation.setUpdateTime(System.currentTimeMillis());
			folderResourceRelationDao.update(folderResourceRelation);
		}
	}

	@Override
	public FolderResourceRelation getRelationByRecourseId(String resourceId) {
		List<FolderResourceRelation> relationByRecourseId = folderResourceRelationDao
				.getRelationByRecourseId(resourceId);
		if (!relationByRecourseId.isEmpty()) {
			return relationByRecourseId.get(0); // 文件id可以确定唯一一条文件与文件夹的关系
		}
		return null;
	}

	@Override
	public List<Resource> getResourcesInType(String uid, int type,int orderBy) {
		return folderResourceRelationDao.getResourcesInType(uid, type,orderBy);
	}

	@Override
	public int getResourceCountInType(String uid, int type) {
		return folderResourceRelationDao.getResourceCountInType(uid, type);
	}

	@Override
	public void restoreAndOverwriteRerource(String uid, String resourceId) {
		List<FolderResourceRelation> relations = folderResourceRelationDao.restoreRerource(uid, resourceId);
		if (!relations.isEmpty()) { // 根据资源id和用户id可以查询到唯一一条资源与文件夹的关系记录
			FolderResourceRelation folderResourceRelation = relations.get(0);
			Resource resource = folderResourceRelation.getResource(); // 删除掉的资源
			String folderId = folderResourceRelation.getFolder().getId(); // 资源原来所在文件夹id
			List<FolderResourceRelation> resouces = folderResourceRelationDao.getRelations(folderId, false); // 资源原来所在文件夹下的资源
			List<FolderResourceRelation> resouceNameRepeatRelation = resouces.parallelStream()
					.filter(rs -> rs.getResource().getName().equals(resource.getName())).collect(Collectors.toList()); // 与删除的文件同名的文件与文件夹关系
			if (resouceNameRepeatRelation != null && !resouceNameRepeatRelation.isEmpty()) { // 与删除的文件同名的文件最多只能有一条
				FolderResourceRelation frr = resouceNameRepeatRelation.get(0);
				frr.setIsDeleted(false);
				frr.setIsRestored(true);
				frr.setUpdateTime(Instant.now().toEpochMilli());
				frr.setResourceId(resource.getId());
				frr.setResource(resource);
				folderResourceRelationDao.update(frr);
				folderResourceRelationDao.delete(folderResourceRelation);
			}
		}
	}

	@Override
	public void restoreAndRenameResource(String uid, String resourceId) {
		List<FolderResourceRelation> relations = folderResourceRelationDao.restoreRerource(uid, resourceId);
		if (!relations.isEmpty()) { // 根据资源id和用户id可以查询到唯一一条资源与文件夹的关系记录
			FolderResourceRelation folderResourceRelation = relations.get(0);
			String fileName = folderResourceRelation.getResource().getName(); // 资源名称
			String folderId = folderResourceRelation.getFolder().getId(); // 资源原来所在文件夹id
			List<FolderResourceRelation> resouces = folderResourceRelationDao.getRelations(folderId, false);
			List<String> resourceNameList = resouces.stream().map(FolderResourceRelation::getResource)
					.map(Resource::getName).collect(Collectors.toList());
			Integer repeatTimes = CommUtils.nameRepeatTimes(fileName, resourceNameList);
			if (repeatTimes > 0 || resourceNameList.contains(fileName)) {
				fileName = CommUtils.renameDuplicationResourceName(fileName, repeatTimes);
			}
			Resource resource = folderResourceRelation.getResource();
			resource.setName(fileName);
			resourceDao.update(resource);
			
			folderResourceRelation.setResource(resource);
			folderResourceRelation.setIsDeleted(false);
			folderResourceRelation.setIsRestored(true);
			folderResourceRelation.setUpdateTime(Instant.now().toEpochMilli());
			folderResourceRelationDao.update(folderResourceRelation);
			
		}
	}
}
