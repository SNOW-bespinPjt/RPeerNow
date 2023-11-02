package com.example.peernow360.controller;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.dto.SprintDto;
import com.example.peernow360.response.ListResponse;
import com.example.peernow360.response.MapResponse;
import com.example.peernow360.response.ResponseService;
import com.example.peernow360.response.SingleResponse;
import com.example.peernow360.service.SprintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/project/sprint")
public class SprintController {

    private final SprintService sprintService;
    private final ResponseService responseService;

    /*
     * 스프린트 생성
     */
    @PostMapping("")
    @Transactional
    public String createSprint(@RequestParam (value="project_no") int project_no ,
                               @RequestPart List<BacklogDto> backlogDto, // 이 파트는 슬비님이 작업하실때 어떤 방법으로 보낼지 알려드림 []배열이나, {}객체 둘중 하나. 현재는 객체로 해놨다.
                               @RequestPart SprintDto sprintDto) {
        log.info("[SprintController] createSprint()");

        return sprintService.createNewSprint(sprintDto, project_no, backlogDto);

    }

    /*
     * 스프린트 전체 보기
     */
    @GetMapping("/list")
    public ListResponse<SprintDto> sprintListDetail(@RequestParam (value = "project_no") int project_no) {
        log.info("[SprintController] sprintListDetail()");

        return responseService.getListResponse(sprintService.sprintListDetailInfo(project_no));

    }

    /*
     * 스프린트 상세 보기
     */
    @GetMapping("")
    @Transactional(readOnly = true)
    public SingleResponse<SprintDto> sprintDetail(@RequestParam (value = "sprint_no") int no) {
        log.info("[SprintController] SprintDetail()");

        return responseService.getSingleResponse(sprintService.sprintDetailInfo(no));

    }

    /*
     * 스프린트 수정
     */
    @PutMapping("")
    public String modifySprint(@RequestParam (value="no") int no , @RequestPart SprintDto sprintDto) {
        log.info("[SprintController] modifySprint()");

        return sprintService.updateSprint(sprintDto, no);

    }

    /*
     * 스프린트 삭제
     */
    @DeleteMapping("")
    public String deleteSprint(@RequestParam (value="no") int no) {
        log.info("[SprintController] deleteSprint()");

        return sprintService.removeSprint(no);

    }

}
