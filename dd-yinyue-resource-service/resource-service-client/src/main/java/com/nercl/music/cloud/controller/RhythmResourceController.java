package com.nercl.music.cloud.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.util.PropertiesUtil;

@RestController
public class RhythmResourceController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	private static final String DRUM_EXT = "drum";

	@Value("${dd-yinyue.rhythm}")
	private String rhythm;

	@Autowired
	private PropertiesUtil propertiesUtil;

	@GetMapping(value = "/rhythm", produces = JSON_PRODUCES)
	public Map<String, Object> get(String name, HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(name)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "name is null");
			return ret;
		}
		File file = new File(rhythm + File.separator + name + "." + DRUM_EXT);
		if (!file.exists()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "rhythm file not be found");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		OutputStream to = null;
		response.setContentType(propertiesUtil.get(DRUM_EXT) + ";charset=UTF-8");
		response.addHeader("Content-Disposition", "filename=" + URLEncoder.encode(name, "UTF-8") + "." + DRUM_EXT);
		try {
			to = response.getOutputStream();
			FileUtils.copyFile(file, to);
			to.flush();
			response.flushBuffer();
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
		ret.put("name", name);
		return ret;
	}

}
