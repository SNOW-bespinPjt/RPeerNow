package com.example.peernow360.service;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.dto.FileDto;
import com.example.peernow360.mappers.IBacklogMapper;
import com.example.peernow360.service.impl.IBacklogService;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class BacklogService implements IBacklogService {

    private final IBacklogMapper iBacklogMapper;

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Override
    public String createNewBacklog(BacklogDto backlogDto, int sprint_no, MultipartFile[] files) {
        log.info("[BacklogService] createNewBacklog()");

        backlogDto.setSprint_no(sprint_no);
        List<String> listFile = new ArrayList<>();

        try{
            int result = iBacklogMapper.createBacklogInfo(backlogDto);
            Map<String, Object> map = new HashMap<>();
            log.info("backlogDto.getNo() : " + backlogDto.getNo());
            map.put("backlog_no", backlogDto.getNo());

            //백로그 생성에 성공하면, 파일을 입력함
            if(result > 0) {
                log.info("CREATE BACKLOG SUCCESS");

                for(MultipartFile file : files) {
                    String fileName = UUID.randomUUID() + StringUtils.cleanPath(file.getOriginalFilename());
                    log.info("[BacklogService] fileName: " + fileName);

                    Path uploadPath = Paths.get(uploadDir);
                    log.info("[BacklogService]uploadPath: " + uploadPath);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);

                    }
                    Path filePath = uploadPath.resolve(fileName);
                    log.info("[BacklogService]filePath: " + filePath);

                    /*
                     * file.getInputStream(): MultipartFile 객체에서 파일의 내용을 읽어오기 위한 InputStream을 반환
                     * filePath: 복사할 대상 파일의 Path입니다. / Paths.get() 메서드나 다른 방법으로 filePath를 생성
                     * StandardCopyOption.REPLACE_EXISTING: 파일 복사 중에 대상 파일이 이미 존재하는 경우, 대상 파일을 덮어쓰기 위한 옵션입니다.
                     * 즉, 만약 대상 파일이 이미 존재한다면, 해당 옵션을 사용하여 새로운 파일로 대상 파일을 교체
                     */
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    listFile.add(fileName);
                    map.put("fileName" , fileName);

                    iBacklogMapper.insertBacklogFile(map);
                    log.info("YES~");

                }

                return "success";

            } else {
                log.info("CREATE BACKLOG FAIL");

                return "FAIL";

            }
        }catch (IOException e) {
            // 파일 저장 실패 시 예외 처리
            e.printStackTrace();

            return null;

        }

    }

    @Override
    public List<String> storeFile(MultipartFile[] files) {
        log.info("[BacklogService] storeFile()");
        List<String> listFile = new ArrayList<>();

        try {
            for(MultipartFile file : files) {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());

                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);

                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                listFile.add(fileName);

            }



            return listFile;

        } catch (IOException e) {
            // 파일 저장 실패 시 예외 처리
            e.printStackTrace();

            return null;

        }

    }

}
