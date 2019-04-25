package com.nercl.music.util;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Strings;

public class CommUtils {

	/**
	 * 重命名文件名已存在的文件，在文件后面加“(1)”，如果已经重命名过则在“()”中累加
	 * 
	 * @param duplicatedResourceName
	 *            文件名
	 * @param duplicatedTimes
	 *            文件重命名次数
	 */
	public static String renameDuplicationResourceName(String duplicatedResourceName, int duplicatedTimes) {
		if (Strings.isNullOrEmpty(duplicatedResourceName)) {
			return duplicatedResourceName;
		}
		if (duplicatedResourceName.indexOf(".") > 0) { // 有后缀名
			String fileName = duplicatedResourceName.substring(0, duplicatedResourceName.indexOf(".")); // 文件名
			String suffix = duplicatedResourceName.substring(duplicatedResourceName.indexOf(".")); // 文件后缀名
			return fileName + "(" + (duplicatedTimes + 1) + ")" + suffix;
		} else {
			return duplicatedResourceName + "(" + (duplicatedTimes + 1) + ")";
		}
	}

	/**
	 * @param resourceName
	 *            文件名
	 * @param existNames
	 *            查询重复的文件名列表
	 * @return 如果有重复命名的就返回重复命名中最后一个重命名的次数，如果没有重命名过则返回0
	 */
	public static Integer nameRepeatTimes(String resourceName, List<String> existNames) {
		if (existNames == null || existNames.isEmpty()) {
			return 0;
		}
		String fileName = null;
		if (resourceName.lastIndexOf(".") > 0) { // 有后缀名
			existNames = existNames.stream().filter(it -> it.lastIndexOf(".")!=-1).filter(name -> resourceName.substring(resourceName.lastIndexOf("."))
					.equals(name.substring(name.lastIndexOf(".")))).peek(it->System.out.println(it)).map((name) -> {
						return name.substring(0, name.lastIndexOf("."));
					}).collect(Collectors.toList());
			fileName = resourceName.substring(0, resourceName.lastIndexOf("."));
		} else { // 没有后缀名
			existNames = existNames.stream().filter(name -> resourceName.equals(name)).collect(Collectors.toList());
			fileName = resourceName;
		}
		int compareIndex = 0;
		for (String name : existNames) {
			if (name.startsWith(fileName)) {
				String lastStr = name.substring(fileName.length());
				// 如果截去fileName之后剩下的字符串是以"("开头以")"结尾，且中间的是数字，说明这个名称是fileName的重名名称
				if (lastStr.startsWith("(") && lastStr.endsWith(")")
						&& isNumeric(lastStr.substring(1, lastStr.length() - 1))) {
					int repeatNameNum = Integer.parseInt(lastStr.substring(1, lastStr.length() - 1));
					if (repeatNameNum > compareIndex) {
						compareIndex = repeatNameNum;
					}
				}
			}
		}
		return compareIndex;
	}

	/**
	 * 判断字符串是否是数字组成
	 * 
	 * @return 字符串是数字返回true，否则返回false
	 */
	public static boolean isNumeric(String str) {
		if (Strings.isNullOrEmpty(str)) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		String str = "adada.txt";
		System.out.println(str);
		System.out.println(renameDuplicationResourceName(str, 2));
		System.out.println(renameDuplicationResourceName("dadada(3).jpg", 2));
	}

}
