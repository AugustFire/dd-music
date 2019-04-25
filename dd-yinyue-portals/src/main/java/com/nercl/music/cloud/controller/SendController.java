package com.nercl.music.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@RestController
public class SendController {
	
	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Gson gson;

	@RequestMapping(value = "/")
	public ModelAndView test(ModelAndView mv) {
	    mv.setViewName("/index");
	    return mv;
	}

	@RequestMapping(value = "/index1")
	public ModelAndView test1(ModelAndView mv) {
	    mv.setViewName("/index1");
	    return mv;
	}
	
	
	public static void main(String[] args) {
		
	}
}
