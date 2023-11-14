package com.example.peernow360.controller;

import com.example.peernow360.dto.PeerDto;
import com.example.peernow360.dto.PeerReviewDto;
import com.example.peernow360.dto.ReviewDto;
import com.example.peernow360.dto.TestDto;
import com.example.peernow360.response.ListResponse;
import com.example.peernow360.response.ResponseResult;
import com.example.peernow360.response.ResponseService;
import com.example.peernow360.response.SingleResponse;
import com.example.peernow360.service.ReviewService;
import com.example.peernow360.service.S3Download;
import com.example.peernow360.service.S3GetImage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/peer")
@Tag(name = "peer", description = "동료평가")
public class ReviewController {

    private final ResponseService responseService;
    private final ReviewService reviewService;

    private final S3GetImage s3GetImage;
    private final S3Download s3Download;

    @PostMapping("/evaluation")
    @Operation(summary = "동료 평가하기", description = "동료 평가하기", tags = {"create"})
    public String createScore(@RequestParam("projectNumber") int no, @RequestParam("peerId") String peerId, @RequestBody ReviewDto reviewDto) {
        log.info("createScore()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        reviewDto.setProject_no(no);
        reviewDto.setUser_id(user_id);
        reviewDto.setPeer_id(peerId);

        int result = reviewService.createScore(reviewDto);

        return ResponseResult.result(result);
    }

    @GetMapping("/feedback")
    @Operation(summary = "나의 피드백", description = "나의 피드백", tags = {"detail"})
    public ListResponse<ReviewDto> feedback(@RequestParam("projectNumber") int no) {
        log.info("feedback()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        return responseService.getListResponse(reviewService.feedback(no, user_id));
    }

    @GetMapping("")
    @Operation(summary = "동료평가 메인", description = "동료평가 메인", tags = {"detail"})
    public SingleResponse<PeerDto> evaluationInfo(@RequestParam("projectNumber") int no) {
        log.info("evaluationInfo()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        PeerDto peerDto = new PeerDto();
        peerDto.setNo(no);
        peerDto.setUser_id(user_id);

        return responseService.getSingleResponse(reviewService.evaluationInfo(peerDto));
    }

    @GetMapping("/peerlist")
    @Operation(summary = "나의 동료", description = "나의 동료", tags = {"detail"})
    public ListResponse<PeerReviewDto> peerlist(@RequestParam("projectNumber") int no) throws IOException {
        log.info("peerlist()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        PeerReviewDto peerReviewDto = new PeerReviewDto();
        peerReviewDto.setNo(no);
        peerReviewDto.setUser_id(user_id);

        return responseService.getListResponse(reviewService.peerlist(peerReviewDto));

    }

    @PostMapping("/test")
    public String test(@RequestPart("image") MultipartFile multipartFile, @RequestPart TestDto testDto) throws IOException {
        log.info("test");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        testDto.setUser_id(user_id);

        int result = reviewService.test(multipartFile, testDto);

        return ResponseResult.result(result);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download() throws IOException {
        log.info("download()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        String fileName = reviewService.fileName(user_id);

        return s3Download.getObject(user_id + "/" + fileName);
    }


    @DeleteMapping("/modify")
    public String modify(@RequestParam(value = "fileName", required = false) String fileName, @RequestPart("image") MultipartFile multipartFile) throws IOException {
        log.info("modify()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        int result = reviewService.modify(user_id, fileName, multipartFile);

        return ResponseResult.result(result);
    }


}
