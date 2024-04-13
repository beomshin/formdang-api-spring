package com.kr.formdang.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.kr.formdang.repository.ConfigTbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@Slf4j
public class AwsConfig {

    @Value("${cloud.aws.region.static}")
    private String region;
    private final String AWS_ACCESS_KEY = "aws_access_key";
    private final String AWS_SECRET_KEY = "aws_secret_key";
    private String accessKey;
    private String secretKey;

    public AwsConfig(ConfigTbRepository configTbRepository) { // AWS 키 세팅
        accessKey = configTbRepository.findByKey(AWS_ACCESS_KEY).getValue();
        secretKey = configTbRepository.findByKey(AWS_SECRET_KEY).getValue();
    }

    @Bean
    public AmazonS3Client amazonS3Client() {
        log.info("[Bean][Amazon S4 Client] 설정 ");
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
