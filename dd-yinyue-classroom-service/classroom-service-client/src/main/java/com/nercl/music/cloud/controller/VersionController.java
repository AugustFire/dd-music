package com.nercl.music.cloud.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.util.PropertiesUtil;

@RestController
public class VersionController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Value("${dd-yinyue.version}")
	private String version;

	@Autowired
	private PropertiesUtil propertiesUtil;

	private static final String XML_EXT = "xml";

	private static final String XML_FILE = "update.xml";

	private static final String ZIP_EXT = "zip";

	private static final String ZIP_FILE = "update.zip";

	@GetMapping(value = "/version/xml", produces = JSON_PRODUCES)
	public Map<String, Object> getXml(HttpServletResponse response) {
		Map<String, Object> ret = Maps.newHashMap();
		File file = new File(version + File.separator + XML_FILE);
		if (!file.exists()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "file is null");
			return ret;
		}
		OutputStream to = null;
		response.setContentType(getContentType(XML_EXT) + ";charset=UTF-8");
		response.addHeader("Content-Disposition", "filename=" + XML_FILE);
		response.addHeader("Content-Length", String.valueOf(file.length()));
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

	@GetMapping(value = "/version/zip", produces = JSON_PRODUCES)
	public Map<String, Object> getZip(HttpServletResponse response) {
		Map<String, Object> ret = Maps.newHashMap();
		File file = new File(version + File.separator + ZIP_FILE);
		if (!file.exists()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "file is null");
			return ret;
		}
		OutputStream to = null;
		response.setContentType(getContentType(ZIP_EXT) + ";charset=UTF-8");
		response.addHeader("Content-Disposition", "filename=" + "update_" + String.valueOf(file.length()) + ".zip");
		response.addHeader("Content-Length", String.valueOf(file.length()));
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

}
