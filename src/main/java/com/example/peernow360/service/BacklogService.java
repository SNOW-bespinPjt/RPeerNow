package com.example.peernow360.service;

import com.example.peernow360.dto.BacklogDto;
import com.example.peernow360.dto.FileDto;
import com.example.peernow360.mappers.IBacklogMapper;
import com.example.peernow360.service.impl.IBacklogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class BacklogService implements IBacklogService {

    private final IBacklogMapper iBacklogMapper;
    private final S3Uploader s3Uploader;
    private final S3GetImage S3GetImage;

    @Override
    public String createNewBacklog(BacklogDto backlogDto, int project_no, String sprint_no, MultipartFile fileDto) throws IOException {
        log.info("[BacklogService] backlogListInfo()");

        Map<String, Object> msgData = new HashMap<>();
        /*
         * sprint_no를 requestParam으로 받는데 이를 require = false를 하면 안받고 null값으로 가져온다.
         * 하지만 int값이기 때문에 형변환을 해줘야 하는데 이게 해당 로직이다.
         */
        if(sprint_no == null) {
            sprint_no = "0";
        }

        log.info("sprint_no: " + Integer.parseInt(sprint_no));

        backlogDto.setSprint_no(Integer.parseInt(sprint_no));
        backlogDto.setProject_no(project_no);

        int result = iBacklogMapper.createBacklogInfo(backlogDto);
        log.info("backlogDto.getNo() : " + backlogDto.getNo());
        msgData.put("backlog_no",backlogDto.getNo());

        if(result > 0) {
            log.info("백로그 생성에 성공하였습니다.");

            if(fileDto != null) {
                log.info("이미지가 존재합니다");

                s3Uploader.upload(fileDto, String.valueOf(backlogDto.getNo()));
                String storedFileName = fileDto.getOriginalFilename();

                msgData.put("name", storedFileName);
                iBacklogMapper.insertBacklogFile(msgData);

            }

            return "SUCCESS";

        } else {
            log.info("백로그 생성에 실패하였습니다.");

            return "FAIL";

        }

    }

    @Override
    public Map<String, Object> backlogListInfo(int sprint_no) throws IOException {
        log.info("[BacklogService] backlogListInfo()");

        Map<String, Object> data = new HashMap<>();
        List<BacklogDto> newBacklogDtos = new ArrayList<>();

        List<BacklogDto> backlogDtos = iBacklogMapper.searchBacklogList(sprint_no);

        if(backlogDtos != null && backlogDtos.size() > 0) {
            log.info("CALL BACKLOG INFO SUCCESS!!");

            for(BacklogDto backlogDto : backlogDtos) {
                 backlogDto.setImage(S3GetImage.getObject(backlogDto.getUser_id() + "/" + backlogDto.getImage()));
                 newBacklogDtos.add(backlogDto);

            }

            data.put("backlogDtos", newBacklogDtos);
            return data;

        } else {
            log.info("CALL BACKLOG INFO FAIL!!");

            return null;

        }

    }

    @Override
    public Map<String, Object> backlogDetailInfo(int no) throws IOException {
        log.info("[BacklogService] backlogDetailInfo()");

        Map<String, Object> data = new HashMap<>();

        BacklogDto backlogDto = iBacklogMapper.searchBacklogDetail(no);
        log.info("backlogDto: " + backlogDto);

        if(backlogDto != null) {
            log.info("백로그 상세정보를 불러오는데 성공하였습니다.");

            backlogDto.setImage(S3GetImage.getObject(backlogDto.getUser_id() + "/" + backlogDto.getImage()));

            data.put("backlogDto", backlogDto);

            return data;

        } else {
            log.info("백로그 상세정보를 불러오는데 실패하였습니다.");

            return null;

        }

    }

    @Override
    public List<BacklogDto> backlogDayAndIngInfo(int sprintNo) throws IOException {
        log.info("[BacklogService] backlogDayAndIngInfo()");

        List<BacklogDto> newBacklogDtos = new ArrayList<>();
        List<BacklogDto> backlogDtos = iBacklogMapper.searchBacklogDayAndIng(sprintNo);

        log.info("backlogDtos: " + backlogDtos);

        if(backlogDtos != null && backlogDtos.size() >0 ) {
            log.info("오늘 날짜에 진행중인 백로그들을 불러오는데 성공하였습니다.");

            for(BacklogDto backlogDto : backlogDtos) {
                backlogDto.setImage(S3GetImage.getObject(backlogDto.getUser_id() + "/" + backlogDto.getImage()));
                newBacklogDtos.add(backlogDto);

            }

            return newBacklogDtos;

        } else {
            log.info("오늘 날짜에 진행중인 백로그들을 불러오는데 실패하였습니다.");

            return null;

        }

    }

    @Override
    public String backlogUpdateStatus(int no, String status) {
        log.info("[BacklogService] backlogUpdateStatus()");

        BacklogDto backlogDto = new BacklogDto();
        backlogDto.setNo(no);
        backlogDto.setStatus(status);


        int result = iBacklogMapper.updateBacklogStatus(backlogDto);
        if(result > 0) {
            log.info("STATUS 변경에 성공하였습니다.");

            return "success";

        } else {
            log.info("STATUS 변경에 실패하였습니다.");

            return "fail";

        }

    }

    @Override
    public String backlogUpdateInfo(BacklogDto backlogDto, int no, MultipartFile fileDto) throws IOException {
        log.info("[BacklogService] backlogUpdateInfo()");

        backlogDto.setNo(no);

        int result = iBacklogMapper.updateBacklogInfo(backlogDto);

        if(result > 0) {
            log.info("백로그 수정에 성공하였습니다.");

            if(fileDto != null) {
                log.info("이미지가 존재합니다");

                Map<String, Object> msgData = new HashMap<>();
                msgData.put("backlog_no", no);

                FileDto fileDBName = iBacklogMapper.searchBacklogFile(no);
                s3Uploader.delete(String.valueOf(fileDBName.getNo()), fileDBName.getName());
                int isDel = iBacklogMapper.removeBacklogFile(no);

                log.info("isDel : 1 -> True / isDel : 0 -> false : " + isDel);

                String storedFileName = s3Uploader.upload(fileDto, backlogDto.getUser_id());
                msgData.put("name", storedFileName);
                iBacklogMapper.insertBacklogFile(msgData);

            }

            return "success";

        } else {
            log.info("백로그 수정에 실패하였습니다.");

            return "fail";

        }

    }

    @Override
    @Transactional
    public String backlogDeleteInfo(int no) {
        log.info("[BacklogService] backlogDeleteInfo()");

        int result = iBacklogMapper.removeBacklogInfo(no);

        if(result > 0) {
            log.info("백로그 삭제에 성공하였습니다.");

            FileDto fileDBName = iBacklogMapper.searchBacklogFile(no);

            s3Uploader.delete(String.valueOf(fileDBName.getNo()), fileDBName.getName());

            return iBacklogMapper.removeBacklogFile(no) > 0 ? "SUCCESS" : "FAIL";

        } else {
            log.info("백로그 삭제에 실패하였습니다.");

            return "FAIL";

        }
    }

    @Override
    public List<BacklogDto> searchALlbacklogList(int project_no) throws IOException {
        log.info("[BacklogService] backlogDeleteInfo()");

        List<BacklogDto> newBacklogDtos = new ArrayList<>();
        List<BacklogDto> backlogDtos = iBacklogMapper.searchAllBacklogList(project_no);
        log.info("backlogDtos : " + backlogDtos);

        if(backlogDtos != null && backlogDtos.size() > 0) {
            log.info("프로젝트안에 존재하는 모든 백로그를 가져오는데 성공하였습니다.");

            for(BacklogDto backlogDto : backlogDtos) {
                backlogDto.setImage(S3GetImage.getObject(backlogDto.getUser_id() + "/" + backlogDto.getImage()));
                newBacklogDtos.add(backlogDto);

            }

            return newBacklogDtos;

        } else {
            log.info("프로젝트안에 존재하는 모든 백로그를 가져오는데 실패하였습니다.");

            return backlogDtos;

        }

    }

    @Override
    public String backlogFileDownload(int no) {
        log.info("[BacklogService] backlogFileDownload()");

        FileDto file = iBacklogMapper.searchBacklogFile(no);

        return file.getName();

    }

}

