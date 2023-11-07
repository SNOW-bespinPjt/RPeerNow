package com.example.peernow360.handler;

import com.example.peernow360.dto.ChatMessage;
import com.example.peernow360.dto.ChatRoomDto;
import com.example.peernow360.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;

@Log4j2
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    /*
     * socket통신은 서버와 클라이언트가 1:N으로 관계를 맺습니다. 따라서 한 서버에 여러 클라이언트가 접속할 수 있으며,
     * 서버에는 여러 클라이언트가 발송한 메시지를 받아 처리해줄 Handler의 작성이 필요
     */

    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    }

    /*
     * HTTP 기반의 세션과는 목적과 사용되는 맥락이 다릅니다.
     * WebSocketSession은 WebSocket 통신의 연결을 관리하는 데 사용되며,
     * 사용자의 웹 애플리케이션 세션과는 직접적인 연관이 없습니다.
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("[WebSocketChatHandler] handleTextMessage()");

        String payload = message.getPayload();
        log.info("payload {},", payload);

        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoomDto room = chatService.findRoomById(chatMessage.getRoomId()); //방에 있는 현재 사용자 한명이 WebsocketSession

        Set<WebSocketSession> sessions = room.getSessions();

        if(chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            //사용자가 방에 입장하면  Enter메세지를 보내도록 해놓음. 이건 이건 새로운사용자가 socket 연결한 것이랑은 다름.
           //socket연결은 이 메세지 보내기전에 이미 되어있는 상태
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다."); //TALK일 경우 msg가 있을 거고, ENTER일 경우 메세지 없으니까 message set
            sendToEachSocket(sessions,new TextMessage(objectMapper.writeValueAsString(chatMessage)));

        } else if(chatMessage.getType().equals(ChatMessage.MessageType.QUIT)) {
            sessions.remove(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장했습니다.");
            sendToEachSocket(sessions, new TextMessage(objectMapper.writeValueAsString(chatMessage)));

        } else {
            sendToEachSocket(sessions, message); // 입장, 퇴장 아닐 때는 클라이언트로부터 온 메세지 그대로 전달.
        }

    }

    private void sendToEachSocket(Set<WebSocketSession> sessions, TextMessage message) {
        log.info("[WebSocketChatHandler] sendToEachSocket()");

        sessions.parallelStream().forEach(roomSession -> {
            try {
                roomSession.sendMessage(message);

            } catch (IOException e) {
                throw new RuntimeException(e);

            }

        });

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //javascript에서  session.close해서 연결 끊음. 그리고 이 메소드 실행.
        //session은 연결 끊긴 session을 매개변수로 이거갖고 뭐 하세요.... 하고 제공해주는 것 뿐
    }


}
