package com.nercl.music.cloud.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.folder.FolderResourceRelation;
import com.nercl.music.cloud.entity.resource.Resource;
import com.nercl.music.constant.ResourceConstant;

@Repository
public class FolderResourceRelationDaoImpl extends AbstractBaseDaoImpl<FolderResourceRelation, String>
		implements FolderResourceRelationDao {

	@Override
	public List<FolderResourceRelation> getRelations(String folderId, Boolean isDeleted) {
		String jpql = "from FolderResourceRelation frr where frr.folderId = ?1 and frr.isDeleted is ?2";
		return this.executeQueryWithoutPaging(jpql, folderId, isDeleted);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Resource> getFavoriteList(String uid) {
		String jpql = "select frr.resource" + " from FolderResourceRelation frr, Folder fd,Resource rs "
				+ "where frr.folderId=fd.id and frr.resourceId = rs.id "
				+ "and frr.isCollection is true and fd.userId = ?1";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1, uid);
		return (List<Resource>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FolderResourceRelation> getDeletedResourceList(String uid) {
		String jpql = "select frr from FolderResourceRelation frr, Folder fd "
				+ "where (frr.folderId=fd.id or frr.folderId is null) and fd.userId = ?1 and frr.isDeleted is true";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1, uid);
		return (List<FolderResourceRelation>) query.getResultList();
	}

	@Override
	public List<FolderResourceRelation> restoreRerource(String uid, String resourceId) {
		String jpql = "select frr from FolderResourceRelation frr, Folder fd "
				+ "where frr.folderId = fd.id and fd.userId = ?1 and frr.resourceId = ?2";
		return this.executeQueryWithoutPaging(jpql, uid, resourceId);
	}

	@Override
	public List<FolderResourceRelation> getRelationByRecourseId(String resourceId) {
		String jpql = "from FolderResourceRelation frr where frr.resourceId = ?1";
		return this.executeQueryWithoutPaging(jpql, resourceId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Resource> getResourcesInType(String uid, int type,int orderBy) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(
				"select rs from FolderResourceRelation frr, Folder fd,Resource rs where ((frr.folderId = fd.id or frr.folderId is null) and frr.resourceId = rs.id) and fd.isDeleted is false ");
		switch (type) {
		case ResourceConstant.VIDEO:
			sbf.append(
					"and rs.ext in ('mp3pro','wma','mp3','mid','midi','ogg','wav','ape','module','vqf','m4a')");
			break;
		case ResourceConstant.AUDIO:
			sbf.append(
					"and rs.ext in ('mpeg','flash','cd','mp4','asf','rmvb','rm','real','3gp','flv','mov','avi','m4v','dat','mkv','vob','wmv')");
			break;
		case ResourceConstant.DOCUMENT:
			sbf.append(
					"and rs.ext in ('zip','rar','doc','docx','xls','xlsx','ppt','pptx','vsd','vsdx','mmp','txt','csv','pdf','xml','json','html','htm')");
			break;
		case ResourceConstant.PICTURE:
			sbf.append(
					"and rs.ext in ('jpg','bmp','pcx','tiff','gif','jpeg','tga','exif','fpx','svg','psd','cdr','pcd','dxf','ufo','eps','png')");
			break;
		case ResourceConstant.STAFF:
			sbf.append("and rs.ext = 'staff'");
			break;
		case ResourceConstant.NUM:
			sbf.append("and rs.ext = 'num'");
			break;
		case ResourceConstant.DRUM:
			sbf.append("and rs.ext = 'drum'");
			break;
		case ResourceConstant.OTHER:
			sbf.append(
					"and rs.ext not in ('zip','rar','jpg','wma','rmvb','rm','flash','mp4','mid','3gp','mpeg','flv','mov','m4v','avi','dat','mkv','vob','cd','ogg','mp3','asf','wma','wmv','wav','mp3pro','rm','real','ape','module','midi','vqf','doc','docx','xls','xlsx','ppt','pptx','vsd','vsdx','mmp','txt','csv','pdf','xml','json','html','htm','drum','num','staff','bmp ','pcx','tiff','gif','jpeg','tga','exif','fpx','svg','psd','cdr','pcd','dxf','ufo','eps','png','m4a')");
			break;
		default:
			return null;
		}
		if(orderBy==0){
			sbf.append("and fd.userId = ?1 and frr.isDeleted=false and fd.isDeleted=false group by rs.id order by rs.createAt desc");
		}else{
			sbf.append("and fd.userId = ?1 and frr.isDeleted=false and fd.isDeleted=false group by rs.id order by rs.createAt asc");
		}
		Query query = entityManager.createQuery(sbf.toString());
		query.setParameter(1, uid);
		return query.getResultList();
	}

	@Override
	public int getResourceCountInType(String uid, int type) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(
				"select count(rs.id) from FolderResourceRelation frr, Folder fd,Resource rs where frr.folderId = fd.id and frr.resourceId = rs.id ");
		switch (type) {
		case -1: // 查询语句不带条件，查询全部文件数量

			break;
		case ResourceConstant.VIDEO: // 查询音频文件数量
			sbf.append(
					"and rs.ext in ('mp3pro','wma','mp3','mid','midi','ogg','wav','ape','module','vqf','m4a')");
			break;
		case ResourceConstant.AUDIO: // 查询视频文件数量
			sbf.append(
					"and rs.ext in ('mpeg','flash','cd','mp4','asf','rmvb','rm','real','3gp','flv','mov','avi','m4v','dat','mkv','vob','wmv')");
			break;
		case ResourceConstant.DOCUMENT: // 查询文档类型文件数量
			sbf.append(
					"and rs.ext in ('zip','rar','doc','docx','xls','xlsx','ppt','pptx','vsd','vsdx','mmp','txt','csv','pdf','xml','json','html','htm')");
			break;
		case ResourceConstant.PICTURE: // 查询图片文件数量
			sbf.append(
					"and rs.ext in ('jpg','bmp','pcx','tiff','gif','jpeg','tga','exif','fpx','svg','psd','cdr','pcd','dxf','ufo','eps','png')");
			break;
		case ResourceConstant.STAFF:
			sbf.append("and rs.ext = 'staff'");
			break;
		case ResourceConstant.NUM:
			sbf.append("and rs.ext = 'num'");
			break;
		case ResourceConstant.DRUM:
			sbf.append("and rs.ext = 'drum'");
			break;
		case ResourceConstant.OTHER: // 其他类型文件数量
			sbf.append(
					"and rs.ext not in ('zip','rar','jpg','wma','rmvb','rm','flash','mp4','mid','3gp','mpeg','flv','mov','m4v','avi','dat','mkv','vob','cd','ogg','mp3','asf','wma','wmv','wav','mp3pro','rm','real','ape','module','midi','vqf','doc','docx','xls','xlsx','ppt','pptx','vsd','vsdx','mmp','txt','csv','pdf','xml','json','html','htm','drum','num','staff','bmp ','pcx','tiff','gif','jpeg','tga','exif','fpx','svg','psd','cdr','pcd','dxf','ufo','eps','png','m4a')");
			break;
		default:
			return 0;
		}
		sbf.append(" "); //加个空格
		sbf.append("and fd.userId = ?1");
		sbf.append(" "); //加个空格
		sbf.append("and frr.isDeleted is false and fd.isDeleted is false"); // 没有删除的
		return this.executeCountQuery(sbf.toString(), uid);
	}
}