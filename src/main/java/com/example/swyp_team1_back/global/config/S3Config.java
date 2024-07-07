package com.example.swyp_team1_back.global.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    // S3 등록한 사람이 전달받은 KEY 값
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    // S3 등록한 사람이 전달받은 SECRET KEY 값
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    //S3 등록한 사람이 S3를 사용할 지역
    @Value("${cloud.aws.region.static}")
    private String region;


    //전달받은 ACCESSKEY와 SECRETKEY로 아마존 서비스 실행 준비
    @Bean
    public AmazonS3Client amazonS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }
}
