package com.kr.formdang.service.file.impl;

import com.kr.formdang.exception.CustomException;
import com.kr.formdang.model.common.GlobalCode;
import com.kr.formdang.model.common.GlobalFile;
import com.kr.formdang.service.file.FileService;
import com.kr.formdang.utils.AwsS3Utils;
import com.kr.formdang.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileServiceImpl implements FileService<GlobalFile> {

    @Value("${file.size.image}")
    private int IMAGE_MAX_SIZE;
    @Value("${file.size.video}")
    private int VIDEO_MAX_SIZE;
    @Value("${file.size.replay}")
    private int REPLAY_MAX_SIZE;
    private final Set<String> accessFileExt, accessVideoExt, accessReplayExt;
    private final AwsS3Utils awsS3Utils;


    public FileServiceImpl(AwsS3Utils awsS3Utils) {
        this.awsS3Utils = awsS3Utils;
        this.accessFileExt = new HashSet<>(Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp"));
        this.accessVideoExt = new HashSet<>(Arrays.asList(".mp4", ".avi", ".wmv", ".mpg", ".mkv", ".webm"));
        this.accessReplayExt = new HashSet<>(Collections.singletonList(".rofl"));
    }

    @Override
    public List<GlobalFile> uploadMultiple(List<MultipartFile> files) {
        return files.stream().map(file -> this.upload(file,IMAGE_MAX_SIZE, accessFileExt)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public GlobalFile uploadSingle(MultipartFile file) {
        return this.upload(file, IMAGE_MAX_SIZE, accessFileExt);
    }

    @Override
    public GlobalFile uploadVideo(MultipartFile file) {
        return this.upload(file, VIDEO_MAX_SIZE, accessVideoExt);
    }

    @Override
    public GlobalFile uploadReplay(MultipartFile file) {
        return this.upload(file, REPLAY_MAX_SIZE, accessReplayExt);
    }

    private GlobalFile upload(MultipartFile file, Integer maxSize, Set<String> accessSet) {
        try {
            if (file == null || file.isEmpty()) throw new CustomException(GlobalCode.NOT_EXIST_FILE);
            String ext = FileUtils.getAccessFileExtension(file.getOriginalFilename());
            log.debug("[파일 업로드] 파일명 [{}], 사이즈 [{}], 최대 사이즈 [{}], 확장자 [{}]", file.getOriginalFilename(), file.getSize(), maxSize, ext);
            if (file.getSize() >= maxSize || !accessSet.contains(ext)) throw new CustomException(GlobalCode.FAIL_FILE_CONDITION);
            String path = awsS3Utils.fileUploadToS3(file, ext);
            return path != null ? new GlobalFile(file, path) : null;
        } catch (Exception e) {
            log.error("[파일 업로드 실패]======================>");
            log.error("{}", e);
            return null;
        }
    }
}
