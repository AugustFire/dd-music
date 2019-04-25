package com.nercl.music.util;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.Assert;

public class CommonUtils {

	/**
	 * 核对选择题答案,多选题回答正确或部份正确返回回答正确的选项个数,回答错误返回0
	 * 
	 * @param standardAnswer
	 *            标准答案
	 * @param answer
	 *            学生答案
	 * @return 返回回答正确的选项个数
	 */
	public static int checkChoiceQuestions(List<String> standardAnswer, String answer) {
		String[] answers = answer.split(",");
		int result = 0;
		for(int i=0;i<answers.length;i++){
			if(!standardAnswer.contains(answers[i])){
				result = 0;
				break;
			}else{
				result++;
			}
		}
		return result;
	}
	
	/**
	 * 取length个0到max之间的不相同的随机整数
	 * 
	 * @param max
	 *            取随机数的区间的上限(上限值不能小于随机数的个数，否则值不够取)
	 * @param length
	 *            随机数的个数
	 */
	public static Set<Integer> getRandomInt(int max, int length) {
		Assert.isTrue(max >= length, "length[length=" + length + "] is bigger than max[mxa=" + max + "]");
		Set<Integer> randomSet = new LinkedHashSet<Integer>();
		while (randomSet.size() < length) {
			int random = (int) (max * Math.random());
			randomSet.add(random);
		}
		return randomSet;
	}
	
	/**
	 * 删除文件夹和文件夹下的所有文件
	 * 
	 * @param path
	 *            文件夹目录
	 */
	public static void deleteFolder(String path) {
		File folder = new File(path);
		if (folder.isDirectory()) {
			String[] files = folder.list();
			for (int i = 0; i < files.length; i++) {
				deleteFolder(path + File.separator + files[i]);
			}
			folder.delete();
		} else {
			folder.delete();
		}
	}
	
	
	
	public static void main(String[] args) {
//		Set<Integer> randomInt = getRandomInt(5, 9);
//		Iterator<Integer> iterator = randomInt.iterator();
//		while (iterator.hasNext()) {
//			Integer integer = (Integer) iterator.next();
//			System.out.println(integer);
//		}
		deleteFolder("F:\\1");
	}
}
