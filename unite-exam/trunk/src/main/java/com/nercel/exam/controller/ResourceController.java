package com.nercel.exam.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.nercel.exam.constant.CList;
import com.nercel.exam.util.StaffUtil;

@RestController
public class ResourceController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Value("${unit-exam.audio}")
	private String audio;

	@Value("${unit-exam.staff}")
	private String staffPath;

	@Value("${unit-exam.data}")
	private String dataPath;

	@Autowired
	private StaffUtil staffUtil;

	@Autowired
	private Gson gson;

	private static final String WAV = "wav";

	private Map<String, Integer> scores = initScores();

	private Map<String, String> notes = initNotes();

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/", produces = JSON_PRODUCES)
	public Map<String, Object> upload(String staff, String gender, HttpServletRequest request) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(staff)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "staff is null");
			return ret;
		}
		if (!(request instanceof MultipartHttpServletRequest)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no file be found");
			return ret;
		}
		MultipartFile mfile = ((MultipartHttpServletRequest) request).getFile("file");
		if (null == mfile) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no file be found");
			return ret;
		}
		InputStream in = mfile.getInputStream();
		String name = mfile.getOriginalFilename();
		if (Strings.isNullOrEmpty(name)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "file is not wav file");
			return ret;
		}
		String ext = Files.getFileExtension(name);
		if (Strings.isNullOrEmpty(ext) || !WAV.equalsIgnoreCase(ext)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "file is not wav file");
			return ret;
		}
		String uuid = UUID.randomUUID().toString().replace("-", "");
		File wav = new File(audio + File.separator + uuid + "-" + staff + "." + WAV);
		FileUtils.copyInputStreamToFile(in, wav);
		File staffFile = new File(staffPath + File.separator + staff + ".xml");
		File json = new File(dataPath + File.separator + uuid + "-" + staff + ".json");
		int result = staffUtil.getLookSingData(staffFile.getPath(), wav.getPath(), "MAN", json.getPath(),
				scores.getOrDefault(staff, 0), notes.getOrDefault(staff, " "));
		System.out.println("--------------result:" + result);
		if (result < 0) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "parse file failed");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		String data = FileUtils.readFileToString(json, "UTF-8");
		Map<String, Object> map = gson.fromJson(data, Map.class);
		ret.put("data", map);
		return ret;
	}

	private Map<String, Integer> initScores() {
		Map<String, Integer> scores = Maps.newHashMap();
		scores.put("1", 75);
		scores.put("2", 75);
		scores.put("3", 75);
		scores.put("4", 80);
		scores.put("5", 80);
		scores.put("6", 80);
		scores.put("7", 75);
		scores.put("8", 75);
		scores.put("9", 75);
		scores.put("10", 80);
		scores.put("11", 80);
		scores.put("12", 80);
		scores.put("13", 75);
		scores.put("14", 75);
		scores.put("15", 75);
		scores.put("16", 80);
		scores.put("17", 80);
		scores.put("18", 80);
		scores.put("19", 75);
		scores.put("20", 75);
		scores.put("21", 75);
		scores.put("22", 80);
		scores.put("23", 80);
		scores.put("24", 80);
		scores.put("25", 75);
		scores.put("26", 75);
		scores.put("27", 75);
		scores.put("28", 80);
		scores.put("29", 85);
		scores.put("30", 80);
		scores.put("31", 75);
		scores.put("32", 80);
		scores.put("33", 75);
		scores.put("34", 80);
		scores.put("35", 80);
		scores.put("36", 80);
		scores.put("37", 75);
		scores.put("38", 75);
		scores.put("39", 75);
		scores.put("40", 80);
		scores.put("41", 85);
		scores.put("42", 80);
		scores.put("43", 75);
		scores.put("44", 80);
		scores.put("45", 75);
		scores.put("46", 80);
		scores.put("47", 85);
		scores.put("48", 85);
		scores.put("49", 75);
		scores.put("50", 75);
		scores.put("51", 80);
		scores.put("52", 80);
		scores.put("53", 80);
		scores.put("54", 80);
		scores.put("55", 75);
		scores.put("56", 80);
		scores.put("57", 75);
		scores.put("58", 80);
		scores.put("59", 85);
		scores.put("60", 80);
		scores.put("61", 75);
		scores.put("62", 80);
		scores.put("63", 75);
		scores.put("64", 80);
		scores.put("65", 85);
		scores.put("66", 80);
		scores.put("67", 75);
		scores.put("68", 75);
		scores.put("69", 75);
		scores.put("70", 80);
		scores.put("71", 85);
		scores.put("72", 80);
		scores.put("73", 75);
		scores.put("74", 80);
		scores.put("75", 75);
		scores.put("76", 80);
		scores.put("77", 85);
		scores.put("78", 80);
		scores.put("79", 75);
		scores.put("80", 75);
		scores.put("81", 75);
		scores.put("82", 80);
		scores.put("83", 85);
		scores.put("84", 80);
		scores.put("85", 75);
		scores.put("86", 75);
		scores.put("87", 75);
		scores.put("88", 80);
		scores.put("89", 85);
		scores.put("90", 80);
		scores.put("91", 75);
		scores.put("92", 75);
		scores.put("93", 80);
		scores.put("94", 80);
		scores.put("95", 85);
		scores.put("96", 80);
		scores.put("97", 75);
		scores.put("98", 80);
		scores.put("99", 75);
		scores.put("100", 80);
		scores.put("101", 85);
		scores.put("102", 80);
		scores.put("103", 75);
		scores.put("104", 75);
		scores.put("105", 75);
		scores.put("106", 80);
		scores.put("107", 85);
		scores.put("108", 80);
		scores.put("109", 75);
		scores.put("110", 75);
		scores.put("111", 75);
		scores.put("112", 80);
		scores.put("113", 85);
		scores.put("114", 80);
		scores.put("115", 75);
		scores.put("116", 75);
		scores.put("117", 80);
		scores.put("118", 80);
		scores.put("119", 85);
		scores.put("120", 80);
		scores.put("121", 75);
		scores.put("122", 80);
		scores.put("123", 75);
		scores.put("124", 80);
		scores.put("125", 85);
		scores.put("126", 80);
		scores.put("127", 75);
		scores.put("128", 80);
		scores.put("129", 75);
		scores.put("130", 80);
		scores.put("131", 80);
		scores.put("132", 80);
		scores.put("133", 75);
		scores.put("134", 75);
		scores.put("135", 75);
		scores.put("136", 80);
		scores.put("137", 85);
		scores.put("138", 80);
		scores.put("139", 75);
		scores.put("140", 75);
		scores.put("141", 75);
		scores.put("142", 80);
		scores.put("143", 85);
		scores.put("144", 80);
		scores.put("145", 75);
		scores.put("146", 80);
		scores.put("147", 75);
		scores.put("148", 80);
		scores.put("149", 85);
		scores.put("150", 80);
		return scores;
	}

	private Map<String, String> initNotes() {
		Map<String, String> notes = Maps.newHashMap();
		notes.put("1", "Eb6");
		notes.put("2", "D#5,A#5");
		notes.put("3", " ");
		notes.put("4", "G#5,C#5");
		notes.put("5", "C#5");
		notes.put("6", " ");
		notes.put("7", "C#5,D#5,F5");
		notes.put("8", "D#5,A#4,A#5");
		notes.put("9", " ");
		notes.put("10", "B5");
		notes.put("11", "C#5");
		notes.put("12", " ");
		notes.put("13", "C#6,Eb6,F5,D#5");
		notes.put("14", "D#5");
		notes.put("15", " ");
		notes.put("16", "F#5,C#5");
		notes.put("17", "C#5,C#6");
		notes.put("18", "C#5");
		notes.put("19", " ");
		notes.put("20", "A#5,D#5");
		notes.put("21", " ");
		notes.put("22", "B5");
		notes.put("23", "G#5,C#5");
		notes.put("24", " ");
		notes.put("25", "G#5,A#5");
		notes.put("26", "D#5");
		notes.put("27", " ");
		notes.put("28", " ");
		notes.put("29", "G#5");
		notes.put("30", " ");
		notes.put("31", "C#6");
		notes.put("32", " ");
		notes.put("33", " ");
		notes.put("34", "F#5,G#5,B4,B5");
		notes.put("35", "C#5,C#6");
		notes.put("36", "G#5");
		notes.put("37", "C#6,Eb6,D#6");
		notes.put("38", "D#5,G#5");
		notes.put("39", " ");
		notes.put("40", " ");
		notes.put("41", " ");
		notes.put("42", " ");
		notes.put("43", "Bb5,C#6");
		notes.put("44", " ");
		notes.put("45", " ");
		notes.put("46", "G#5,B5");
		notes.put("47", " ");
		notes.put("48", " ");
		notes.put("49", " ");
		notes.put("50", "D#5,D#6");
		notes.put("51", " ");
		notes.put("52", "B4");
		notes.put("53", "C#5");
		notes.put("54", " ");
		notes.put("55", "G#5");
		notes.put("56", "A#5");
		notes.put("57", " ");
		notes.put("58", "Ab5,G#5");
		notes.put("59", " ");
		notes.put("60", " ");
		notes.put("61", " ");
		notes.put("62", " ");
		notes.put("63", "G#5");
		notes.put("64", "B5,C#6");
		notes.put("65", " ");
		notes.put("66", " ");
		notes.put("67", "C#5,C#6");
		notes.put("68", "D#5,A#5,G#5");
		notes.put("69", " ");
		notes.put("70", "B5,C#5");
		notes.put("71", " ");
		notes.put("72", " ");
		notes.put("73", "C#5");
		notes.put("74", "D6");
		notes.put("75", " ");
		notes.put("76", "G#5");
		notes.put("77", "G#5,C6");
		notes.put("78", " ");
		notes.put("79", "E#5,G#5,C#6");
		notes.put("80", "G#5");
		notes.put("81", " ");
		notes.put("82", "B5");
		notes.put("83", "G#5");
		notes.put("84", " ");
		notes.put("85", "C#6,A#5");
		notes.put("86", "D#5");
		notes.put("87", " ");
		notes.put("88", "B5,Db6");
		notes.put("89", "C#5,G#5");
		notes.put("90", " ");
		notes.put("91", " ");
		notes.put("92", "A#5");
		notes.put("93", "G#5");
		notes.put("94", " ");
		notes.put("95", "C6,G#5");
		notes.put("96", "C#5");
		notes.put("97", "G#5,C#6,Bb5");
		notes.put("98", "D6,E#5,G#5");
		notes.put("99", " ");
		notes.put("100", "B5");
		notes.put("101", "G#5,F#4");
		notes.put("102", " ");
		notes.put("103", "Eb5,A#4,D#5,C#6,Ab5");
		notes.put("104", "D#6,D#5");
		notes.put("105", "D#5,G#5,C#5");
		notes.put("106", "C#5");
		notes.put("107", "G#5,F#5");
		notes.put("108", "C#5");
		notes.put("109", "A#5,G#5");
		notes.put("110", "D#5");
		notes.put("111", " ");
		notes.put("112", "Db5");
		notes.put("113", " ");
		notes.put("114", "F#5,C#5");
		notes.put("115", "G#5,Eb6");
		notes.put("116", "D#5,A#5");
		notes.put("117", " ");
		notes.put("118", "G#5,B5");
		notes.put("119", "D#6");
		notes.put("120", " ");
		notes.put("121", "A#4,D#5,Eb5,D#6,Bb5");
		notes.put("122", "D5,A#6");
		notes.put("123", "D#5");
		notes.put("124", "C#5");
		notes.put("125", " ");
		notes.put("126", " ");
		notes.put("127", "C#6");
		notes.put("128", "A#5,G#5");
		notes.put("129", " ");
		notes.put("130", "B5,Gb5");
		notes.put("131", "C#5,F#5,G#5");
		notes.put("132", "C#5");
		notes.put("133", "D#5");
		notes.put("134", "A#5,D#5");
		notes.put("135", " ");
		notes.put("136", "B5");
		notes.put("137", " ");
		notes.put("138", "F#5");
		notes.put("139", " ");
		notes.put("140", "D#6");
		notes.put("141", " ");
		notes.put("142", "B5");
		notes.put("143", "C6,G#5");
		notes.put("144", "C#5");
		notes.put("145", "C#5,C#6");
		notes.put("146", "A#5,D6");
		notes.put("147", " ");
		notes.put("148", "Db6,B5");
		notes.put("149", " ");
		notes.put("150", "C#5,C#6,F#5");
		return notes;
	}

}
