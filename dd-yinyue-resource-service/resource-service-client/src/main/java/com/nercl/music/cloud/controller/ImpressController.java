package com.nercl.music.cloud.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.google.gson.Gson;
import com.nercl.music.cloud.entity.resource.Dimension;
import com.nercl.music.cloud.service.ImpressService;
import com.nercl.music.constant.CList;

@RestController
public class ImpressController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ImpressService impressService;

	@Autowired
	private Gson gson;

	@GetMapping(value = "/song/impress", produces = JSON_PRODUCES)
	public Map<String, Object> getImpress(String summary, String dimension) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(summary)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "summary is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(dimension)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "dimension is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		List<Map<String, Object>> res = impressService.getImpress(summary, Dimension.valueOf(dimension));
		if (null == res) {
			return ret;
		}
		ret.put("impress", res);
		return ret;
	}

	@GetMapping(value = "/song/impress/{id}", produces = JSON_PRODUCES)
	public Map<String, Object> getImpress(@PathVariable String id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		Map<String, Object> impress = impressService.getImpress(id);
		if (null != impress) {
			ret.put("impress", impress);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/song/impress", produces = JSON_PRODUCES)
	public Map<String, Object> add(String sid, String json, HttpServletRequest request) throws IOException {
		System.out.println("------------:"+json);
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(json)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "json is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(sid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "sid is null");
			return ret;
		}
		InputStream in = null;
		String filename = null;
		String ext = null;
		if (request instanceof MultipartHttpServletRequest) {
			MultipartFile mfile = ((MultipartHttpServletRequest) request).getFile("file");
			if (null != mfile) {
				in = mfile.getInputStream();
				filename = URLDecoder.decode(mfile.getOriginalFilename(), "UTF-8");
				ext = Files.getFileExtension(filename);
				if (Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
					ret.put("code", CList.Api.Client.PROCESSING_FAILED);
					ret.put("desc", "file is error");
					return ret;
				}
				ext = ext.toLowerCase();
			}
		}
		boolean success = impressService.add(sid, gson.fromJson(json, Map.class), in, filename, ext);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "add impress failed");
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/song/impress/{iid}", produces = JSON_PRODUCES)
	public Map<String, Object> update(@PathVariable String iid, String json, HttpServletRequest request)
			throws IOException {
		System.out.println("+++++++++++++:"+json);
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(iid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "iid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(json)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "json is null");
			return ret;
		}
		InputStream in = null;
		String filename = null;
		String ext = null;
		if (request instanceof MultipartHttpServletRequest) {
			MultipartFile mfile = ((MultipartHttpServletRequest) request).getFile("file");
			if (null != mfile) {
				in = mfile.getInputStream();
				filename = URLDecoder.decode(mfile.getOriginalFilename(), "UTF-8");
				ext = Files.getFileExtension(filename);
				if (Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
					ret.put("code", CList.Api.Client.PROCESSING_FAILED);
					ret.put("desc", "file is error");
					return ret;
				}
				ext = ext.toLowerCase();
			}
		}
		impressService.update(iid, gson.fromJson(json, Map.class), in, filename, ext);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	@DeleteMapping(value = "/song/impress/{iid}", produces = JSON_PRODUCES)
	public Map<String, Object> delete(@PathVariable String iid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(iid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "iid is null");
			return ret;
		}
		boolean success = impressService.delete(iid);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "delete failed");
		}
		return ret;
	}

}
