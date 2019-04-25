package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.folder.Folder;

public interface FolderDao extends BaseDao<Folder, String> {

	/**
	 * 根据userId查询根目录
	 */
	List<Folder> getRootFolders(String userId);

	/**
	 * 根据用户Id和parentId查询某个文件夹下的所有子文件夹
	 * 
	 * @param parentId
	 *            父文件夹Id，传空则查根目录
	 * @param userId
	 *            用户Id
	 * @param isdelete
	 *            是否删除
	 */
	List<Folder> getChildFolders(String userId, Boolean isdelete, String parentId);

	/**
	 * 查询某个用户的删除状态文件夹(根目录除外)
	 * 
	 * @param userId
	 *            用户Id
	 */
	List<Folder> getDeletedFodlers(String uid);

	/**
	 * 根据userId获取所有状态为删除的文件夹
	 */
	List<Folder> getAllDeletedFodlers(String uid);

	/**
	 * 根据Id批量删除文件夹，仅仅当要删除的文件夹之间没有外键关联时可用
	 */
	int deleteByIds(List<String> folderIds);

	/**
	 * 根据用户id查询根目录
	 */
	List<Folder> getUserRoot(String uid);

}