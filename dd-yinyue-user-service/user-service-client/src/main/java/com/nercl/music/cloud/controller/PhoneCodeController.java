package com.nercl.music.cloud.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.nercl.music.cloud.service.PhoneCodeService;

@RestController
public class PhoneCodeController {

	@Autowired
	private PhoneCodeService phoneCodeService;

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@RequestMapping(value = "/register/code", params = { "project", "phone" }, produces = JSON_PRODUCES)
	public Map<String, Object> sendCode(@RequestParam(value = "project") String project,
	        @RequestParam(value = "phone") String phone) {
		Map<String, Object> ret = Maps.newHashMap();
		if (this.phoneCodeService.isSendedCodeInOneMinute(phone)) {
			return ret;
		} else {
			return ret;
		}
	}

}
