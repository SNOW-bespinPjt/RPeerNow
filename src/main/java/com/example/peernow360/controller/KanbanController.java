package com.example.peernow360.controller;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.dto.BurnDownDto;
import com.example.peernow360.response.ListResponse;
import com.example.peernow360.response.ResponseService;
import com.example.peernow360.response.SingleResponse;
import com.example.peernow360.service.KanbanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/kanban")
@Tag(name = "kanban", description = "칸반보드")
public class KanbanController {

    private final KanbanService kanbanService;
    private final ResponseService responseService;
    private final Lock lock = new ReentrantLock();

    /*
     * 칸반보드에 보일 백로그 가져오기 (이전에 백로그 컨트롤러에도 해당 메서드 내용이 존재)
     * 하지만, 파일명까지 가져오기 때문에 만약 스프린트에 해당하는 백로그 목록만 필요한거면 해당 메서드를 사용.
     */
    @GetMapping("")
    @Operation(summary = "칸반보드 보여주기", description = "칸반보드 보여주기", tags = {"detail"})
    public ListResponse<BacklogDto> showKanban(@RequestParam (value = "sprint_no") int sprint_no) throws IOException {
        log.info("[KanbanController] showKanban()");

        return responseService.getListResponse(kanbanService.searchBacklogs(sprint_no));

    }

    /*
     * 스프린트에 등록되지 않은 백로그 목록만 가져오기
     */
    @GetMapping("/others")
    @Operation(summary = "등록되지 않은 백로그 목록", description = "등록되지 않은 백로그 목록", tags = {"detail"})
    public ListResponse<BacklogDto> showBacklogsOther(@RequestParam (value = "project_no") int project_no) throws IOException {
        log.info("[KanbanController] showKanban()");

        return responseService.getListResponse(kanbanService.searchBacklogsOther(project_no));

    }

    /*
     * 스프린트에 미등록된 백로그를 등록
     */
    @PutMapping("")
    @Operation(summary = "미등록된 백로그 등록", description = "미등록된 백로그 등록", tags = {"modify"})
    public String modifySprint(@RequestPart BacklogDto backlogDto) {
        log.info("[KanbanController] showKanban()");

        return kanbanService.modifySprint(backlogDto);

    }

    /*
     * 스프린트에 등록된 백로그를 미등록으로 변경
     */
    @PutMapping("/none")
    @Operation(summary = "등록된 백로그 미등록", description = "등록된 백로그 미등록", tags = {"modify"})
    public String modifyNone(@RequestPart BacklogDto backlogDto) {
        log.info("[KanbanController] modifyNone()");

        return kanbanService.modifyNone(backlogDto);

    }

    /*
     * 번다운 차트 (매일 10:00에 실행)
     * Synchronized와 ReentrantLock을 이용해 스케쥴링된 작업을 동시에 실행되지 않도록 동시성 제어를 함.
     */
    @Scheduled(cron = "0 0 10 * * *", zone = "Asia/Seoul")
    @Operation(summary = "번다운 차트 기록 스케쥬링", description = "번다운 차트 기록 스케쥬링", tags = {"create"})
    public synchronized void modifyBurnDown() {
        log.info("[KanbanController] updateBurnDown()");

        kanbanService.updateBurnDown();

    }

    /*
     * 번다운 차트 가져오기
     */
    @GetMapping("/burndown")
    @Operation(summary = "번다운 차트 기록 불러오기", description = "번다운 차트 기록 불러오기", tags = {"detail"})
    public ListResponse<BurnDownDto> callBurndown(@RequestParam (value = "sprint_no") int sprint_no) {
        log.info("[KanbanController] callBurndown()");

        return responseService.getListResponse(kanbanService.callInBurndown(sprint_no));

    }

    /*
     * 프로젝트 번호에 속해있는 전체 번다운 차트 가져오기(30일치)
     */
    @GetMapping("/allburndown")
    @Operation(summary = "전체 번다운 차트 기록 불러오기", description = "전체 번다운 차트 기록 불러오기", tags = {"detail"})
    public ListResponse<BurnDownDto> callAllBurndown(@RequestParam (value = "project_no") int project_no) {
        log.info("[KanbanController] callBurndown()");

        return responseService.getListResponse(kanbanService.callInAllBurndown(project_no));

    }

    /*
     * 프로젝트 내 존재하는 스프린트 백로그 총개수, 완료 개수, 진행중인 개수
     */
    @GetMapping("/ptotal")
    @Operation(summary = "프로젝트 내 존재하는 스프린트 백로그 총개수, 완료 개수, 진행중인 개수", description = "프로젝트 내 존재하는 스프린트 백로그 총개수, 완료 개수, 진행중인 개수", tags = {"detail"})
    public SingleResponse<BurnDownDto> backlogStatusTotal(@RequestParam (value="project_no") int project_no) {
        log.info("[KanbanController] backlogStatusTotal()");

        return responseService.getSingleResponse(kanbanService.backlogStatusTotal(project_no));

    }

    /*
     * 프로젝트 내 존재하는 각 스프린트들의 총 개수 및 완료 백로그 개수
     */
    @GetMapping("/barburndown")
    @Operation(summary = "프로젝트 내 존재하는 각 스프린트들의 총 개수 및 완료 백로그 개수", description = "프로젝트 내 존재하는 각 스프린트들의 총 개수 및 완료 백로그 개수", tags = {"detail"})
    public ListResponse<BurnDownDto> backlogBarBurndown(@RequestParam (value="project_no") int project_no) {
        log.info("[KanbanController] backlog()");

        return responseService.getListResponse(kanbanService.selectBarBurndown(project_no));

    }

}
