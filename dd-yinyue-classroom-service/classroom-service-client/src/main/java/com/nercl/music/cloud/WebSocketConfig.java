package com.nercl.music.cloud;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.nercl.music.websocket.ChatWebSocketHandler;
import com.nercl.music.websocket.WebSocketHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(chatWebSocketHandler(), "/ws/wss").addInterceptors(webSocketHandshakeInterceptor());
		registry.addHandler(chatWebSocketHandler(), "/sockjs/wss").addInterceptors(webSocketHandshakeInterceptor())
		        .withSockJS();
	}

	@Bean
	public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(3276800);
		container.setMaxBinaryMessageBufferSize(3276800);
		return container;
	}

	@Bean
	public ChatWebSocketHandler chatWebSocketHandler() {
		return new ChatWebSocketHandler();
	}

	@Bean
	public WebSocketHandshakeInterceptor webSocketHandshakeInterceptor() {
		return new WebSocketHandshakeInterceptor();
	}
	
}
