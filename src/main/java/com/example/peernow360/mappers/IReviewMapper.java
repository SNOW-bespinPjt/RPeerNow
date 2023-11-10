package com.example.peernow360.mappers;

import com.example.peernow360.dto.PeerDto;
import com.example.peernow360.dto.ReviewDto;
import com.example.peernow360.dto.TestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IReviewMapper {

    int createScore(ReviewDto reviewDto);

    List<ReviewDto> feedback(int no);

    List<ReviewDto> evaluationInfo(ReviewDto reviewDto);

    void totalScore(ReviewDto total);


    float avgScore(PeerDto peerDto);

    String bestId(PeerDto peerDto);

    String bestName(PeerDto peerDto);

    List<String> getPeer(PeerDto peerDto);

    int test(TestDto testDto);


    String fileName(String userId);

    List<ReviewDto> feedback(String no, String user_id);
}
