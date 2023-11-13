package com.example.peernow360.mappers;


import com.example.peernow360.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IProjectMapper {

//    void createProject(ProjectDto projectDto);



    int createProject(ProjectDto projectDto);

    void createTeam(TeamDto teamDto);

    ProjectDto projectDetail(int no);

    int modifyProject(ProjectDto projectDto);

    int acceptProject(Map<String, Object> map);

    int acceptTeam(AcceptTeamDto acceptTeamDto);

    int declineProject(Map<String, Object> map);

    int deleteProject(Map<String, Object> map);

    List<UserMemberDto> getPeer(String peerName);

    List<ProjectDto> projectList(String user_id);

    List<UserMemberDto> peerlist(Map<String, Object> map);

    List<InvitationDto> projectInvitation(InvitationDto invitationDto);



}
