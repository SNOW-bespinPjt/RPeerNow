package com.example.peernow360.controller;

import com.example.peernow360.dto.ProjectDto;
import com.example.peernow360.dto.TeamDto;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/project")
@Tag(name = "project", description = "프로젝트")
public class ProjectController {

    private final ResponseService responseService;
    private final ProjectService projectService;

    //dto 공통 구조

    @PostMapping("")
    @Operation(summary = "프로젝트 생성", description = "프로젝트 생성", tags = {"create"})
    public String createProject(@RequestBody Map<String, Object> map) {
        log.info("createProject()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        ProjectDto project = new ProjectDto();
        project.setUser_id(user_id);

        List<TeamDto> teams = new ArrayList<>();

        int result = projectService.createProject(map, project, teams);

        String message;
        if (result > 0) {
            message = "success";
        } else {
            message = "fail";
        }

        return message;
    }

    @GetMapping("")
    @Operation(summary = "프로젝트 내용", description = "프로젝트 내용", tags = {"detail"})
    public SingleResponse<ProjectDto> projectDetail(@RequestParam("projectNumber") int no) {
        log.info("projectDetail()");

        return responseService.getSingleResponse(projectService.projectDetail(no));
    }

    @PutMapping("/change")
    @Operation(summary = "프로젝트 수정", description = "프로젝트 수정", tags = {"modify"})
    public String modifyProject(@RequestParam("projectNumber") int no, @RequestPart ProjectDto projectDto) {
        log.info("modifyProject()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        projectDto.setUser_id(user_id);
        projectDto.setNo(no);

        int result = projectService.modifyProject(projectDto);

        String message;
        if (result > 0) {
            message = "success";
        } else {
            message = "fail";
        }

        return message;
    }

    @PutMapping("/accept")
    @Operation(summary = "프로젝트 수락", description = "프로젝트 수락", tags = {"modify"})
    public String acceptProject(@RequestParam("projectNumber") int no) {
        log.info("acceptProject()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        int result = projectService.acceptProject(no, user_id);

        String message;
        if (result > 0) {
            message = "success";
        } else {
            message = "fail";
        }

        return message;
    }

    @PutMapping("/decline")
    @Operation(summary = "프로젝트 거절", description = "프로젝트 거절", tags = {"modify"})
    public String declineProject(@RequestParam("projectNumber") int no) {
        log.info("declineProject()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        int result = projectService.declineProject(no, user_id);

        String message;
        if (result > 0) {
            message = "success";
        } else {
            message = "fail";
        }

        return message;
    }

    @DeleteMapping("/delete")
    @Operation(summary = "프로젝트 삭제", description = "프로젝트 삭제", tags = {"delete"})
    public String deleteProject(@RequestParam("projectNumber") int no){
        log.info(("deleteProject()"));

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        int result = projectService.deleteProject(no, user_id);

        String message;
        if (result > 0) {
            message = "success";
        } else {
            message = "fail";
        }

        return message;
    }

}
