package com.example.peernow360.service.impl;

import com.example.peernow360.dto.ProjectDto;
import com.example.peernow360.dto.TeamDto;

import java.util.List;
import java.util.Map;

public interface IProjectService {

//    int createProject(ProjectDto project, TeamDto team);

    int createProject(Map<String, Object> map, ProjectDto project, List<TeamDto> teams);

    ProjectDto projectDetail(int no);

    int modifyProject(ProjectDto projectDto);

    int acceptProject(int no, String user_id);

    int declineProject(int no, String user_id);

    int deleteProject(int no, String user_id);



//    List<ProjectDto> createProject(ProjectDto projectDto);
}
