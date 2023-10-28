package com.example.peernow360.service.impl;

import com.example.peernow360.dto.ProjectDto;

import java.util.List;

public interface IProjectService {

    int createProject(ProjectDto projectDto);

    ProjectDto projectDetail(int no);


//    List<ProjectDto> createProject(ProjectDto projectDto);
}
