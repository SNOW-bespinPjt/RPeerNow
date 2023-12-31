package com.example.peernow360.service;

import com.example.peernow360.dto.*;
import com.example.peernow360.exception.CustomException;
import com.example.peernow360.exception.RestApiException;
import com.example.peernow360.exception.ReviewErrorCode;
import com.example.peernow360.exception.UserErrorCode;
import com.example.peernow360.mappers.IReviewMapper;
import com.example.peernow360.service.impl.IReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final IReviewMapper iReviewMapper;

    private final S3Uploader s3Uploader;
    private final S3GetImage s3GetImage;

    public int createScore(ReviewDto reviewDto) {
        log.info("createScore()");

        int searchResult = iReviewMapper.getReviewInfo(reviewDto);

        if (searchResult > 0) {

            int result = iReviewMapper.createScore(reviewDto);

            int total = reviewDto.getScore1() + reviewDto.getScore2() + reviewDto.getScore3() + reviewDto.getScore4() + reviewDto.getScore5();
            reviewDto.setTotal(total);
            iReviewMapper.totalScore(reviewDto);

            return result;

        } else {
            throw new CustomException(ReviewErrorCode.ALREADY_EVALUATE);
        }

//        int result = iReviewMapper.createScore(reviewDto);
//
//        int total = reviewDto.getScore1() + reviewDto.getScore2() + reviewDto.getScore3() + reviewDto.getScore4() + reviewDto.getScore5();
//        reviewDto.setTotal(total);
//        iReviewMapper.totalScore(reviewDto);

//        return result;
    }

    public List<ReviewDto> feedback(int no, String user_id) {
        log.info("feedback()");

        return iReviewMapper.feedback(String.valueOf(no), user_id);
    }

    public PeerDto evaluationInfo(PeerDto peerDto) throws IOException {
        log.info("evaluatinoInfo()");

        peerDto.setAvg(iReviewMapper.avgScore(peerDto));
        peerDto.setBest_id(iReviewMapper.bestId(peerDto));
        peerDto.setBest_name(iReviewMapper.bestName(peerDto));
//        peerDto.setBest_image(iReviewMapper.bestImg(peerDto));

        try {
            peerDto.setBest_image(iReviewMapper.bestImg(peerDto));

            Object image = s3GetImage.getObject(peerDto.getBest_id() + "/" + peerDto.getBest_image());

            if(image == null) {
                image = s3GetImage.getObject("defaultImg/defaultImg.png");
            }

            peerDto.setBest_image(image);

        } catch (Exception e){

            peerDto.setBest_image(s3GetImage.getObject("defaultImg/defaultImg.png"));
        }

        return peerDto;
    }

    public List<PeerReviewDto> peerlist(PeerReviewDto peerReviewDto) throws IOException {
        log.info("peerlist()");

        List<PeerReviewDto> list = iReviewMapper.peerlist(peerReviewDto);
        for(PeerReviewDto peerReviewDtos : list) {

            try {
                Object image = s3GetImage.getObject(peerReviewDtos.getPeer_id() + "/" + peerReviewDtos.getPeer_image());

                if(image == null) {
                    image = s3GetImage.getObject("defaultImg/defaultImg.png");
                }
                peerReviewDtos.setPeer_image(image);

            } catch (Exception e) {
                peerReviewDtos.setPeer_image(s3GetImage.getObject("defaultImg/defaultImg.png"));
            }

        }

        return list;
    }

    public int test(MultipartFile multipartFile, TestDto testDto) throws IOException {
        log.info("test()");

        if(multipartFile != null) {
            s3Uploader.upload(multipartFile, testDto.getUser_id());
            testDto.setFile(multipartFile.getOriginalFilename());
        }

        int result = iReviewMapper.test(testDto);

        return result;
    }

    public String fileName(String userId) {

        return iReviewMapper.fileName(userId);
    }

    public int modify(String userId, String fileName, MultipartFile multipartFile) throws IOException {
        log.info("modify()");

        s3Uploader.delete(userId, fileName);
        int result = iReviewMapper.delete(userId);

        s3Uploader.upload(multipartFile, userId);

        return result;
    }


}
