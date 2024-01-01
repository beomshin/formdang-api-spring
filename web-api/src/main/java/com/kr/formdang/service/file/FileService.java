package com.kr.formdang.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService<T> {
    T uploadSingle(MultipartFile file);
}
