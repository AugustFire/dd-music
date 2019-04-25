package com.nercl.music.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

@Controller
@EnableConfigurationProperties({ ServerProperties.class })
public class ExceptionController implements ErrorController {

	private final static String ERROR_PATH = "/error";

	@Autowired
	private ErrorAttributes errorAttributes;

	/**
	 * 初始化ExceptionController
	 * 
	 * @param errorAttributes
	 */
	@Autowired
	public ExceptionController(ErrorAttributes errorAttributes) {
		Asserts.notNull(errorAttributes, "ErrorAttributes must not be null");
		this.errorAttributes = errorAttributes;
	}

	/**
	 * errorHtml
	 */
	@RequestMapping(produces = "text/html", value = ERROR_PATH)
	public String errorHtml(HttpServletResponse response) {
		if (HttpStatus.NOT_FOUND.value() == response.getStatus()) {
			return "error/404";
		}
		return "error/500";
	}

	/**
	 * error json
	 */
	@RequestMapping(value = ERROR_PATH)
	@ResponseBody
	public Map<String, Object> errorJson(HttpServletResponse response) {
		Map<String, Object> ret = Maps.newHashMap();
		if (HttpStatus.NOT_FOUND.value() == response.getStatus()) {
			ret.put("code", 404);
			ret.put("desc", "服务未找到");
			return ret;
		}
		ret.put("code", 500);
		ret.put("desc", "服务器发生错误");
		return ret;
	}

	/**
	 * 
	 * @see ExceptionMvcAutoConfiguration#containerCustomizer()
	 * @return
	 */
	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

	public ErrorAttributes getErrorAttributes() {
		return errorAttributes;
	}

}
