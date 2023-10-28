package com.example.peernow360.service.impl;


import com.example.peernow360.dto.UserMemberDto;

import java.util.Map;

public interface IUserMemberService {

    /*
     * 유저 계정 생성
     */
    public int createAccountConfirm(UserMemberDto userMemberDto);

    /*
     * 유저 로그인
     */
    public Map<String, Object> loginMember(UserMemberDto userMemberDto);

    /*
     * refresh token 재발급
     */
     public Map<String, Object> reCreateAccessToken(String name);

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
    public Map<String, Object> updateAccountConfirm(String id, UserMemberDto userMemberDto);

}
