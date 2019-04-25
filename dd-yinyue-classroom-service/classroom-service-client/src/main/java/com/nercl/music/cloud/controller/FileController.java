package com.nercl.music.cloud.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.nercl.music.cloud.entity.classroom.ClassRoom;
import com.nercl.music.cloud.service.ClassRoomService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;
import com.nercl.music.util.CloudFileUtil;
import com.nercl.music.util.PropertiesUtil;
import com.nercl.music.websocket.WebSocketSessionManager;

@RestController
public class FileController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ClassRoomService classRoomService;

	@Autowired
	private WebSocketSessionManager sessionManager;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private CloudFileUtil cloudFileUtil;

	@Autowired
	private PropertiesUtil propertiesUtil;

	@Value("${dd-yinyue-temp_file}")
	private String tempFile;

	@PostMapping(value = "/upload_file2", produces = JSON_PRODUCES)
	public Map<String, Object> uploadFile2(HttpServletRequest request) {
		Map<String, Object> ret = Maps.newHashMap();

		InputStream in = null;
		try {
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
				MultipartFile mfile = mreq.getFile("file");
				if (null != mfile) {
					in = mfile.getInputStream();
					String originalName = mfile.getOriginalFilename();
					String ext = Files.getFileExtension(originalName);
					if (StringUtils.isBlank(originalName) || StringUtils.isBlank(ext)) {
						ret.put("code", CList.Api.Client.PROCESSING_FAILED);
						ret.put("desc", "file is null");
						return ret;
					}

					String json = "{'resourceType':'普通资源','fileName':'" + originalName + "'}";
					File file = new File(tempFile + File.separator + UUID.randomUUID().toString() + "." + ext);
					FileUtils.copyInputStreamToFile(in, file);
					String tfileId = cloudFileUtil.upload(file, json);
					if (!Strings.isNullOrEmpty(tfileId)) {
						ret.put("code", CList.Api.Client.OK);
						ret.put("file_id", tfileId);
					} else {
						ret.put("code", CList.Api.Client.PROCESSING_FAILED);
					}
				}
			} else {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "file is null");
				return ret;
			}
		} catch (IOException e) {
			e.printStackTrace();
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "upload file failed");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/upload_file", produces = JSON_PRODUCES)
	public Map<String, Object> uploadFile(String rid, String sender_id, String[] receiver_ids, String messageType,
			HttpServletRequest request) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(rid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "roomId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(sender_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "senderId is null");
			return ret;
		}
		ClassRoom classRoom = classRoomService.get(rid);
		if (null == classRoom) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classRoom is null");
			return ret;
		}
		String senderName = "";
		Map<String, Object> user = restTemplate.getForObject(ApiClient.GET_USER, Map.class, sender_id);
		if (null != user) {
			Object person = user.get("person");
			if (null != person) {
				Map<String, Object> map = (Map<String, Object>) person;
				senderName = String.valueOf(map.getOrDefault("name", ""));
			}
		}

		InputStream in = null;
		try {
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
				MultipartFile mfile = mreq.getFile("file");
				if (mfile != null) {
					in = mfile.getInputStream();
					String originalName = mfile.getOriginalFilename();
					String ext = Files.getFileExtension(originalName);
					Long size = mfile.getSize();
					if (StringUtils.isBlank(originalName) || StringUtils.isBlank(ext)) {
						ret.put("code", CList.Api.Client.PROCESSING_FAILED);
						ret.put("desc", "file is null");
						return ret;
					}
					String json = "{'resourceType':'普通资源','fileName':'" + originalName + "'}";
					File file = new File(tempFile + File.separator + UUID.randomUUID().toString() + "." + ext);
					FileUtils.copyInputStreamToFile(in, file);
					String tfileId = cloudFileUtil.upload(file, json);
					if (!Strings.isNullOrEmpty(tfileId)) {
						ret.put("code", CList.Api.Client.OK);
						ret.put("file_id", tfileId);
						sessionManager.sendDownLoadFileNotice(classRoom.getCode(), tfileId, sender_id, senderName,
								receiver_ids, originalName, size, messageType);
					} else {
						ret.put("code", CList.Api.Client.PROCESSING_FAILED);
					}
				}
			} else {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "file is null");
				return ret;
			}
		} catch (IOException e) {
			e.printStackTrace();
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "upload file failed");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	@GetMapping(value = "/download_file")
	public Map<String, Object> downloadFile(String file_id, HttpServletResponse response) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(file_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "file_id is null");
			return ret;
		}
		Map<String, Object> tfile = cloudFileUtil.getResource(file_id);
		if (null == tfile || tfile.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "file is null");
			return ret;
		}
		String ext = (String) tfile.getOrDefault("ext", "");
		String name = (String) tfile.getOrDefault("name", "");
		String contentType = this.getContentType(ext);
		if (Strings.isNullOrEmpty(contentType)) {
			contentType = "application/octet-stream";
		}
		InputStream is = cloudFileUtil.download(file_id, ext);
		response.setContentType(contentType + ";charset=UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + name);
		OutputStream to = null;
		try {
			to = response.getOutputStream();
			IOUtils.copy(is, to);
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

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/question_translation", produces = JSON_PRODUCES)
	public Map<String, Object> questionTranslateQuestion(String rid, String sender_id, String[] receiver_ids,
			String[] qids, String messageType, HttpServletResponse response) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(rid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "roomId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(sender_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "senderId is null");
			return ret;
		}
		if (null == qids || qids.length == 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "qids is null");
			return ret;
		}
		ClassRoom classRoom = classRoomService.get(rid);
		if (null == classRoom) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classRoom is null");
			return ret;
		}

		String senderName = "";
		Map<String, Object> user = restTemplate.getForObject(ApiClient.GET_USER, Map.class, sender_id);
		if (null != user) {
			Object person = user.get("person");
			if (null != person) {
				Map<String, Object> map = (Map<String, Object>) person;
				senderName = String.valueOf(map.getOrDefault("name", ""));
			}
		}
		String timestamp = String.valueOf(System.currentTimeMillis());
		sessionManager.sendDownLoadQuestionNotice(classRoom.getCode(), qids, sender_id, senderName, receiver_ids,
				messageType, timestamp);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	private String getContentType(String ext) {
		return this.propertiesUtil.get(ext);
	}

}
