package com.nercl.music.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.user.Login;
import com.nercl.music.service.LoginService;

@Component
public class OnlyOneOnLineInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private Gson gson;

	@Autowired
	private LoginService loginService;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {
		Map<String, Object> ret = Maps.newHashMap();
		String uid = request.getHeader("X-Uid");
		String xlogin = request.getHeader("X-Login");
		if (Strings.isNullOrEmpty(uid) || Strings.isNullOrEmpty(xlogin)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no login");
			response.setStatus(401);
			writeMessageToResponse(response, gson.toJson(ret));
			return false;
		}
		Login login = loginService.getByPerson(uid);
		if (null == login) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no login");
			response.setStatus(401);
			writeMessageToResponse(response, gson.toJson(ret));
			return false;
		}
		if (!xlogin.equals(login.getLoginToken())) {
			ret.put("code", CList.Api.Client.REPEATE_LOGIN);
			ret.put("desc", "该账号在其他地方被登录");
			response.setStatus(401);
			writeMessageToResponse(response, gson.toJson(ret));
			return false;
		}
		return true;
	}

	private void writeMessageToResponse(HttpServletResponse response, String message) {
		response.setContentType("application/json;charset=UTF-8");
		try {
			response.getWriter().write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
