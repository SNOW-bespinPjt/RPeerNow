package com.example.peernow360.controller;

import com.example.peernow360.dto.ReviewDto;
import com.example.peernow360.dto.TeamDto;
import com.example.peernow360.dto.UserMemberDto;
import com.example.peernow360.response.ListResponse;
import com.example.peernow360.response.ResponseService;
import com.example.peernow360.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/peer")
@Tag(name = "peer", description = "동료평가")
public class ReviewController {

    private final ResponseService responseService;
    private final ReviewService reviewService;

    @PostMapping("/evaluation")
    @Operation(summary = "동료 평가하기", description = "동료 평가하기", tags = {"create"})
    public String createScore(@RequestParam("projectNumber") int no, @RequestParam("peerId") String peerId, @RequestPart ReviewDto reviewDto) {
        log.info("createScore()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        reviewDto.setProject_no(no);
        reviewDto.setUser_id(user_id);
        reviewDto.setPeer_id(peerId);

        int result = reviewService.createScore(reviewDto);

        String message;
        if (result > 0) {
            message = "success";
        } else {
            message = "fail";
        }

        return message;
    }

    @GetMapping("/feedback")
    @Operation(summary = "나의 피드백", description = "나의 피드백", tags = {"detail"})
    public ListResponse<ReviewDto> feedback(@RequestParam("projectNumber") int no) {
        log.info("feedback()");

        return responseService.getListResponse(reviewService.feedback(no));
    }

    @GetMapping("")
    @Operation(summary = "동료평가 메인", description = "동료평가 메인", tags = {"detail"})
    public ListResponse<ReviewDto> evaluationInfo(@RequestParam("projectNumber") int no) {
        log.info("evaluationInfo()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setProject_no(no);
        reviewDto.setUser_id(user_id);

        return responseService.getListResponse(reviewService.evaluatinoInfo(reviewDto));
    }
}
