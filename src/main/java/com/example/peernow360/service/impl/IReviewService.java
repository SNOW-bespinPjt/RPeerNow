package com.example.peernow360.service.impl;

import com.example.peernow360.dto.PeerDto;
import com.example.peernow360.dto.PeerReviewDto;
import com.example.peernow360.dto.ReviewDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IReviewService {

    int createScore(ReviewDto reviewDto);

    List<ReviewDto> feedback(int no, String user_id);

    PeerDto evaluationInfo(PeerDto peerDto);

    int modify(String userId, String fileName, MultipartFile multipartFile) throws IOException;

    List<PeerReviewDto> peerlist(PeerReviewDto peerReviewDto);
}
