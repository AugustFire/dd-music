package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.folder.FolderResourceRelation;
import com.nercl.music.cloud.entity.resource.Resource;

public interface FolderResourceRelationService {
	
	/**
	 * 保存文件夹与资源的关系
	 * */
	void save(FolderResourceRelation relation);

	/**
	 * 删除文件夹与资源的关系
	 * */
	void delete(String relationId);
	
	/**
	 * 更新文件夹与资源的关系
	 * */
	void update(FolderResourceRelation relation);
	
	/**
	 * 根据文件夹的uuid查询文件夹与资源的关系
	 * @param id 文件夹与资源的关系Id
	 * */
	FolderResourceRelation getByID(String relationId);
	
	/**
	 * 根据folderId查询所有相关资源关系
	 * */
	List<FolderResourceRelation> getRelations(String folderId,Boolean isDeleted);
	
	/**
	 * 根据条件查询指定资源
	 * */
	FolderResourceRelation getRelationsByConditions(FolderResourceRelation relation) throws Exception;
	
	/**
	 * 查询收藏的资源
	 * @param uid userId
	 * */
	List<Resource> getFavoriteList(String uid);
	
	/**
	 * 查询删除（逻辑删除）资源列表
	 * @param uid userId
	 * */
	List<FolderResourceRelation> getDeletedResourceList(String uid);
	
	/**
	 * 还原逻辑删除的文件
	 * @param uid 用户id
	 * @param resourceId 文件id
	 * */
	void restoreRerource(String uid, String resourceId);

	/**
	 * 根据resourceId查询文件与文件夹的关系
	 * @param resourceId文件id
	 * */
	FolderResourceRelation getRelationByRecourseId(String resourceId);

	/**
	 * 根据文件类型查询文件列表
	 * @param uid 用户id
	 * @param type 文件类型：0-视频 1-音频 2-文档 3-图片 99-其他
	 * @param order 按时间排序 0-降序 1-升序
	 * */
	List<Resource> getResourcesInType(String uid, int type,int orderBy);

	/**
	 * 根据文件类型查询文件数量
	 * @param uid 用户id
	 * @param type 文件类型：0-视频 1-音频 2-文档 3-图片 99-其他 -1 全部类型
	 * */
	int getResourceCountInType(String uid, int type);

	/**
	 * 还原文件并覆盖已存在的文件
	 * @param uid 用户id
	 * @param resourceId 文件id
	 * */
	void restoreAndOverwriteRerource(String uid, String resourceId);

	/**
	 * 还原文件重命名还原的文件
	 * @param uid 用户id
	 * @param resourceId 文件id
	 * */
	void restoreAndRenameResource(String uid, String resourceId);
}
