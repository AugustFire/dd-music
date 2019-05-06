package com.nercl.music.websocket;

import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.google.common.collect.Maps;

@Component
public class WebSocketSessionManager {

	private static Map<String, Map<String, YinYueWebSocketSession>> sessionManager = Maps.newConcurrentMap();

	public void addSession(WebSocketSession session) {
		YinYueWebSocketSession yinYueWebSocketSession = new YinYueWebSocketSession(session);
		String roomCodeKey = this.getRoomCodeKey(yinYueWebSocketSession);
		String singleSessionKey = this.getSingleSessionKey(yinYueWebSocketSession);
		if (!sessionManager.containsKey(roomCodeKey)) {
			Map<String, YinYueWebSocketSession> map = Maps.newHashMap();
			map.put(singleSessionKey, yinYueWebSocketSession);
			sessionManager.put(roomCodeKey, map);
		} else {
			Map<String, YinYueWebSocketSession> sessions = sessionManager.get(roomCodeKey);
			if (yinYueWebSocketSession.isTeacher() && sessions.containsKey(singleSessionKey)) {
				YinYueWebSocketSession old = sessions.get(singleSessionKey);
				sendTeacherLoginAtOtherSomewhere(old);
			}
			sessions.put(singleSessionKey, yinYueWebSocketSession);
			if (yinYueWebSocketSession.isTeacher()) {
				sendTeacherConnectedNotice(roomCodeKey);
			}
		}
		System.out.println("sessionManager size:" + sessionManager.size());
	}

	public void remove(WebSocketSession session) {
		YinYueWebSocketSession yinYueWebSocketSession = new YinYueWebSocketSession(session);
		String roomCodeKey = this.getRoomCodeKey(yinYueWebSocketSession);
		if (!sessionManager.containsKey(roomCodeKey)) {
			return;
		}
		if (yinYueWebSocketSession.isTeacher()) {
			sendTeacherLogoutNotice(roomCodeKey);
		} else {
			sendUserLogoutNotice(roomCodeKey, yinYueWebSocketSession.getRoomId(), yinYueWebSocketSession.getUserId(),
					yinYueWebSocketSession.getName());
		}
		String singleSessionKey = this.getSingleSessionKey(yinYueWebSocketSession);
		sessionManager.get(roomCodeKey).remove(singleSessionKey);
	}

	public boolean online(String roomCode, String userId) {
		if (!sessionManager.containsKey(roomCode)) {
			return false;
		}
		Map<String, YinYueWebSocketSession> sessionMap = sessionManager.get(roomCode);
		return null != sessionMap && !sessionMap.isEmpty() && sessionMap.containsKey(userId);
	}

	public void sendDownLoadFileNotice(String roomCode, String uuid, String senderId, String senderName,
			String[] receiverIds, String fileName, Long size, String messageType) {
		Map<String, YinYueWebSocketSession> map = sessionManager.get(roomCode);
		if (null == map || map.isEmpty()) {
			return;
		}
		if (null != receiverIds && receiverIds.length > 0) {
			for (Map.Entry<String, YinYueWebSocketSession> entry : map.entrySet()) {
				Arrays.stream(receiverIds).filter(receiverId -> receiverId.equals(entry.getKey()))
						.forEach(receiverId -> {
							YinYueWebSocketSession session = entry.getValue();
							if (null != session && !session.getUserId().equals(senderId)) {
								session.sendDownloadFileNotice(uuid, senderId, senderName, fileName, size, messageType);
							}
						});
			}
		} else {
			for (Map.Entry<String, YinYueWebSocketSession> entry : map.entrySet()) {
				YinYueWebSocketSession session = entry.getValue();
				if (null != session && !session.getUserId().equals(senderId)) {
					session.sendDownloadFileNotice(uuid, senderId, senderName, fileName, size, messageType);
				}
			}
		}
	}

	public void sendUserJoinedNotice(String roomCode, String roomId, String studentId, String name, String gender,
			String photo) {
		YinYueWebSocketSession teacherSocket = getTeacherSession(roomCode);
		if (null != teacherSocket && teacherSocket.getSession().isOpen()) {
			teacherSocket.sendUserJoinedNotice(studentId, name, gender, photo);
		}
	}

