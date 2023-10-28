package com.example.peernow360.controller;

import com.example.peernow360.dto.UserMemberDto;
import com.example.peernow360.security.JWTtokenProvider;
import com.example.peernow360.service.UserMemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
//@RequestMapping("/user")
public class UserMemberController {

    private final UserMemberService userMemberService;
    private final JWTtokenProvider jwTtokenProvider;

    /*
     * 유저 계정 생성
     */
    @PostMapping("/join")
    public int createAccountConfirm(@RequestBody UserMemberDto userMemberDto) {
        log.info("[HomeController] createAccountConfirm()");

        int result = userMemberService.createAccountConfirm(userMemberDto);

        return result;

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
            msgData.put("status",0);
            msgData.put("msg","fail to login");
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
//        msgData.remove("refreshToken");

        msgData.put("status",1);
        msgData.put("msg","success");

        return ResponseEntity.ok(msgData);

    }

    /*
     * accessToken 완료시 refreshToken을 이용한 재발급
     */
    @PostMapping("/request_refreshToken")
    public ResponseEntity<Map<String, Object>> reissuanceAccessToken(@RequestHeader(value = "cookie") String refreshToken, HttpServletResponse response) {
        log.info("[HomeController] ReissuanceRefreshToken()");

        Map<String, Object> msgData = new HashMap<>();

        refreshToken = refreshToken.substring(refreshToken.indexOf("=", 1)+1);

        log.info("RefreshToken :" + refreshToken);



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

            msgData.put("status", 1);
            msgData.put("msg", "success");
            log.info("msgData" + msgData);

            return ResponseEntity.ok(msgData);

        }

        String msg = logOutInfo(refreshToken, response);
        log.info("자동화 로그아웃 msg : " + msg);

        msgData.put("msg",msg);

//        msgData.put("status", 0);
//        msgData.put("msg", "리플레쉬 토큰이 잘못되었습니다. 재 입력하거나 로그아웃 해주시기 바랍니다.");
//        log.info("msgData" + msgData);

        return ResponseEntity.ok(msgData);

    }

    @PostMapping("/logout_info")
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
    public Map<String, Object> updateAccountConfirm(@RequestParam ("id") String id, UserMemberDto userMemberDto) {
        log.info("[HomeController] deleteAccountConfirm()");
        log.info("id : " + id);

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
