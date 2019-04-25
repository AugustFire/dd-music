package com.nercl.music.util;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.BeanUtils;


public class CommonUtils {

	/**
	 * 取定长的随机字符串
	 * 
	 * @param length
	 *            字符串长度
	 */
	public static String getRandomString(int length) {
		StringBuffer sb = new StringBuffer();
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt((int) (Math.random() * 62)));
		}
		return sb.toString();
	}

	/**
	 * 核对多选题答案,回答正确或部份正确返回回答正确的选项个数,回答错误返回0
	 * 
	 * @param standardAnswer
	 *            标准答案
	 * @param answer
	 *            学生答案
	 * @return 返回回答正确的选项个数
	 */
	public static int checkMultiSelect(List<String> standardAnswer, String answer) {
		String[] answers = answer.split(",");
		int result = 0;
		for (int i = 0; i < answers.length; i++) {
			if (!standardAnswer.contains(answers[i])) {
				result = 0;
				break;
			} else {
				result++;
			}
		}
		return result;
	}

	/**
	 * 从source中复制指定属性到target,复制和被复制的一定要是bean对象。如果不设置属性,则复制全部属性
	 * 
	 * @param source
	 *            copy的源
	 * @param target
	 *            copy的目标
	 * @param copyProperties
	 *            要复制的属性字段名
	 */
	public static void copyProperties(Object source, Object target, List<String> copyProperties) throws Exception {
		if (null == copyProperties || copyProperties.isEmpty()) {
			BeanUtils.copyProperties(source, target);
		} else {
			for (String prop : copyProperties) {
				Method getProperty = source.getClass()
						.getDeclaredMethod("get" + prop.substring(0, 1).toUpperCase() + prop.substring(1));
				Method setProperty = source.getClass().getDeclaredMethod(
						"set" + prop.substring(0, 1).toUpperCase() + prop.substring(1), getProperty.getReturnType());
				setProperty.invoke(target, getProperty.invoke(source));
			}
		}
	}

	public static String getIp(InetAddress addr) throws UnknownHostException{
		return addr.getHostAddress().toString();
	}
	
	public static String getHost(InetAddress addr) throws UnknownHostException{
		return addr.getHostName().toString();
	}
	
	
	public static void main(String[] args) {

	}
}
