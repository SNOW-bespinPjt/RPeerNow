package com.example.peernow360.service.impl;


import com.example.peernow360.dto.UserMemberDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface IUserMemberService {

    /*
     * 유저 계정 생성
     */
    public int createAccountConfirm(MultipartFile multipartFile, UserMemberDto userMemberDto) throws IOException;

    /*
     * 유저 로그인
     */
    public ResponseEntity<Map<String, Object>> loginMember(UserMemberDto userMemberDto);

    /*
     * refresh token 재발급
     */
     public ResponseEntity<Map<String, Object>> reCreateAccessToken(String refreshToken);

    /*
     * 회원 상세 정보
     */
    public UserMemberDto userDetailInfo() throws IOException;

     /*
      * 로그아웃
      */
    public ResponseEntity<String> logOutInfo(String refreshToken);

    /*
     * 유저 계정 삭제
     */
    public int deleteAccountConfirm(String id, String refreshToken);

    /*
     * 유저 계정 수정
     */
    public int updateAccountConfirm(String id, UserMemberDto userMemberDto, MultipartFile image) throws IOException;



    int updateAccountImage(String id, String fileName, MultipartFile multipartFile) throws IOException;

    /*
     * 프로젝트 변경 시 권한 변경
     */
    public UserMemberDto changeAuthority(int project_no);

}
