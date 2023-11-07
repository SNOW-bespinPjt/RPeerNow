package com.example.peernow360.service;


import com.example.peernow360.dto.TokenInfo;
import com.example.peernow360.dto.UserMemberDto;
import com.example.peernow360.mappers.IUserMemberMapper;
import com.example.peernow360.security.JWTtokenProvider;
import com.example.peernow360.service.impl.IUserMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
@Lazy
public class UserMemberService implements IUserMemberService {

    private final PasswordEncoder passwordEncoder;
    private final IUserMemberMapper iUserMemberMapper;
    private final UserDetailsService userDetailsService;
    private final JWTtokenProvider jwTtokenProvider;

    private final int INSERT_ACCOUNT_AT_DB_SUCCESS = 1;
    private final int INSERT_ACCOUNT_AT_DB_FAIL = 0;
    private final int DB_ERROR = -1;
    private final int ID_IS_ALREADY_EXIST = -2;

    @Value("${jwt.HttpHeaderValue}")
    private String HttpHeaderValue;

    @Override
    public int createAccountConfirm(UserMemberDto userMemberDto) {
        log.info("[UserMemberService] createAccountConfirm()");
        log.info("userMemberDto id : {} pw : {} " , userMemberDto.getId(), userMemberDto.getPw());

        boolean isUserId = iUserMemberMapper.duplicateById(userMemberDto.getId());

        // 중복ID가 없을 시
        if(!isUserId) {
            userMemberDto.setPw(passwordEncoder.encode(userMemberDto.getPw()));
            int result = iUserMemberMapper.insertUserMember(userMemberDto);

            switch (result) {
                case DB_ERROR:
                    log.info("DB ERROR");

                case INSERT_ACCOUNT_AT_DB_FAIL:
                    log.info("INSERT ACCOUNT AT DB FAIL");
                    return result;

                case INSERT_ACCOUNT_AT_DB_SUCCESS:
                    log.info("INSERT ACCOUNT AT DB SUCCESS");
                    return result;
            }
        }

        log.info("ID_IS_ALREADY_EXIST");
        return ID_IS_ALREADY_EXIST;

    }

    @Override
    public Map<String, Object> loginMember(UserMemberDto userMemberDto) {
        log.info("[UserMemberService] loginMember()");
        log.info("loginmember id " + userMemberDto.getId());

        UserMemberDto pwCheckUserMemberDto=iUserMemberMapper.selectUserForLogin(userMemberDto);

        if(!(userMemberDto.getId().equals(pwCheckUserMemberDto.getId()) && passwordEncoder.matches(userMemberDto.getPw(), pwCheckUserMemberDto.getPw()))) {
            log.info("로그인에 실패했습니다.");

            return null;

        }

        //UsernamePasswordAuthenticationToken에는 인증을 원하는 주체(Principal)의 신원을 확인할 수 있는 정보를 담아준다.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userMemberDto.getId(), userMemberDto.getPw());

        log.info("authenticationToken in loginMember : " + authenticationToken);


        // 해당 인증정보를 SecurityContextHolder에 저장하고 전역적으로 사용
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성
        String accessToken = jwTtokenProvider.createAccessToken(authenticationToken, "member");
        String refreshToken = jwTtokenProvider.createRefreshToken(authenticationToken, "member");
        String user = (String) authenticationToken.getPrincipal(); //user 정보

        log.info("AccessToken : " + accessToken);
        log.info("RefreshToken : " + refreshToken);
        log.info("user : " + user);
        log.info("=====================================");
        log.info("");

        // 로그인 시 DB에 남아있는 컬럼 삭제
        int isDel = iUserMemberMapper.checkRefreshAndDel(userMemberDto.getId());

        if(isDel > 0) {
            log.info("남아있는 refresh Token을 제거하였습니다.");

        }

        // 초기 생성한 refresh token 저장
        int result = jwTtokenProvider.insertRefreshToken(authenticationToken.getPrincipal(), refreshToken);

        if (result > 0) {
            log.info("성공적으로 DB에 refresh token이 저장되었습니다,");

        } else {
            log.info("DB에 refresh token 저장을 하지 못했습니다.");

        }

        Map<String ,Object> msgData = new HashMap<>();

        TokenInfo tokenInfo = TokenInfo.builder()
                .accessToken(accessToken)
                .grantType("Bearer ")
                .build();

        msgData.put("tokenInfo", tokenInfo);
        msgData.put("refreshToken", refreshToken);

        return msgData;

    }

