package com.example.peernow360.mappers;


import com.example.peernow360.dto.UserMemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IUserMemberMapper {

    /*
     * CHECK DUPLICATE ID
     */
    public boolean duplicateById(String id);

    /*
     * INSERT NEW ACCOUNT
     */
    public int insertUserMember(UserMemberDto userMemberDto);

    /*
     * USER LOGIN CONFIRM
     */
    public UserMemberDto selectUserForLogin(UserMemberDto userMemberDto);

    /*
     * Login 시 Refresh Token 있으면 삭제
     */
    public int checkRefreshAndDel(String id);

    /*
     * refreshToken DB에 삽입
     */
    public int insertRefreshToken(Map<String, Object>msgData);

    /*
     * ID에 맞는 refreshtoken 반환
     */
    public String findByToken(String user_id);

    /*
     * ID에 맞는 refreshtoken 수정
     */
    public int modifyRefreshToken(Map<String, Object> msgData);

    /*
     * refreshToken 비교하기(쿠키용 토큰과 DB에 저장된 토큰)
     */
    public boolean compareRefreshToken(String refreshToken);

    /*
     * Blacklist에 refreshToken 삽입
     */
    public int insertBlacklist(String refreshToken);

    /*
     * refreshToken tokens DB에서 삭제
     */
    public int removeRefreshToken(String refreshToken);

    /*
     * 회원 상세 정보 불러오기
     */
    public UserMemberDto searchUserDetail(String id);

    /*
     * 유저 계정 삭제
     */
    public int removeAccountInfo(String id);

    /*
     * 유저 계정 수정
     */
    public int modifyAccountInfo(UserMemberDto userMemberDto);

    /*
     * BlackList가 존재 여부 확인
     */
    public boolean selectBlackListToken(String refreshToken);

    /*
     * 유저 아이디를 이용해 사용자 이름 불러오기(채팅에 쓰일 메서드)
     */
    public String selectUserName(String user_id);

    int updateAccountImage(String id, String image);
}
