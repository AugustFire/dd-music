package com.nercl.music.cloud.entity;

public enum AnswerSource {

	/**
	 * 随堂练习
	 */
	EXERCISE,

	/**
	 * 课后作业
	 */
	TASK,

	/**
	 * 单元测试
	 */
	CHAPTER_TEST,

	/**
	 * 期中测试
	 */
	MIDDLE_TERM_TEST,

	/**
	 * 期末考试
	 */
	FINAL_TERM_TEST,
	
	/**
	 * 其他考试
	 * */
	OTHER;

	/**
	 * 判断字符串是否是枚举指定的值,是则返回true
	 */
	public static final Boolean isDefined(String str) {
		AnswerSource[] en = AnswerSource.values();
		for (int i = 0; i < en.length; i++) {
			if (en[i].toString().equals(str)) {
				return true;
			}
		}
		return false;
	}
}
