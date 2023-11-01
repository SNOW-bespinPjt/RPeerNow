package com.example.peernow360.controller;

import com.example.peernow360.dto.UserMemberDto;
import com.example.peernow360.response.SingleResponse;
import com.example.peernow360.security.JWTtokenProvider;
import com.example.peernow360.service.UserMemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserMemberController {

    private final UserMemberService userMemberService;
    private final JWTtokenProvider jwTtokenProvider;

    /*
     * 유저 계정 생성
     */
    @PostMapping("/join")
    public Map<String ,Object> createAccountConfirm(@RequestBody UserMemberDto userMemberDto) {
        log.info("[UserMemberController] createAccountConfirm()");

        Map<String, Object> msgData = new HashMap<>();

        int result = userMemberService.createAccountConfirm(userMemberDto);

        if(result > 0) {
            msgData.put("success", true);
            msgData.put("code", result);
            msgData.put("message", "회원가입에 성공하였습니다.");

            return msgData;

        } else {
            msgData.put("success", false);
            msgData.put("code", result);
            msgData.put("message", "회원가입에 실패하였습니다.");

            return msgData;

        }

    }

    /*
     * 유저 계정 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<Map> userLogin(@RequestBody UserMemberDto userMemberDto, HttpServletResponse response) {
        log.info("[HomeController] UserLogin()");

        Map<String, Object> msgData = userMemberService.loginMember(userMemberDto);
        log.info("TokenINfo: " + msgData.get("tokenInfo"));

        if(msgData.get("tokenInfo") == null) {
            msgData.put("code",0);
            msgData.put("success",false);
            msgData.put("message","로그인에 실패하였습니다.");

            return ResponseEntity.ok(msgData);

        }

        ResponseCookie cookie = ResponseCookie.from("refreshToken", (String) msgData.get("refreshToken"))
                .maxAge(7 * 24 * 60 * 60) //쿠키의 수명을 설정 / 기간은 7일
                .path("/") //쿠키의 경로를 설정 "/" 로 설정하면 서버의 모든 경로에서 쿠키에 접근할 수 있습니다.
                .secure(true) //쿠키를 보안 연결 (HTTPS)에서만 전송하도록 설정
                .sameSite("None") //쿠키의 SameSite 속성을 설정합니다. "None"은 쿠키가 모든 요청에 대해 전송되도록 허용함을 의미
                .httpOnly(true) //쿠키를 JavaScript로 접근할 수 없도록 설정
                .build(); //체를 빌드하고 구성된 쿠키를 생성
        response.setHeader("Set-Cookie", cookie.toString());

        // msgData Map안에는 refreshToken 정보가 담겨있기 때문에 해당 키값을 삭제 해주고 response 해줘야 한다. refresh는 쿠키에 담아서 주기 때문
        msgData.remove("refreshToken");
        msgData.put("code",1);
        msgData.put("success",true);
        msgData.put("message","로그인에 성공하였습니다.");

        return ResponseEntity.ok(msgData);

    }

    /*
     * accessToken 완료시 refreshToken을 이용한 재발급
     */
    @PostMapping("/request_refreshToken")
    public ResponseEntity<Map<String, Object>> reissuanceAccessToken(@RequestHeader(value = "cookie") String refreshToken, HttpServletResponse response) {
        log.info("[HomeController] ReissuanceRefreshToken()");

        Map<String, Object> msgData = new HashMap<>();

        // refresh Token만 추출
        refreshToken = refreshToken.substring(refreshToken.indexOf("=", 1)+1);
        log.info("RefreshToken :" + refreshToken);

        /*
         * jwt token 유효성 검증 및 blacklist 확인
         */
        if(jwTtokenProvider.validateToken(refreshToken) && jwTtokenProvider.selectForBlacklist(refreshToken)) {
            String user_id = jwTtokenProvider.extractRefreshToken(refreshToken);

            msgData = userMemberService.reCreateAccessToken(user_id);

            ResponseCookie cookie = ResponseCookie.from("refreshToken", (String) msgData.get("refreshToken"))
                    .maxAge(7 * 24 * 60 * 60) //쿠키의 수명을 설정 / 기간은 7일
                    .path("/") //쿠키의 경로를 설정 "/" 로 설정하면 서버의 모든 경로에서 쿠키에 접근할 수 있습니다.
                    .secure(false) //쿠키를 보안 연결 (HTTPS)에서만 전송하도록 설정
                    .sameSite("None") //쿠키의 SameSite 속성을 설정합니다. "None"은 쿠키가 모든 요청에 대해 전송되도록 허용함을 의미
                    .httpOnly(true) //쿠키를 JavaScript로 접근할 수 없도록 설정
                    .build(); //체를 빌드하고 구성된 쿠키를 생성
            response.setHeader("Set-Cookie", cookie.toString());

            msgData.put("code", 1);
            msgData.put("success", true);
            msgData.put("message", "access Token 및 refresh Token을 재발급에 성공하였습니다.");
            log.info("msgData" + msgData);

            return ResponseEntity.ok(msgData);

        }

        log.info("refresh Token에 대한 유효성이 실패하였거나, 허용되지 않은 refresh Token을 이용하여 접속해 로그아웃을 진행합니다 loading....");

        String msg = logOutInfo(refreshToken, response);
        log.info("자동화 로그아웃 msg : " + msg);

        msgData.put("code", 0);
        msgData.put("success", false);
        msgData.put("message",msg);

        return ResponseEntity.ok(msgData);

    }

    @PostMapping("/logout_info")
    @Transactional
    public String logOutInfo(@RequestHeader(value = "cookie") String refreshToken, HttpServletResponse response) {
        log.info("[HomeController] logOut()");

        // 로그아웃을 하면 쿠키 삭제화 작업
        ResponseCookie cookie = ResponseCookie.from("refreshToken", null)
                .maxAge(0) //쿠키의 수명을 설정 / 기간은 7일
                .path("/") //쿠키의 경로를 설정 "/" 로 설정하면 서버의 모든 경로에서 쿠키에 접근할 수 있습니다.
                .build(); //체를 빌드하고 구성된 쿠키를 생성
        response.setHeader("Set-Cookie", cookie.toString());

        refreshToken = refreshToken.substring(refreshToken.indexOf("=", 1)+1);
        log.info("RefreshToken :" + refreshToken);

        //본인 Thread Local에 저장되어 있는 authorization 정보를 초기화 하는 작업
        SecurityContextHolder.clearContext();

        return userMemberService.logOutInfo(refreshToken);

    }

    /*
     * 유저 계정 삭제
     */
    @DeleteMapping("/leave")
    public String deleteAccountConfirm(@RequestParam ("id") String id, @RequestHeader(value = "cookie") String refreshToken, HttpServletResponse response) {
        log.info("[HomeController] deleteAccountConfirm()");
        log.info("id : " + id);

        // 삭제 시 남아있는 쿠키 삭제화 작업
        ResponseCookie cookie = ResponseCookie.from("refreshToken", null)
                .maxAge(0) //쿠키의 수명을 설정 / 기간은 7일
                .path("/") //쿠키의 경로를 설정 "/" 로 설정하면 서버의 모든 경로에서 쿠키에 접근할 수 있습니다.
                .build(); //체를 빌드하고 구성된 쿠키를 생성
        response.setHeader("Set-Cookie", cookie.toString());


        String msg = userMemberService.deleteAccountConfirm(id, refreshToken);

        return msg;

    }

    /*
     * 유저 계정 수정
     */
    @PutMapping("/change")
    public Map<String, Object> updateAccountConfirm(@RequestParam ("id") String id, @RequestPart UserMemberDto userMemberDto) {
        log.info("[HomeController] deleteAccountConfirm()");
        log.info("id : " + id);
        log.info("name : " + userMemberDto.getName());
        log.info("name : " + userMemberDto.getMail());

        Map<String, Object> msgData = userMemberService.updateAccountConfirm(id, userMemberDto);

        return msgData;

    }

    @GetMapping("/gettest")
    public  String TestController(){
        log.info("Test On!");

        return "gettest Success!!";
    }

    @PostMapping("/posttest")
    public String test() {
        log.info("Test On!");

        return "gettest success";
    }


}
