package com.nercl.music.cloud.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.nercl.music.cloud.dao.ResourceDao;
import com.nercl.music.cloud.entity.resource.BookResource;
import com.nercl.music.cloud.entity.resource.PersonalResouce;
import com.nercl.music.cloud.entity.resource.QuestionResource;
import com.nercl.music.cloud.entity.resource.Resource;
import com.nercl.music.cloud.entity.resource.ResourceType;
import com.nercl.music.cloud.entity.resource.RhythmResource;
import com.nercl.music.cloud.entity.resource.SongResource;
import com.nercl.music.cloud.entity.resource.Structure;
import com.nercl.music.cloud.entity.resource.TopicResource;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceDao resourceDao;

	@Value("${dd-yinyue.resource}")
	private String resource;

	@Autowired
	private Gson gson;

	@Override
	public void save(Resource resource) {
		resourceDao.save(resource);
	}

	@Override
	public String save(String json, InputStream in, String name, String ext) throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, Object> fromJson = gson.fromJson(json, Map.class);
		fromJson = null == fromJson ? Maps.newHashMap() : fromJson;
		String fileName = Strings.isNullOrEmpty(name) ? (String) fromJson.getOrDefault("fileName", "") : name; // 资源名
		String resourceType = (String) fromJson.getOrDefault("resourceType", null); // 资源类型

		// 教本资源
		String structure = (String) fromJson.getOrDefault("structure", null); // 结构
		boolean isHelp = (boolean) fromJson.getOrDefault("isHelp", false); // 是否help文件
		String groupNo = (String) fromJson.getOrDefault("groupNo", null); // groupNo

		// 个人资源
		String uploaderId = (String) fromJson.getOrDefault("uploaderId", null); // 上传资源者

		// 专题资源
		String topicId = (String) fromJson.getOrDefault("topicId", null);

		String description = (String) fromJson.getOrDefault("description", null);

		String rid = "";
		String uuid = UUID.randomUUID().toString();
		File f = new File(resource + File.separator + uuid + "." + ext);
		FileUtils.copyInputStreamToFile(in, f);
		Long size = f.length() / 1024;
		size = size == 0 ? 1 : size;
		ResourceType typeByDesc = ResourceType.getResourceTypeByDesc(resourceType);
		typeByDesc = null == typeByDesc ? ResourceType.RESOURCE : typeByDesc;
		switch (typeByDesc) {
		case PERSONAL_RESOURCE: // 个人资源
			PersonalResouce res = new PersonalResouce();
			res.setCloudId(uuid);
			res.setCreateAt(System.currentTimeMillis());
			res.setExt(ext);
			res.setName(fileName);
			res.setSize(size);
			res.setDescription(description);
			res.setPersonId(uploaderId);
			save(res);
			rid = res.getId();
			break;
		case RHYTHM_RESOURCE: // 节奏训练资源
			RhythmResource ryr = new RhythmResource();
			ryr.setCloudId(uuid);
			ryr.setCreateAt(System.currentTimeMillis());
			ryr.setExt(ext);
			ryr.setName(fileName);
			ryr.setSize(size);
			ryr.setDescription(description);
			save(ryr);
			rid = ryr.getId();
			break;
		case BOOK_RESOURCE: // 教本资源
			BookResource br = new BookResource();
			br.setCloudId(uuid);
			br.setCreateAt(System.currentTimeMillis());
			br.setExt(ext);
			br.setIsHelp(isHelp);
			br.setGroupNo(groupNo);
			br.setName(fileName);
			br.setSize(size);
			br.setDescription(description);
			if (!Strings.isNullOrEmpty(structure)) {
				br.setStructure(Structure.valueOf(structure));
			}
			save(br);
			rid = br.getId();
			break;
		case TOPIC_RESOURCE: // 专题资源
			TopicResource tr = new TopicResource();
			tr.setCloudId(uuid);
			tr.setCreateAt(System.currentTimeMillis());
			tr.setExt(ext);
			tr.setName(fileName);
			tr.setTopicId(topicId);
			tr.setSize(size);
			tr.setDescription(description);
			save(tr);
			rid = tr.getId();
			break;
		case QUESTION_RESOURCE: // 题目资源
			QuestionResource qr = new QuestionResource();
			qr.setCloudId(uuid);
			qr.setCreateAt(System.currentTimeMillis());
			qr.setExt(ext);
			qr.setName(fileName);
			qr.setSize(size);
			qr.setDescription(description);
			save(qr);
			rid = qr.getId();
			break;
		case SONG_RESOURCE: // 歌曲资源
			SongResource sr = new SongResource();
			sr.setCloudId(uuid);
			sr.setCreateAt(System.currentTimeMillis());
			sr.setExt(ext);
			sr.setName(fileName);
			sr.setSize(size);
			sr.setDescription(description);
			save(sr);
			rid = sr.getId();
			break;
		case RESOURCE: // 普通资源
			Resource rs = new Resource();
			rs.setCloudId(uuid);
			rs.setCreateAt(System.currentTimeMillis());
			rs.setExt(ext);
			rs.setName(fileName);
			rs.setSize(size);
			rs.setDescription(description);
			save(rs);
			rid = rs.getId();
			break;
		default:
			break;
		}
		if (null != in) {
			in.close();
		}
		return rid;
	}

	@Override
	public Resource getByID(String id) {
		return resourceDao.findByID(id);
	}

	@Override
	public Resource getByCloudId(String cloudId) {
		List<Resource> byCloudId = resourceDao.getByCloudId(cloudId);
		// 如果对应的资源存在，那么一定是第一条，一个cloudId只可能对应一条记录
		if (!byCloudId.isEmpty()) {
			return byCloudId.get(0);
		}
		return null;
	}

	@Override
	public void deleteById(String rid) {
		resourceDao.deleteById(rid);
	}

	@Override
	public List<Resource> getByConditions(Resource conditions) throws Exception {
		return resourceDao.findByConditions(conditions);
	}

	@Override
	public List<Resource> fuzzyQueryResources(String condition) {
		return resourceDao.fuzzyQueryResources(condition);
	}

	@Override
	public List<Resource> fuzzyQueryFolderResources(String uid, String condition) {
		return resourceDao.fuzzyQueryFolderResources(uid,condition);
	}

}
