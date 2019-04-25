package com.nercl.music.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.Key;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class Tester {

	public static final String KEY_ALGORITHM = "DES";
	// 算法名称/加密模式/填充方式
	public static final String CIPHER_ALGORITHM_ECB = "DES/ECB/PKCS5Padding";
	public static final String CIPHER_ALGORITHM_CBC = "DES/CBC/PKCS5Padding";

	@Before
	public void setup() throws Exception {
	}

	@Test
	public void test1() throws Exception {
		System.out.println(UUID.randomUUID().toString());
		System.out.println(System.currentTimeMillis());
	}

	@Test
	public void test2() throws Exception {
		List<String> list = this.list();
		list.stream().forEach(l -> System.out.println("sd"));
	}

	@Test
	public void test3() throws Exception {
		String base64Str = Base64.getEncoder()
				.encodeToString(Files.toByteArray(new File("D:\\20160112193018_ftvm3.jpeg")));
		System.out.println(base64Str);
		String ss = "";
		byte[] bytes = Base64.getDecoder().decode(base64Str);
		FileUtils.copyInputStreamToFile(new ByteArrayInputStream(bytes), new File("D:\\aa.jpeg"));
	}

	@Test
	public void test4() throws Exception {
		System.out.println((int) ((double) 3 / (double) 5 * 100));
	}

	private List<String> list() {
		// return null;
		return Lists.newArrayList();
	}

	@Test
	public void testCSharp() throws Exception {
		File file = new File("D:\\c_sharp_exe_demo\\HelloWorld.exe");
		System.out.println(file.getPath());
		Process process = Runtime.getRuntime().exec(new String[] { "D:\\c_sharp_exe_demo\\HelloWorld.exe", "abcd" });
		Thread.sleep(10000);
		process.destroy();
	}

	@Test
	public void testDesc() throws Exception {
		/*
		 * 使用 ECB mode 密钥生成器 生成密钥 ECB mode cannot use IV
		 */
		// byte[] key = generateKey();
		byte[] key = "Music@17".getBytes();
		byte[] encrypt = encrypt("胃炎F#*（x）aa".getBytes(), key);
		System.out.println(new String(decrypt(encrypt, key)));

		/*
		 * 使用CBC mode 使用密钥工厂生成密钥，加密 解密 iv: DES in CBC mode and RSA ciphers with
		 * OAEP encoding operation.
		 */
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey secretKey = factory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(getIV()));
		byte[] enc = cipher.doFinal("胃炎A%F#*（x）aa".getBytes()); // 加密

		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(getIV()));
		byte[] dec = cipher.doFinal(enc); // 解密
		System.out.println(new String(dec));
	}

	private byte[] getIV() {
		String iv = "Music.2017"; // IV length: must be 16 bytes long
		return iv.getBytes();
	}

	/**
	 * 生成密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	private byte[] generateKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
		keyGenerator.init(56); // des 必须是56, 此初始方法不必须调用
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey.getEncoded();
	}

	/**
	 * 还原密钥
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private Key toKey(byte[] key) throws Exception {
		DESKeySpec des = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(des);
		return secretKey;
	}

	/**
	 * 加密
	 * 
	 * @param data
	 *            原文
	 * @param key
	 * @return 密文
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] data, byte[] key) throws Exception {
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	/**
	 * 解密
	 * 
	 * @param data
	 *            密文
	 * @param key
	 * @return 明文、原文
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] data, byte[] key) throws Exception {
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
		cipher.init(Cipher.DECRYPT_MODE, k);
		return cipher.doFinal(data);
	}
}
