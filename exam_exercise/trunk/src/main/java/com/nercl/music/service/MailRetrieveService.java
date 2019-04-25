package com.nercl.music.service;

public interface MailRetrieveService {

	/**
	 * 发送邮箱验证码
	 *
	 * @param email
	 *            邮箱
	 */
	void sendAuthCode(String email);

	/**
	 * 校验验证码
	 *
	 * @param email
	 *            邮箱
	 * @param sid
	 *            验证码
	 */
	boolean authentication(String email, String sid);
}
