package com.nercl.music.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MusicDataUtil {

	@Value("${exam_music.looksing.data.table}")
	private String looksingDataTablePath;

	@Value("${exam_music.looksing.data.curve}")
	private String looksingDataCurvePath;

	public String getSourceAndVoiceData(String recordId) {
		String path = this.looksingDataTablePath + File.separator + recordId + ".json";
		return readFileToString(path);
	}

	public String getCurveData(String recordId) {
		String path = this.looksingDataCurvePath + File.separator + recordId + ".json";
		return readFileToString(path);
	}

	private String readFileToString(String path) {
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fileInputStream = new FileInputStream(path);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
}
