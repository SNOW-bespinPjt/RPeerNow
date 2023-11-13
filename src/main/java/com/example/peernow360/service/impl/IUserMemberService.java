package com.example.peernow360.service.impl;


import com.example.peernow360.dto.UserMemberDto;
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
    public Map<String, Object> loginMember(UserMemberDto userMemberDto);

    /*
     * refresh token 재발급
     */
     public Map<String, Object> reCreateAccessToken(String name, int project_no);

    /*
     * 회원 상세 정보
     */
    public UserMemberDto userDetailInfo() throws IOException;

     /*
      * 로그아웃
      */
    public String logOutInfo(String refreshToken);

    /*
     * 유저 계정 삭제
     */
    public String deleteAccountConfirm(String id, String refreshToken);

    /*
     * 유저 계정 수정
     */
    public String updateAccountConfirm(String id, UserMemberDto userMemberDto, MultipartFile image) throws IOException;



    int updateAccountImage(String id, String fileName, MultipartFile multipartFile) throws IOException;


}
