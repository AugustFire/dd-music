package com.nercl.music.util;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class StaffUtil {

	private static final String PIC_EXE_PATH = File.separator + "MusicStaff" + File.separator + "MusicStaff.exe";

	private static final String SHORT_ANSWR_SCORE_EXE_PATH = File.separator + "shortAnswerScore" + File.separator
			+ "DDMusic.ShortAnswerScore.exe";

	private static final String ACT_SCORE_EXE_PATH = File.separator + "PerformanceScore" + File.separator
			+ "PerScore.exe";

	private static final String CREATING_AUDIO_EXE_PATH = File.separator + "CreativeAddin" + File.separator
			+ "CreativeAddin.exe";

	@Value("${dd-yinyue.satff.img.path}")
	private String satffImgPath;

	@Value("${dd-yinyue.c_sharp_exe}")
	private String cSharpExePath;

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() throws Exception {
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

	public Float getShortAnswerScore(String standarAnswer, String answer, String presentType) {
		int result = 0;
		Process process = null;
		File exeFile = null;
		try {
			exeFile = ResourceUtils.getFile(cSharpExePath + SHORT_ANSWR_SCORE_EXE_PATH);
			process = Runtime.getRuntime().exec(new String[] { exeFile.getPath(), standarAnswer, answer, presentType });
			process.waitFor();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			result = process.exitValue();
			System.out.println("----------result:" + result);
			result = result < 0 ? 0 : result > 100 ? 100 : result;
			process.destroy();
		}
		return Float.valueOf(String.valueOf(result));
	}

	public Float getActScore(String standarAnswer, String answer, String questionType, boolean isMan, int age,
			int tempo, String jsonFile) {
		int result = 0;
		Process process = null;
		File exeFile = null;
		try {
			exeFile = ResourceUtils.getFile(cSharpExePath + ACT_SCORE_EXE_PATH);
			System.out.println("--------------standarAnswer:" + standarAnswer);
			System.out.println("--------------answer:" + answer);
			System.out.println("--------------questionType:" + questionType);
			System.out.println("--------------isMan:" + String.valueOf(isMan));
			System.out.println("--------------age:" + String.valueOf(age));
			System.out.println("--------------tempo:" + String.valueOf(tempo));
			System.out.println("--------------jsonFile:" + jsonFile);
			process = Runtime.getRuntime().exec(new String[] { exeFile.getPath(), standarAnswer, answer, questionType,
					String.valueOf(isMan), String.valueOf(age), String.valueOf(tempo), jsonFile });
			process.waitFor();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			result = process.exitValue();
			System.out.println("----------result:" + result);
			result = result < 0 ? 0 : result > 100 ? 100 : result;
			process.destroy();
		}
		return Float.valueOf(String.valueOf(result));
	}

	public void getCreatingAudio(String answer, String audio) {
		int result = 0;
		Process process = null;
		File exeFile = null;
		try {
			exeFile = ResourceUtils.getFile(cSharpExePath + CREATING_AUDIO_EXE_PATH);
			process = Runtime.getRuntime().exec(new String[] { exeFile.getPath(), answer, audio });
			process.waitFor();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			result = process.exitValue();
			System.out.println("----------result:" + result);
			result = result < 0 ? 0 : result > 100 ? 100 : result;
			process.destroy();
		}
	}
}
