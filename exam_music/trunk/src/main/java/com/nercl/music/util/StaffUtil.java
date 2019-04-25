package com.nercl.music.util;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.google.common.io.Files;

@Component
public class StaffUtil {

	private static final String PIC_EXE_PATH = File.separator + "musicSatff" + File.separator + "MusicStaff.exe";

	private static final String SHORT_ANSWR_SCORE_EXE_PATH = File.separator + "musicShortAnswerScore" + File.separator
	        + "ShortAnswerQScore.exe";

	private static final String LOOK_SING_SCORE_EXE_PATH = File.separator + "musicLookSingScore" + File.separator
	        + "SightSingingScore.exe";

	@Value("${exam_music.satff.img.path}")
	private String satffImgPath;

	@Value("${exam_music.looksing.data.table}")
	private String looksingDataTablePath;

	@Value("${exam_music.looksing.data.curve}")
	private String looksingDataCurvePath;

	@Value("${exam_music.looksing.data.exerciser.table}")
	private String exerciserLooksingDataTablePath;

	@Value("${exam_music.looksing.data.exerciser.curve}")
	private String exerciserLooksingDataCurvePath;

	@Value("${exam_music.examinee.photo}")
	private String examineePhotoPath;

	@Value("${exam_music.c_sharp_exe}")
	private String cSharpExePath;

	@PostConstruct
	public void init() throws Exception {

	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	public int createStaffPic(File xmlFile) {
		String imgFilePath = null;
		String uuid = Files.getNameWithoutExtension(xmlFile.getName());
		if (StringUtils.isNotBlank(uuid)) {
			imgFilePath = this.satffImgPath + File.separator + uuid + ".png";
		}
		int result = 0;
		Process process = null;
		File exeFile = null;
		try {
			exeFile = ResourceUtils.getFile(cSharpExePath + PIC_EXE_PATH);
			process = Runtime.getRuntime().exec(new String[] { exeFile.getPath(), xmlFile.getPath(), imgFilePath });
			process.waitFor();
			result = process.exitValue();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			process.destroy();
		}
		return result;
	}

	public int getStaffPic(String xmlPath, String imgPath) {
		int result = 0;
		Process process = null;
		File exeFile = null;
		try {
			exeFile = ResourceUtils.getFile(cSharpExePath + PIC_EXE_PATH);
			process = Runtime.getRuntime().exec(new String[] { exeFile.getPath(), xmlPath, imgPath });
			process.waitFor();
			result = process.exitValue();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != process) {
				process.destroy();
			}
		}
		return result;
	}

	public int getShortAnswerScore(String examineeAnswerPath, String standarAnswerPath, Integer examField) {
		int result = 0;
		Process process = null;
		File exeFile = null;
		try {
			exeFile = ResourceUtils.getFile(cSharpExePath + SHORT_ANSWR_SCORE_EXE_PATH);
			process = Runtime.getRuntime().exec(new String[] { exeFile.getPath(), standarAnswerPath, examineeAnswerPath,
			        String.valueOf(examField) });
			process.waitFor();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			result = process.exitValue();
			System.out.println("result:" + result);
			result = result < 0 ? 0 : result > 100 ? 100 : result;
			process.destroy();
		}
		return result;
	}

	public int getLookSingData(String recordId, String standarAnswerPath, String audioPath) {
		int result = 0;
		Process process = null;
		File exeFile = null;
		try {
			String curveFilePath = this.looksingDataCurvePath + File.separator + recordId + ".json";
			String tableFilePath = this.looksingDataTablePath + File.separator + recordId + ".json";
			boolean hasCurveFile = true;
			File curveFile = new File(curveFilePath);
			if (!curveFile.exists() || curveFile.length() <= 0) {
				Files.createParentDirs(curveFile);
				curveFile.createNewFile();
				hasCurveFile = false;
			}

			File tableFile = new File(tableFilePath);
			if (!tableFile.exists() || tableFile.length() <= 0) {
				Files.createParentDirs(tableFile);
				tableFile.createNewFile();
				hasCurveFile = false;
			}
			if (!hasCurveFile) {
				exeFile = ResourceUtils.getFile(cSharpExePath + LOOK_SING_SCORE_EXE_PATH);
				process = Runtime.getRuntime().exec(
				        new String[] { exeFile.getPath(), standarAnswerPath, audioPath, tableFilePath, curveFilePath });
				process.waitFor();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			if (result == -1) {
				return result;
			}
		} finally {
			if (null != process) {
				result = process.exitValue();
				result = result < 0 ? 0 : result > 100 ? 100 : result;
				process.destroy();
			} else {
				result = 100;
			}
		}
		return result;
	}

	public int getExerciserLookSingData(String recordId, String standarAnswerPath, String audioPath) {
		int result = 0;
		Process process = null;
		File exeFile = null;
		try {
			String curveFilePath = this.exerciserLooksingDataCurvePath + File.separator + recordId + ".json";
			String tableFilePath = this.exerciserLooksingDataTablePath + File.separator + recordId + ".json";
			boolean hasCurveFile = true;
			File curveFile = new File(curveFilePath);
			if (!curveFile.exists() || curveFile.length() <= 0) {
				Files.createParentDirs(curveFile);
				curveFile.createNewFile();
				hasCurveFile = false;
			}

			File tableFile = new File(tableFilePath);
			if (!tableFile.exists() || tableFile.length() <= 0) {
				Files.createParentDirs(tableFile);
				tableFile.createNewFile();
				hasCurveFile = false;
			}
			if (!hasCurveFile) {
				exeFile = ResourceUtils.getFile(cSharpExePath + LOOK_SING_SCORE_EXE_PATH);
				process = Runtime.getRuntime().exec(
				        new String[] { exeFile.getPath(), standarAnswerPath, audioPath, tableFilePath, curveFilePath });
				process.waitFor();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			if (result == -1) {
				return result;
			}
		} finally {
			if (null != process) {
				result = process.exitValue();
				result = result < 0 ? 0 : result > 100 ? 100 : result;
				process.destroy();
			} else {
				result = 100;
			}
		}
		return result;
	}

}
