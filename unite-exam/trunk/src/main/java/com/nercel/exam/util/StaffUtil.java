package com.nercel.exam.util;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class StaffUtil {

	private static final String LOOK_SING_SCORE_EXE_PATH = File.separator + "SightSingingScore" + File.separator
			+ "SightSingingScore.exe";

	@Value("${unit-exam.c_sharp_exe}")
	private String exe;

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	public int getLookSingData(String staff, String wav, String gender, String json, Integer keySigScore,
			String outKeyNotes) {
		int result = 0;
		Process process = null;
		File exeFile = null;
		try {
			exeFile = ResourceUtils.getFile(exe + LOOK_SING_SCORE_EXE_PATH);
			System.out.println("------staff:"+staff);
			System.out.println("------wav:"+wav);
			System.out.println("------gender:"+gender);
			System.out.println("------json:"+json);
			System.out.println("------keySigScore:"+String.valueOf(keySigScore));
			System.out.println("------outKeyNotes:"+outKeyNotes);
			process = Runtime.getRuntime().exec(new String[] { exeFile.getPath(), staff, wav, gender, json,
					String.valueOf(keySigScore), outKeyNotes });
			process.waitFor();
			result = process.exitValue();
		} catch (Exception ex) {
			ex.printStackTrace();
			result = process.exitValue();
		} finally {
			if (null != process) {
				process.destroy();
			}
		}
		return result;
	}
}