//    CREATEBACKLOG 한게 아까워서 무덤에 놔두다..
//    public String createNewBacklog(BacklogDto backlogDto, int sprint_no, MultipartFile[] files) {
//        log.info("[BacklogService] createNewBacklog()");
//
//        User user_info = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String user_id = user_info.getUsername();
//
//        backlogDto.setUser_id(user_id);
//        backlogDto.setSprint_no(sprint_no);
//        List<String> listFile = new ArrayList<>();
//
//        try{
//            int result = iBacklogMapper.createBacklogInfo(backlogDto);git
//            Map<String, Object> map = new HashMap<>();
//            log.info("backlogDto.getNo() : " + backlogDto.getNo());
//            map.put("backlog_no", backlogDto.getNo());
//
//            //백로그 생성에 성공하면, 파일을 입력함
//            if(result > 0) {
//                log.info("CREATE BACKLOG SUCCESS");
//
//                for(MultipartFile file : files) {
//                    String fileName = UUID.randomUUID() + StringUtils.cleanPath(file.getOriginalFilename());
//                    log.info("[BacklogService] fileName: " + fileName);
//
//                    Path uploadPath = Paths.get(uploadDir);
//                    log.info("[BacklogService]uploadPath: " + uploadPath);
//                    if (!Files.exists(uploadPath)) {
//                        Files.createDirectories(uploadPath);
//
//                    }
//                    Path filePath = uploadPath.resolve(fileName);
//                    log.info("[BacklogService]filePath: " + filePath);
//
//                    /*
//                     * file.getInputStream(): MultipartFile 객체에서 파일의 내용을 읽어오기 위한 InputStream을 반환
//                     * filePath: 복사할 대상 파일의 Path입니다. / Paths.get() 메서드나 다른 방법으로 filePath를 생성
//                     * StandardCopyOption.REPLACE_EXISTING: 파일 복사 중에 대상 파일이 이미 존재하는 경우, 대상 파일을 덮어쓰기 위한 옵션입니다.
//                     * 즉, 만약 대상 파일이 이미 존재한다면, 해당 옵션을 사용하여 새로운 파일로 대상 파일을 교체
//                     */
//                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//                    listFile.add(fileName);
//                    map.put("fileName" , fileName);
//
//                    iBacklogMapper.insertBacklogFile(map);
//                    log.info("YES~");
//
//                }
//
//                return "success";
//
//            } else {
//                log.info("CREATE BACKLOG FAIL");
//
//                return "FAIL";
//
//            }
//        }catch (IOException e) {
//            // 파일 저장 실패 시 예외 처리
//            e.printStackTrace();
//
//            return null;
//
//        }
//
//    }
//


















