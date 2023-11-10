package com.example.peernow360.service.impl;

import com.example.peernow360.dto.PeerDto;
import com.example.peernow360.dto.ReviewDto;

import java.util.List;

public interface IReviewService {

    int createScore(ReviewDto reviewDto);

    List<ReviewDto> feedback(int no, String user_id);

    PeerDto evaluationInfo(PeerDto peerDto);
}
