package com.kr.formdang.service.form;

import com.kr.formdang.model.file.S3File;
import com.kr.formdang.exception.ResultCode;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    void insertFailUploadFile(MultipartFile file, ResultCode resultCode);

    void insertFailUploadFile(S3File file, ResultCode resultCode);

}
