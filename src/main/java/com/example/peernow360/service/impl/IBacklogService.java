package com.example.peernow360.service.impl;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface IBacklogService {

    /*
     * 백로그 생성
     */
    public String createNewBacklog(BacklogDto backlogDto, int project_no, String sprint_no, MultipartFile[] fileDto) throws IOException;

    /*
     * 백로그 리스트 불러오기
     */
    public Map<String, Object> backlogListInfo(int sprint_no);

    /*
     * 백로그 상세페이지 불러오기
     */
    public Object backlogDetailInfo(int no);

    /*
     * 스프린트에 맞는 오늘 날짜에 진행중인 백로그 값만 가져오기.
     */
    public List<BacklogDto> backlogDayAndIngInfo(int sprintNo);

    /*
     * 백로그 status(상태 ex. todo -> 해야할 일 , ing -> 진행중, done -> 완료) 변경
     */
    public String backlogUpdateStatus(int no, String status);

    /*
     * 백로그 상세정보 수정
     */
    public String backlogUpdateInfo(BacklogDto backlogDto, int no, MultipartFile[] fileDto) throws IOException;

    /*
     * 백로그 삭제
     */
    public String backlogDeleteInfo(int no);

    /*
     * 프로젝트안에 있는 전체 백로그 불러오기
     */
    public Object searchALlbacklogList(int project_no);

}
