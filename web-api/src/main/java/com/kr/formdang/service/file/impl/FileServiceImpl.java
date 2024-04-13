package com.kr.formdang.service.file.impl;

import com.kr.formdang.exception.CustomException;
import com.kr.formdang.model.common.GlobalCode;
import com.kr.formdang.model.common.GlobalFile;
import com.kr.formdang.model.layer.FileDataDto;
import com.kr.formdang.service.file.FileService;
import com.kr.formdang.utils.AwsS3Utils;
import com.kr.formdang.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class FileServiceImpl implements FileService<GlobalFile> {

    @Value("${file.size.image}")
    private int IMAGE_MAX_SIZE; // 30MB

    private final Set<String> accessFileExt;
    private final AwsS3Utils awsS3Utils;

    /**
     * 확장자 제어 생성자
     *
     * 이미지: jpg, jpeg, png, gif, bmp
     * 영상: mp4, avi, wmv, mpg, mkv, webm
     *
     * @param awsS3Utils
     */
    public FileServiceImpl(AwsS3Utils awsS3Utils) {
        this.awsS3Utils = awsS3Utils;
        this.accessFileExt = new HashSet<>(Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp"));
    }

    @Override
    public GlobalFile uploadSingle(MultipartFile file) {
        return this.upload(file, IMAGE_MAX_SIZE, accessFileExt); // 이미지 최대사이즈, 확장자 제어 결정 후 공통 함수 호출
    }

    @Override
    @Async
    public CompletableFuture<FileDataDto> uploadSingle(FileDataDto fileDataDto) {
        GlobalFile globalFile = this.upload(fileDataDto.getFile(), IMAGE_MAX_SIZE, accessFileExt);
        fileDataDto.setAwsFile(globalFile);
        return CompletableFuture.completedFuture(fileDataDto);
    }


    private GlobalFile upload(MultipartFile file, Integer maxSize, Set<String> accessSet) {
        try {
            if (file == null || file.isEmpty()) throw new CustomException(GlobalCode.NOT_EXIST_FILE); // 파일 누락
            String ext = FileUtils.getAccessFileExtension(file.getOriginalFilename()); // 확장자 제어
            log.info("[파일 업로드] 파일명 [{}], 사이즈 [{}], 최대 사이즈 [{}], 확장자 [{}]", file.getOriginalFilename(), file.getSize(), maxSize, ext);
            if (file.getSize() >= maxSize || !accessSet.contains(ext)) throw new CustomException(GlobalCode.FAIL_FILE_CONDITION); // 파일 사이즈 제어
            String path = awsS3Utils.fileUploadToS3(file, ext); // AWS S3 파일 업로드
            return path != null ? new GlobalFile(file, path) : null;
        } catch (Exception e) {
            log.error("[파일 업로드 실패]======================>");
            log.error("{}", e);
            return null;
        }
    }
}
