package com.example.peernow360.service.impl;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.dto.BurnDownDto;

import java.io.IOException;
import java.util.List;

public interface IKanbanService {

    /*
     * 칸반보드 백로그 불러오기
     */
    public List<BacklogDto> searchBacklogs(int sprint_no) throws IOException;

    /*
     * 스프린트에 등록되지 않은 백로그 목록만 가져오기
     */
    public List<BacklogDto> searchBacklogsOther(int project_no) throws IOException;

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
    public int updateBurnDown();

    /*
     * 스프린트 번호에 맞는 번다운 차트 가져오기
     */
    public List<BurnDownDto> callInBurndown(int sprint_no);

    /*
     * 프로젝트 번호에 맞는 모든 번다운 차트 가져오기(30일치)
     */
    public List<BurnDownDto> callInAllBurndown(int project_no);

    /*
     * 프로젝트 내 존재하는 스프린트 백로그 총개수, 완료 개수, 진행중인 개수
     */
    public BurnDownDto backlogStatusTotal(int project_no);

    /*
     * 프로젝트 내 존재하는 각 스프린트들의 총 개수 및 완료 백로그 개수
     */
    public List<BurnDownDto> selectBarBurndown(int project_no);

}
