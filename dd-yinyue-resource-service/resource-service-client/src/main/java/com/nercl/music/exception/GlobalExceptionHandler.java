package com.nercl.music.exception;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 拦截捕捉自定义异常 LogicException.class
	 */
	@ExceptionHandler(value = LogicException.class)
	public Map<String, Object> logicExceptionHandler(LogicException ex) {
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		ret.put("desc", ex.getMsg());
		ret.put("exception", ex.toString());
		ex.printStackTrace();
		logger.info("====================" + ex.toString() + ":" + ex.getMessage());
		logger.debug("com.nercl.music.exception.LogicException", ex);
		return ret;
	}

	/**
	 * 全局异常捕捉处理
	 */
	@ExceptionHandler(value = Exception.class)
	public Map<String, Object> globalExceptionHandler(Exception ex) {
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		ret.put("desc", "internal server error");
		ret.put("exception", ex.toString());
		ex.printStackTrace();
		logger.info("====================" + ex.toString() + ":" + ex.getMessage());
		logger.debug("java.lang.Exception", ex);
		return ret;
	}
}
