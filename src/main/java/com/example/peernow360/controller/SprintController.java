package com.example.peernow360.controller;

import com.example.peernow360.dto.SprintDto;
import com.example.peernow360.response.ListResponse;
import com.example.peernow360.response.ResponseService;
import com.example.peernow360.service.SprintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public Map<String, Object> createSprint(@RequestParam (value="no") int project_no , @RequestPart SprintDto sprintDto) {
        log.info("[SprintController] createSprint()");

        return sprintService.createNewSprint(sprintDto, project_no);

    }

    /*
     * 스프린트 상세보기
     */
    @GetMapping("")
    public ListResponse<SprintDto> sprintDetail(@RequestParam (value = "no") int project_no) {
        log.info("[SprintController] SprintDetail()");

        return responseService.getListResponse(sprintService.sprintDetailInfo(project_no));

    }

    /*
     * 스프린트 수정
     */
    @PutMapping("")
    public Map<String, Object> modifySprint(@RequestParam (value="no") int no , @RequestPart SprintDto sprintDto) {
        log.info("[SprintController] modifySprint()");

        return sprintService.updateSprint(sprintDto, no);

    }

    /*
     * 스프린트 삭제
     */
    @DeleteMapping("")
    public Map<String, Object> deleteSprint(@RequestParam (value="no") int no) {
        log.info("[SprintController] deleteSprint()");

        return sprintService.removeSprint(no);

    }

}
