package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.folder.Folder;

public interface FolderService {

	/**
	 * 保存文件夹
	 */
	void save(Folder folder);

	/**
	 * 删除文件夹
	 */
	void deleteById(String folderId);

	/**
	 * 更新文件夹
	 */
	void update(Folder folder);

	/**
	 * 根据文件夹的uuid查询
	 * 
	 * @param id
	 *            资源Id
	 */
	Folder getByID(String id);

	/**
	 * 根据userId查询根目录
	 */
	List<Folder> getRootFolders(String userId);

	/**
	 * 查询某个文件夹下的所有子文件夹
	 * 
	 * @param parentId
	 *            父文件夹Id，传空则查根目录
	 * @param userId
	 *            用户Id
	 */
	List<Folder> getChildFolders(String userId, Boolean isdelete, String parentId);

	/**
	 * 根据用户userId和folderId,完全删除该文件夹及其所有子文件夹和文件
	 * 
	 * @param folderId
	 *            要完全删除的文件夹的id
	 * @param userId
	 *            用户id
	 */
	void deleteFolderOverall(String userId, String folderId);

	/**
	 * 根据用户userId和folderId,逻辑删除该文件夹及其所有子文件夹和文件(即将文件夹及其所有子文件夹和文件的删除状态设置这true)
	 * 
	 * @param folderId
	 *            要逻辑删除的文件夹的id
	 * @param userId
	 *            用户id
	 */
	void deleteFolderLogical(String userId, String folderId);

	/**
	 * 查询某个用户的删除状态文件夹(根目录除外)
	 * 
	 * @param userId
	 *            用户Id
	 */
	List<Folder> getDeletedFodlers(String uid);

	/**
	 * 还原逻辑删除的文件夹
	 * 
	 * @param uid
	 *            用户id
	 * @param folderId
	 *            文件夹id
	 */
	void restoreFolder(String uid, String folderId);

	/**
	 * 根据userId清空回收站
	 */
	void clearupRecyclebin(String uid);

	/**
	 * 根据当前文件夹id和用户id，查询所有子文件夹名称
	 * 
	 * @param userId
	 * @param folderId
	 *            当folderId为空时，查询的是根文件夹下的所有子文件夹
	 */
	List<String> getChildFolderNames(String userId, String folderId);

	/**
	 * 根据Id批量删除文件夹，仅仅当要删除的文件夹之间没有外键关联时可用
	 */
	int deleteByIds(List<String> folderIds);

	/**
	 * 根据Folder对象的字段值查询对应的Folder列表
	 * 
	 * @param folder
	 * @exception reflect
	 *                exception
	 */
	List<Folder> getByConditions(Folder folder) throws Exception;

	/**
	 * 根据用户id查询用户根目录
	 * 
	 * @param uid
	 *            用户id
	 */
	List<Folder> getUserRoot(String uid);
}
