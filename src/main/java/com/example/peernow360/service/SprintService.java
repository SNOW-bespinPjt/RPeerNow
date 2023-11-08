package com.example.peernow360.service;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.dto.SprintDto;
import com.example.peernow360.mappers.IBacklogMapper;
import com.example.peernow360.mappers.IKanbanMapper;
import com.example.peernow360.mappers.ISprintMapper;
import com.example.peernow360.service.impl.ISprintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class SprintService implements ISprintService {

    private final ISprintMapper iSprintMapper;
    private final IBacklogMapper iBacklogMapper;
    private final IKanbanMapper iKanbanMapper;

    @Override
    public String createNewSprint(SprintDto sprintDto, int project_no, int[] backlogDto) {
        log.info("[SprintService] createNewSprint()");

        Map<String, Integer> msgData = new HashMap<>();

        User user_info = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = user_info.getUsername();
        log.info("[SprintService]user_id : " + user_id);

        // 프로젝트 넘버 DTO에 삽입
        sprintDto.setProject_no(project_no);
        sprintDto.setUser_id(user_id);

        int result = iSprintMapper.createSprintInfo(sprintDto);
        log.info("sprintDto.getNo() : " + sprintDto.getNo());
        msgData.put("sprint_no",sprintDto.getNo());

//        msgData.put("project_no", project_no);

        if(result > 0) {
            log.info("스프린트 생성에 성공하였습니다.");

            if(backlogDto != null) {
                // 스프린트 생성 시 담은 백로그들 스프린트 번호 업데이트.
                for(int no : backlogDto) {
                    msgData.put("backlog_no", no);
                    iBacklogMapper.updateBacklogSprint(msgData);

                }

            }

            SprintDto sprintInfo = iSprintMapper.searchSprintDetail(sprintDto.getNo());
            log.info("sprintINfo : " + sprintInfo.getNo());

            int isCreate = iKanbanMapper.createBurndown(sprintInfo);

            if(isCreate > 0) {
                log.info("번다운 차트를 생성하는데 성공하였습니다.");

                /*
                 * 현재 번다운 차트가 생성되었다.. 그러면 가장 최신꺼는 no값이 가장 높은 즉 Max값일것이다. 그것을 가져온다.
                 */
                int maxNo = iKanbanMapper.getMaxNo();

                /*
                 * 가장 최신의 no값을 ori_no에다가 넣어줄 것이다.
                 */
                iKanbanMapper.updateNo(maxNo);

            }

            return "success";

        } else {
            log.info("스프린트 생성에 실패하였습니다.");

            return "fail";

        }

    }

    @Override
    public List<SprintDto> sprintListDetailInfo(int project_no) {
        log.info("[SprintService] sprintDetailInfo()");

        List<SprintDto> sprintDtos  = iSprintMapper.searchSprintListDetail(project_no);

        if(sprintDtos != null && sprintDtos.size() > 0) {
            log.info("스프린트 정보를 불러오는데 성공하였습니다.");

            return sprintDtos;

        } else {
            log.info("스프린트 정보를 불러오는데 실패하였습니다.");

            return null;

        }

    }

    @Override
    public SprintDto sprintDetailInfo(int no) {
        log.info("[SprintService] sprintDetailInfo()");

        SprintDto sprintDto = iSprintMapper.searchSprintDetail(no);

        if(sprintDto != null) {
            log.info("스프린트 상세 정보를 불러오는데 성공하였습니다.");

            return sprintDto;

        } else {
            log.info("스프린트 상세 정보를 불러오는데 실패하였습니다.");

            return null;
        }

    }

    @Override
    public String updateSprint(SprintDto sprintDto, int no) {
        log.info("[SprintService] sprintDetailInfo()");

        User user_info = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = user_info.getUsername();

        sprintDto.setNo(no);
        sprintDto.setUser_id(user_id);

        int result = iSprintMapper.updateSprintInfo(sprintDto);

        if(result > 0) {
            log.info("스프린트 정보를 수정하는데 성공하였습니다.");

            return "success";

        } else {
            log.info("스프린트 정보를 수정하는데 실패하였습니다.");

            return "fail";

        }


    }

    @Override
    public String removeSprint(SprintDto sprintDto) {
        log.info("[SprintService] removeSprint()");

        User user_info = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = user_info.getUsername();

        sprintDto.setUser_id(user_id);

        int result = iSprintMapper.removeSprintInfo(sprintDto);

        if(result > 0) {
            log.info("스프린트 정보를 삭제하는데 성공하였습니다.");

            return "success";

        } else {
            log.info("스프린트 정보를 삭제하는데 실패하였습니다.");

            return "fail";

        }

    }


}
