package com.nercl.music.cloud.entity;

/**
 * 艺术活动的审批状态
 */
public enum CheckStatus {

	/**
	 * 末审核
	 */
	UN_CHECK,

	/**
	 * 审核通过
	 */
	PASS,

	/**
	 * 审核不通过
	 */
	REFUSE;

	/**
	 * 判断字符串是否是枚举指定的值,是则返回true
	 */
	public static final Boolean isDefined(String str) {
		CheckStatus[] en = CheckStatus.values();
		for (int i = 0; i < en.length; i++) {
			if (en[i].toString().equals(str)) {
				return true;
			}
		}
		return false;
	}
}
