package com.example.peernow360.service;

import com.example.peernow360.dto.ProjectDto;
import com.example.peernow360.dto.TeamDto;
import com.example.peernow360.mappers.IProjectMapper;
import com.example.peernow360.service.impl.IProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProjectService implements IProjectService {

    private final IProjectMapper iProjectMapper;

//    public ProjectDto createProject(ProjectDto projectDto) {
//        log.info("createProject()");
//
//        return iProjectMapper.createProject(projectDto);
//    }

    public int createProject(ProjectDto projectDto) {

        int result = iProjectMapper.createProject(projectDto);
        Map<String, Object> map = new HashMap<>();
        map.put("no", projectDto.getNo());

        iProjectMapper.createTeam(map);

        if (result > 0) {
            log.info("CREATE PROJECT SUCCESS");
        } else {
            log.info("CREATE PROJECT FAIL");
        }

        return result;
    }

    public ProjectDto projectDetail(int no) {
        log.info("projectDetail()");

        return iProjectMapper.projectDetail(no);
    }


//    public List<ProjectDto> createProject(ProjectDto projectDto) {
//        log.info("createProject()");
//
//        return iProjectMapper.createProject(projectDto);
//    }
}
