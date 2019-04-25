package com.nercl.music.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class ChatWebSocketHandler implements WebSocketHandler {

	@Autowired
	private WebSocketSessionManager sessionManager;

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		System.out.println("-----------handleMessage---------");
		if (message.getPayloadLength() == 0) {
			return;
		}
		if (message instanceof TextMessage) {
			System.out.println("message:" + message);
			handleTextMessage(session, (TextMessage) message);
		} else if (message instanceof BinaryMessage) {
			handleBinaryMessage(session, (BinaryMessage) message);
		} else if (message instanceof PongMessage) {
			handlePongMessage(session, (PongMessage) message);
		} else {
			throw new IllegalStateException("Unexpected WebSocket message type: " + message);
		}
	}

	private void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// sessionManager.sendMessage(session, message);
	}

	private void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}

	private void handlePongMessage(WebSocketSession session, PongMessage message) {
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("-----------afterConnectionEstablished---------");
		sessionManager.addSession(session);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println("-----------handleTransportError---------");
		System.out.println("-----------exception---------"+exception.getMessage());
		if (session.isOpen()) {
			session.close();
		}
//		sessionManager.remove(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		sessionManager.remove(session);
		System.out.println("-----------afterConnectionClosed---------");
		if (session.isOpen()) {
			session.close();
		}
	}

	@Override
	public boolean supportsPartialMessages() {
		return true;
	}

}
