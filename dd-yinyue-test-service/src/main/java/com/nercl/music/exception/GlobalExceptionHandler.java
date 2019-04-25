package com.nercl.music.exception;

import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.collect.Maps;

@ControllerAdvice
public class GlobalExceptionHandler {

//	@ExceptionHandler(CommonLogicException.class)
//	@ResponseStatus(HttpStatus.OK)
//	public String handleCommonLogicException(HttpServletRequest request, Exception ex) {
//		Map<String, Object> ret = Maps.newHashMap();
//		CommonLogicException exception = (CommonLogicException) ex;
//		ret.put("code", exception.getErrorCode());
//		ret.put("desc", ex.getMessage());
//		return "error/500";
//	}

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

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public void handleException(EntityNotFoundException exception) {
		System.out.println("Entity Not Found Exception " + exception.getMessage());
	}

}
