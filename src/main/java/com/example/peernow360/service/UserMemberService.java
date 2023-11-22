package com.example.peernow360.service;


import com.example.peernow360.dto.TokenInfo;
import com.example.peernow360.dto.UserMemberDto;
import com.example.peernow360.mappers.IUserMemberMapper;
import com.example.peernow360.security.JWTtokenProvider;
import com.example.peernow360.service.impl.IUserMemberService;
import com.example.peernow360.utils.UserEnum;
import com.example.peernow360.utils.UserEnumCode;
import com.example.peernow360.utils.UserMember.UserMemberUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
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
    private final S3Uploader s3Uploader;
    private final UserMemberUtils userMemberUtils;

    @Value("${jwt.HttpHeaderValue}")
    private String HttpHeaderValue;

    public int createAccountConfirm(MultipartFile multipartFile, UserMemberDto userMemberDto) throws IOException {
        log.info("[UserMemberService] createAccountConfirm()");
        log.info("userMemberDto id : {} pw : {} ", userMemberDto.getId(), userMemberDto.getPw());

        boolean isUserId = iUserMemberMapper.duplicateById(userMemberDto.getId());

        // 중복ID가 없을 시
        if (!isUserId) {

            if (multipartFile != null) {
                s3Uploader.upload(multipartFile, userMemberDto.getId());
                userMemberDto.setImage(multipartFile.getOriginalFilename());

            }

            userMemberDto.setPw(passwordEncoder.encode(userMemberDto.getPw()));

            int result = iUserMemberMapper.insertUserMember(userMemberDto);

            UserEnum resultEnum = Arrays.stream(UserEnum.values()) // UserEnum 열거형의 모든 상수를 스트림으로 변환
                    .filter(enumValue -> enumValue.getUserEnum() == result) // 스트림에서 getUserEnum() 메소드를 이용하여 각 상수의 userEnum 필드 값이 result 값과 일치하는지 필터링
                    .findFirst() // 필터링된 결과 중 첫 번째 값을 반환합니다. 즉, result 값과 일치하는 첫 번째 열거형 상수
                    .orElse(null);

            if (resultEnum != null)
                switch (resultEnum) {
                    case DB_ERROR:
                        log.info("DB ERROR");

                    case INSERT_ACCOUNT_AT_DB_FAIL:
                        log.info("INSERT ACCOUNT AT DB FAIL");
                        return UserEnum.INSERT_ACCOUNT_AT_DB_FAIL.getUserEnum();

                    case INSERT_ACCOUNT_AT_DB_SUCCESS:
                        log.info("INSERT ACCOUNT AT DB SUCCESS");
                        return UserEnum.INSERT_ACCOUNT_AT_DB_SUCCESS.getUserEnum();

                }

        }

        log.info("ID_IS_ALREADY_EXIST");
        return UserEnum.ID_IS_ALREADY_EXIST.getUserEnum();

    }

    @Override
    public ResponseEntity<Map<String, Object>> loginMember(UserMemberDto userMemberDto) {
        log.info("[UserMemberService] loginMember()");
        log.info("loginmember id " + userMemberDto.getId());

        Map<String ,Object> msgData = new HashMap<>();

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

        // 최초 로그인 시 현재 입장된 프로젝트가 없기 때문에 0으로 처리.
        int project_no = 0;

        // authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성
        String accessToken = jwTtokenProvider.createAccessToken(authenticationToken, authenticationToken.getAuthorities().toString(), project_no);
        String refreshToken = jwTtokenProvider.createRefreshToken(authenticationToken);
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

            TokenInfo tokenInfo = TokenInfo.builder()
                    .accessToken(accessToken)
                    .grantType("Bearer ")
                    .build();

            log.info("TokenINfo: " + tokenInfo);

            msgData.put("tokenInfo", tokenInfo);
            msgData.put("code",1);
            msgData.put("status","success");
            msgData.put("message","로그인에 성공하였습니다.");

            /*
             * ResponseEntity.ok로 (200code)를 보내며 header와 body를 보냄.
             * ResponseEntity는 HttpStatus 와 HttpHeader , HttpBody 를 설정하여 HTTP 응답을 보낼 수 있다
             * 아래의 getCokkieToken을 통해서 쿠키 정보를 가져올 수 있다.
             */
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, userMemberUtils.getCookieToken(refreshToken).toString())
                    .body(msgData);

        } else {
            log.info("DB에 refresh token 저장을 하지 못했습니다.");

            msgData.put("code",0);
            msgData.put("status","fail");
            msgData.put("message","로그인에 실패하였습니다.");

            return ResponseEntity.ok(msgData);

        }

    }

    @Override
    public ResponseEntity<Map<String, Object>> reCreateAccessToken(String refreshToken) {
        log.info("[UserMemberService] reCreateAccessToken()");

        Map<String, Object> msgData = new HashMap<>();

        // 쿠키에 담긴 값들 중 refreshtoken만 추출
        refreshToken = userMemberUtils.separateRefToken(refreshToken);

        /*
         * jwt token 유효성 검증 및 blacklist 확인
         */
        if(jwTtokenProvider.validateToken(refreshToken) && jwTtokenProvider.selectForBlacklist(refreshToken)) {
            log.info("refresh token 유효성 검사 완료 및 블랙리스트에 존재하지 않은 refresh token");

            // 요청 헤더에 담긴 project_no 추출
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            int project_no = Integer.parseInt(request.getHeader("project_no"));

            String user_id = jwTtokenProvider.checkUser(refreshToken);
            Boolean isToken = jwTtokenProvider.getRefreshToken(user_id);

            /*
             * 일치하는 refresh 토큰이 존재할 떄
             */
            if(isToken) {
                log.info("DB에 refresh 토큰이 존재");
                UserDetails userDetails = userDetailsService.loadUserByUsername(user_id);
                log.info("userDetails : " + userDetails);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());

                String accessToken = jwTtokenProvider.createAccessToken(authenticationToken, authenticationToken.getAuthorities().toString(), project_no);
                refreshToken = jwTtokenProvider.createRefreshToken(authenticationToken);
                int result = jwTtokenProvider.updateRefreshToken(user_id, refreshToken);

                if (result > 0) {
                    log.info("성공적으로 DB에 재발급 refresh token이 저장되었습니다,");
                    log.info("refresh accessToken : " + accessToken);
                    log.info("refresh refreshToken : " + refreshToken);

                    TokenInfo tokenInfo = TokenInfo.builder()
                            .accessToken(accessToken)
                            .grantType("Bearer ")
                            .build();

                    msgData.put("tokenInfo", tokenInfo);
                    msgData.put("code", 1);
                    msgData.put("status", true);
                    msgData.put("message", "access Token 및 refresh Token을 재발급에 성공하였습니다.");
                    log.info("msgData" + msgData);

                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, userMemberUtils.getCookieToken(refreshToken).toString())
                            .body(msgData);

                }

            }

        }

        log.info("DB에 재발급 refresh token 저장을 하지 못했습니다.");
        log.info("refresh Token에 대한 유효성이 실패하였거나, 허용되지 않은 refresh Token을 이용하여 접속해 로그아웃을 진행합니다 loading....");

        return logOutInfo(refreshToken);

    }

    public UserMemberDto userDetailInfo() throws IOException {
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
    @Override
    @Transactional
    public ResponseEntity logOutInfo(String refreshToken) {
        log.info("[UserMemberService] logOut()");

        refreshToken = userMemberUtils.separateRefToken(refreshToken);

        //본인 Thread Local에 저장되어 있는 authorization 정보를 초기화 하는 작업
        SecurityContextHolder.clearContext();

        //Token에서 로그인한 사용자 정보 get해 로그아웃 처리
        boolean isToken = iUserMemberMapper.compareRefreshToken(refreshToken);

        // 토큰이 존재 시
        if(isToken) {
            int result = iUserMemberMapper.insertBlacklist(refreshToken);

            //blacklist 삽입 성공
            if(result > 0) {
                iUserMemberMapper.removeRefreshToken(refreshToken);

                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, userMemberUtils.clearCookieToken().toString()).body("success");

            }

        }

        log.info("토큰이 유효하지 않습니다.");
        return ResponseEntity.ok("fail");

    }

    @Override
    @Transactional
    public int deleteAccountConfirm(String id, String refreshToken) {
        log.info("[UserMemberService] deleteAccountConfirm()");

        refreshToken = userMemberUtils.separateRefToken(refreshToken);

        //Token에서 로그인한 사용자 정보 get해 로그아웃 처리
        boolean isToken = iUserMemberMapper.compareRefreshToken(refreshToken);

        // 토큰이 존재 시
        if(isToken) {
            iUserMemberMapper.removeRefreshToken(refreshToken);

            return iUserMemberMapper.removeAccountInfo(id);

        }

        return 0;

    }

    @Override
    public int updateAccountConfirm(String id, UserMemberDto userMemberDto, MultipartFile image) throws IOException {
        log.info("[UserMemberService] updateAccountConfirm()");

        userMemberDto.setId(id);

        if(image != null) {
            log.info("이미지가 존재합니다");
            s3Uploader.upload(image, userMemberDto.getId());
            userMemberDto.setImage(image.getOriginalFilename());

        }

        return iUserMemberMapper.modifyAccountInfo(userMemberDto);

    }

    public int updateAccountImage(String id, String fileName, MultipartFile multipartFile) throws IOException {
        log.info("updateAccountImage()");

        s3Uploader.delete(id, fileName);

        String image = multipartFile.getOriginalFilename();

        int result = iUserMemberMapper.updateAccountImage(id, image);

        s3Uploader.upload(multipartFile, id);

        return result;

    }

    @Override
    public UserMemberDto changeAuthority(int project_no) {
        log.info("[UserMemberService] changeAuthority()");

        Map<String, Object> data = new HashMap<>();

        User user_info = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = user_info.getUsername();

        data.put("user_id", user_id);
        data.put("project_no", project_no);

        UserMemberDto userMemberDto = iUserMemberMapper.selectAuthority(data);

        if(userMemberDto != null) {
            log.info(" 프로젝트 별 권한을 재발급하는데 성공하였습니다.");

            return userMemberDto;

        } else {
            log.info(" 프로젝트 별 권한을 재발급하는데 실패하였습니다.");

            return null;

        }

    }

    public String fileName(String userId) {

        return iUserMemberMapper.fileName(userId);

    }

}
