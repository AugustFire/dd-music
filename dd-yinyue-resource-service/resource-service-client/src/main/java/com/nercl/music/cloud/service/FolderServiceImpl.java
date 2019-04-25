package com.nercl.music.cloud.service;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.nercl.music.cloud.dao.FolderDao;
import com.nercl.music.cloud.entity.folder.Folder;
import com.nercl.music.cloud.entity.folder.FolderResourceRelation;

@Service
@Transactional
public class FolderServiceImpl implements FolderService {

	@Autowired
	private FolderDao folderDao;

	@Autowired
	private FolderResourceRelationService folderResourceRelationService;

	@Autowired
	private ResourceService resourceService;

	@Override
	public void save(Folder folder) {
		folderDao.save(folder);
	}

	@Override
	public void deleteById(String folderId) {
		folderDao.deleteById(folderId);
	}

	@Override
	public void update(Folder folder) {
		folderDao.update(folder);
	}

	@Override
	public Folder getByID(String id) {
		return folderDao.findByID(id);
	}

	@Override
	public List<Folder> getRootFolders(String userId) {
		return folderDao.getRootFolders(userId);
	}

	@Override
	public List<Folder> getChildFolders(String userId, Boolean isdelete, String parentId) {
		return folderDao.getChildFolders(userId, isdelete, parentId);
	}

	@Override
	public void deleteFolderOverall(String userId, String folderId) {

		Folder folder = folderDao.findByID(folderId);
		// 要删除的文件夹及其所有子文件夹
		List<Folder> deleteFolders = Lists.newArrayList();
		deleteFolders.add(folder); // 当前文件夹
		List<Folder> childFolders = folderDao.getChildFolders(userId, false, folderId);
		deleteFolders.addAll(childFolders);
		while (!childFolders.isEmpty()) {
			childFolders = getChildFoldersByParents(childFolders, userId);
			deleteFolders.addAll(childFolders);
		}

		// 要删除的文件夹与资源关系
		List<FolderResourceRelation> deleteFolderResourceRelations = Lists.newArrayList();
		deleteFolders.forEach(deleteFolder -> {
			deleteFolderResourceRelations
					.addAll(folderResourceRelationService.getRelations(deleteFolder.getId(), true));
		});

		// 删除资源与文件夹的关系
		deleteFolderResourceRelations.forEach(relat -> {
			folderResourceRelationService.delete(relat.getId());
		});

		// 删除相关资源
		deleteFolderResourceRelations.forEach(relation -> {
			resourceService.deleteById(relation.getResourceId());
		});

		// 删除文件夹
		deleteFolders.forEach(dFolder -> {
			folderDao.deleteById(dFolder.getId());
		});
	}

	@Override
	public void deleteFolderLogical(String userId, String folderId) {
		Folder folder = folderDao.findByID(folderId);
		Long now = System.currentTimeMillis();
		// 文件夹及其所有子文件夹
		List<Folder> deleteFolders = Lists.newArrayList();
		deleteFolders.add(folder); // 当前文件夹
		List<Folder> childFolders = folderDao.getChildFolders(userId, false, folderId);
		deleteFolders.addAll(childFolders);
		while (!childFolders.isEmpty()) {
			childFolders = getChildFoldersByParents(childFolders, userId);
			deleteFolders.addAll(childFolders);
		}

		// 文件夹与资源关系
		List<FolderResourceRelation> deleteFolderResourceRelations = Lists.newArrayList();
		deleteFolders.forEach(deleteFolder -> {
			deleteFolderResourceRelations.addAll(folderResourceRelationService.getRelations(deleteFolder.getId(),false));
		});

		// 逻辑删除资源与文件夹的关系
		deleteFolderResourceRelations.forEach(relation -> {
			if (!relation.getIsDeleted()) {
				relation.setUpdateTime(now);
			}
			relation.setIsDeleted(true);
			folderResourceRelationService.update(relation);
		});

		// 逻辑删除文件夹
		deleteFolders.forEach(dFolder -> {
			if (true != dFolder.getIsDeleted()) {
				dFolder.setUpdateTime(now);
			}
			dFolder.setIsDeleted(true);
			folderDao.update(dFolder);
		});
	}

	/**
	 * 获取所有子文件夹
	 */
	private List<Folder> getChildFoldersByParents(List<Folder> folders, String uid) {
		List<Folder> resultFolders = Lists.newArrayList();
		if (!folders.isEmpty()) {
			folders.forEach(folder -> {
				resultFolders.addAll(folderDao.getChildFolders(uid, false, folder.getId()));
			});
		}
		return resultFolders;
	}

	@Override
	public List<Folder> getDeletedFodlers(String uid) {
		return folderDao.getDeletedFodlers(uid);
	}

