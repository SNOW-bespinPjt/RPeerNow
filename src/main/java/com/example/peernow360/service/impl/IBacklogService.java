package com.example.peernow360.service.impl;

import com.example.peernow360.dto.BacklogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface IBacklogService {

    /*
     * 백로그 생성
     */
    public String createNewBacklog(BacklogDto backlogDto, int sprint_no, MultipartFile[] files);

    /*
     * 파일 로컬에 저장
     */
    public List<String> storeFile(MultipartFile[] files);

}
