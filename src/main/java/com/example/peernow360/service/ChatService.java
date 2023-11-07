package com.example.peernow360.service;

import com.example.peernow360.dto.ChatRoomDto;
import com.example.peernow360.mappers.IUserMemberMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoomDto> chatRooms;
    private final IUserMemberMapper iUserMemberMapper;

    @PostConstruct
    private void init() {
        log.info("[ChatService] init()");

        // LinkedHashMap은 put을 통해 입력된 순서대로 Key가 보장 (기존 Map은 순서성을 보장하지 않음)
        chatRooms = new LinkedHashMap<>();

    }

    public List<ChatRoomDto> findAllRoom() {
        log.info("[ChatService] findAllRoom()");

        return new ArrayList<>(chatRooms.values());

    }

    public ChatRoomDto findRoomById(String roomId) {
        log.info("[ChatService] findRoomById()");

        return chatRooms.get(roomId);

    }

    public ChatRoomDto createRoom() {
        log.info("[ChatService] createRoom()");

        User user_info = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = user_info.getUsername();

        String name = iUserMemberMapper.selectUserName(user_id);

        String randomId = UUID.randomUUID().toString();
        ChatRoomDto chatRoom = ChatRoomDto.builder()
                .roomId(randomId)
                .name(name)
                .build();

        chatRooms.put(randomId, chatRoom);

        return chatRoom;

    }

}
