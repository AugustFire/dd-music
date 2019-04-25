package com.nercl.music.cloud.entity;

public enum ExamPaperType {

	/**
	 * 表演
	 */
	ACT,

	/**
	 * 音乐素养
	 */
	MUSIC_ABILITY;
	
	/**
	 * 判断字符串是否是枚举指定的值,是则返回true
	 */
	public static final Boolean IsDefined(String str) {
		ExamPaperType[] en = ExamPaperType.values();
		for (int i = 0; i < en.length; i++) {
			if (en[i].toString().equals(str)) {
				return true;
			}
		}
		return false;
	}

}
