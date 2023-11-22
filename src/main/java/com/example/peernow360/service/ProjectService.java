package com.example.peernow360.service;

import com.example.peernow360.dto.*;
import com.example.peernow360.mappers.IProjectMapper;
import com.example.peernow360.service.impl.IProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProjectService implements IProjectService {

    private final IProjectMapper iProjectMapper;
    private final S3GetImage s3GetImage;

//    @Transactional
//    public void Project() {
//        createProject();
//
//    }

    public int createProject(Map<String, Object> map, ProjectDto project, List<TeamDto> teams, AcceptTeamDto acceptTeamDto) {
        log.info("createProject()");

        project.setTitle((String) map.get("title"));
        project.setDetail((String) map.get("detail"));
        project.setStart_date((String) map.get("start_date"));
        project.setEnd_date((String) map.get("end_date"));

        Map<String, String> peerMap = (Map<String, String>) map.get("peer_id");
        for (Map.Entry<String, String> entry : peerMap.entrySet()) {
            TeamDto team = new TeamDto();
            team.setPeer_id(entry.getKey());
            team.setRole(entry.getValue());
            teams.add(team);
        }

        int result = iProjectMapper.createProject(project);
        if (result > 0) {
            for (TeamDto team : teams) {
                team.setNo(project.getNo()); // 프로젝트 번호 설정
                iProjectMapper.createTeam(team);
            }
        }
//        AcceptTeamDto acceptTeamDto = new AcceptTeamDto();
//        acceptTeamDto.setUser_id((String) map.get("user_id"));
        acceptTeamDto.setNo(project.getNo());
        iProjectMapper.createAcceptTeam(acceptTeamDto);

        return result;
    }

    public List<UserMemberDto> getPeer(String peerName) throws IOException {
        log.info("getPeer()");

        List<UserMemberDto> list = iProjectMapper.getPeer(peerName);
        for(UserMemberDto userMemberDto : list) {

            try {
                Object image = s3GetImage.getObject(userMemberDto.getId() + "/" + userMemberDto.getImage());

                if(image == null) {
                    image = s3GetImage.getObject("defaultImg/defaultImg.png");
                }
                userMemberDto.setImage(image);

            } catch (Exception e) {
                userMemberDto.setImage(s3GetImage.getObject("defaultImg/defaultImg.png"));
            }
        }

        return list;
    }

    public ProjectDto projectDetail(int no) {
        log.info("projectDetail()");

        return iProjectMapper.projectDetail(no);
    }

    public List<ProjectDto> projectList(String user_id) {
        log.info("projectList()");

        return iProjectMapper.projectList(user_id);
    }

    public List<UserMemberDto> peerlist(int no) throws IOException {
        log.info("peerlist()");

        List<UserMemberDto> list = iProjectMapper.peerlist(no);
        for(UserMemberDto userMemberDto : list) {

            try {
                Object image = s3GetImage.getObject(userMemberDto.getId() + "/" + userMemberDto.getImage());

                if(image == null) {
                    image = s3GetImage.getObject("defaultImg/defaultImg.png");
                }
                userMemberDto.setImage(image);

            } catch (Exception e) {
                userMemberDto.setImage(s3GetImage.getObject("defaultImg/defaultImg.png"));
            }
        }

        return list;
    }

    public int modifyProject(ProjectDto projectDto) {
        log.info("modifyProject()");

        int result = iProjectMapper.modifyProject(projectDto);

        if (result > 0) {
          log.info("MODIFY PROJECT SUCCESS");
        } else {
            log.info("MODIFY PROJECT FAIL");
        }

        return result;

    }

    public int acceptProject(int no, String user_id, String role) {
        log.info("acceptProject()");

        Map<String, Object> map = new HashMap<>();
        map.put("no", no);
        map.put("user_id", user_id);

        AcceptTeamDto acceptTeamDto = new AcceptTeamDto();
        acceptTeamDto.setNo(no);
        acceptTeamDto.setUser_id(user_id);
        acceptTeamDto.setRole(role);

        iProjectMapper.acceptProject(map);

        int result = iProjectMapper.acceptTeam(acceptTeamDto);
        log.info("result: " + result);

        if(result > 0) {
            log.info("팀 수락 완료!!");

            List<String> acceptTeamDtos = iProjectMapper.selectedAcceptId(no);
            log.info("acceptTeamDtos" + acceptTeamDtos);

            Map<String, Object> data = new HashMap<>();
            data.put("user_id", user_id);
            data.put("project_no", no);

            for(int i = 0; i<acceptTeamDtos.size()-1; i++) {
                data.put("peer_id", String.valueOf(acceptTeamDtos.get(i)));

                iProjectMapper.insertReview(data);

            }

            for(int i = 0; i<acceptTeamDtos.size()-1; i++) {
                data.put("peer_id", String.valueOf(acceptTeamDtos.get(i)));

                iProjectMapper.insertReversReview(data);

            }

        }

        return result;

    }

    public int declineProject(int no, String user_id) {
        log.info("declineProject()");

        Map<String, Object> map = new HashMap<>();
        map.put("no", no);
        map.put("user_id", user_id);

        return iProjectMapper.declineProject(map);
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteProject(int no, String user_id) {
        log.info("deleteProject()");

        Map<String, Object> map = new HashMap<>();
        map.put("no", no);
        map.put("user_id", user_id);

        return iProjectMapper.deleteProject(map);
    }


    public List<InvitationDto> projectInvitation(InvitationDto invitationDto) throws IOException {
        log.info("projectInvitation()");

        List<InvitationDto> list = iProjectMapper.projectInvitation(invitationDto);
        for(InvitationDto invitationDtos : list) {

            try {

                Object image = s3GetImage.getObject(invitationDtos.getOwner_id() + "/" + invitationDtos.getOwner_image());


                if (image == null) {
                    image = s3GetImage.getObject("defaultImg/defaultImg.png");
                }
                invitationDtos.setOwner_image(image);

            } catch (Exception e) {
                invitationDtos.setOwner_image(s3GetImage.getObject("defaultImg/defaultImg.png"));
            }

        }

        return list;

//        return  iProjectMapper.projectInvitation(invitationDto);
    }
}

