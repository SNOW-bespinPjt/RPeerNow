package com.example.peernow360.service.impl;

import com.example.peernow360.dto.BacklogDto;

import java.util.List;

public interface IKanbanService {

    /*
     * 칸반보드 백로그 불러오기
     */
    public List<BacklogDto> searchBacklogs(int sprint_no);

    /*
     * 스프린트에 등록되지 않은 백로그 목록만 가져오기
     */
    public List<BacklogDto> searchBacklogsOther();

    /*
     * 스프린트에 미등록된 백로그를 등록
     */
    public String modifySprint(BacklogDto backlogDto);

    /*
     * 스프린트에 등록된 백로그를 미등록
     */
    public String modifyNone(BacklogDto backlogDto);

    /*
     * 번다운 차트 (매일 09:00에 실행)
     */
    public void updateBurnDown();

}
