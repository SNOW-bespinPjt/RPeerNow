package com.example.peernow360.service;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.dto.BurnDownDto;
import com.example.peernow360.dto.SprintDto;
import com.example.peernow360.mappers.IKanbanMapper;
import com.example.peernow360.service.impl.IKanbanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class KanbanService implements IKanbanService {

    private final IKanbanMapper iKanbanMapper;

    @Override
    public List<BacklogDto> searchBacklogs(int sprint_no) {
        log.info("[KanbanService] searchBacklogs()");

        List<BacklogDto> backlogDtos = iKanbanMapper.showKanbanInfo(sprint_no);

        if (backlogDtos != null && backlogDtos.size() > 0) {
            log.info("스프린트에 해당하는 백로그를 불러오는데 성공하였습니다.");

            return backlogDtos;

        } else {
            log.info("스프린트에 해당하는 백로그를 불러오는데 실패하였습니다.");

            return null;

        }

    }

    @Override
    public List<BacklogDto> searchBacklogsOther(int project_no) {
        log.info("[KanbanService] searchBacklogsOther()");

        List<BacklogDto> backlogDtos = iKanbanMapper.showOtherInfo(project_no);

        if (backlogDtos != null && backlogDtos.size() > 0) {
            log.info("스프린트에 해당하는 백로그를 불러오는데 성공하였습니다.");

            return backlogDtos;

        } else {
            log.info("스프린트에 해당하는 백로그를 불러오는데 실패하였습니다.");

            return null;

        }

    }

    @Override
    public String modifySprint(BacklogDto backlogDto) {
        log.info("[KanbanService] modifySprint()");

        int result = iKanbanMapper.updateSprint(backlogDto);

        if(result > 0) {
            log.info("백로그 스프린트 등록에 성공하였습니다.");

            return "success";

        } else {
            log.info("백로그 스프린트 등록에 실패하였습니다.");

            return "fail";

        }

    }

    @Override
    public String modifyNone(BacklogDto backlogDto) {
        log.info("[KanbanService] modifyNone()");

        int result = iKanbanMapper.updateNone(backlogDto);

        if(result > 0) {
            log.info("백로그 스프린트 미등록에 성공하였습니다.");

            return "success";

        } else {
            log.info("백로그 스프린트 미등록에 실패하였습니다.");

            return "fail";

        }

    }

    @Override
    public int updateBurnDown() {
        log.info("[KanbanService] updateBurnDown()");

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // LocalDate 와 LocalTime을 LocalDateTime으로 합치기
        LocalDateTime currentDateTime  = LocalDateTime.of(currentDate, currentTime);

        // LocalDateTime을 원하는 형식의 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowTime = currentDateTime.format(formatter);

        log.info("nowTime: " + nowTime);

        List<BurnDownDto> burnDownDtos = iKanbanMapper.compareEndTime(nowTime);
        if(burnDownDtos != null && burnDownDtos.size() > 0) {
            log.info("현재 진행중인 스프린트가 존재합니다");
            log.info("SprintDto : " + burnDownDtos);

            log.info("sprintDto.get(0).getNo(); : " + burnDownDtos.get(0).getNo());

            for(BurnDownDto burnDownDto : burnDownDtos) {
                iKanbanMapper.updateBurndown(burnDownDto);

            }

            return 1;

        }

        log.info("현재 진행중인 스프린트가 존재하지 않아 스케쥴러가 작동하지 않습니다.");

        return 0;

    }

    @Override
    public List<BurnDownDto> callInBurndown(int sprint_no) {
        log.info("[KanbanService] updateBurnDown()");

        List<BurnDownDto> burnDownDtos = iKanbanMapper.searchBurndown(sprint_no);

        if(burnDownDtos != null && burnDownDtos.size() > 0) {
            log.info("번다운 차트 기록을 불러오는데 성공하였습니다.");

            return burnDownDtos;

        } else {
            log.info("번다운 차트 기록을 불러오는데 실패하였습니다.");

            return null;
        }

    }

}
