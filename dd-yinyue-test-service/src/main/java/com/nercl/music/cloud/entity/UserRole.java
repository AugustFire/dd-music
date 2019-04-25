package com.nercl.music.cloud.entity;

/**
 * 用于创建活动的角色
 */
public enum UserRole {
	SCHOOL_MASTER, TEACHER, STUDENT, OTHER;

	/**
	 * 判断字符串是否是枚举指定的值,是则返回true
	 */
	public static final Boolean isDefined(String str) {
		UserRole[] en = UserRole.values();
		for (int i = 0; i < en.length; i++) {
			if (en[i].toString().equals(str)) {
				return true;
			}
		}
		return false;
	}
}
