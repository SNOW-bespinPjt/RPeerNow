package com.example.peernow360.controller;

import com.example.peernow360.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@Log4j2
@RequestMapping("/user")
@Tag(name = "project", description = "프로젝트")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @PostMapping("")
    @Operation(summary = "프로젝트 생성", description = "프로젝트 생성", tags = {"create"})
    public String createProject() {
        log.info("createProject()!!");

        return "test";
    }
}
