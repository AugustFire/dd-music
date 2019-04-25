package com.nercl.music.cloud.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.nercl.music.cloud.dao.FolderResourceRelationDao;
import com.nercl.music.cloud.entity.folder.Folder;
import com.nercl.music.cloud.entity.folder.FolderResourceRelation;
import com.nercl.music.cloud.entity.resource.Resource;
import com.nercl.music.cloud.service.FolderResourceRelationService;
import com.nercl.music.cloud.service.FolderService;
import com.nercl.music.cloud.service.ResourceService;
import com.nercl.music.constant.CList;
import com.nercl.music.util.CommUtils;

@RestController
public class FolderController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private FolderService folderService;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private FolderResourceRelationService folderResourceRelationService;

	@Autowired
	private FolderResourceRelationDao folderResourceRelationDao;

	@Value("${dd-yinyue.resource}")
	private String resource;

	/**
	 * 初始化根目录
	 * 
	 * @param uid
	 *            用户Id
	 */
	@GetMapping(value = "/{uid}/folder/init", produces = JSON_PRODUCES)
	public Map<String, Object> initFolder(@PathVariable String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		Folder folder = new Folder();
		try {
			List<Folder> root = folderService.getUserRoot(uid);
			// 如果查询结果为空，说明此用户的根目录不存在
			if (root.isEmpty()) {
				// 初始化根目录
				Long now = System.currentTimeMillis();
				folder.setFolderName(uid + UUID.randomUUID()); // 文件夹的名称用userId加一个uuid，防止有用户命名的文件夹名与根目录冲突
				folder.setIsCollection(false);
				folder.setIsDeleted(false);
				folder.setIsRestored(false);
				folder.setCreateTime(now);
				folder.setUpdateTime(now);
				folderService.save(folder);
				ret.put("folder", folder);
			} else {
				ret.put("folder", root.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 获取指定目录下的所有文件和文件夹
	 * 
	 * @param uid
	 *            用户Id
	 * @param parentId
	 *            父文件夹Id
	 */
	@GetMapping(value = "/{uid}/folder", params = { "parentId" }, produces = JSON_PRODUCES)
	public Map<String, Object> getFolderAndResource(@PathVariable String uid, String parentId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(parentId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "parentId is null");
			return ret;
		}
		List<Map<String, Object>> resources = Lists.newArrayList();
		List<Folder> childFolders = folderService.getChildFolders(uid, false, parentId);
		List<FolderResourceRelation> relations = folderResourceRelationService.getRelations(parentId, false);
		relations.forEach(relation -> {
			Resource reses = resourceService.getByID(relation.getResourceId());
			resources.add(reform(reses));
		});
		ret.put("code", CList.Api.Client.OK);
		ret.put("folders", childFolders);
		ret.put("resources", resources);
		return ret;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param uid
	 *            用户id
	 * @param parentId
	 *            父文件夹Id
	 * @param folderName
	 *            创建的文件夹的名称
	 */
	@PostMapping(value = "/{uid}/folder", produces = JSON_PRODUCES)
	public Map<String, Object> newFolder(@PathVariable String uid, String parentId, String folderName) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(parentId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "parentId is null");
			return ret;
		}

		// 父级文件夹不存在时，不能创建文件夹
		Folder parentFolder = folderService.getByID(parentId);
		if (parentFolder == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified parent folder not exist");
			return ret;
		}
		// 查询文件夹名称是否存在，若存在，则不能创建该名称的文件夹
		List<String> childFolderNames = folderService.getChildFolderNames(uid, parentId);
		if (childFolderNames.contains(folderName)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified folderName exist,please rename the folder");
			return ret;
		}
		Long now = System.currentTimeMillis();
		Folder folder = new Folder();
		folder.setFolderName(folderName);
		folder.setUserId(uid);
		folder.setParentId(parentId);
		folder.setCreateTime(now);
		folder.setIsCollection(false);
		folder.setIsDeleted(false);
		folder.setIsRestored(false);
		folder.setUpdateTime(now);
		folderService.save(folder);
		ret.put("code", CList.Api.Client.OK);
		ret.put("folder", folder);
		return ret;
	}

	/**
	 * 在文件夹中上传资源
	 * 
	 * @param uid
	 *            用户id
	 * @param folderId
	 *            文件夹Id
	 * @param fileName
	 *            资源名称
	 */
	@PostMapping(value = "/{uid}/folder/upload", produces = JSON_PRODUCES)
	public Map<String, Object> uploadInFolder(@PathVariable String uid, String folderId, String fileName,
			HttpServletRequest request) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		List<FolderResourceRelation> relations = folderResourceRelationService.getRelations(folderId, false);
		List<Resource> resourceList = relations.stream().map(FolderResourceRelation::getResource)
				.collect(Collectors.toList());
		List<String> resourceNameList = resourceList.stream().map(Resource::getName).collect(Collectors.toList());
		Integer repeatTimes = CommUtils.nameRepeatTimes(fileName, resourceNameList);
		if (repeatTimes > 0 || resourceNameList.contains(fileName)) {
			fileName = CommUtils.renameDuplicationResourceName(fileName, repeatTimes);
		}
		if (Strings.isNullOrEmpty(folderId)) {
			folderId = null;
		}
		Resource uploadResource = null;
		try {
			uploadResource = uploadResource(fileName, request);
		} catch (IOException e) {
			throw new RuntimeException("文件上传异常！");
		}
		FolderResourceRelation relation = new FolderResourceRelation();
		Long now = System.currentTimeMillis();
		relation.setFolderId(folderId);
		relation.setIsDeleted(false);
		relation.setIsRestored(false);
		relation.setIsCollection(false);
		relation.setResourceId(uploadResource.getId());
		relation.setCreateTime(now);
		relation.setUpdateTime(now);
		folderResourceRelationService.save(relation);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 文件的逻辑删除
	 * 
	 * @param fid
	 *            文件夹Id
	 * @param resourceId
	 *            文件Id
	 */
	@PostMapping(value = "/folder/deleteresourcelogical", produces = JSON_PRODUCES)
	public Map<String, Object> deleteResourceLogical(String fid, String resourceId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(resourceId) || Strings.isNullOrEmpty(fid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "fid or resourceId is null");
			return ret;
		}
		Resource resource = resourceService.getByID(resourceId);
		if (resource == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified resource not exist");
			return ret;
		}
		// 根据文件夹id和资源id查询文件夹与资源的关系
		FolderResourceRelation relation = new FolderResourceRelation();
		relation.setFolderId(fid);
		relation.setResourceId(resourceId);
		FolderResourceRelation resourceRelation = new FolderResourceRelation();
		try {
			resourceRelation = folderResourceRelationService.getRelationsByConditions(relation);
			if (resourceRelation == null) {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "it is unmounted rerource");
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 文件夹与资源的关系存在则更新是否删除为是
		resourceRelation.setIsDeleted(true);
		resourceRelation.setUpdateTime(System.currentTimeMillis());
		folderResourceRelationService.update(resourceRelation);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 文件的逻辑删除-批量删除
	 * 
	 * @param fid
	 *            文件夹Id
	 * @param resourceIds
	 *            文件id数组
	 */
	@PostMapping(value = "/folder/deleteresourceslogical", produces = JSON_PRODUCES)
	public Map<String, Object> deleteResourcesLogical(String fid, String[] resourceIds) {
		Map<String, Object> ret = Maps.newHashMap();
		if (resourceIds == null || resourceIds.length == 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "resourceIds is null");
			return ret;
		}
		Arrays.asList(resourceIds).forEach(resourceId -> {
			// 根据文件夹id和资源id查询文件夹与资源的关系
			FolderResourceRelation relation = new FolderResourceRelation();
			relation.setFolderId(fid);
			relation.setResourceId(resourceId);
			FolderResourceRelation resourceRelation = new FolderResourceRelation();
			try {
				resourceRelation = folderResourceRelationService.getRelationsByConditions(relation);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (resourceRelation != null) {
				// 文件夹与资源的关系存在则更新是否删除为是
				resourceRelation.setIsDeleted(true);
				resourceRelation.setUpdateTime(System.currentTimeMillis());
				folderResourceRelationService.update(resourceRelation);
			}
		});
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 文件夹的逻辑删除
	 * 
	 * @param folderId
	 *            文件夹Id
	 */
	@PostMapping(value = "/{uid}/folder/deletefolderlogical", produces = JSON_PRODUCES)
	public Map<String, Object> deleteFolderLogical(@PathVariable String uid, String folderId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(folderId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "folderId is null");
			return ret;
		}

		Folder folder = folderService.getByID(folderId);
		if (folder == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified folder not exist");
			return ret;
		}
		folderService.deleteFolderLogical(uid, folderId);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 文件夹的逻辑删除-批量删除
	 * 
	 * @param folderIds
	 *            文件夹Id数组
	 */
	@PostMapping(value = "/{uid}/folder/deletefolderslogical", produces = JSON_PRODUCES)
	public Map<String, Object> deleteFoldersLogical(@PathVariable String uid, String[] folderIds) {
		Map<String, Object> ret = Maps.newHashMap();
		if (folderIds == null || folderIds.length == 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "folderIds is null");
			return ret;
		}
		Arrays.asList(folderIds).forEach(folderId -> {
			folderService.deleteFolderLogical(uid, folderId);
		});
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 彻底删除文件夹
	 * 
	 * @param uid
	 *            用户id
	 * @param folderId
	 *            要删除的文件夹Id
	 */
	@DeleteMapping(value = "/{uid}/folder", params = { "folderId" }, produces = JSON_PRODUCES)
	public Map<String, Object> deleteFolder(@PathVariable String uid, String folderId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid) || Strings.isNullOrEmpty(folderId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "folderId is null");
			return ret;
		}
		Folder folder = folderService.getByID(folderId);
		if (folder == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified folder not exist");
			return ret;
		}
		folderService.deleteFolderOverall(uid, folderId);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 彻底删除文件
	 * 
	 * @param uid
	 *            用户id
	 * @param resourceId
	 *            要删除的文件Id
	 */
	@DeleteMapping(value = "/{uid}/resource", params = { "resourceId" }, produces = JSON_PRODUCES)
	public Map<String, Object> deleteResource(@PathVariable String uid, String resourceId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid) || Strings.isNullOrEmpty(resourceId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "resourceId or uid is null");
			return ret;
		}
		// 文件是否存在
		Resource resource = resourceService.getByID(resourceId);
		if (resource == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified resource not exist");
			return ret;
		}
		// 文件是否被删除
		FolderResourceRelation relation = folderResourceRelationService.getRelationByRecourseId(resourceId);
		if (relation == null || relation.getIsDeleted() == false) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified resource not deleted");
			return ret;
		}
		// 删除文件与文件夹关系
		folderResourceRelationService.delete(relation.getId());
		// 删除文件
		resourceService.deleteById(resourceId);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 重命名文件夹
	 * 
	 * @param uid
	 *            用户id
	 * @param folderId
	 *            文件夹Id
	 * @param newFolderName
	 *            新的文件夹的名称
	 */
	@PostMapping(value = "/{uid}/folder/rename", produces = JSON_PRODUCES)
	public Map<String, Object> renameFolder(@PathVariable String uid, String folderId, String newFolderName) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(folderId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "folderId is null");
			return ret;
		}
		// 查询文件夹是否存在
		Folder folder = folderService.getByID(folderId);
		if (folder == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified folder not exist");
			return ret;
		}
		// 查询文件夹名称在同一层级下的所有文件夹中是否存在，若存在，则不能重命名为此名称
		List<String> childFolderNames = folderService.getChildFolderNames(uid, folder.getParentId());
		if (childFolderNames.contains(newFolderName)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified folderName exist,please rename the folder");
			return ret;
		}
		folder.setFolderName(newFolderName);
		folder.setUpdateTime(System.currentTimeMillis());
		folderService.update(folder);
		ret.put("code", CList.Api.Client.OK);
		ret.put("folder", folder);
		return ret;
	}

	/**
	 * 复制文件到目标文件夹
	 * 
	 * @param uid
	 *            用户id
	 * @param sourceId
	 *            源文件Id
	 * @param targetFolderId
	 *            目标文件夹id
	 */
	@PostMapping(value = "/{uid}/folder/copy", produces = JSON_PRODUCES)
	public Map<String, Object> copyFolder(@PathVariable String uid, String sourceId, String targetFolderId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid) || Strings.isNullOrEmpty(sourceId) || Strings.isNullOrEmpty(targetFolderId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid or sourceId or targetFolderId is null");
			return ret;
		}
		Folder folder = folderService.getByID(targetFolderId);
		if (folder == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified folder not exist");
			return ret;
		}
		Resource resource = resourceService.getByID(sourceId);
		if (resource == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified resource not exist");
			return ret;
		}
		Long now = System.currentTimeMillis();

		Resource newResource = new Resource();
		newResource.setCloudId(resource.getCloudId());
		newResource.setCreateAt(now);
		newResource.setExt(resource.getExt());
		newResource.setName(resource.getName());
		newResource.setSize(resource.getSize());
		resourceService.save(newResource);

		FolderResourceRelation frRalation = new FolderResourceRelation();
		frRalation.setCreateTime(now);
		frRalation.setFolderId(targetFolderId);
		frRalation.setIsCollection(false);
		frRalation.setIsDeleted(false);
		frRalation.setIsRestored(false);
		frRalation.setResourceId(newResource.getId());
		frRalation.setUpdateTime(now);
		folderResourceRelationService.save(frRalation);

		ret.put("code", CList.Api.Client.OK);
		ret.put("newResource", newResource);
		return ret;
	}

	/**
	 * 收藏文件
	 * 
	 * @param uid
	 *            用户id
	 * @param resourceId
	 *            文件Id
	 * @param folderId
	 *            文件夹Id
	 */
	@PostMapping(value = "/{uid}/folder/enshrine", produces = JSON_PRODUCES)
	public Map<String, Object> enshrineResource(@PathVariable String uid, String resourceId, String folderId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		Resource resource = resourceService.getByID(resourceId);
		if (resource == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified resource not exist");
			return ret;
		}
		// 根据文件id和文件夹id查询文件和文件夹的关系
		FolderResourceRelation relation = new FolderResourceRelation();
		relation.setFolderId(folderId);
		relation.setResourceId(resourceId);
		FolderResourceRelation folderResourceRelation = new FolderResourceRelation();
		try {
			folderResourceRelation = folderResourceRelationService.getRelationsByConditions(relation);
			if (folderResourceRelation == null) {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "rerource is unmounted ");
				return ret;
			} else if (folderResourceRelation.getIsCollection()) {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "rerource is enshrined ");
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		folderResourceRelation.setIsCollection(true);
		folderResourceRelation.setUpdateTime(System.currentTimeMillis());
		folderResourceRelationService.update(folderResourceRelation);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 取消收藏文件
	 * 
	 * @param uid
	 *            用户id
	 * @param resourceId
	 *            文件Id
	 * @param folderId
	 *            文件夹Id
	 */
	@PostMapping(value = "/{uid}/folder/cancelEnshrine", produces = JSON_PRODUCES)
	public Map<String, Object> cancelEnshrineResource(@PathVariable String uid, String resourceId, String folderId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		Resource resource = resourceService.getByID(resourceId);
		if (resource == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified resource not exist");
			return ret;
		}
		// 根据文件id和文件夹id查询文件和文件夹的关系
		FolderResourceRelation relation = new FolderResourceRelation();
		relation.setFolderId(folderId);
		relation.setResourceId(resourceId);
		FolderResourceRelation folderResourceRelation = new FolderResourceRelation();
		try {
			folderResourceRelation = folderResourceRelationService.getRelationsByConditions(relation);
			if (folderResourceRelation == null) {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "rerource is unmounted ");
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		folderResourceRelation.setIsCollection(false);
		folderResourceRelation.setUpdateTime(System.currentTimeMillis());
		folderResourceRelationService.update(folderResourceRelation);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 查看收藏夹列表
	 * 
	 * @param uid
	 *            用户id
	 */
	@GetMapping(value = "/{uid}/favorite/list", produces = JSON_PRODUCES)
	public Map<String, Object> favoriteList(@PathVariable String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		List<Map<String,Object>> list = Lists.newArrayList();
		List<Resource> favoriteList = folderResourceRelationService.getFavoriteList(uid);
		favoriteList.forEach(res->{
			list.add(reform(res));
		});
		ret.put("code", CList.Api.Client.OK);
		ret.put("resources", list);
		return ret;
	}

	/**
	 * 查看回收站列表
	 * 
	 * @param uid
	 *            用户id
	 */
	@GetMapping(value = "/{uid}/recyclebin/list", produces = JSON_PRODUCES)
	public Map<String, Object> recycleBinList(@PathVariable String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		List<Map<String, Object>> deletedResources = Lists.newArrayList(); // 删除状态的文件
		List<Folder> showFolders = Lists.newArrayList(); // 要展示的文件夹

		List<Folder> deletedFolders = folderService.getDeletedFodlers(uid); // 删除状态的文件夹
		// 将所有删除状态的文件夹以更新时间分组,每组只取最顶级的folder展示
		Map<Long, List<Folder>> collect = deletedFolders.stream()
				.collect(Collectors.groupingBy(Folder::getUpdateTime, Collectors.toList()));
		for (Long key : collect.keySet()) {
			List<Folder> listFolder = collect.get(key);
			// 取所有删除状态的文件夹的parentId
			List<String> parentId = listFolder.stream().map(Folder::getParentId).collect(Collectors.toList());
			listFolder.forEach(folder -> {
				// Id不在所有parentId集合中的folder是最顶级的folder
				if (!parentId.contains(folder.getId())) {
					showFolders.add(folder);
				}
			});
		}

		// 查询删除状态为true的 资源关系
		List<FolderResourceRelation> list = folderResourceRelationService.getDeletedResourceList(uid);
		list.forEach(l -> {
			if (l.getFolder().getIsDeleted()) { // 资源对应的文件夹的删除状态也是true,说明该资源和其所在文件夹都被删除了
				// 如果资源的删除时间与
				// 其所在文件夹的删除时间不一致，说明该资源不是被联动删除的，要展示该资源.否则不说明该资源是被所在文件夹联动删除的，不用展示该资源
				if (l.getUpdateTime().longValue() != l.getFolder().getUpdateTime().longValue()) {
					deletedResources.add(reform(resourceService.getByID(l.getResourceId())));
				}
			} else { // 资源对应的文件夹的删除状态是false，说明只删除资源，没有删除资源所在文件夹，要展示该资源
				deletedResources.add(reform(resourceService.getByID(l.getResourceId())));
			}
		});
		ret.put("code", CList.Api.Client.OK);
		ret.put("deletedFolders", showFolders);
		ret.put("deletedResources", deletedResources);
		return ret;
	}

	/**
	 * 还原文件夹
	 * 
	 * @param uid
	 *            用户id
	 * @param folderId
	 *            文件夹id
	 */
	@PostMapping(value = "/{uid}/folder/restore", produces = JSON_PRODUCES)
	public Map<String, Object> restoreFolder(@PathVariable String uid, String folderId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(folderId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "folderId is null");
			return ret;
		}
		Folder folder = folderService.getByID(folderId);
		if (folder == null) { // 要还原的文件不存在
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified folder not exist");
			return ret;
		} else if (!folder.getIsDeleted()) { // 要还原的文件不是删除状态
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified folder not in delete status");
			return ret;
		}
		folderService.restoreFolder(uid, folderId);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 还原文件夹-批量还原
	 * 
	 * @param uid
	 *            用户id
	 * @param folderIds
	 *            文件夹id数组
	 */
	@PostMapping(value = "/{uid}/folders/restore", produces = JSON_PRODUCES)
	public Map<String, Object> restoreFolders(@PathVariable String uid, String[] folderIds) {
		Map<String, Object> ret = Maps.newHashMap();
		if (folderIds == null || folderIds.length == 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "folderId is null");
			return ret;
		}
		for (int i = 0; i < folderIds.length; i++) {
			Folder folder = folderService.getByID(folderIds[i]);
			if (folder == null) { // 要还原的文件不存在
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "specified folder not exist");
				return ret;
			} else if (!folder.getIsDeleted()) { // 要还原的文件不是删除状态
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "specified folder not in delete status");
				return ret;
			}
			folderService.restoreFolder(uid, folderIds[i]);
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * <p>
	 * 还原文件之前先判断还原文件夹中是否有同名文件,如果没有同名文件则直接调用还原接口，
	 * 如果有同名文件则给3个选择：1-直接覆盖原来的文件;2-重命名还原过去的文件;3-不还原。 如果没有则发直接还原
	 * </p>
	 * 
	 * @param uid
	 *            用户id
	 * @param resourceId
	 *            资源id
	 */
	@GetMapping(value = "/{uid}/resource/restore_prefix", produces = JSON_PRODUCES)
	public Map<String, Object> hasRepeatResouce(@PathVariable String uid, String resourceId) {
		Map<String, Object> ret = Maps.newHashMap();
		List<FolderResourceRelation> relations = folderResourceRelationDao.restoreRerource(uid, resourceId);
		if (!relations.isEmpty()) { // 根据资源id和用户id可以查询到唯一一条资源与文件夹的关系记录
			FolderResourceRelation folderResourceRelation = relations.get(0);
			String fileName = folderResourceRelation.getResource().getName(); // 资源名称
			String folderId = folderResourceRelation.getFolder().getId(); // 资源原来所在文件夹id
			List<FolderResourceRelation> childResouceRelations = folderResourceRelationService.getRelations(folderId,
					false);
			List<String> resourceNameList = childResouceRelations.stream().map(FolderResourceRelation::getResource)
					.map(Resource::getName).collect(Collectors.toList());
			Integer repeatTimes = CommUtils.nameRepeatTimes(fileName, resourceNameList);
			if (repeatTimes > 0) {
				ret.put("desc", "此位置已包含同名文件");
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			} else {
				folderResourceRelationService.restoreRerource(uid, resourceId);
				ret.put("code", CList.Api.Client.OK);
			}
		}
		return ret;
	}

	/**
	 * 还原文件-覆盖原来的文件
	 * 
	 * @param uid
	 *            用户id
	 * @param resourceId
	 *            资源id
	 */
	@PostMapping(value = "/{uid}/resource/restore_overwrite", produces = JSON_PRODUCES)
	public Map<String, Object> restoreAndOverwriteResource(@PathVariable String uid, String resourceId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(resourceId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rerourceId is null");
			return ret;
		}
		Resource resource = resourceService.getByID(resourceId);
		if (resource == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified resource not exist");
			return ret;
		}
		folderResourceRelationService.restoreAndOverwriteRerource(uid, resourceId);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 还原文件-重命名还原的文件
	 * 
	 * @param uid
	 *            用户id
	 * @param resourceId
	 *            资源id
	 */
	@PostMapping(value = "/{uid}/resource/restore_rename", produces = JSON_PRODUCES)
	public Map<String, Object> restoreAndRenameResource(@PathVariable String uid, String resourceId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(resourceId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rerourceId is null");
			return ret;
		}
		Resource resource = resourceService.getByID(resourceId);
		if (resource == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified resource not exist");
			return ret;
		}
		folderResourceRelationService.restoreAndRenameResource(uid, resourceId);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 还原文件
	 * 
	 * @param uid
	 *            用户id
	 * @param resourceId
	 *            资源id
	 */
	@PostMapping(value = "/{uid}/resource/restore", produces = JSON_PRODUCES)
	public Map<String, Object> restoreResource(@PathVariable String uid, String resourceId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(resourceId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rerourceId is null");
			return ret;
		}
		Resource resource = resourceService.getByID(resourceId);
		if (resource == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified resource not exist");
			return ret;
		}
		folderResourceRelationService.restoreRerource(uid, resourceId);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 还原文件-批量还原
	 * 
	 * @param uid
	 *            用户id
	 * @param resourceIds
	 *            文件id数组
	 */
	@PostMapping(value = "/{uid}/resources/restore", produces = JSON_PRODUCES)
	public Map<String, Object> restoreResources(@PathVariable String uid, String[] resourceIds) {
		Map<String, Object> ret = Maps.newHashMap();
		if (resourceIds == null || resourceIds.length == 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rerourceId is null");
			return ret;
		}
		for (int i = 0; i < resourceIds.length; i++) {
			Resource resource = resourceService.getByID(resourceIds[i]);
			if (resource == null) {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "specified resource not exist");
				return ret;
			}
			folderResourceRelationService.restoreRerource(uid, resourceIds[i]);
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 清空回收站
	 * 
	 * @param uid
	 *            用户id
	 */
	@PostMapping(value = "/{uid}/recyclebin/clearup", produces = JSON_PRODUCES)
	public Map<String, Object> clearupRecyclebin(@PathVariable String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		folderService.clearupRecyclebin(uid);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 根据文件类型获取文件列表
	 * 
	 * @param uid
	 *            用户id
	 * @param type
	 *            文件类型：0-视频 1-音频 2-文档 3-图片 4-五线谱 5-简谱 6-鼓谱 99-其他
	 * @param order
	 *            按时间排序 0-降序 1-升序
	 */
	@GetMapping(value = "/{uid}/resourcelist", params = { "type", "order" }, produces = JSON_PRODUCES)
	public Map<String, Object> getResourcesInType(@PathVariable String uid, int type, int order) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid or type is null");
			return ret;
		}
		if (type != 0 && type != 1 && type != 2 && type != 3 && type != 4 && type != 5 && type != 6 && type != 99) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "undefined type");
			return ret;
		}
		if (order != 0 && order != 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "undefined order");
			return ret;
		}

		List<Resource> resources = folderResourceRelationService.getResourcesInType(uid, type, order);
		List<Map<String, Object>> resourceList = Lists.newArrayList();
		resources.forEach(res -> {
			resourceList.add(reform(res));
		});
		ret.put("code", CList.Api.Client.OK);
		ret.put("resources", resourceList);
		return ret;
	}

	/**
	 * 根据文件类型获取文件数量
	 * 
	 * @param uid
	 *            用户id
	 * @param type
	 *            文件类型：0-视频 1-音频 2-文档 3-图片 4-五线谱 5-简谱 6-鼓谱 99-其他 -1-全部类型
	 */
	@GetMapping(value = "/{uid}/resourcecount", params = { "type" }, produces = JSON_PRODUCES)
	public Map<String, Object> getResourcesCountInType(@PathVariable String uid, int type) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid or type is null");
			return ret;
		}
		if (type != 0 && type != 1 && type != 2 && type != 3 && type != 4 && type != 5 && type != 6 && type != 99
				&& type != -1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "undefined type");
			return ret;
		}
		int resourceCount = folderResourceRelationService.getResourceCountInType(uid, type);
		ret.put("code", CList.Api.Client.OK);
		ret.put("resourceCount", resourceCount);
		return ret;
	}

	/**
	 * 根据文件类型获取文件数量
	 * 
	 * @param uid
	 *            用户id
	 */
	@GetMapping(value = "/{uid}/resourcesummarize", produces = JSON_PRODUCES)
	public Map<String, Object> getResourcesSummarize(@PathVariable String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid or type is null");
			return ret;
		}
		Map<String, Object> summarize = Maps.newHashMap();
		// 文件类型：0-视频 1-音频 2-文档 3-图片 4-五线谱 5-简谱 6-鼓谱 99-其他 -1-全部类型
		int video = folderResourceRelationService.getResourceCountInType(uid, 0);
		int audio = folderResourceRelationService.getResourceCountInType(uid, 1);
		int doc = folderResourceRelationService.getResourceCountInType(uid, 2);
		int pic = folderResourceRelationService.getResourceCountInType(uid, 3);
		int staff = folderResourceRelationService.getResourceCountInType(uid, 4); // 五线谱
		int num = folderResourceRelationService.getResourceCountInType(uid, 5); // 简谱
		int drum = folderResourceRelationService.getResourceCountInType(uid, 6); // 鼓谱
		int total = folderResourceRelationService.getResourceCountInType(uid, -1);
		int other = total - audio - video - doc - pic - staff - num - drum;
		summarize.put("audio", audio);
		summarize.put("video", video);
		summarize.put("doc", doc);
		summarize.put("pic", pic);
		summarize.put("staff", staff);
		summarize.put("num", num);
		summarize.put("drum", drum);
		summarize.put("other", other);
		ret.put("code", CList.Api.Client.OK);
		ret.put("summarize", summarize);
		return ret;
	}

	/**
	 * 移动文件
	 * 
	 * @param uid
	 *            用户id
	 */
	@PostMapping(value = "/{uid}/resource/remove", produces = JSON_PRODUCES)
	public Map<String, Object> removeResource(@PathVariable String uid, String resourceId, String targetFolderId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(resourceId) || Strings.isNullOrEmpty(targetFolderId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "resourceId or targetFolderId is null");
			return ret;
		}
		// 要移动的文件是否存在
		Resource resource = resourceService.getByID(resourceId);
		if (null == resource) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified resource not exist");
			return ret;
		}
		// 文件移动的目标文件夹是否存在
		Folder targetFolder = folderService.getByID(targetFolderId);
		if (null == targetFolder) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified targetFolder not exist");
			return ret;
		}

		FolderResourceRelation relation = folderResourceRelationService.getRelationByRecourseId(resourceId);
		relation.setFolderId(targetFolder.getId());
		relation.setUpdateTime(System.currentTimeMillis());
		folderResourceRelationService.update(relation);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 移动文件夹
	 * 
	 * @param uid
	 *            用户id
	 */
	@PostMapping(value = "/{uid}/folder/remove", produces = JSON_PRODUCES)
	public Map<String, Object> removeFolder(@PathVariable String uid, String folderId, String targetFolderId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(folderId) || Strings.isNullOrEmpty(targetFolderId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "folderId or targetFolderId is null");
			return ret;
		}

		// 要移动的文件夹是否存在
		Folder folder = folderService.getByID(folderId);
		if (null == folder) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified folder not exist");
			return ret;
		}

		// 文件移动的目标文件夹是否存在
		Folder targetFolder = folderService.getByID(targetFolderId);
		List<Folder> childFolders = Lists.newArrayList();
		if (null == targetFolder) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "specified targetFolder not exist");
			return ret;
		} else {
			childFolders = folderService.getChildFolders(uid, false, targetFolder.getId());
		}
		childFolders.add(targetFolder);
		// 不能将文件夹移动到自身或其子目录下
		Iterator<Folder> iterator = childFolders.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getId().equals(folder.getId())) {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "不能将文件夹移动到自身或其子目录下");
				return ret;
			}
		}

		folder.setParentId(targetFolderId);
		folder.setUpdateTime(System.currentTimeMillis());
		folderService.update(folder);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	private Resource uploadResource(String fileName, HttpServletRequest request) throws IOException {
		// 上传资源
		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
		MultipartFile file = mreq.getFile("file");
		InputStream in = null;
		String uuid = UUID.randomUUID().toString();
		Resource rs = new Resource();
		if (file != null) {
			in = file.getInputStream();
			String ext = Files.getFileExtension(file.getOriginalFilename());
			File f = new File(resource + File.separator + uuid + "." + ext);
			FileUtils.copyInputStreamToFile(in, f);
			rs.setCloudId(uuid);
			rs.setCreateAt(System.currentTimeMillis());
			rs.setExt(ext.toLowerCase());
			rs.setName(fileName);
			rs.setSize(f.length() / 1024 < 1 ? 1 : f.length());
			resourceService.save(rs);
		} else {
			throw new RuntimeException("no file found");
		}
		return rs;
	}
	
	private Map<String, Object> reform(Resource reses) {
		Map<String, Object> resource = Maps.newHashMap();
		if (reses == null) {
			return null;
		}
		resource.put("id", reses.getId());
		resource.put("cloudId", reses.getCloudId());
		resource.put("createAt", reses.getCreateAt());
		resource.put("description", reses.getDescription());
		resource.put("ext", reses.getExt());
		resource.put("name", reses.getName());
		Long size = reses.getSize();
		String resouceSize = "";
		if (size < 1 << 10) {
			resouceSize = size + "B";
		} else if (1 << 10 <= size && size < 1 << 20) {
			resouceSize = divide(size, 1 << 10) + "KB";
		} else if (1 << 20 <= size && size < 1 << 30) {
			resouceSize = divide(size, 1 << 20) + "MB";
		} else if (1 << 30 <= size && size < 1 << 40) {
			resouceSize = divide(size, 1 << 30) + "GB";
		} else if (1 << 40 <= size && size < 1 << 50) {
			resouceSize = divide(size, 1 << 40) + "TB";
		}
		resource.put("size", resouceSize);
		return resource;
	}

	/**
	 * 计算除法,四舍五入保留2位小数
	 * 
	 * @param divider
	 *            除数
	 * @param divided
	 *            被除数
	 */
	private Float divide(Number divider, Number divided) {
		divider = null == divider ? 0 : divider;
		divided = null == divided || divided.intValue() <= 0 ? 1 : divided;
		BigDecimal bdDivider = new BigDecimal(divider.floatValue());
		BigDecimal bdDivided = new BigDecimal(divided.floatValue());
		return bdDivider.divide(bdDivided, 2, RoundingMode.HALF_UP).floatValue();
	}
}
