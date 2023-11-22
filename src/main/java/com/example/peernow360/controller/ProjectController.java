package com.example.peernow360.controller;

import com.example.peernow360.dto.*;
import com.example.peernow360.response.ListResponse;
import com.example.peernow360.response.ResponseResult;
import com.example.peernow360.response.ResponseService;
import com.example.peernow360.response.SingleResponse;
import com.example.peernow360.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
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

    @PostMapping("")
    @Operation(summary = "프로젝트 생성", description = "프로젝트 생성", tags = {"create"})
    public String createProject(@RequestBody Map<String, Object> map) {
        log.info("createProject()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        ProjectDto project = new ProjectDto();
        project.setUser_id(user_id);

        List<TeamDto> teams = new ArrayList<>();

        AcceptTeamDto acceptTeamDto = new AcceptTeamDto();
        acceptTeamDto.setUser_id(user_id);

        int result = projectService.createProject(map, project, teams, acceptTeamDto);

        return ResponseResult.result(result);
    }

    @GetMapping("/peer")
    @Operation(summary = "팀원 조회", description = "팀원 조회", tags = {"detail"})
    public ListResponse<UserMemberDto> getPeer(@RequestParam("peerName") String peerName) throws IOException {
        log.info("getPeer()");

        return responseService.getListResponse(projectService.getPeer(peerName));
    }

    @GetMapping("")
    @Operation(summary = "프로젝트 내용", description = "프로젝트 내용", tags = {"detail"})
    public SingleResponse<ProjectDto> projectDetail(@RequestParam("projectNumber") int no) {
        log.info("projectDetail()");

        return responseService.getSingleResponse(projectService.projectDetail(no));
    }

    @GetMapping("/list")
    @Operation(summary = "프로젝트 리스트", description = "프로젝트 리스트", tags = {"detail"})
    public ListResponse<ProjectDto> projectList() {
        log.info("projectList()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();
        log.info("userInfo in List: {}", userInfo);
        return responseService.getListResponse(projectService.projectList(user_id));
    }

    @GetMapping("/peerlist")
    @Operation(summary = "프로젝트 팀원", description = "프로젝트 팀원", tags = {"detail"})
    public ListResponse<UserMemberDto> peerlist(@RequestParam("projectNumber") int no) throws IOException {
        log.info(("peerlist()"));

        return responseService.getListResponse(projectService.peerlist(no));
    }

    @PutMapping("/change")
    @Operation(summary = "프로젝트 수정", description = "프로젝트 수정", tags = {"modify"})
    public String modifyProject(@RequestParam("projectNumber") int no, @RequestBody ProjectDto projectDto) {
        log.info("modifyProject()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        projectDto.setUser_id(user_id);
        projectDto.setNo(no);

        int result = projectService.modifyProject(projectDto);

        return ResponseResult.result(result);
    }

    @PutMapping("/accept")
    @Operation(summary = "프로젝트 수락", description = "프로젝트 수락", tags = {"modify"})
    public String acceptProject(@RequestParam("projectNumber") int no, @RequestParam("role") String role) {
        log.info("acceptProject()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        int result = projectService.acceptProject(no, user_id, role);

        return ResponseResult.result(result);
    }

    @PutMapping("/decline")
    @Operation(summary = "프로젝트 거절", description = "프로젝트 거절", tags = {"modify"})
    public String declineProject(@RequestParam("projectNumber") int no) {
        log.info("declineProject()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        int result = projectService.declineProject(no, user_id);

        return ResponseResult.result(result);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "프로젝트 삭제", description = "프로젝트 삭제", tags = {"delete"})
    public String deleteProject(@RequestParam("projectNumber") int no){
        log.info(("deleteProject()"));

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        int result = projectService.deleteProject(no, user_id);

        return ResponseResult.result(result);
    }

    @GetMapping("/invitation")
    @Operation(summary = "프로젝트 초대", description = "프로젝트 초대", tags = {"detail"})
    public ListResponse<InvitationDto> projectInvitation() throws IOException {
        log.info(("projectInvitation()"));

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        InvitationDto invitationDto = new InvitationDto();
        invitationDto.setUser_id(user_id);

        return responseService.getListResponse(projectService.projectInvitation(invitationDto));
    }


}
