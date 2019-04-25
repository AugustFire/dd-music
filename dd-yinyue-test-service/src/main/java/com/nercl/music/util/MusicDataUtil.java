package com.nercl.music.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;

@Component
public class MusicDataUtil {

	@Value("${dd-yinyue.looksing.data.table}")
	private String looksingDataTablePath;

	@Value("${dd-yinyue.looksing.data.curve}")
	private String looksingDataCurvePath;

	public String getSourceAndVoiceData(String recordId) {
		String path = this.looksingDataTablePath + File.separator + recordId + ".json";
		String content = "";
		try {
			content = Files.toString(new File(path), Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public String getCurveData(String recordId) {
		String path = this.looksingDataCurvePath + File.separator + recordId + ".json";
		String content = "";
		try {
			content = Files.toString(new File(path), Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
}
