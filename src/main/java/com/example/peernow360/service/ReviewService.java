package com.example.peernow360.service;

import com.example.peernow360.dto.PeerDto;
import com.example.peernow360.dto.ReviewDto;
import com.example.peernow360.mappers.IReviewMapper;
import com.example.peernow360.service.impl.IReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final IReviewMapper iReviewMapper;

    public int createScore(ReviewDto reviewDto) {
        log.info("createScore()");

        int result = iReviewMapper.createScore(reviewDto);

        int total = reviewDto.getScore1() + reviewDto.getScore2() + reviewDto.getScore3() + reviewDto.getScore4() + reviewDto.getScore5();
        reviewDto.setTotal(total);
        iReviewMapper.totalScore(reviewDto);

        return result;
    }

    public List<ReviewDto> feedback(int no) {
        log.info("feedback()");

        return iReviewMapper.feedback(no);
    }

    public PeerDto evaluationInfo(PeerDto peerDto) {
        log.info("evaluatinoInfo()");

        peerDto.setAvg(iReviewMapper.avgScore(peerDto));
        peerDto.setBest(iReviewMapper.best(peerDto));

        List<String> peerIds = iReviewMapper.getPeer(peerDto);
        peerDto.setPeer_id(peerIds);

        return peerDto;
    }
}
