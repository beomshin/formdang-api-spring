package com.kr.formdang.utils.file;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Configuration
public class AwsS3Utils {

    private static AmazonS3Client amazonS3Client;
    private static String buket;
    private static String host;

    public AwsS3Utils(
            @Value("${aws.s3.region}") String region,
            @Value("${aws.s3.access-key}") String accessKey,
            @Value("${aws.s3.secret-key}") String secretKey,
            @Value("${aws.s3.buket}") String s3_buket,
            @Value("${aws.s3.host}") String s3_host
    ) {
        amazonS3Client = (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
        buket = s3_buket;
        host = s3_host;
    }

    /**
     * AWS 파일 업로드
     * @param stream
     * @param size
     * @param contentType
     * @param ext
     * @return
     */
    public static String fileUploadToS3(InputStream stream, long size, String contentType, String ext) {
        try(InputStream inputStream = stream) {
            String fileName = UUID.randomUUID().toString().concat(ext); // 파일명 UUID 생성
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(size);
            objectMetadata.setContentType(contentType);
            amazonS3Client.putObject(new PutObjectRequest(buket, fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead)); // 업로드 호출
            return host + fileName; // 호스트명 + 파일명 URL 생성
        } catch(IOException e) {
            log.error("[AWS 이미지 업로드 실패] ========>");
            return null;
        }
    }

}
