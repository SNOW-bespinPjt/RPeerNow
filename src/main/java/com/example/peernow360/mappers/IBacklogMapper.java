package com.example.peernow360.mappers;

import com.example.peernow360.dto.BacklogDto;
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
    public void insertBacklogFile(Map<String, Object> map);

    /*
     * 백로그 리스트 불러오기
     */
    public List<BacklogDto> searchBacklogList(int sprintNo);

    /*
     * 백로그 상세페이지 불러오기
     */
    public BacklogDto searchBacklogDetail(int no);

    /*
     * 백로그 status(상태 ex. todo -> 해야할 일 , ing -> 진행중, done -> 완료) 변경
     */
    public int updateBacklogStatus(BacklogDto backlogDto);

    /*
     * 백로그 상세정보 수정
     */
    public int updateBacklogInfo(BacklogDto backlogDto);

}
