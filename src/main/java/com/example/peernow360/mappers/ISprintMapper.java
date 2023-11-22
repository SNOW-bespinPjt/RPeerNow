package com.example.peernow360.mappers;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.dto.SprintDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISprintMapper {

    /*
     * 스프린트 정보 생성
     */
    public int createSprintInfo(SprintDto sprintDto);

    /*
     * 스프린트 번호를 이용한 (전체)스프린트 상세 정보 불러오기
     */
    public List<SprintDto> searchSprintListDetail(int project_no);

    /*
     * 스프린트 상세 정보 불러오기(칸반보드)
     */
    public SprintDto searchSprintDetail(int no);

    /*
     * 스프린트 정보 수정
     */
    public int updateSprintInfo(SprintDto sprintDto);

    /*
     * 스프린트 정보 삭제
     */
    public int removeSprintInfo(SprintDto sprintDto);

    /*
     * 스프린트 정보 삭제 시 스프린트 번호가 등록된 백로그 0으로 만들기
     */
    public void removeSprintNoAtBLog(SprintDto sprintDto);

}
