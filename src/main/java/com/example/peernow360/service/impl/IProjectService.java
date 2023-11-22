package com.example.peernow360.service.impl;

import com.example.peernow360.dto.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IProjectService {

//    int createProject(ProjectDto project, TeamDto team);

    int createProject(Map<String, Object> map, ProjectDto project, List<TeamDto> teams, AcceptTeamDto acceptTeamDto);

    ProjectDto projectDetail(int no);

    int modifyProject(ProjectDto projectDto);

    int acceptProject(int no, String user_id, String role);

    int declineProject(int no, String user_id);

    int deleteProject(int no, String user_id);

    List<UserMemberDto> getPeer(String peerName) throws IOException;

    List<ProjectDto> projectList(String user_id);

    List<UserMemberDto> peerlist(int no) throws IOException;

    List<InvitationDto> projectInvitation(InvitationDto invitationDto) throws IOException;

//    List<ProjectDto> createProject(ProjectDto projectDto);
}
