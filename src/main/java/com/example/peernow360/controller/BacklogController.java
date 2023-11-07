package com.example.peernow360.controller;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.dto.FileDto;
import com.example.peernow360.response.ListResponse;
import com.example.peernow360.response.MapResponse;
import com.example.peernow360.response.ResponseService;
import com.example.peernow360.service.BacklogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/project/backlog")
@Tag(name = "backlog", description = "백로그")
public class BacklogController {

    private final BacklogService backlogService;
    private final ResponseService responseService;

    /*
     * 백로그 생성
     */
    @PostMapping("")
    @Transactional
    @Operation(summary = "백로그 생성", description = "백로그 생성", tags = {"create"})
    public String createBacklog(@RequestParam (value = "project_no") int project_no,
                                @RequestParam (value = "sprint_no", required = false) String sprint_no, //백로그만 생성할 시 -> sprintnumber을 받지 않는다.
                                @RequestPart BacklogDto backlogDto,
                                @RequestPart (required = false) List<FileDto> fileDto) {
        log.info("[BacklogController] createBacklog()");

        return backlogService.createNewBacklog(backlogDto,project_no, sprint_no, fileDto);

    }

    /*
     * 백로그 전체 리스트 불러오기
     */
    @GetMapping("/list")
    @Transactional(readOnly = true)
    @Operation(summary = "백로그 전체 리스트 불러오기", description = "백로그 전체 리스트 불러오기", tags = {"detail"})
    public MapResponse<String,Object> backlogList(@RequestParam (value = "sprint_no") int sprint_no) {
        log.info("[BacklogController] backlogDetail()");

        return responseService.getMapResponse( backlogService.backlogListInfo(sprint_no));

    }

    /*
     * 백로그 상세 페이지 불러오기 ( title을 클릭시 이동하는)
     */
    @GetMapping("")
    @Transactional(readOnly = true)
    @Operation(summary = "백로그 상세 정보 불러오기", description = "백로그 상세 정보 불러오기", tags = {"detail"})
    public MapResponse<String,Object> backlogDetail(@RequestParam (value = "no") int no) {
        log.info("[BacklogController] backlogDetailByNo()");

        return responseService.getMapResponse(backlogService.backlogDetailInfo(no));

    }

    /*
     * 프로젝트안에 담겨있는 전체 백로그 불러오기
     */
    @GetMapping("/all")
    public ListResponse<BacklogDto> searchAllBacklog(@RequestParam (value = "project_no") int project_no) {
        log.info("[BacklogController] searchAllBacklog()");

        return responseService.getListResponse(backlogService.searchALlbacklogList(project_no));

    }

    /*
     * 스프린트에 맞는 오늘 날짜에 진행중인 백로그 값만 가져오기.
     */
    @GetMapping("/ing")
    @Operation(summary = "백로그 진쟁중인 백로그만 불러오기", description = "백로그 진쟁중인 백로그만 불러오기", tags = {"detail"})
    public ListResponse<BacklogDto> backlogDayAndIng(@RequestParam (value = "sprint_no") int sprint_no) {
        log.info("[BacklogController] backlogDayAndIngInfo()");

        return responseService.getListResponse(backlogService.backlogDayAndIngInfo(sprint_no));

    }

    /*
     * 백로그 status(상태 ex. todo -> 해야할 일 , ing -> 진행중, done -> 완료)
     */
    @PutMapping("/status")
    @Operation(summary = "백로그 상태(status)수정", description = "백로그 상태(status)수정", tags = {"modify"})
    public String backlogModifyStatus(@RequestParam (value = "no") int no ,
                                      @RequestParam (value = "status") String status) {
        log.info("[BacklogController] backlogModifyStatus()");

        return backlogService.backlogUpdateStatus(no, status);

    }

    /*
     * 백로그 수정
     */
    @PutMapping("")
    @Transactional
    @Operation(summary = "백로그 수정", description = "백로그 수정", tags = {"modify"})
    public String backlogModify(@RequestParam (value = "no") int no,
                                @RequestPart BacklogDto backlogDto,
                                @RequestPart (required = false) List<FileDto> fileDto) {
        log.info("[BacklogController] backlogModify()");

        return backlogService.backlogUpdateInfo(backlogDto, no, fileDto);

    }

    /*
     * 백로그 삭제
     */
    @DeleteMapping("")
    @Transactional
    @Operation(summary = "백로그 삭제", description = "백로그 삭제", tags = {"delete"})
    public String backlogDelete(@RequestParam (value = "no") int no) {
        log.info("[BacklogController] backlogDelete()");

        return backlogService.backlogDeleteInfo(no);

    }

}
