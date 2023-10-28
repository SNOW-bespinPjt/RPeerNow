package com.example.peernow360.security;


import com.example.peernow360.mappers.IUserMemberMapper;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//토큰을 만들고 분석하는 클래스
@Component
@Log4j2
@RequiredArgsConstructor
public class JWTtokenProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;
    private long accesstokenValidMillisecond=60* 60 * 1000L; // 30분
    private long refreshtokenValidMillisecond=60 * 60 * 24 * 7 * 1000L; // 1주잂
//    private long refreshtokenValidMillisecond=1000L; // 1주잂
    private final UserDetailsService userDetailsService;
    private final IUserMemberMapper iUserMemberMapper;

    @PostConstruct
    protected void secretEncoding(){
        secretKey= Base64.getEncoder().encodeToString(secretKey.getBytes());

    }

    /*
     * Access Token 생성
     */
    public String createAccessToken(Authentication authentication, String roles) {
        log.info("");
        log.info("=====================================");
        log.info("[JWTtokenProvider] createAccessToken()");
        log.info("Access secretKey: {}",secretKey);
        Claims claims= Jwts.claims().setSubject(authentication.getName()); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
        claims.put("roles",roles);
        Date now=new Date();

        /*
         * accessToken & refreshToken에 같은 유효기간(tokenValidMillisecond)을 가지는 이유
         * Refresh Token이 유출되어서 다른 사용자가 이를 통해 새로운 Access Token을 발급받았다면?
         * 이 경우, Access Token의 충돌이 발생하기 때문에, 서버측에서는 두 토큰을 모두 폐기
         * 표준화기구(IETF)에서 이를 방지하기 위해 Refresh Token도 Access Token과 같은 유효 기간을 가지도록 권장
         */
        String accessToken=Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+accesstokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256,secretKey).compact();

        return accessToken;

    }

    /*
     * Refresh Token 생성
     */
    public String createRefreshToken(Authentication authentication, String roles) {
        log.info("[JWTtokenProvider] createRefreshToken()");
        log.info("Refresh secretKey: {}",secretKey);
        Claims claims= Jwts.claims().setSubject(authentication.getName()); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
//        claims.put("roles",authentication.getAuthorities());
        claims.put("roles", roles);
        Date now=new Date();

        /*
         * accessToken & refreshToken에 같은 유효기간(tokenValidMillisecond)을 가지는 이유
         * Refresh Token이 유출되어서 다른 사용자가 이를 통해 새로운 Access Token을 발급받았다면?
         * 이 경우, Access Token의 충돌이 발생하기 때문에, 서버측에서는 두 토큰을 모두 폐기
         * 표준화기구(IETF)에서 이를 방지하기 위해 Refresh Token도 Access Token과 같은 유효 기간을 가지도록 권장
         */
        String refreshToken=Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(now.getTime()+refreshtokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();

        return refreshToken;

    }

    //JWT 토큰에서 인증 정보 조회
    public Authentication selectAuthority(String token){
        log.info("[JWTtokenProvider] selectAuthority()");

        UserDetails userDetails = userDetailsService.loadUserByUsername(this.checkUser(token));
        log.info("userDetails : " + userDetails);

        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());

    }

    //토큰의 유효성 + 만료일자 확인
    public boolean checkToken(String token){
        log.info("[JWTtokenProvider] checkToken()");

        try {
            Jws<Claims> claims=Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            log.info("what is claim: " + claims.getBody());
            log.info("claims: " + claims.getBody().getExpiration().before(new Date()));

            // 오늘 날짜보다 이전일 경우 true지만 이전이면 만료된 것이기 때문에 false로 줌
            // 오늘 날짜보다 이후일 경우 false이지만 만료가 된 것이 아니기 때문에 true
            return !claims.getBody().getExpiration().before(new Date());

        } catch (ExpiredJwtException ex) {
            log.error("JWT 토큰이 만료되었습니다.");

            return false;

        } catch (Exception e) {
            log.info("JWT 유효성 검증에 문제가 발생하였습니다.");

            return false;

        }

    }

    //토큰에서 회원 정보 추출
    public String checkUser(String token){
        log.info("[JWTtokenProvider] checkUser()");
        log.info("checkUserInfo: " + Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();

    }

    @Transactional(readOnly = true)
    public boolean getRefreshToken(String user_id) {
        log.info("[JWTtokenProvider] getRefreshToken()");

        String refreshToken = iUserMemberMapper.findByToken(user_id);
        log.info("refreshToken: " + refreshToken);

        if(!StringUtils.hasText(refreshToken)) {
            log.info("회원가입이 되지 않았거나, 로그아웃을 한 유저입니다.");
            return false;

        }

        return true;

    }

    public int insertRefreshToken(Object principal, String refreshToken) {
        log.info("[JWTtokenProvider] insertRefreshToken()");

        log.info("insertRefreshToken id : " + principal);
//        log.info("saveOrUpdateRefreshToken id : " + principal.getUsername());
        log.info("insertRefreshToken token : " + refreshToken);

        Map<String, Object> msgData = new HashMap<>();
//        msgData.put("id",user.getUsername());
        msgData.put("user_id",principal);
        msgData.put("refreshToken",refreshToken);

       return iUserMemberMapper.insertRefreshToken(msgData);

    }

    /*
     * DB에 재발급 refreshToken 입력
     */
    public int updateRefreshToken(String name, String refreshToken) {
        log.info("[JWTtokenProvider] updateRefreshToken()");

        log.info("insertRefreshToken id : " + name);
        log.info("insertRefreshToken token : " + refreshToken);

        Map<String, Object> msgData = new HashMap<>();
//        msgData.put("id",user.getUsername());
        msgData.put("user_id",name);
        msgData.put("refreshToken",refreshToken);

        return iUserMemberMapper.modifyRefreshToken(msgData);

    }


    /*
     * 토큰 정보를 검증하는 메서드
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT Token", e);

        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);

        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);

        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);

        }
        return false;
    }

    /*
     * 토큰 예외처리를 반환하는 메서드
     */
    public ResponseEntity<String> validateTokenAndReturnMessage(String token) {

        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return ResponseEntity.ok("Token is valid");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT Token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Expired JWT Token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unsupported JWT Token: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT claims string is empty: " + e.getMessage());
        }

    }

    public String extractRefreshToken(String refreshToken) {
        log.info("[JWTtokenProvider] extractRefreshToken()");
        log.info("extractRefreshToken: " + Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken).getBody().getSubject());

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken).getBody().getSubject();

    }


    public boolean selectForBlacklist(String refreshToken) {
        log.info("[JWTtokenProvider] selectForBlacklist()");

        return !(iUserMemberMapper.selectBlackListToken(refreshToken));

    }
}
