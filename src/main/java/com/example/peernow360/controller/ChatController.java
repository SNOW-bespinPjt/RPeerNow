package com.example.peernow360.controller;

import com.example.peernow360.dto.ChatRoomDto;
import com.example.peernow360.response.ListResponse;
import com.example.peernow360.response.ResponseService;
import com.example.peernow360.response.SingleResponse;
import com.example.peernow360.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final ResponseService responseService;

    @GetMapping("/chat_list")
    public ListResponse<ChatRoomDto> chatList() {
        log.info("[ChatController] chatList()");

        return responseService.getListResponse(chatService.findAllRoom());

    }

    // 방을 만들었으면 해당 방으로 이동
    @PostMapping("/createRoom")
    public SingleResponse<ChatRoomDto> createRoom() {
        log.info("[ChatController] createRoom()");

        return responseService.getSingleResponse(chatService.createRoom()); //만든 사람이 채팅방에 1번으로 들어가게 된다.

    }

    @GetMapping("/chat_room")
    public SingleResponse<ChatRoomDto> chatRoom(@RequestParam (value = "roomId") String roomId) {
        log.info("[ChatController] chatRoom()");

        return responseService.getSingleResponse(chatService.findRoomById(roomId));
        //현재 방에 들어오기위해서 필요한데...... 접속자 수 등등은 실시간으로 보여줘야 돼서 여기서는 못함

    }

}