	public void sendUserLogoutNotice(String roomCode, String roomId, String studentId, String name) {
		YinYueWebSocketSession teacherSocket = getTeacherSession(roomCode);
		if (null == teacherSocket) {
			return;
		}
		if (!teacherSocket.getTeacherId().equals(studentId)) {
			teacherSocket.sendUserLogoutNotice(studentId, name);
		} else {
			sendTeacherLogoutNotice(roomCode);
		}
	}

	public void sendTeacherLogoutNotice(String roomCode) {
		if (!sessionManager.containsKey(roomCode)) {
			return;
		}
		Map<String, YinYueWebSocketSession> map = sessionManager.get(roomCode);
		for (Map.Entry<String, YinYueWebSocketSession> entry : map.entrySet()) {
			YinYueWebSocketSession yySession = entry.getValue();
			if (!yySession.isTeacher()) {
				yySession.sendTeacherLogoutNotice();
			}
		}
	}

	public void sendTeacherConnectedNotice(String roomCode) {
		if (!sessionManager.containsKey(roomCode)) {
			return;
		}
		Map<String, YinYueWebSocketSession> map = sessionManager.get(roomCode);
		for (Map.Entry<String, YinYueWebSocketSession> entry : map.entrySet()) {
			YinYueWebSocketSession yySession = entry.getValue();
			if (!yySession.isTeacher()) {
				yySession.sendTeacherConnectedNotice();
			}
		}
	}

	public void sendTeacherLoginAtOtherSomewhere(YinYueWebSocketSession yySession) {
		yySession.sendTeacherLoginAtOtherSomewhereNotice();
	}

	public void sendRemoveUserNotice(String roomCode, String studentId) {
		if (!sessionManager.containsKey(roomCode)) {
			return;
		}
		Map<String, YinYueWebSocketSession> map = sessionManager.get(roomCode);
		for (Map.Entry<String, YinYueWebSocketSession> entry : map.entrySet()) {
			YinYueWebSocketSession session = entry.getValue();
			if (null != session && !session.getUserId().equals(studentId)) {
				session.sendRemoveUserNotice(roomCode);
			}
		}
	}

	public void sendDownLoadQuestionNotice(String roomCode, String[] qids, String senderId, String senderName,
			String[] receiverIds, String messageType, String timestamp) {
		Map<String, YinYueWebSocketSession> map = sessionManager.get(roomCode);
		if (null == map || map.isEmpty()) {
			return;
		}
		if (null != receiverIds && receiverIds.length > 0) {
			for (Map.Entry<String, YinYueWebSocketSession> entry : map.entrySet()) {
				Arrays.stream(receiverIds).filter(receiverId -> receiverId.equals(entry.getKey()))
						.forEach(receiverId -> {
							YinYueWebSocketSession session = entry.getValue();
							if (null != session && !session.getUserId().equals(senderId)) {
								session.sendDownLoadQuestionNotice(qids, senderId, senderName, messageType, timestamp);
							}
						});
			}
		} else {
			for (Map.Entry<String, YinYueWebSocketSession> entry : map.entrySet()) {
				YinYueWebSocketSession session = entry.getValue();
				if (null != session && !session.getUserId().equals(senderId)) {
					session.sendDownLoadQuestionNotice(qids, senderId, senderName, messageType, timestamp);
				}
			}
		}
	}

	public void sendSaveAnswerNotice(String roomCode, String senderId, String answer) {
		YinYueWebSocketSession teacherSocket = getTeacherSession(roomCode);
		if (null != teacherSocket) {
			teacherSocket.sendSaveAnswerNotice(senderId, answer);
		}
	}

	private String getRoomCodeKey(YinYueWebSocketSession yinYueWebSocketSession) {
		return null == yinYueWebSocketSession ? "" : yinYueWebSocketSession.getRoomCode();
	}

	private String getSingleSessionKey(YinYueWebSocketSession yinYueWebSocketSession) {
		return null == yinYueWebSocketSession ? "" : yinYueWebSocketSession.getUserId();
	}

	private YinYueWebSocketSession getTeacherSession(String code) {
		if (!sessionManager.containsKey(code)) {
			return null;
		}
		Map<String, YinYueWebSocketSession> map = sessionManager.get(code);
		for (Map.Entry<String, YinYueWebSocketSession> entry : map.entrySet()) {
			YinYueWebSocketSession yySession = entry.getValue();
			if (yySession.isTeacher()) {
				return yySession;
			}
		}
		return null;
	}

}
