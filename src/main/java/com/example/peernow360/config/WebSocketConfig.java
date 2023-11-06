//package com.example.peernow360.config;
//
//import com.example.peernow360.handler.WebSocketChatHandler;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
//@Configuration
//@Log4j2
//@EnableWebSocket    // 이게 websocket 서버로서 동작하겠다는 어노테이션
//@RequiredArgsConstructor
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    private final WebSocketChatHandler webSocketChatHandler;
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        log.info("[WebSocketConfig] registerWebSocketHandlers()");
//
//        registry.addHandler(webSocketChatHandler, "/ws/chat").setAllowedOrigins("*");
//        // handler 등록,  js에서 new WebSocket할 떄 경로 지정
//        // 다른 url에서도 접속할 수 있게(CORS 방지)
//
//    }
//}
