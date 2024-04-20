package com.kr.formdang.service.form.impl;

import com.kr.formdang.dto.S3File;
import com.kr.formdang.entity.FileUploadFailTbEntity;
import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.repository.FileUploadFailTbRepository;
import com.kr.formdang.service.form.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final FileUploadFailTbRepository fileUploadFailTbRepository;

    @Override
    public void insertFailUploadFile(MultipartFile file, ResultCode resultCode) {
        FileUploadFailTbEntity failTbEntity = FileUploadFailTbEntity.builder()
                .oriName(file.getOriginalFilename())
                .ext(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")))
                .size(String.valueOf(file.getSize()))
                .code(resultCode.getCode())
                .msg(resultCode.getMsg())
                .build();

        fileUploadFailTbRepository.save(failTbEntity);
    }

    @Override
    public void insertFailUploadFile(S3File file, ResultCode resultCode) {
        FileUploadFailTbEntity failTbEntity = FileUploadFailTbEntity.builder()
                .oriName(file.getOriName())
                .ext(file.getOriName().substring(file.getOriName().lastIndexOf(".")))
                .size(String.valueOf(file.getSize()))
                .code(resultCode.getCode())
                .msg(resultCode.getMsg())
                .build();

        fileUploadFailTbRepository.save(failTbEntity);
    }
}
