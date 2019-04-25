package com.nercl.music.websocket;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

public class YinYueWebSocketSession {

	private WebSocketSession session;

	private String teacherId;

	private String userId;

	private String name;

	private String roomId;

	private String roomCode;

	public YinYueWebSocketSession(WebSocketSession session) {
		String teacherId = String.valueOf(session.getAttributes().getOrDefault("teacherId", ""));
		String userId = String.valueOf(session.getAttributes().getOrDefault("userId", ""));
		String roomId = String.valueOf(session.getAttributes().getOrDefault("roomId", ""));
		String name = String.valueOf(session.getAttributes().getOrDefault("name", ""));
		String roomCode = String.valueOf(session.getAttributes().getOrDefault("roomCode", ""));
		this.session = session;
		this.teacherId = teacherId;
		this.userId = userId;
		this.roomId = roomId;
		this.name = name;
		this.roomCode = roomCode;
	}

	public boolean isTeacher() {
		return !Strings.isNullOrEmpty(teacherId) && !Strings.isNullOrEmpty(userId) && teacherId.equals(userId);
	}

	public void close() {
		try {
			this.session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDownloadFileNotice(String uuid, String senderId, String senderName, String fileName, Long size,
			String messageType) {
		Map<String, Object> contentMap = Maps.newHashMap();
		if (Strings.isNullOrEmpty(messageType)) {
			contentMap.put("message_type", MessageType.IMAGE);
		} else {
			contentMap.put("message_type", messageType);
		}
		contentMap.put("uuid", uuid);
		contentMap.put("sender_id", senderId);
		contentMap.put("sender_name", senderName);
		contentMap.put("file_name", fileName);
		contentMap.put("size", size);
		TextMessage message = new TextMessage(new Gson().toJson(contentMap));
		try {
			this.getSession().sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendUserJoinedNotice(String userId, String name, String gender, String photo) {
		Map<String, Object> contentMap = Maps.newHashMap();
		contentMap.put("message_type", MessageType.USER_JOINED);
		contentMap.put("user_id", userId);
		contentMap.put("name", name);
		contentMap.put("gender", gender);
		contentMap.put("photo", photo);
		TextMessage message = new TextMessage(new Gson().toJson(contentMap));
		try {
			this.getSession().sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendUserLogoutNotice(String userId, String name) {
		Map<String, Object> contentMap = Maps.newHashMap();
		contentMap.put("message_type", MessageType.USER_LOGOUT);
		contentMap.put("user_id", userId);
		contentMap.put("name", name);
		TextMessage message = new TextMessage(new Gson().toJson(contentMap));
		try {
			this.getSession().sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendTeacherLogoutNotice() {
		Map<String, Object> contentMap = Maps.newHashMap();
		contentMap.put("message_type", MessageType.TEACHER_LOGOUT);
		TextMessage message = new TextMessage(new Gson().toJson(contentMap));
		try {
			this.getSession().sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendTeacherConnectedNotice() {
		Map<String, Object> contentMap = Maps.newHashMap();
		contentMap.put("message_type", MessageType.TEACHER_CONNECTED);
		TextMessage message = new TextMessage(new Gson().toJson(contentMap));
		try {
			this.getSession().sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendTeacherLoginAtOtherSomewhereNotice() {
		Map<String, Object> contentMap = Maps.newHashMap();
		contentMap.put("message_type", MessageType.TEACHER_LOGIN_AT_OTHER);
		TextMessage message = new TextMessage(new Gson().toJson(contentMap));
		try {
			this.getSession().sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendRemoveUserNotice(String roomCode) {
		Map<String, Object> contentMap = Maps.newHashMap();
		contentMap.put("message_type", MessageType.REMOVE);
		contentMap.put("room_code", roomCode);
		TextMessage message = new TextMessage(new Gson().toJson(contentMap));
		try {
			this.getSession().sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDownLoadQuestionNotice(String[] qids, String senderId, String senderName,
			String messageType, String timestamp) {
		Map<String, Object> contentMap = Maps.newHashMap();
		if (Strings.isNullOrEmpty(messageType)) {
			contentMap.put("message_type", MessageType.DOWNLOAD_QUESTION);
		} else {
			contentMap.put("message_type", messageType);
		}
		contentMap.put("sender_id", senderId);
		contentMap.put("sender_name", senderName);
		contentMap.put("qids", Joiner.on(",").join(qids));
		contentMap.put("timestamp", timestamp);
		TextMessage message = new TextMessage(new Gson().toJson(contentMap));
		try {
			this.getSession().sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendSaveAnswerNotice(String senderId, String answer) {
		Map<String, Object> contentMap = Maps.newHashMap();
		contentMap.put("message_type", MessageType.FEEDBACK);
		contentMap.put("sender_id", senderId);
		contentMap.put("answer", answer);
		TextMessage message = new TextMessage(new Gson().toJson(contentMap));
		try {
			this.getSession().sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendRemoveNotice(Long roomId) {
		Map<String, Object> contentMap = Maps.newHashMap();
		contentMap.put("message_type", MessageType.REMOVE);
		contentMap.put("room_id", roomId);
		contentMap.put("url", "/teachrooms");
		TextMessage message = new TextMessage(new Gson().toJson(contentMap));
		try {
			this.getSession().sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public WebSocketSession getSession() {
		return session;
	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

}
