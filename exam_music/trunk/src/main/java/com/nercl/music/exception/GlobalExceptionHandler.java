package com.nercl.music.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.collect.Maps;

//@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CommonLogicException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> handleCommonLogicException(HttpServletRequest request, Exception ex) {
		Map<String, Object> ret = Maps.newHashMap();
		CommonLogicException exception = (CommonLogicException) ex;
		ret.put("code", exception.getErrorCode());
		ret.put("desc", ex.getMessage());
		return ret;
	}

	@ExceptionHandler(NoPrivilegeException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> handleNoPrivilegeException(HttpServletRequest request, Exception ex) {
		Map<String, Object> ret = Maps.newHashMap();
		NoPrivilegeException exception = (NoPrivilegeException) ex;
		ret.put("code", exception.getErrorCode());
		ret.put("desc", ex.getMessage());
		return ret;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> handleException(HttpServletRequest request, Exception ex) {
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", 405);
		ret.put("desc", "服务器发生错误");
		return ret;
	}

}
