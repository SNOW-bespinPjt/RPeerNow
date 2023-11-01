package com.example.peernow360.controller;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.response.ResponseService;
import com.example.peernow360.service.BacklogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public String createBacklog(@RequestParam (value = "no") int sprint_no, @RequestPart BacklogDto backlogDto, @RequestPart MultipartFile[] files) {
        log.info("[BacklogController] createBacklog()");

        return backlogService.createNewBacklog(backlogDto, sprint_no, files);

    }




}
