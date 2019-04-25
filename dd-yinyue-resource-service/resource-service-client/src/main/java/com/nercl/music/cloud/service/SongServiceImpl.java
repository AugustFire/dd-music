package com.nercl.music.cloud.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.nercl.music.cloud.dao.SongDao;
import com.nercl.music.cloud.entity.resource.Resource;
import com.nercl.music.cloud.entity.song.ResourceSongRelation;
import com.nercl.music.cloud.entity.song.Song;
import com.nercl.music.constant.ApiClient;

@Service
@Transactional
public class SongServiceImpl implements SongService {

	@Autowired
	private SongDao songDao;

	@Autowired
	private ResourceSongRelationService resourceSongRelationService;

	@Autowired
	private ImpressService impressService;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private Gson gson;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Song get(String sid) {
		return songDao.findByID(sid);
	}

	@Override
	public List<Map<String, String>> get(String[] sids) {
		List<Song> songs = songDao.get(sids);
		List<Map<String, String>> list = Lists.newArrayList();
		Optional.ofNullable(songs).ifPresent(ss -> {
			ss.forEach(song -> {
				Resource resource = getMainResource(song.getId());
				if (isXmlOrStaff(resource)) {
					Map<String, String> map = Maps.newHashMap();
					list.add(map);
					map.put("id", song.getId());
					map.put("name", song.getName());
					map.put("rid", resource.getId());
					map.put("rname", resource.getName());
				}
			});
		});
		return list;
	}

	private boolean isXmlOrStaff(Resource resource) {
		return null != resource
				&& ("staff".equalsIgnoreCase(resource.getExt()) || "xml".equalsIgnoreCase(resource.getExt()));
	}

	@Override
	public List<Map<String, Object>> getByLikeName(String name) {
		List<Song> songs = songDao.getByLikeName(name);
		List<Map<String, Object>> list = Lists.newArrayList();
		Optional.ofNullable(songs).ifPresent(ss -> {
			ss.forEach(song -> {
				Map<String, Object> map = Maps.newHashMap();
				list.add(map);
				map.put("id", song.getId());
				map.put("name", song.getName());
				Resource resource = getMainResource(song.getId());
				if (null != resource) {
					map.put("rid", resource.getId());
					map.put("rname", resource.getName());
				}
				map.put("impresses", impressService.getBySong(song.getId()));
				map.put("attachments", getResourceSongs(song.getId()));
			});
		});
		return list;
	}

	@Override
	public Song save(String name) {
		List<Song> songs = songDao.getByName(name);
		if (null == songs || songs.isEmpty()) {
			Song song = new Song();
			song.setName(name);
			songDao.save(song);
			return !Strings.isNullOrEmpty(song.getId()) ? song : null;
		}
		return null;
	}

	@Override
	public boolean update(String sid, String name) {
		Song song = songDao.findByID(sid);
		if (null == song) {
			return false;
		}
		song.setName(name);
		songDao.update(song);
		return !Strings.isNullOrEmpty(song.getId());
	}

	@Override
	public boolean delete(String sid) {
		resourceSongRelationService.deleteBySong(sid);
		songDao.deleteById(sid);
		return null == songDao.findByID(sid);
	}

	@Override
	public boolean addResourceSongRelation(String sid, String rid, boolean isMain) {
		ResourceSongRelation relation = resourceSongRelationService.getMainResourceSongBySong(sid);
		if (null != relation) {
			resourceSongRelationService.delete(relation);
			resourceService.deleteById(relation.getResourceId());
		}
		relation = new ResourceSongRelation();
		relation.setSongId(sid);
		relation.setResourceId(rid);
		relation.setIsMain(isMain);
		resourceSongRelationService.save(relation);
		return !Strings.isNullOrEmpty(relation.getId());
	}

	@Override
	public boolean addResourceSongRelation(String name, String sid, InputStream is, boolean isMain) {
		String oldRid = "";
		ResourceSongRelation relation = resourceSongRelationService.getMainResourceSongBySong(sid);
		if (null != relation) {
			resourceSongRelationService.delete(relation);
			oldRid = relation.getResourceId();
			resourceService.deleteById(oldRid);

		}
		String ext = Files.getFileExtension(name).toLowerCase();
		Map<String, String> params = Maps.newHashMap();
		params.put("fileName", name);
		params.put("resourceType", "SONG_RESOURCE");
		String rid = null;
		try {
			rid = resourceService.save(gson.toJson(params), is, name, ext);
			System.out.println("--------------rid:" + rid);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!Strings.isNullOrEmpty(rid)) {
			relation = new ResourceSongRelation();
			relation.setSongId(sid);
			relation.setResourceId(rid);
			relation.setIsMain(isMain);
			resourceSongRelationService.save(relation);
			if (!Strings.isNullOrEmpty(oldRid)) {
				MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
				form.add("oldRid", oldRid);
				form.add("newRid", rid);
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
				HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(form,
						headers);
				restTemplate.exchange(ApiClient.UPDATE_TEMPLATE_RESOURCE, HttpMethod.PUT, entity, Map.class);
			}
			return !Strings.isNullOrEmpty(relation.getId());
		}
		return false;
	}

	@Override
	public Resource getMainResource(String sid) {
		ResourceSongRelation relation = resourceSongRelationService.getMainResourceSongBySong(sid);
		return null == relation ? null : relation.getResource();
	}

	@Override
	public List<Map<String, String>> getResourceSongs(String sid) {
		List<ResourceSongRelation> resourceSongRelations = resourceSongRelationService.getResourceSongs(sid);
		if (null == resourceSongRelations || resourceSongRelations.isEmpty()) {
			return null;
		}
		List<Map<String, String>> list = Lists.newArrayList();
		resourceSongRelations.forEach(resourceSongRelation -> {
			Map<String, String> map = Maps.newHashMap();
			map.put("rid", resourceSongRelation.getResourceId());
			if (null != resourceSongRelation.getResource()) {
				map.put("rname", resourceSongRelation.getResource().getName());
				map.put("description", resourceSongRelation.getResource().getDescription());
			}
			list.add(map);
		});
		return list;
	}

	@Override
	public void updateAttachments(String sid, List<InputStream> iss, List<String> names, List<String> exts)
			throws IOException {
		List<ResourceSongRelation> resourceSongRelations = resourceSongRelationService.getResourceSongs(sid);
		if (null != resourceSongRelations && !resourceSongRelations.isEmpty()) {
			resourceSongRelations.forEach(relation -> resourceSongRelationService.delete(relation));
		}
		for (int i = 0; i < iss.size(); i++) {
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", names.get(i));
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), iss.get(i), names.get(i), exts.get(i));
			if (!Strings.isNullOrEmpty(rid)) {
				addResourceSongRelation(sid, rid, false);
			}
		}
	}

	@Override
	public void deleteAttachments(String sid) {
		resourceSongRelationService.deleteBySong(sid);
	}

}
