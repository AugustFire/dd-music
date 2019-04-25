package com.nercl.music.cloud.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.nercl.music.cloud.entity.resource.Resource;
import com.nercl.music.cloud.entity.song.Song;
import com.nercl.music.cloud.service.ImpressService;
import com.nercl.music.cloud.service.SongService;
import com.nercl.music.constant.CList;
import com.nercl.music.util.ZipFileUtil;

@RestController
public class SongController {

	@Autowired
	private SongService songService;

	@Autowired
	private ZipFileUtil zipFileUtil;

	@Value("${dd-yinyue.zip}")
	private String zipFilePath;

	@Autowired
	private SongParser songParser;

	@Autowired
	private ImpressService impressService;

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@GetMapping(value = "/song/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> getById(@PathVariable String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		Song song = songService.get(sid);
		if (null == song) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "not found song");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("id", sid);
		ret.put("name", song.getName());
		Resource resource = songService.getMainResource(sid);
		if (null != resource) {
			ret.put("rid", resource.getId());
			ret.put("rname", resource.getName());
		}
		ret.put("impresses", impressService.getBySong(sid));
		ret.put("attachments", songService.getResourceSongs(sid));
		return ret;
	}

	@GetMapping(value = "/songs", produces = JSON_PRODUCES)
	public Map<String, Object> getByIds(String[] sids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == sids || sids.length <= 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		List<Map<String, String>> songs = songService.get(sids);
		ret.put("code", CList.Api.Client.OK);
		ret.put("songs", songs);
		return ret;
	}

	@GetMapping(value = "/song/songs", produces = JSON_PRODUCES)
	public Map<String, Object> get(String name) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(name)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "name is null");
			return ret;
		}
		List<Map<String, Object>> songs = songService.getByLikeName(name);
		ret.put("code", CList.Api.Client.OK);
		ret.put("songs", songs);
		System.out.println("---------ret：" + ret);
		return ret;
	}

	@PostMapping(value = "/song", produces = JSON_PRODUCES)
	public Map<String, Object> add(HttpServletRequest request) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (!(request instanceof MultipartHttpServletRequest)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no file found");
			return ret;
		}
		MultipartFile mfile = ((MultipartHttpServletRequest) request).getFile("file");
		if (null == mfile) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no file found");
			return ret;
		}
		String uuid = UUID.randomUUID().toString();
		InputStream in = mfile.getInputStream();
		String filename = mfile.getOriginalFilename();
		String ext = Files.getFileExtension(filename);
		new File(zipFilePath).mkdir();
		File zipFile = new File(zipFilePath + File.separator + uuid + "." + ext);
		FileUtils.copyInputStreamToFile(in, zipFile);
		zipFileUtil.unzip(zipFile, zipFilePath);
		String path = zipFilePath + File.separator + uuid;
		songParser.parse(path);
		zipFile.delete();
		new File(path).delete();
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@PostMapping(value = "/song/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> update(@PathVariable String sid, String name, HttpServletRequest request)
			throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(name)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "name is null");
			return ret;
		}
		boolean success = songService.update(sid, name);
		if (!success) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "update name failed");
			return ret;
		}
		if (!(request instanceof MultipartHttpServletRequest)) {
			ret.put("code", CList.Api.Client.OK);
			return ret;
		}
		MultipartFile mfile = ((MultipartHttpServletRequest) request).getFile("file");
		if (null == mfile) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no file be found");
			return ret;
		}
		InputStream in = mfile.getInputStream();
		String fname = URLDecoder.decode(mfile.getOriginalFilename(), "UTF-8");
		if (Strings.isNullOrEmpty(fname)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no file be found");
			return ret;
		}
		success = songService.addResourceSongRelation(fname, sid, in, true);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			return ret;
		}
		ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		ret.put("desc", "update song failed");
		return ret;
	}

	@DeleteMapping(value = "/song/{sid}", produces = JSON_PRODUCES)
	public Map<String, Object> delete(@PathVariable String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		boolean success = songService.delete(sid);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "delete failed");
		}
		return ret;
	}

	@GetMapping(value = "/song/{sid}/attachments", produces = JSON_PRODUCES)
	public Map<String, Object> getAttachments(@PathVariable String sid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		List<Map<String, String>> attachments = songService.getResourceSongs(sid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("attachments", attachments);
		return ret;
	}
}