    @Override
    public Map<String, Object> reCreateAccessToken(String user_id) {
        log.info("[UserMemberService] reCreateAccessToken()");

        Boolean isToken = jwTtokenProvider.getRefreshToken(user_id);

        /*
         * 일치하는 refresh 토큰이 존재할 떄
         */
        if(isToken) {
            log.info("refresh 토큰이 존재");
            UserDetails userDetails = userDetailsService.loadUserByUsername(user_id);
            log.info("userDetails : " + userDetails);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());

            String accessToken = jwTtokenProvider.createAccessToken(authenticationToken, "member");
            String refreshToken = jwTtokenProvider.createRefreshToken(authenticationToken, "member");
            int result = jwTtokenProvider.updateRefreshToken(user_id, refreshToken);

            if (result > 0) {
                log.info("성공적으로 DB에 재발급 refresh token이 저장되었습니다,");

            } else {
                log.info("DB에 재발급 refresh token 저장을 하지 못했습니다.");

            }

            log.info("refresh accessToken : " + accessToken);
            log.info("refresh refreshToken : " + refreshToken);

            Map<String, Object> msgData = new HashMap<>();

            TokenInfo tokenInfo = TokenInfo.builder()
                    .accessToken(accessToken)
                    .grantType("Bearer ")
                    .build();

            msgData.put("tokenInfo", tokenInfo);

            return msgData;

        }

        return null;

    }

    @Override
    public UserMemberDto userDetailInfo() {
        log.info("[UserMemberService] userDetailInfo()");

        User user_info = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = user_info.getUsername();
        log.info("id: " + id);

        UserMemberDto userMemberDto = iUserMemberMapper.searchUserDetail(id);

        if(userMemberDto != null) {
            log.info("계정 상세 정보를 불러오는데 성공하였습니다.");

            return userMemberDto;

        }

        log.info("계정 상세 정보를 불러오는데 실패하였습니다.");

        return null;

    }

    /*
     * Transactional
     * 트랜잭션이 시작되고, 메서드가 성공적으로 실행되면 트랜잭션이 커밋됩니다. 만약 예외가 발생하면 트랜잭션이 롤백됩니다.
     */
    @Transactional
    @Override
    public String logOutInfo(String refreshToken) {
        log.info("[UserMemberService] logOut()");

        //Token에서 로그인한 사용자 정보 get해 로그아웃 처리
        boolean isToken = iUserMemberMapper.compareRefreshToken(refreshToken);

        // 토큰이 존재 시
        if(isToken) {
            int result = iUserMemberMapper.insertBlacklist(refreshToken);

            //blacklist 삽입 성공
            if(result > 0) {
                result = iUserMemberMapper.removeRefreshToken(refreshToken);


                return result > 0 ? "success" :"fail";

            }

            log.info("DB에 refreshToken을 블랙리스트화 화였지만, DB 최신화는 실패하였습니다.");
            return "fail";

        }

        log.info("토큰이 유효하지 않습니다.");
        return "fail";

    }

    @Override
    public String deleteAccountConfirm(String id, String refreshToken) {
        log.info("[UserMemberService] deleteAccountConfirm()");

        refreshToken = refreshToken.substring(refreshToken.indexOf("=", 1)+1);
        log.info("deleteAccountConfirm() refreshToken: " + refreshToken);

        //Token에서 로그인한 사용자 정보 get해 로그아웃 처리
        boolean isToken = iUserMemberMapper.compareRefreshToken(refreshToken);

        // 토큰이 존재 시
        if(isToken) {
            iUserMemberMapper.removeRefreshToken(refreshToken);
            int result = iUserMemberMapper.removeAccountInfo(id);

            return result > 0 ? "success" :"fail";

        }

        log.info("refresh Token이 존재하지 않습니다 다시 로그인 하시거나, 없는 회원입니다.");
        return "fail";

    }

    @Override
    public String updateAccountConfirm(String id, UserMemberDto userMemberDto) {
        log.info("[UserMemberService] updateAccountConfirm()");

        userMemberDto.setId(id);

        int result = iUserMemberMapper.modifyAccountInfo(userMemberDto);

        if(result > 0) {
            log.info("계정 수정이 완료되었습니다!");

            return "success";

        } else {
            log.info("계정 수정에 실패하였습니다!");

            return "fail";

        }

    }

}
