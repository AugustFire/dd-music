package com.nercl.music.cloud.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nercl.music.cloud.entity.valueobject.ReturnBean;
import com.nercl.music.constant.CList;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 拦截捕捉自定义异常 LogicException.class
	 */
	@ExceptionHandler(value = LogicException.class)
	public ReturnBean<String> logicExceptionHandler(LogicException ex) {
		ReturnBean<String> resultBean = new ReturnBean<String>();
		resultBean.setCode(CList.Api.Client.PROCESSING_FAILED);
		resultBean.setMsg(ex.getMsg());
		ex.printStackTrace();
		logger.info("====================" + ex.toString() + ":" + ex.getMessage());
		logger.debug("com.nercl.music.exception.LogicException", ex);
		return resultBean;
	}

	/**
	 * 全局异常捕捉处理
	 */
	@ExceptionHandler(value = Exception.class)
	public ReturnBean<String> globalExceptionHandler(Exception ex) {
		ReturnBean<String> resultBean = new ReturnBean<String>();
		resultBean.setCode(CList.Api.Client.PROCESSING_FAILED);
		resultBean.setMsg("internal server error");
		ex.printStackTrace();
		logger.info("====================" + ex.toString() + ":" + ex.getMessage());
		logger.debug("java.lang.Exception", ex);
		return resultBean;
	}
}
