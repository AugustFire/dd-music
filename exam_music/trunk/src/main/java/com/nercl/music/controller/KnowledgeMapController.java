package com.nercl.music.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.nercl.music.entity.KnowledgeMap;
import com.nercl.music.service.KnowledgeMapService;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

@Controller
public class KnowledgeMapController {

	@Autowired
	private KnowledgeMapService knowledgeMapService;

	private String rootNodeNo = "";

	@GetMapping(value = "/tree")
	@ResponseBody
	public String getYLTree() {
		String result = "";
		try {
			Workbook book = Workbook.getWorkbook(new File("d:/音乐知识地图.xls"));
			Sheet sheet = book.getSheet(0);
			for (int line = 9; line <= 440; line++) {
				for (int column = 0; column <= 5; column++) {
					Cell cell1 = sheet.getCell(column, line);
					result = cell1.getContents();
					getInformationFromCell(result);
				}
			}
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	private void getInformationFromCell(String result) {
		if (Strings.isNullOrEmpty(result)) {
			return;
		}
		String no = "";
		String title = "";
		String difficulty = "";
		String parentNodeNo = "";
		int locatoin1 = result.indexOf("]");
		int location2 = result.indexOf("(");
		int location3 = result.indexOf("（");
		location2 = location2 > location3 ? location2 : location3;
		if (location2 == -1) {
			no = result.substring(1, locatoin1);
			title = result.substring(locatoin1 + 1, result.length());
			difficulty = "";
			parentNodeNo = "";
			rootNodeNo = no;
			KnowledgeMap knowledgeMap = getKnowledgeMapEntity(no, title, difficulty, parentNodeNo);
			KnowledgeMap getByNo = knowledgeMapService.get(no);
			if (getByNo == null) {
				knowledgeMapService.save(knowledgeMap);
			}
			return;
		}
		no = result.substring(1, locatoin1);
		parentNodeNo = getParentNodeNo(no);
		title = result.substring(locatoin1 + 1, location2);
		difficulty = result.substring(location2 + 1, result.length() - 1);
		KnowledgeMap knowledgeMap = getKnowledgeMapEntity(no, title, difficulty, parentNodeNo);
		KnowledgeMap getByNo = knowledgeMapService.get(no);
		if (getByNo == null) {
			knowledgeMapService.save(knowledgeMap);
		}
	}

	private String getParentNodeNo(String no) {
		String reverse = new StringBuffer(no).reverse().toString();
		int location = reverse.indexOf("-");
		if (location == -1) {
			return rootNodeNo;
		}
		return new StringBuffer(reverse.substring(location + 1, reverse.length())).reverse().toString();
	}

	private KnowledgeMap getKnowledgeMapEntity(String no, String title, String difficulty, String parentNo) {
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		knowledgeMap.setNo(no);
		knowledgeMap.setTitle(title);
		knowledgeMap.setDifficulty(Strings.isNullOrEmpty(difficulty) ? 0 : Float.valueOf(difficulty));
		KnowledgeMap parent = this.knowledgeMapService.get(parentNo);
		if (null != parent) {
			knowledgeMap.setParentId(parent.getId());
		}
		return knowledgeMap;
	}
}
