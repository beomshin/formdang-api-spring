package com.kr.formdang.controller;

import com.kr.formdang.model.common.GlobalFile;
import com.kr.formdang.model.net.request.FileRequest;
import com.kr.formdang.model.net.response.FileResponse;
import com.kr.formdang.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final FileService<GlobalFile> fileService;

    @PostMapping("/public/file/upload")
    public ResponseEntity uploadFile(
            @ModelAttribute @Valid FileRequest fileRequest
            ) {
        GlobalFile file = fileService.uploadSingle(fileRequest.getFile());
        return ResponseEntity.ok().body(new FileResponse(file));
    }

}
