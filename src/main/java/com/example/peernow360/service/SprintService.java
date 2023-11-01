package com.example.peernow360.service;

import com.example.peernow360.dto.SprintDto;
import com.example.peernow360.mappers.ISprintMapper;
import com.example.peernow360.service.impl.ISprintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class SprintService implements ISprintService {

    private final ISprintMapper iSprintMapper;
    @Override
    public String createNewSprint(SprintDto sprintDto, int project_no) {
        log.info("[SprintService] createNewSprint()");

        Map<String, Object> data = new HashMap<>();
        // 프로젝트 넘버 DTO에 삽입
        sprintDto.setProject_no(project_no);

        int result = iSprintMapper.createSprintInfo(sprintDto);

        if(result > 0) {
            log.info("스프린트 생성에 성공하였습니다.");

            return "SUCCESS";

        } else {
            log.info("스프린트 생성에 성공하였습니다.");

            return "FAIL";

        }

    }

    @Override
    public List<SprintDto> sprintDetailInfo(int project_no) {
        log.info("[SprintService] sprintDetailInfo()");

        List<SprintDto> sprintDtos  = iSprintMapper.sprintDetailInfoByNo(project_no);

        if(sprintDtos.get(0).getNo() > 0) {
            log.info("스프린트 정보를 불러오는데 성공하였습니다.");

            return sprintDtos;

        } else {
            log.info("스프린트 정보를 불러오는데 실패하였습니다.");

            return null;

        }

    }

    @Override
    public String updateSprint(SprintDto sprintDto, int no) {
        log.info("[SprintService] sprintDetailInfo()");

        Map<String, Object> data = new HashMap<>();

        sprintDto.setNo(no);

        int result = iSprintMapper.updateSprintInfo(sprintDto);

        if(result > 0) {
            log.info("스프린트 정보를 수정하는데 성공하였습니다.");

            return "SUCCESS";

        } else {
            log.info("스프린트 정보를 수정하는데 실패하였습니다.");

            return "FAIL";

        }


    }

    @Override
    public String removeSprint(int no) {
        log.info("[SprintService] removeSprint()");

        Map<String, Object> data = new HashMap<>();

        int result = iSprintMapper.removeSprintInfo(no);

        if(result > 0) {
            log.info("스프린트 정보를 삭제하는데 성공하였습니다.");

            return "SUCCESS";

        } else {
            log.info("스프린트 정보를 삭제하는데 실패하였습니다.");

            return "FAIL";

        }

    }

}
