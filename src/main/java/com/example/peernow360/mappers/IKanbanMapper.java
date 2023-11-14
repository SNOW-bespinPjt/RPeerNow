package com.example.peernow360.mappers;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.dto.BurnDownDto;
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
    public List<BacklogDto> showOtherInfo(int project_no);

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
    public List<BurnDownDto> compareEndTime(String nowTime);

    /*
     * 번다운 차트 생성
     */
    public int createBurndown(SprintDto sprintInfo);

    /*
     * 번다운 차트 최신화(09:00 마다)
     */
    public void updateBurndown(BurnDownDto burnDownDto);

    /*
     * 번다운 차트 가져오기
     */
    public List<BurnDownDto> searchBurndown(int sprint_no);

    /*
     * 현재 번다운 차트가 생성되었다.. 그러면 가장 최신꺼는 no값이 가장 높은 즉 Max값일것이다. 그것을 가져온다.
     */
    public int getMaxNo();

    /*
     * 가장 최신의 No값으로 수정해주는 작업
     */
    public void updateNo(int maxNo);

    /*
     * 프로젝트에 포함된 모든 번다운 차트 불러오기(30일치)
     */
    public List<BurnDownDto> searchAllBurndown(int project_No);

    /*
     * 프로젝트 내 존재하는 스프린트 백로그 총개수, 완료 개수, 진행중인 개수
     */
    public BurnDownDto selectBacklogStatusTotal(int project_no);

}
