package com.example.peernow360.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoomDto {

    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    /*
     * HashSet을 쓴 이유
     * Set이란? : 객체를 중복해서 저장할 수 없으며, 하나의 null 값만 저장할 수 있다.
     * 중복을 자동으로 제거해준다.
     * Set은 비선형 구조이기 때문에 순서의 개념과 인덱스가 존재하지 않는다.
     * 값을 추가 / 삭제 하는 경우 Set 내부에 해당 값을 검색하여 해당 기능을 수행해야 한다.
     * 이로 인해 처리 속도가 List구조보다 느린 단점이 존재.
     */

    //방 한개마다 여러사용자들을 Set형태로 가지고 있습니다.
    @Builder
    public ChatRoomDto(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;

    }

}
