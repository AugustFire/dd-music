package com.nercl.music.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

import com.google.common.io.Files;

@Component
public class ZipFileUtil {

	public static void main(String[] args) {
		ZipFileUtil zipFileUtil = new ZipFileUtil();
		zipFileUtil.zipFile(new File("D:\\question\\23423423.zip"), "D:\\question\\21620651054");
	}

	public void zipFile(File zipFile, String... filePaths) {
		ZipOutputStream zipOut = null;
		try {
			System.out.println("压缩中...");
			zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
			for (String filePath : filePaths) {
				File sourceFile = new File(filePath);
				compress(zipOut, sourceFile, sourceFile.getName());
			}
			System.out.println("压缩完成");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != zipOut) {
					zipOut.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void compress(ZipOutputStream out, File sourceFile, String base) {
		try {
			if (sourceFile.isDirectory()) {
				File[] flist = sourceFile.listFiles();
				if (flist.length == 0) {// 如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
					out.putNextEntry(new ZipEntry(base + File.separator));
				} else {// 如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
					for (File file : flist) {
						compress(out, file, base + File.separator + file.getName());
					}
				}
			} else {// 如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
				out.putNextEntry(new ZipEntry(base));
				Files.copy(sourceFile, out);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
