package com.example.peernow360.utils.UserMember;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserMemberUtils {

    /*
     * 쿠키를 가져오는 메서드
     */
    public ResponseCookie getCookieToken(String refreshToken) {
        log.info("[UserMemberController] getCookie()");

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(7 * 24 * 60 * 60) //쿠키의 수명을 설정 / 기간은 7일
                .path("/") //쿠키의 경로를 설정 "/" 로 설정하면 서버의 모든 경로에서 쿠키에 접근할 수 있습니다.
                .secure(true) //쿠키를 보안 연결 (HTTPS)에서만 전송하도록 설정
                .sameSite("None") //쿠키의 SameSite 속성을 설정합니다. "None"은 쿠키가 모든 요청에 대해 전송되도록 허용함을 의미
                .httpOnly(true) //쿠키를 JavaScript로 접근할 수 없도록 설정
                .build(); //체를 빌드하고 구성된 쿠키를 생성
//        response.setHeader("Set-Cookie", cookie.toString());

        // msgData Map안에는 refreshToken 정보가 담겨있기 때문에 해당 키값을 삭제 해주고 response 해줘야 한다. refresh는 쿠키에 담아서 주기 때문

        return cookie;

    }

    /*
     * 쿠키 초기화
     */
    public ResponseCookie clearCookieToken() {
        log.info("[UserMemberController] clearCookieToken()");

        // 로그아웃을 하면 쿠키 삭제화 작업
        ResponseCookie cookie = ResponseCookie.from("refreshToken", null)
                .maxAge(0) //쿠키의 수명을 설정 / 기간은 7일
                .path("/") //쿠키의 경로를 설정 "/" 로 설정하면 서버의 모든 경로에서 쿠키에 접근할 수 있습니다.
                .build(); //체를 빌드하고 구성된 쿠키를 생성

        return cookie;

    }


    /*
     * RefreshToken 추출 메서드
     */
    public String separateRefToken(String refreshToken) {
        log.info("[UserMemberController] extractRefToken()");
        log.info("RefreshToken :" + refreshToken.substring(refreshToken.indexOf("=", 1)+1));

        // refresh Token만 추출
        return refreshToken.substring(refreshToken.indexOf("=", 1)+1);

    }

}
