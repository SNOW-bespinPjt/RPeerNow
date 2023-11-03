package com.example.peernow360.mappers;

import com.example.peernow360.dto.ReviewDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IReviewMapper {

    int createScore(ReviewDto reviewDto);

    List<ReviewDto> feedback(int no);

    List<ReviewDto> evaluationInfo(ReviewDto reviewDto);

    void totalScore(ReviewDto total);
}
