package com.example.peernow360.mappers;


import com.example.peernow360.dto.ProjectDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IProjectMapper {

//    void createProject(ProjectDto projectDto);

    ProjectDto projectDetail(int no);


    int createProject(ProjectDto projectDto);
}
