package com.example.peernow360.mappers;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.dto.FileDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IBacklogMapper {

    /*
     * 백로그 생성
     */
    public int createBacklogInfo(BacklogDto backlogDto);

    /*
     * 백로그 파일 삽입(DB에 저장)
     */
    public int insertBacklogFile(Map<String, Object> msgData);

    /*
     * 백로그 리스트 불러오기
     */
    public List<BacklogDto> searchBacklogList(int sprint_no);

    /*
     * 백로그 파일 리스트 불러오기
     */
//    public List<FileDto> searchBacklogFiles(int sprint_no);

    /*
     * 백로그 상세페이지 불러오기
     */
    public BacklogDto searchBacklogDetail(int no);

    /*
     * 백로그 상세정보 파일 불러오기
     */
    public FileDto searchBacklogFile(int no);

    /*
     * 백로그 status(상태 ex. todo -> 해야할 일 , ing -> 진행중, done -> 완료) 변경
     */
    public int updateBacklogStatus(BacklogDto backlogDto);

    /*
     * 백로그 상세정보 수정
     */
    public int updateBacklogInfo(BacklogDto backlogDto);

    /*
     * 백로그 정보 삭제
     */
    public int removeBacklogInfo(int no);

    /*
     * 백로그 파일 삭제
     */
    public int removeBacklogFile(int no);

    /*
     * 백로그 스프린트 번호 변경
     */
    public void updateBacklogSprint(Map<String, Integer> msgData);

    /*
     * 백로그 당일 날짜에 마감되지 않으며 진행중인 스프린트에 존재하는 백로그 출력.
     */
    public List<BacklogDto> searchBacklogDayAndIng(int sprintNo);

    /*
     * 프로젝트안에 있는 전체 백로그 불러오기
     */
    public List<BacklogDto> searchAllBacklogList(int projectNo)
    ;
}
