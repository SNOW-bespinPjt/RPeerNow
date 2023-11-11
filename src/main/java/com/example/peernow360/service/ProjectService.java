package com.example.peernow360.service;

import com.example.peernow360.dto.*;
import com.example.peernow360.mappers.IProjectMapper;
import com.example.peernow360.service.impl.IProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProjectService implements IProjectService {

    private final IProjectMapper iProjectMapper;

//    @Transactional
//    public void Project() {
//        createProject();
//
//    }

    public int createProject(Map<String, Object> map, ProjectDto project, List<TeamDto> teams) {
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
        return result;
    }

    public List<UserMemberDto> getPeer(String peerName) {
        log.info("getPeer()");

        return iProjectMapper.getPeer(peerName);
    }

    public ProjectDto projectDetail(int no) {
        log.info("projectDetail()");

        return iProjectMapper.projectDetail(no);
    }

    public List<ProjectDto> projectList(String user_id) {
        log.info("projectList()");

        return iProjectMapper.projectList(user_id);
    }

    public List<UserMemberDto> peerlist(int no, String owner) {
        log.info("peerlist()");

        Map<String, Object> map = new HashMap<>();
        map.put("no", no);
        map.put("owner", owner);

        return iProjectMapper.peerlist(map);
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

        return result;
    }

    public int declineProject(int no, String user_id) {
        log.info("declineProject()");

        Map<String, Object> map = new HashMap<>();
        map.put("no", no);
        map.put("user_id", user_id);

        return iProjectMapper.declineProject(map);
    }

    @Transactional
    public int deleteProject(int no, String user_id) {
        log.info("deleteProject()");

        Map<String, Object> map = new HashMap<>();
        map.put("no", no);
        map.put("user_id", user_id);

        return iProjectMapper.deleteProject(map);
    }


    public List<InvitationDto> projectInvitation(InvitationDto invitationDto) {
        log.info("projectInvitation()");

        return  iProjectMapper.projectInvitation(invitationDto);
    }
}
