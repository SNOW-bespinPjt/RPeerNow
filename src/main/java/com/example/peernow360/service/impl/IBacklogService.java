package com.example.peernow360.service.impl;

import com.example.peernow360.dto.BacklogDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface IBacklogService {

    /*
     * 백로그 생성
     */
    public String createNewBacklog(BacklogDto backlogDto, int sprint_no, String user_id, MultipartFile[] files);

    /*
     * 백로그 리스트 불러오기
     */
    public List<BacklogDto> backlogListInfo(int sprintNo);

    /*
     * 백로그 상세페이지 불러오기
     */
    public Object backlogDetailInfo(int no);

    /*
     * 백로그 status(상태 ex. todo -> 해야할 일 , ing -> 진행중, done -> 완료) 변경
     */
    public String backlogUpdateStatus(int no, String status);

    /*
     * 백로그 상세정보 수정
     */
    public String backlogUpdateInfo(BacklogDto backlogDto, int no);
}
