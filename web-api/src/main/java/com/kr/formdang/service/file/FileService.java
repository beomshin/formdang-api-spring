package com.kr.formdang.service.file;

import com.kr.formdang.model.common.GlobalFile;
import com.kr.formdang.model.layer.FileDataDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface FileService<T> {
    T uploadSingle(MultipartFile file);

    CompletableFuture<FileDataDto> uploadSingle(FileDataDto fileDataDto);
}
