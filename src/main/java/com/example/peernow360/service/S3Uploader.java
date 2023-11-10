package com.example.peernow360.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Log4j2
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

//    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
//
//        return upload2(multipartFile, dirName);
//    }

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
//        String timestamp = dateFormat.format(new Date());
//        String fileName = dirName + "/" + timestamp + "_" + multipartFile.getOriginalFilename();

        String fileName = dirName + "/" + multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        String uploadImageUrl = putS3(fileName,multipartFile.getInputStream(),metadata);

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    private String putS3(String fileName, InputStream inputStream, ObjectMetadata metadata) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName,inputStream,metadata)
                        .withCannedAcl(CannedAccessControlList.PublicReadWrite)	// PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

}