	@Override
	public void restoreFolder(String uid, String folderId) {
		Long now = System.currentTimeMillis();
		Folder me = folderDao.findByID(folderId);
		Long updateTime = me.getUpdateTime(); // 此文件夹被删除的时间
		// 还原本节点的以上的所有被删除的父节点，只还原文件夹，忽略所有父级节点中的文件
		List<Folder> deletedFatherFolders = getDeletedFatherFolders(folderId);
//		deletedFatherFolders.add(me); // 节点本身加入要还原的list中

		// 查询所有被删除的子节点(文件夹)
		List<Folder> deleteChildFolders = Lists.newArrayList();
		List<FolderResourceRelation> deleteChildResource = Lists.newArrayList();
		List<FolderResourceRelation> myRelations = folderResourceRelationService.getRelations(folderId,true); // 当前文件夹下的所有文件
		myRelations.forEach(relation->{
			if(updateTime.longValue() == relation.getUpdateTime().longValue()){
				deleteChildResource.add(relation); // 删除时间与选定文件夹一致的资源与文件夹关系
			}
		});
		List<Folder> childFolders = folderDao.getChildFolders(uid, true, folderId);
		deleteChildFolders.add(me);
		childFolders.forEach(folder -> {
			if (updateTime.longValue() == folder.getUpdateTime().longValue()) { // 删除时间与选定文件夹的删除时间一致的文件夹才还原
				deleteChildFolders.add(folder); // 删除时间与选定文件夹的删除时间一致的文件夹
				
				List<FolderResourceRelation> relations = folderResourceRelationService.getRelations(folder.getId(),true);
				relations.forEach(relation->{
					if(updateTime.longValue() == relation.getUpdateTime().longValue()){
						deleteChildResource.add(relation); // 删除时间与选定文件夹一致的资源与文件夹关系
					}
				});
			}
		});
		
		while (!childFolders.isEmpty()) {
			childFolders = getChildFoldersByParents(childFolders, uid);
			childFolders.forEach(folder -> {
				if (updateTime.longValue() == folder.getUpdateTime().longValue()) { // 删除时间与选定文件夹的删除时间一致的文件夹才还原
					deleteChildFolders.add(folder); // 删除时间与选定文件夹的删除时间一致的文件夹
					
					List<FolderResourceRelation> relations = folderResourceRelationService.getRelations(folder.getId(),true);
					relations.forEach(relation->{
						if(updateTime.longValue() == relation.getUpdateTime().longValue()){
							deleteChildResource.add(relation); // 删除时间与选定文件夹一致的资源与文件夹关系
						}
					});
				}
			});
		}

		// 还原文件夹（包括父级文件夹和子文件夹）
		deleteChildFolders.addAll(deletedFatherFolders);
		deleteChildFolders.forEach(dff -> {
			dff.setIsDeleted(false);
			dff.setIsRestored(true);
			dff.setUpdateTime(now);
			folderDao.update(dff);
		});

		// 还原文件下的所有文件
		deleteChildResource.forEach(dcr -> {
			dcr.setIsDeleted(false);
			dcr.setIsRestored(true);
			dcr.setUpdateTime(now);
			folderResourceRelationService.update(dcr);
		});

	}

	/**
	 * 根据文件夹id向上查找所有被删除的父节点
	 */
	private List<Folder> getDeletedFatherFolders(String folderId) {
		List<Folder> deletedFolders = Lists.newArrayList();
		String parentId = folderDao.findByID(folderId).getParentId();
		while (!Strings.isNullOrEmpty(parentId)) {
			Folder father = folderDao.findByID(parentId);
			if (father.getIsDeleted()) {
				deletedFolders.add(father);
				parentId = father.getParentId();
			} else {
				parentId = null;
			}
		}
		return deletedFolders;
	}

	@Override
	public void clearupRecyclebin(String uid) {
		// List<Folder> deletedFolders = folderDao.getAllDeletedFodlers(uid);
		// 查询删除状态为true的 资源关系
		List<FolderResourceRelation> deletedFolderResourceRelations = folderResourceRelationService.getDeletedResourceList(uid);
		deletedFolderResourceRelations.forEach(relation -> {
			// 删除回收站中的资源与文件夹对应关系
			folderResourceRelationService.delete(relation.getId());
			// 删除回收站中的资源
			resourceService.deleteById(relation.getResourceId());
		});
		// 收藏夹中展示的已删除的文件夹
		List<Folder> deletedFolders = Lists.newArrayList();
		// 先查询根目录是否是删除状态
		List<Folder> root = folderDao.getRootFolders(uid);
		root.forEach(r->{
			if(r.getIsDeleted()){
				deletedFolders.add(r);
			}else{
				deletedFolders.addAll(folderDao.getDeletedFodlers(uid));
			}
		});

		// 删除与文件夹一起删除的文件和文件夹
		deleteInOrder(deletedFolders, uid);
	}

	@Override
	public List<String> getChildFolderNames(String userId, String folderId) {
		List<Folder> childFolders = folderDao.getChildFolders(userId, false, folderId);
		List<String> childFolderNames = Lists.newArrayList();
		if (!childFolders.isEmpty()) {
			childFolders.forEach(childFolder -> {
				childFolderNames.add(childFolder.getFolderName());
			});
		}
		return childFolderNames;
	}

	/**
	 * 批量删除文件夹
	 * @param folders 回收站内的所有顶层文件夹
	 */
	private void deleteInOrder(List<Folder> folders, String userId) {
		Iterator<Folder> iterator = folders.iterator();
		while(iterator.hasNext()){
			Folder folder = iterator.next();
			List<Folder> childFolders = folderDao.getChildFolders(userId, true, folder.getId());
			if (childFolders.isEmpty()) {
				folderDao.delete(folder);
				iterator.remove();
			}else{
				deleteInOrder(childFolders, userId);
			}
		}
		if (!folders.isEmpty()) {
			deleteInOrder(folders, userId);
		}
	}

	@Override
	public int deleteByIds(List<String> folderIds) {
		return folderDao.deleteByIds(folderIds);
	}
	
	@Override
	public List<Folder> getByConditions(Folder folder) throws Exception{
		return folderDao.findByConditions(folder);
	}

	@Override
	public List<Folder> getUserRoot(String uid) {
		return folderDao.getUserRoot(uid);
	}
}
