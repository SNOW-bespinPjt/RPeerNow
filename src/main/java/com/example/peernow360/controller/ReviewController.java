package com.example.peernow360.controller;

import com.example.peernow360.dto.ReviewDto;
import com.example.peernow360.response.ResponseService;
import com.example.peernow360.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/peer")
public class ReviewController {

    private final ResponseService responseService;
    private final ReviewService reviewService;

    @PostMapping("/evaluation")
    @Operation(summary = "동료 평가하기", description = "동료 평가하기", tags = {"create"})
    public String createScore(@RequestParam("projectNumber") int no, @RequestParam("peerId") String peerId, ReviewDto reviewDto) {
        log.info("createScore()");

        User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_id = userInfo.getUsername();

        reviewDto.setProject_no(no);
        reviewDto.setUser_id(user_id);

        int result = reviewService.createScore(reviewDto);

        String message;
        if (result > 0) {
            message = "success";
        } else {
            message = "fail";
        }

        return message;
    }
}
