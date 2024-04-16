package com.kr.formdang.file.impl;

import com.kr.formdang.file.FileService;
import com.kr.formdang.file.dto.FormFile;
import com.kr.formdang.file.dto.S3File;
import com.kr.formdang.utils.file.AwsS3Utils;
import com.kr.formdang.exception.CustomException;
import com.kr.formdang.common.GlobalCode;
import com.kr.formdang.utils.file.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AwsS3FileService implements FileService<S3File> {

    @Value("${file.size.image}")
    private int IMAGE_MAX_SIZE; // 30MB

    private final Set<String> accessFileExt;

    /**
     * 확장자 제어 생성자
     *
     * 이미지: jpg, jpeg, png, gif, bmp
     * 영상: mp4, avi, wmv, mpg, mkv, webm
     *
     */
    public AwsS3FileService() {
        this.accessFileExt = new HashSet<>(Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp"));
    }

    @Override
    public S3File uploadSingle(MultipartFile file) {
        return this.upload(file, IMAGE_MAX_SIZE, accessFileExt); // 이미지 최대사이즈, 확장자 제어 결정 후 공통 함수 호출
    }

    @Override
    @Async
    public CompletableFuture<S3File> uploadSingle(FormFile formFile) {
        S3File s3File = this.upload(formFile.getFile(), IMAGE_MAX_SIZE, accessFileExt);
        s3File.setOrder(formFile.getOrder());
        s3File.setType(formFile.getType());
        return CompletableFuture.completedFuture(s3File);
    }


    private S3File upload(MultipartFile file, Integer maxSize, Set<String> accessSet) {
        try {
            if (file == null || file.isEmpty()) throw new CustomException(GlobalCode.NOT_EXIST_FILE); // 파일 누락
            String ext = FileUtils.getFileExtension(Objects.requireNonNull(file.getOriginalFilename())); // 확장자 제어
            if (file.getSize() >= maxSize || !accessSet.contains(ext)) throw new CustomException(GlobalCode.FAIL_FILE_CONDITION); // 파일 사이즈 제어
            log.info("■ [AWS S3 파일 업로드 요청] 파일명 [{}], 사이즈 [{}], 확장자 [{}]", file.getOriginalFilename(), file.getSize(), ext);
            String path = AwsS3Utils.fileUploadToS3(file.getInputStream(), file.getSize(), file.getContentType(), ext);
            log.debug("■ [AWS S3 파일 업로드 성공] 파일 URL: {}", path);
            return new S3File(path, file.getSize(), file.getOriginalFilename(), StringUtils.isNoneBlank(path));
        } catch (Exception e) {
            log.error("[파일 업로드 실패]======================>");
            log.error("", e);
            return new S3File();
        }
    }
}
