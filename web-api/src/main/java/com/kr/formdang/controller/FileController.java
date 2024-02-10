package com.kr.formdang.controller;

import com.kr.formdang.exception.CustomException;
import com.kr.formdang.jwt.JwtService;
import com.kr.formdang.model.common.GlobalCode;
import com.kr.formdang.model.common.GlobalFile;
import com.kr.formdang.model.layer.FileDataDto;
import com.kr.formdang.model.net.request.FileListRequest;
import com.kr.formdang.model.net.request.FileRequest;
import com.kr.formdang.model.net.response.FileResponse;
import com.kr.formdang.model.root.DefaultResponse;
import com.kr.formdang.service.file.FileService;
import com.kr.formdang.service.form.FormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final FileService<GlobalFile> fileService;
    private final FormService formService;
    private final JwtService jwtService;

    /**
     * 파일 업로드 API
     *
     * AWS S3에 파일 업로드 처리
     * @param fileRequest
     * @return
     */
    @PostMapping("/public/file/upload")
    public ResponseEntity uploadFile(@ModelAttribute @Valid FileRequest fileRequest, @RequestHeader("Authorization") String token) {
        try {
            jwtService.getId(token); // 관리자 아이디 세팅
            GlobalFile file = fileService.uploadSingle(fileRequest.getFile());
            return ResponseEntity.ok().body(new FileResponse(file));
        } catch (CustomException e) {
            log.info("■ 이미지 리스트 등록 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.info("■이미지 리스트 등록 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
        }
    }

    @PostMapping("/public/file/list/upload/{fid}")
    public ResponseEntity uploadFileList(@ModelAttribute @Valid FileListRequest fileListRequest, @RequestHeader("Authorization") String token, @PathVariable("fid") Long fid){
        try {
            log.info("■ 1. 이미지 리스트 등록 요청 성공 (fid: {})", fid);
            List<FileDataDto> fileDataDtos = new ArrayList<>();
            for (int i=0; i < fileListRequest.getFiles().size(); i++) {
                fileDataDtos.add(new FileDataDto(fileListRequest, i));
            }
            final Long aid = jwtService.getId(token); // 관리자 아이디 세팅
            formService.findForm(aid, fid); // 유효한 폼 유효성 처리

            StopWatch stopWatch = new StopWatch();
            log.info("■ 3. AWS 이미지 등록 비동기 처리 시작");
            stopWatch.start();
            List<CompletableFuture<FileDataDto>> futures = fileDataDtos.stream().map(it -> fileService.uploadSingle(it)).collect(Collectors.toList());
            List<FileDataDto> files = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
            stopWatch.stop();
            log.info("■ 4. AWS 이미지 등록 비동기 처리 종료 (걸린 시간: {}ms)", stopWatch.getTotalTimeMillis());

            formService.updateImage(fid, files);
            return ResponseEntity.ok().body(new DefaultResponse());
        } catch (CustomException e) {
            log.info("■ 이미지 리스트 등록 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.info("■이미지 리스트 등록 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
        }
    }

}
