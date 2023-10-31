package com.example.peernow360.service.impl;

import com.example.peernow360.dto.SprintDto;

import java.util.Map;

public interface ISprintService {

    /*
     * 새로운 스프린트 생성
     */
    public Map<String, Object> createNewSprint(SprintDto sprintDto, int project_no);

    /*
     * 스프린트 번호를 이용한 (전체)스프린트 상세 정보 불러오기
     */
    public Map<String, Object> sprintDetailInfo(int project_no);

    /*
     * 스프린트 정보 수정
     */
    public Map<String, Object> updateSprint(SprintDto sprintDto, int no);

    /*
     * 스프린트 정보 수정
     */
    public Map<String, Object> removeSprint(int no);

}
