package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.folder.FolderResourceRelation;
import com.nercl.music.cloud.entity.resource.Resource;

public interface FolderResourceRelationDao extends BaseDao<FolderResourceRelation, String> {
	
	/**
	 * 根据folderId查询所有相关资源Id
	 * */
	List<FolderResourceRelation> getRelations(String folderId,Boolean isDeleted);
	
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
	List<FolderResourceRelation> restoreRerource(String uid, String resourceId);

	/**
	 * 根据resourceId查询文件与文件夹的关系
	 * @param resourceId文件id
	 * */
	List<FolderResourceRelation> getRelationByRecourseId(String resourceId);

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

}