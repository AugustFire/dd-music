package com.nercl.music.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nercl.music.xml.XmlParser2;

@Controller
public class DataController {

	@Autowired
	private XmlParser2 xmlParser2;

//	private String folder = "C:\\extract_questions";
	
	private String folder = "C:\\extract_questions2";

	@GetMapping(value = "/datas")
	@ResponseBody
	public String datas() {
		File file = new File(folder);
		try {
			xmlParser2.parseQuestion(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

}
