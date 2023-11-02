package com.example.peernow360.service.impl;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.dto.SprintDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISprintService {

    /*
     * 새로운 스프린트 생성
     */
    public String createNewSprint(SprintDto sprintDto, int project_no, List<BacklogDto> backlogDto);

    /*
     * 스프린트 번호를 이용한 (전체)스프린트 상세 정보 불러오기
     */
    public List<SprintDto> sprintDetailInfo(int project_no);

    /*
     * 스프린트 정보 수정
     */
    public String updateSprint(SprintDto sprintDto, int no);

    /*
     * 스프린트 정보 수정
     */
    public String removeSprint(int no);


}
