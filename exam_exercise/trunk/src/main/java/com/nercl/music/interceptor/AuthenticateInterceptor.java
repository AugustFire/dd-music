package com.nercl.music.interceptor;

import static com.nercl.music.util.Encryptor.encrypte2;

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
import com.nercl.music.service.ExerciserService;

@Component
public class AuthenticateInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private Gson gson;

	@Autowired
	private ExerciserService exerciserService;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {
		String uid = request.getHeader("X-Uid");
		String timestamp = request.getHeader("X-Timestamp");
		String encryptedMessage = request.getHeader("X-EncryptedMessage");
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		ret.put("desc", "no login");
		String message = gson.toJson(ret);
		if (Strings.isNullOrEmpty(uid) || Strings.isNullOrEmpty(timestamp) || Strings.isNullOrEmpty(encryptedMessage)) {
			response.setStatus(401);
			writeMessageToResponse(response, message);
			return false;
		}
		long currentTime = System.currentTimeMillis();
		if (Long.valueOf(timestamp) < currentTime - 1000 * 60 * 15
		        || Long.valueOf(timestamp) > currentTime + 1000 * 60 * 15) {
			response.setStatus(401);
			writeMessageToResponse(response, message);
			return false;
		}
		String token = exerciserService.getToken(uid);
		if (Strings.isNullOrEmpty(token)) {
			writeMessageToResponse(response, message);
			return false;
		}
		String serverEncryptedMessage = encrypte2(uid, token, timestamp);
		if (serverEncryptedMessage.equalsIgnoreCase(encryptedMessage)) {
			return true;
		} else {
			writeMessageToResponse(response, message);
			return false;
		}
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
