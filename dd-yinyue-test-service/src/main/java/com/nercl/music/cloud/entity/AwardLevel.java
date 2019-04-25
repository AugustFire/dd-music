package com.nercl.music.cloud.entity;

/**
 * 获奖级别
 */
public enum AwardLevel {

	/**
	 * 世界级
	 */
	WORLD_LEVEL("世界级"),

	/**
	 * 国家级
	 */
	NATIONAL_LEVEL("国家级"),

	/**
	 * 省级
	 */
	PROVINCIAL_LEVEL("省级"),

	/**
	 * 市级
	 */
	CITY_LEVEL("市级"),

	/**
	 * 区级
	 */
	DISTRICT_LEVEL("区级"),

	/**
	 * 其他级别
	 */
	OTHER_LEVEL("其他级别");

	private String levelName;

	private AwardLevel(String levelName) {
		this.levelName = levelName;
	}

	/**
	 * 判断字符串是否是枚举指定的值,是则返回true
	 */
	public static final Boolean isDefined(String str) {
		AwardLevel[] en = AwardLevel.values();
		for (int i = 0; i < en.length; i++) {
			if (en[i].toString().equals(str)) {
				return true;
			}
		}
		return false;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
}
