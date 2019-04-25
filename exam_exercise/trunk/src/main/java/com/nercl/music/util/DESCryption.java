package com.nercl.music.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class DESCryption {

	private static final String KEY_STR = "Music@17";
	private static final String IV_STR = "Music.17";

	private static final byte[] DES_KEY = KEY_STR.getBytes();
	private static final byte[] DES_IV = IV_STR.getBytes();

	private static AlgorithmParameterSpec iv = null;// 加密算法的参数接口，IvParameterSpec是它的一个实现

	private static Key key = null;

	@PostConstruct
	public void init() throws Exception {
		synchronized (this) {
			DESKeySpec keySpec = new DESKeySpec(DES_KEY);// 设置密钥参数
			iv = new IvParameterSpec(DES_IV);// 设置向量
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
			key = keyFactory.generateSecret(keySpec);// 得到密钥对象
		}
	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	// 加密方法
	public String encode(String data) throws Exception {
		Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");// 得到加密对象Cipher
		enCipher.init(Cipher.ENCRYPT_MODE, key, iv);// 设置工作模式为加密模式，给出密钥和向量
		byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));
		return new String(Base64.getEncoder().encode(pasByte));
	}

	// 解密方法
	public String decode(String data) {
		if(Strings.isNullOrEmpty(data)){
			return "";
		}
		Cipher deCipher;
		byte[] pasByte = null;
		String value = null;
		try {
			deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			deCipher.init(Cipher.DECRYPT_MODE, key, iv);
			pasByte = deCipher.doFinal(Base64.getDecoder().decode(data.getBytes("utf-8")));
			value = new String(pasByte, "UTF-8");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
		        | InvalidAlgorithmParameterException | UnsupportedEncodingException | IllegalBlockSizeException
		        | BadPaddingException e) {
			e.printStackTrace();
		}
		return value;
	}
}
