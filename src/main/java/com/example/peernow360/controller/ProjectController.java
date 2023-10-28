package com.example.peernow360.controller;

import com.example.peernow360.dto.ProjectDto;
import com.example.peernow360.response.CommonResponse;
import com.example.peernow360.response.ListResponse;
import com.example.peernow360.response.ResponseService;
import com.example.peernow360.response.SingleResponse;
import com.example.peernow360.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/project")
@Tag(name = "project", description = "프로젝트")
public class ProjectController {

    private final ResponseService responseService;
    private final ProjectService projectService;

    @PostMapping("")
    @Operation(summary = "프로젝트 생성", description = "프로젝트 생성", tags = {"create"})
    public String createProject(ProjectDto projectDto) {
        log.info("createProject()");

        int result = projectService.createProject(projectDto);

        String message;
        if (result > 0) {
            message = "success";
        } else {
            message = "fail";
        }

        return message;

//        return responseService.getSingleResponse(projectService.createProject(projectDto));
//        return projectService.createProject(projectDto);
    }

    @GetMapping("")
    @Operation(summary = "프로젝트 내용", description = "프로젝트 내용", tags = {"detail"})
    public SingleResponse<ProjectDto> projectDetail(@RequestParam("projectNumber") int no) {
        log.info("projectDetail()");

        return responseService.getSingleResponse(projectService.projectDetail(no));
    }
}
