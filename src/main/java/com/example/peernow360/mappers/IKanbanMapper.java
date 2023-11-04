package com.example.peernow360.mappers;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.dto.SprintDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IKanbanMapper {

    /*
     * 칸반보드 백로그 불러오기
     */
    public List<BacklogDto> showKanbanInfo(int sprintNo);

    /*
     * 스프린트에 등록되지 않은 백로그 목록만 가져오기
     */
    public List<BacklogDto> showOtherInfo();

    /*
     * 스프린트에 미등록된 백로그를 등록
     */
    public int updateSprint(BacklogDto backlogDto);

    /*
     * 스프린트에 등록된 백로그를 미등록
     */
    public int updateNone(BacklogDto backlogDto);

    /*
     * 현재시간과 스프린트 종료 시간과 비교해서 아직 진행중인
     * 스프린트 가져오기
     */
    public SprintDto compareEndTime(String nowTime);

    /*
     * 번다운 차트 생성
     */
    public int createBurndown(SprintDto sprintInfo);

}