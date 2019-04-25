package com.nercl.music.cloud.controller;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.Knowledge;
import com.nercl.music.cloud.service.KnowledgeService;
import com.nercl.music.constant.CList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

@RestController
public class KnowledgeController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	private static final String FILE_PATH = "d:/新版基础教育知识地图7-25.xls";

	@Autowired
	private KnowledgeService knowledgeService;

	@GetMapping(value = "/knowledges", produces = JSON_PRODUCES)
	public Map<String, Object> getKnowledges() {
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.OK);
		ret.put("knowledges", knowledgeService.getAll());
		return ret;
	}

	@PostMapping(value = "/knowledges", produces = JSON_PRODUCES)
	public Map<String, Object> saveKnowledges() {
		Map<String, Object> ret = Maps.newHashMap();
		String result = "";
		Workbook book = null;
		try {
			book = Workbook.getWorkbook(new File(FILE_PATH));
			Sheet sheet = book.getSheet(0);
			for (int line = 1; line <= 820; line++) {
				for (int column = 0; column <= 4; column++) {
					Cell cell = sheet.getCell(column, line);
					result = cell.getContents();
					getInformationFromCell(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != book) {
				book.close();
			}
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("desc", "success");
		return ret;
	}

	private void getInformationFromCell(String result) {
		if (StringUtils.isBlank(result)) {
			return;
		}
		String no = "";
		String title = "";
		String parentNodeNo = "";
		int locatoin1 = result.indexOf("]");
		no = result.substring(1, locatoin1);
		parentNodeNo = getParentNodeNo(no);
		title = result.substring(locatoin1 + 1, result.length());
		Knowledge knowledge = newKnowledgeEntity(no, title, parentNodeNo);
		Knowledge getByNo = knowledgeService.get(no);
		if (getByNo == null) {
			knowledgeService.save(knowledge);
		}
	}

	private String getParentNodeNo(String no) {
		int location = no.indexOf("-");
		if (location == -1) {
			return no.substring(0, no.length() - 1);
		} else {
			String reverse = new StringBuffer(no).reverse().toString();
			location = reverse.indexOf("-");
			return new StringBuffer(reverse.substring(location + 1, reverse.length())).reverse().toString();
		}
	}

	private Knowledge newKnowledgeEntity(String no, String title, String parentNo) {
		Knowledge knowledge = new Knowledge();
		knowledge.setNo(no);
		knowledge.setTitle(title);
		Knowledge parent = this.knowledgeService.get(parentNo);
		if (null != parent) {
			knowledge.setParentId(parent.getId());
		}
		return knowledge;
	}

	/**
	 * 根据parentId查询下级音乐知识，如果parentId为空则查询的是一级知识点
	 * 
	 * @param parentId
	 */
	@GetMapping(value = "/knowledge/knowledges", produces = JSON_PRODUCES)
	public Map<String, Object> getKnowledgesByParentId(@RequestParam("parent_id") String parentId) {
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.OK);
		ret.put("knowledges", knowledgeService.getKnowledgesByParentId(parentId));
		return ret;
	}
}
