package com.nercl.music.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.question.MFile;
import com.nercl.music.service.MFileService;
import com.nercl.music.util.PropertiesUtil;

@Controller
public class MFileController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private MFileService mfileService;

	@Autowired
	private PropertiesUtil propertiesUtil;

	@Value("${music_exercise.question.xml}")
	private String xmlPath;

	@Value("${music_exercise.question.audio}")
	private String audioPath;

	@Value("${music_exercise.question.img}")
	private String imgPath;

	private static final String XMl_EXT = "xml";

	private static final String NMl_EXT = "nml";

	private static final String WAV_EXT = "wav";

	private static final String MID_EXT = "mid";

	private static final String MIDI_EXT = "midi";

	@PostMapping(value = "/file", produces = JSON_PRODUCES)
	@ResponseBody
	public Map<String, Object> upload(HttpServletRequest request) {
		Map<String, Object> ret = Maps.newHashMap();
		InputStream in = null;
		String uuid = UUID.randomUUID().toString();
		try {
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
				MultipartFile file = mreq.getFile("file");
				if (file != null) {
					in = file.getInputStream();
					String filename = file.getOriginalFilename();
					String ext = Files.getFileExtension(filename);
					String name = uuid + "." + ext;
					File f = new File(this.getPath(ext) + File.separator + uuid + "." + ext);
					FileUtils.copyInputStreamToFile(in, f);
					boolean success = this.mfileService.save(filename, name, uuid, ext, f.getPath(), null);
					if (success) {
						ret.put("code", CList.Api.Client.OK);
						ret.put("path", uuid);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "上传文件出错");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
		return ret;
	}

	@GetMapping(value = "/file/{uuid}", produces = JSON_PRODUCES)
	public void download(@PathVariable String uuid, HttpServletResponse response) {
		MFile mfile = this.mfileService.get(uuid);
		if (null == mfile) {
			return;
		}
		String ext = Files.getFileExtension(mfile.getName());
		String contentType = this.getContentType(ext);
		response.setContentType(contentType + ";charset=UTF-8");
		response.setHeader("Content-disposition", "filename=" + mfile.getName());
		File file = new File(this.getPath(ext) + File.separator + mfile.getName());
		OutputStream to = null;
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
	}

	private String getContentType(String ext) {
		return this.propertiesUtil.get(ext);
	}

	private String getPath(String ext) {
		if (XMl_EXT.equalsIgnoreCase(ext) || NMl_EXT.equalsIgnoreCase(ext)) {
			return this.xmlPath;
		} else if (WAV_EXT.equalsIgnoreCase(ext) || MID_EXT.equalsIgnoreCase(ext) || MIDI_EXT.equalsIgnoreCase(ext)) {
			return this.audioPath;
		} else {
			return this.imgPath;
		}
	}

}
