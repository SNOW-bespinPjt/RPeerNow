package com.example.peernow360.mappers;

import com.example.peernow360.dto.ReviewDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IReviewMapper {

    int createScore(ReviewDto reviewDto);
}
