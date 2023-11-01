package com.example.peernow360.controller;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.response.ListResponse;
import com.example.peernow360.response.ResponseService;
import com.example.peernow360.response.SingleResponse;
import com.example.peernow360.service.BacklogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/project/backlog")
public class BacklogController {

    private final BacklogService backlogService;
    private final ResponseService responseService;

    /*
     * 백로그 생성
     */
    @PostMapping("")
    @Transactional
    public String createBacklog(@RequestParam (value = "sprintNumber") int sprint_no,
                                @RequestPart BacklogDto backlogDto,
                                @RequestPart MultipartFile[] files) {
        log.info("[BacklogController] createBacklog()");

        User user_info = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = user_info.getUsername();

        return backlogService.createNewBacklog(backlogDto, sprint_no, user_id, files);

    }

    /*
     * 백로그 전체 리스트 불러오기
     */
    @GetMapping("/list")
    public ListResponse<BacklogDto> backlogList(@RequestParam (value = "sprintNumber") int sprint_no) {
        log.info("[BacklogController] backlogDetail()");

        return responseService.getListResponse(backlogService.backlogListInfo(sprint_no));

    }

    /*
     * 백로그 상세 페이지 불러오기 ( title을 클릭시 이동하는)
     */
    @GetMapping("")
    public SingleResponse<BacklogDto> backlogDetail(@RequestParam (value = "backlogNumber") int no) {
        log.info("[BacklogController] backlogDetailByNo()");

        return responseService.getSingleResponse(backlogService.backlogDetailInfo(no));

    }

    /*
     * 백로그 status(상태 ex. todo -> 해야할 일 , ing -> 진행중, done -> 완료)
     */
    @PutMapping("/status")
    public String backlogModifyStatus(@RequestParam (value = "backlogNumber") int no ,
                                      @RequestParam (value = "status") String status) {
        log.info("[BacklogController] backlogModifyStatus()");

        return backlogService.backlogUpdateStatus(no, status);

    }

    /*
     * 백로그 수정
     */
    @PutMapping("")
    public String backlogModify(@RequestParam (value = "backlogNumber") int no,
                                @RequestPart BacklogDto backlogDto) {
        log.info("[BacklogController] backlogModify()");

        return backlogService.backlogUpdateInfo(backlogDto, no);

    }



}
