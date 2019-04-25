package com.nercl.music.util;

import java.util.regex.Pattern;

public class PatternUtil {

//	private static final String emailPattern = "^([a-z0-9A-Z._%]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	private static final String emailPattern =  "^[a-z0-9A-Z._%+-]+@[a-z0-9A-Z.-]+\\.[a-zA-Z]{2,4}$";

	private static final String phonePattern = "^((13[0-9])|(17[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";

	private static final String passwordPattern = "[a-zA-Z\\d]{6,20}";
	
	private static final String idCardPattern = "d{15}|\\d{17}[0-9Xx]";

	private static final String loginPattern = passwordPattern;

	public static boolean checkEmail(String email) {
		Pattern pattern = Pattern.compile(emailPattern);
		return pattern.matcher(email).matches();
	}

	public static boolean checkPhone(String phone) {
		Pattern pattern = Pattern.compile(phonePattern);
		return pattern.matcher(phone).matches();
	}

	public static boolean checkPassword(String password) {
		Pattern pattern = Pattern.compile(passwordPattern);
		return pattern.matcher(password).matches();
	}

	public static boolean checkLogin(String login) {
		Pattern pattern = Pattern.compile(loginPattern);
		return pattern.matcher(login).matches();
	}
	
	public static boolean checkIdcard(String idCard) {
		Pattern pattern = Pattern.compile(idCardPattern);
		return pattern.matcher(idCard).matches();
	}

}
