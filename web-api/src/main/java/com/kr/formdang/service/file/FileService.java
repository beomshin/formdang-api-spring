package com.kr.formdang.service.file;

import com.kr.formdang.layer.FileDataDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface FileService<T> {

    T uploadSingle(MultipartFile file);
    CompletableFuture<FileDataDto> uploadSingle(FileDataDto fileDataDto);

}
