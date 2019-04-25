package com.nercl.music.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Encryptor {

	public static String initSalt(int length) {
		if (length < 1) {
			return "";
		}
		Random random = new Random();
		String numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters.charAt(random.nextInt(72));
		}
		return new String(randBuffer);
	}

	public static String encrypte(String password, String salt) {
		String encryptedPassword = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(salt.getBytes("UTF-8"));
			String temp = bytesToHex(digest.digest());
			digest.update((password + temp).getBytes("UTF-8"));
			encryptedPassword = bytesToHex(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptedPassword;
	}

	public static String encrypte2(String password, String salt) {
		String encryptedPassword = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update((password + salt).getBytes("UTF-8"));
			encryptedPassword = bytesToHex(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptedPassword;
	}

	public static String encrypte2(String uid, String token, String timestamp) {
		String encryptedMessage = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update((uid + token + timestamp).getBytes("UTF-8"));
			encryptedMessage = bytesToHex(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptedMessage;
	}

	public static void main(String[] args) {
		System.out.println(encrypte2("123456", "ZnN2JNMay2YdpjDe"));
	}

	public static String encrypte(String content) {
		String encryptedMessage = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(content.getBytes("UTF-8"));
			encryptedMessage = bytesToHex(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptedMessage;
	}

	public static String bytesToHex(byte[] bt) {
		char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < bt.length; j++) {
			buf.append(hexDigit[(bt[j] >> 4) & 0x0f]);
			buf.append(hexDigit[bt[j] & 0x0f]);
		}
		return buf.toString();
	}

}
