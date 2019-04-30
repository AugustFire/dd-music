package com.nercl.music.cloud.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.nercl.music.cloud.entity.resource.BookResource;
import com.nercl.music.cloud.entity.resource.Resource;
import com.nercl.music.cloud.service.ResourceService;
import com.nercl.music.constant.CList;
import com.nercl.music.util.PropertiesUtil;

@RestController
@SuppressWarnings("all")
public class ResourceController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ResourceService resourceService;

	@Value("${dd-yinyue.resource}")
	private String resource;

	@Autowired
	private PropertiesUtil propertiesUtil;

	/**
	 * 根据资源Id获取资源
	 */
	@GetMapping(value = "/{rid}", produces = JSON_PRODUCES)
	public Map<String, Object> get(@PathVariable String rid) {
		Map<String, Object> ret = Maps.newHashMap();
		Resource rs = resourceService.getByID(rid);
		if (null == rs) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "resource is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("id", rs.getId());
		ret.put("name", rs.getName());
		ret.put("ext", rs.getExt());
		ret.put("size", rs.getSize());
		if (rs instanceof BookResource) {
			BookResource br = (BookResource) rs;
			ret.put("group_no", br.getGroupNo());
			ret.put("is_help", br.getIsHelp());
			ret.put("structure", String.valueOf(br.getStructure()));
		}
		return ret;
	}

	/**
	 * 根据资源云Id获取资源
	 */
	@GetMapping(value = "/", params = { "cloudId" }, produces = JSON_PRODUCES)
	public Map<String, Object> getByCloudId(String cloudId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cloudId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cloudId is null");
			return ret;
		}
		Resource rs = resourceService.getByCloudId(cloudId);
		if (null == rs) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "resource is null");
			return ret;
		}
		ret.put("id", rs.getId());
		ret.put("cloudId", rs.getCloudId());
		return ret;
	}

	/**
	 * 文件上传
	 * (注意返回的是资源Id,而不是文件ID)
	 * @param json
	 * @param name
	 * @param request
	 * @return 资源id
	 * @throws IOException
	 */
	@PostMapping(value = "/", produces = JSON_PRODUCES)
	public Map<String, Object> upload(String json, String name, HttpServletRequest request) throws IOException {
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
		InputStream in = mfile.getInputStream();
		if (Strings.isNullOrEmpty(name)) {
			name = mfile.getOriginalFilename();
		}
		String ext = Files.getFileExtension(mfile.getOriginalFilename()).toLowerCase();
		String rid = resourceService.save(json, in, name, ext);
		if (Strings.isNullOrEmpty(rid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "save file failed");
		} else {
			ret.put("code", CList.Api.Client.OK);
			ret.put("rid", rid);
		}
		return ret;
	}

	@GetMapping(value = "/{rid}/bytes")
	public ResponseEntity<byte[]> downloadBytes(@PathVariable String rid, HttpServletResponse response) {
		Resource r = resourceService.getByID(rid);
		if (null == r) {
			return null;
		}
		String ext = r.getExt();
		File file = new File(resource + File.separator + r.getCloudId() + "." + ext);
		if (!file.exists()) {
			return null;
		}
		HttpHeaders headers = new HttpHeaders();
		byte[] bytes = null;
		try {
			bytes = Files.toByteArray(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		headers.setContentLength(file.length());
		return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
	}

	@GetMapping(value = "/{rid}/file", produces = JSON_PRODUCES)
	public Map<String, Object> download(@PathVariable String rid, HttpServletResponse response) {
		Map<String, Object> ret = Maps.newHashMap();
		Resource r = resourceService.getByID(rid);
		if (null == r) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "file is null");
			return ret;
		}
		String ext = r.getExt();
		File file = new File(resource + File.separator + r.getCloudId() + "." + ext);
		if (!file.exists()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "file is null");
			return ret;
		}
		OutputStream to = null;
		
		try {
			String fileName =  URLEncoder.encode(r.getName(), "utf-8");
			  response.setContentType(getContentType(ext) + ";charset=UTF-8");
			  response.addHeader("Content-Disposition", "filename="+ fileName);
			response.setContentType("application/force-download");
		} catch (Exception e) {
			   e.printStackTrace();  
		}
		
		try {
			to = response.getOutputStream();
			FileUtils.copyFile(file, to);
			to.flush();
			response.flushBuffer();
			ret.put("code", CList.Api.Client.OK);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (to != null) {
				try {
					to.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	private String getContentType(String ext) {
		return this.propertiesUtil.get(ext);
	}
	
	private String getContentType1(String fileName) {
		return this.propertiesUtil.get(fileName);
	}

	@GetMapping(value = "/fuzzy/{uid}", produces = JSON_PRODUCES)
	public Map<String, Object> fuzzyQueryResources(@PathVariable String uid, String condition) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(condition)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "condition is null");
			return ret;
		}
		List<Resource> resources = resourceService.fuzzyQueryFolderResources(uid,condition);
		ret.put("code", CList.Api.Client.OK); 
		ret.put("resources", resources);
		return ret;
	}

	@GetMapping(value = "/resources", produces = JSON_PRODUCES)
	public Map<String, Object> getResources(@RequestParam("rids") String[] rids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == rids || rids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rids is null");
			return ret;
		}
		List<Resource> resources = Lists.newArrayList();
		Arrays.stream(rids).forEach(rid -> {
			resources.add(resourceService.getByID(rid));
		});
		ret.put("code", CList.Api.Client.OK);
		ret.put("resourceList", resources);
		return ret;
	}
}
