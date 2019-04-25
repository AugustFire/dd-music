package com.nercl.music.test;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESCryption {

	// DES加密的私钥，必须是8位长的字符串
//	private static final byte[] DESkey = { 6, 120, 10, 5, 7, 50, 8, 25, 6, 120, 10, 5, 7, 50, 8, 25 };// 设置密钥、
//	private static final byte[] DESIV = { 0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xAF };// 设置向量
	
	private static final byte[] DESkey = "Music@17".getBytes();// 设置密钥、
	private static final byte[] DESIV = "Music.17".getBytes();

	private static AlgorithmParameterSpec iv = null;// 加密算法的参数接口，IvParameterSpec是它的一个实现
	private static Key key = null;

	public DESCryption(byte[] DESkey, byte[] DESIV) throws Exception {
		DESKeySpec keySpec = new DESKeySpec(DESkey);// 设置密钥参数
		iv = new IvParameterSpec(DESIV);// 设置向量
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
		key = keyFactory.generateSecret(keySpec);// 得到密钥对象

	}

	// 加密方法
	public String encode(String data) throws Exception {
		Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");// 得到加密对象Cipher
		enCipher.init(Cipher.ENCRYPT_MODE, key, iv);// 设置工作模式为加密模式，给出密钥和向量
		byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));
//		BASE64Encoder base64Encoder = new BASE64Encoder();
//		return base64Encoder.encode(pasByte);
		return new String(Base64.getEncoder().encode(pasByte));
	}

	// 解密方法
	public String decode(String data) throws Exception {
		Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		deCipher.init(Cipher.DECRYPT_MODE, key, iv);
//		BASE64Decoder base64Decoder = new BASE64Decoder();
//		byte[] pasByte = deCipher.doFinal(base64Decoder.decodeBuffer(data));
		byte[] pasByte = deCipher.doFinal(Base64.getDecoder().decode(data));
		return new String(pasByte, "UTF-8");
	}

	// 测试
	public static void main(String[] args) throws Exception {
		DESCryption tools = new DESCryption(DESkey, DESIV);
		System.out.println("加密:" + tools.encode("管理员"));
		System.out.println("解密:" + tools.decode("07FiqOPfgl3N+BWqwna+Xg=="));
		System.out.println("加密:" + tools.encode("管理"));
	}
}
