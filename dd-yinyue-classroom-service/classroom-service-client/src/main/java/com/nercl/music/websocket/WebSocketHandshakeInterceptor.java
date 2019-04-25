package com.nercl.music.websocket;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.google.common.base.Strings;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		System.out.println("-----beforeHandshake-----");
		if (!(request instanceof ServletServerHttpRequest)) {
			return false;
		}
		ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
		HttpServletRequest httpRequest = servletRequest.getServletRequest();
		HttpSession session = httpRequest.getSession(false);

		String userId = httpRequest.getParameter("userId");
		if (!Strings.isNullOrEmpty(userId)) {
			attributes.put("userId", userId);
		} else {
			return false;
		}

		String tid = httpRequest.getParameter("teacherId");
		if (!Strings.isNullOrEmpty(tid)) {
			attributes.put("teacherId", tid);
		} else {
			if (session == null) {
				return false;
			}
			String teacherId = (String) session.getAttribute("teacherId");
			if (Strings.isNullOrEmpty(teacherId)) {
				return false;
			}
			attributes.put("teacherId", teacherId);
		}

		String rid = httpRequest.getParameter("roomId");
		if (!Strings.isNullOrEmpty(rid)) {
			attributes.put("roomId", rid);
		} else {
			if (session == null) {
				return false;
			}
			String roomId = (String) session.getAttribute("roomId");
			if (Strings.isNullOrEmpty(roomId)) {
				return false;
			}
			attributes.put("roomId", roomId);
		}

		String code = httpRequest.getParameter("roomCode");
		if (!Strings.isNullOrEmpty(code)) {
			attributes.put("roomCode", code);
		} else {
			if (session == null) {
				return false;
			}
			String roomCode = (String) session.getAttribute("roomCode");
			if (Strings.isNullOrEmpty(roomCode)) {
				return false;
			}
			attributes.put("roomCode", roomCode);
		}
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		System.out.println("-----afterHandshake-----");
	}
}
