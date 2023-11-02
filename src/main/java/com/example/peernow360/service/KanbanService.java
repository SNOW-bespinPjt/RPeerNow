package com.example.peernow360.service;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.mappers.IKanbanMapper;
import com.example.peernow360.service.impl.IKanbanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

        if (StringUtils.hasText(backlogDtos.get(0).getTitle())) {
            log.info("스프린트에 해당하는 백로그를 불러오는데 성공하였습니다.");

            return backlogDtos;

        } else {
            log.info("스프린트에 해당하는 백로그를 불러오는데 실패하였습니다.");

            return null;

        }

    }

    @Override
    public List<BacklogDto> searchBacklogsOther() {
        log.info("[KanbanService] searchBacklogsOther()");

        List<BacklogDto> backlogDtos = iKanbanMapper.showOtherInfo();

        if (StringUtils.hasText(backlogDtos.get(0).getTitle())) {
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

}
