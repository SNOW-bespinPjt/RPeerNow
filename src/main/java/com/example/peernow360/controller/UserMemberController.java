package com.example.peernow360.controller;

import com.example.peernow360.dto.UserMemberDto;

import com.example.peernow360.response.ResponseService;
import com.example.peernow360.response.SingleResponse;

import com.example.peernow360.response.*;

import com.example.peernow360.security.JWTtokenProvider;
import com.example.peernow360.service.S3GetImage;
import com.example.peernow360.service.UserMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "userMember", description = "유저멤버 서비스")
public class UserMemberController {

    private final UserMemberService userMemberService;
    private final ResponseService responseService;
    private final S3GetImage s3GetImage;

    @GetMapping("/gettest")
    public String dontTouchMeNoDelete(){


        return "okayGoServer!!";
    }

    /*
     * 유저 계정 생성
     */
    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "회원가입", tags = {"create"})
    public String createAccountConfirm(@RequestPart(value = "image", required = false) MultipartFile multipartFile,
                                       @RequestPart UserMemberDto userMemberDto) throws IOException {
        log.info("[UserMemberController] createAccountConfirm()");

        return ResponseResult.result(userMemberService.createAccountConfirm(multipartFile, userMemberDto));

    }

    /*
     * 유저 계정 로그인
     */
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인", tags = {"create"})
    public ResponseEntity userLogin(@RequestBody UserMemberDto userMemberDto) {
        log.info("[HomeController] UserLogin()");

        return userMemberService.loginMember(userMemberDto);

    }

    /*
     * accessToken 완료시 refreshToken을 이용한 재발급
     */
    @PostMapping("/request_refreshToken")
    @Operation(summary = "refreshtoken 발급", description = "refreshtoken 발급", tags = {"create"})
    public ResponseEntity reissuanceAccessToken(@RequestHeader(value = "cookie") String refreshToken) {
        log.info("[HomeController] ReissuanceRefreshToken()");

        return userMemberService.reCreateAccessToken(refreshToken);

    }

    /*
     * 회원 정보 불러오기
     */
    @GetMapping("/detail")
    @Operation(summary = "회원정보 불러오기", description = "회원정보 불러오기", tags = {"detail"})
    public SingleResponse<UserMemberDto> userDetail() throws IOException {
        log.info("[HomeController] userDetail()");

        return responseService.getSingleResponse(userMemberService.userDetailInfo());

    }

    @GetMapping("/userimg")
    @Operation(summary = "회원 이미지", description = "회원 이미지", tags = {"detail"})
    public Object userimg() throws IOException {
        log.info("userimg()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        String fileName = userMemberService.fileName(user_id);

        Map map= new HashMap();
        map.put("image",s3GetImage.getObject(user_id + "/" + fileName));

        return map;

    }

    @PostMapping("/logout_info")
    @Operation(summary = "로그아웃", description = "로그아웃", tags = {"create"})
    public ResponseEntity<String> logOutInfo(@RequestHeader(value = "cookie") String refreshToken) {
        log.info("[HomeController] logOut()");

        return userMemberService.logOutInfo(refreshToken);

    }

    /*
     * 유저 계정 삭제
     */
    @DeleteMapping("/leave")
    @Operation(summary = "계정 삭제", description = "계정 삭제", tags = {"delete"})
    public String deleteAccountConfirm(@RequestParam ("id") String id, @RequestHeader(value = "cookie") String refreshToken) {
        log.info("[HomeController] deleteAccountConfirm()");
        log.info("id : " + id);

       return ResponseResult.result(userMemberService.deleteAccountConfirm(id, refreshToken));

    }

    /*
     * 유저 계정 수정
     */
    @PutMapping("/change")
    @Operation(summary = "계정 수정", description = "계정 수정", tags = {"modify"})
    public String updateAccountConfirm(@RequestParam ("id") String id,
                                       @RequestPart UserMemberDto userMemberDto,
                                       @RequestPart (required = false) MultipartFile image ) throws IOException {
        log.info("[HomeController] deleteAccountConfirm()");

        return ResponseResult.result(userMemberService.updateAccountConfirm(id, userMemberDto, image));

    }

    @PutMapping("/imagechange")
    @Operation(summary = "계정 이미지 수정", description = "계정 이미지 수정", tags = {"modify"})
    public String updateAccountImage(@RequestParam ("id") String id,
                                     @RequestParam(value = "fileName", required = false) String fileName,
                                     @RequestPart("image") MultipartFile multipartFile) throws IOException {
        log.info("[HomeController] updateAccountImage()");

        return ResponseResult.result(userMemberService.updateAccountImage(id, fileName, multipartFile));

    }

    /*
     * 프로젝트 변경 시 권한 재발급
     */
    @GetMapping("/authority")
    @Operation(summary = "계정 이미지 수정", description = "계정 이미지 수정", tags = {"modify"})
    public SingleResponse<UserMemberDto> changeAuthority(@RequestParam (value = "project_no") int project_no) {
        log.info("[HomeController] changeAuthority()");

        return responseService.getSingleResponse(userMemberService.changeAuthority(project_no));

    }

}
