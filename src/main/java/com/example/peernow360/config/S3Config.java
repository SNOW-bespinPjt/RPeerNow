package com.example.peernow360.config;

import com.amazonaws.auth.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.apache.http.client.CredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${modekey}")
    private  String env;

    @Bean
    public AmazonS3 amazonS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
//        AWSCredentials credentials = new InstanceProfileCredentialsProvider();
        AWSCredentialsProvider credentialsProvide;

        if (env.equals("dev")) {
            credentialsProvide =  new ProfileCredentialsProvider();
        } else {
            credentialsProvide = InstanceProfileCredentialsProvider.getInstance();
        }
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withCredentials(credentialsProvide)
                .withRegion(region)
                .build();
    }
}
