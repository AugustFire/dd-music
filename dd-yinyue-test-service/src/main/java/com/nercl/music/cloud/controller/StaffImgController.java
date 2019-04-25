package com.nercl.music.cloud.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nercl.music.util.CloudFileUtil;
import com.nercl.music.util.StaffUtil;

@RestController
public class StaffImgController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private CloudFileUtil cloudFileUtil;

	@Autowired
	private StaffUtil staffUtil;

	@Value("${dd-yinyue.temp.answer}")
	private String tempAnswer;

	@Value("${dd-yinyue.staff.img}")
	private String img;

	@Value("${dd-yinyue.creating.data}")
	private String creating;

	@GetMapping(value = "/staff_img", params = { "rid" }, produces = JSON_PRODUCES)
	public void getStaffImg(@RequestParam(value = "rid") String rid, HttpServletResponse resp) {
		String imgFilePath = img + File.separator + rid + ".png";
		File imgFile = new File(imgFilePath);
		if (imgFile.exists()) {
			setStaffPicToResponse(resp, imgFile);
		} else {
			createImg(rid, imgFile);
			if (!imgFile.exists()) {
				return;
			}
			setStaffPicToResponse(resp, imgFile);
		}
	}

	@GetMapping(value = "/mp3", params = { "rid" }, produces = JSON_PRODUCES)
	public void getMp3(@RequestParam(value = "rid") String rid, HttpServletResponse resp) {
		String mp3Path = creating + File.separator + rid + ".mp3";
		File mp3 = new File(mp3Path);
		if (mp3.exists()) {
			download(mp3, rid + ".mp3", resp);
		} else {
			createMp3(rid, mp3);
			if (!mp3.exists()) {
				return;
			}
			download(mp3, rid + ".mp3", resp);
		}
	}

	private void download(File file, String name, HttpServletResponse response) {
		if (null == file || !file.exists()) {
			return;
		}
		OutputStream to = null;
		try {
			response.setContentType("application/zip;charset=UTF-8");
			response.setHeader("Content-disposition", "filename=" + name);
			response.setContentLengthLong(file.length());
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

	private void createMp3(String rid, File mp3File) {
		Map<String, Object> ret = cloudFileUtil.getResource(rid);
		if (null == ret || null == ret.get("ext")) {
			return;
		}
		String ext = (String) ret.get("ext");
		if (!"staff".equalsIgnoreCase(ext) && !"xml".equalsIgnoreCase(ext) && !"drum".equalsIgnoreCase(ext)) {
			return;
		}
		InputStream is = cloudFileUtil.download(rid, ext);
		if (null == is) {
			return;
		}
		File tempAnswerFile = new File(tempAnswer + File.separator + rid + "." + ext);
		try {
			FileUtils.copyInputStreamToFile(is, tempAnswerFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!tempAnswerFile.exists()) {
			return;
		}
		staffUtil.getCreatingAudio(tempAnswerFile.getPath(), mp3File.getPath());
	}

	private void createImg(String rid, File imgFile) {
		Map<String, Object> ret = cloudFileUtil.getResource(rid);
		if (null == ret || null == ret.get("ext")) {
			return;
		}
		String ext = (String) ret.get("ext");
		InputStream is = cloudFileUtil.download(rid, ext);
		if (null == is) {
			return;
		}
		File tempAnswerFile = new File(tempAnswer + File.separator + rid + "." + ext);
		try {
			FileUtils.copyInputStreamToFile(is, tempAnswerFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!tempAnswerFile.exists()) {
			return;
		}
		int result = staffUtil.getStaffPic(tempAnswerFile.getPath(), imgFile.getPath());
		System.out.println("-----------result:" + result);
	}

	private void setStaffPicToResponse(HttpServletResponse resp, File file) {
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);
		resp.setContentType("image/png");
		ServletOutputStream sos = null;
		FileInputStream is = null;
		BufferedImage image = null;
		try {
			sos = resp.getOutputStream();
			is = new FileInputStream(file);
			image = ImageIO.read(is);
			ImageIO.write(image, "png", sos);
			image.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != sos) {
				try {
					sos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
