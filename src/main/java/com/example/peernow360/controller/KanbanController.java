package com.example.peernow360.controller;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.response.ListResponse;
import com.example.peernow360.response.ResponseService;
import com.example.peernow360.service.KanbanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/kanban")
public class KanbanController {

    private final KanbanService kanbanService;
    private final ResponseService responseService;

    /*
     * 칸반보드에 보일 백로그 가져오기 (이전에 백로그 컨트롤러에도 해당 메서드 내용이 존재)
     * 하지만, 파일명까지 가져오기 때문에 만약 스프린트에 해당하는 백로그 목록만 필요한거면 해당 메서드를 사용.
     */
    @GetMapping("")
    public ListResponse<BacklogDto> showKanban(@RequestParam (value = "sprint_no") int sprint_no) {
        log.info("[KanbanController] showKanban()");

        return responseService.getListResponse(kanbanService.searchBacklogs(sprint_no));

    }

    /*
     * 스프린트에 등록되지 않은 백로그 목록만 가져오기
     */
    @GetMapping("/others")
    public ListResponse<BacklogDto> showBacklogsOther() {
        log.info("[KanbanController] showKanban()");

        return responseService.getListResponse(kanbanService.searchBacklogsOther());

    }

    /*
     * 스프린트에 미등록된 백로그를 등록
     */
    @PutMapping("")
    public String modifySprint(@RequestPart BacklogDto backlogDto) {
        log.info("[KanbanController] showKanban()");

        return kanbanService.modifySprint(backlogDto);

    }

    /*
     * 스프린트에 등록된 백로그를 미등록으로 변경
     */
    @PutMapping("/none")
    public String modifyNone(@RequestPart BacklogDto backlogDto) {
        log.info("[KanbanController] modifyNone()");

        return kanbanService.modifyNone(backlogDto);

    }

    /*
     * 번다운 차트 (매일 09:00에 실행)
     */
//    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    @PutMapping("/burndown")
    public void modifyBurnDown() {
        log.info("[KanbanController] updateBurnDown()");

        kanbanService.updateBurnDown();

        //이곳에서 DB에서 시작일과 종료일을 가져오는 로직을 작성.

    }



}
