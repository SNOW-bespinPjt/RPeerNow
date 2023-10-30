package com.example.peernow360.mappers;


import com.example.peernow360.dto.ProjectDto;
import com.example.peernow360.dto.TeamDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IProjectMapper {

//    void createProject(ProjectDto projectDto);

    ProjectDto projectDetail(int no);

    int createProject(ProjectDto projectDto);

    void createTeam(TeamDto teamDto);
}
