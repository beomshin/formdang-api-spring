package com.kr.formdang.controller.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kr.formdang.exception.FormHttpException;
import com.kr.formdang.external.HttpFormClient;
import com.kr.formdang.model.response.SuccessResponse;
import com.kr.formdang.model.response.FailResponse;
import com.kr.formdang.model.user.FormUser;
import com.kr.formdang.exception.FormException;
import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.external.dto.auth.JwtTokenRequest;
import com.kr.formdang.model.response.FormResponse;
import com.kr.formdang.external.dto.auth.JwtTokenResponse;
import com.kr.formdang.model.FormFile;
import com.kr.formdang.model.request.file.FileListRequest;
import com.kr.formdang.model.request.file.FileProfileRequest;
import com.kr.formdang.model.response.file.FileProfileResponse;
import com.kr.formdang.service.admin.AdminService;
import com.kr.formdang.service.auth.AuthService;
import com.kr.formdang.service.file.FileService;
import com.kr.formdang.service.form.FileUploadService;
import com.kr.formdang.service.form.FormService;
import com.kr.formdang.model.S3File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final HttpFormClient authClient; // 인증 서버 http client
    private final FileService<S3File> awsS3FileService; // 파일 등록 서비스
    private final FormService formService; // 폼 서비스
    private final AdminService adminService; // 어드민 서비스
    private final FileUploadService fileUploadService; // 파일 업로드 서비스
    private final AuthService authService;

    /**
     * 프로필 이미지 변경 API
     */
    @PostMapping("/public/file/upload/profile")
    public ResponseEntity<FormResponse> uploadFileProfile(
            @ModelAttribute @Valid FileProfileRequest fileRequest,
            @RequestHeader("Authorization") String token
    ) throws JsonProcessingException, FormHttpException {

        log.info("■ 1. 프로필 등록 요청 성공");

        FormUser formUser = authService.generateAuthUser(token); // 토큰 정보 객체 생성

        log.info("■ 2. 프로필 AWS S3 등록 요청");
        S3File profile = awsS3FileService.uploadSingle(fileRequest.getProfile()); // AWS S3 파일 등록
        if (profile == null || !profile.isUploadFlag()) {
            log.error("■ 이미지 업로드 실패 확인 필요");
            fileUploadService.insertFailUploadFile(fileRequest.getProfile(), ResultCode.FAIL_UPLOAD_FILE);
            log.info("■ 4. 프로필 등록 응답 실패");
            return ResponseEntity.ok().body(new FailResponse(ResultCode.FAIL_UPLOAD_FILE)); // 프로필 등록 실패
        }

        log.info("■ 3 프로필 업데이트 성공으로 인한 토큰 재발급 시작"); // 폼당폼당 JWT 토큰 재요청 (토큰 내 프로필 변경 정보 변경을 위해 재요청 후 client 에서 토큰 변경 처리)
        JwtTokenResponse jwtTokenResponse = authClient.requestToken(JwtTokenRequest.valueOf(formUser.getId(), formUser.getName(), profile.getPath()), JwtTokenResponse.class);
        if (jwtTokenResponse == null || jwtTokenResponse.isFail()) {
            log.error("■ 인증 토큰 발급 실패 확인 필요");
            fileUploadService.insertFailUploadFile(fileRequest.getProfile(), ResultCode.FAIL_ISSUED_TOKEN);
            log.info("■ 4. 프로필 등록 응답 실패");
            return ResponseEntity.ok().body(new FailResponse(ResultCode.FAIL_ISSUED_TOKEN)); // 프로필 등록 실패
        }

        log.info("■ 4. 프로필 업데이트 시작");
        adminService.updateProfile(formUser.getId(), profile, fileRequest.getProfile()); // 프로필 정보 업데이트 처리

        log.info("■ 5. 프로필 등록 응답 성공");
        FormResponse response = new FileProfileResponse(jwtTokenResponse.getAccessToken(), profile.getPath());

        return ResponseEntity.ok().body(response); // 파일 등록 성공 응답
    }


    /**
     * 다량 파일 업로드 및 폼 데이터 업데이트
     *
     * 비동기 블로킹 방식으로 다량의 파일 AWS 업로드 처리
     * 업로드 완료후 이미지 정보 업데이트 처리
     */
    @PostMapping("/public/file/list/upload/{fid}")
    @Transactional
    public ResponseEntity<FormResponse> uploadFileList(
            @ModelAttribute @Valid FileListRequest request,
            @RequestHeader("Authorization") String token,
            @PathVariable("fid") Long fid
    ) throws FormException {

        log.info("■ 1. 이미지 리스트 등록 요청 성공");
        FormUser formUser = authService.generateAuthUser(token); // 토큰 정보 객체 생성

        log.info("■ 2. 폼 상세 정보 조회 쿼리 시작");
        formService.findForm(formUser.getId(), fid); // 유효한 폼 유효성 처리

        // 파일, 순번, 타입 객체 생성 (요청에서 해당 순서 맞춰서 보내줌 multipart 처리를 위해 개별 처리)
        List<FormFile> formFiles = new ArrayList<>();
        for (int i=0; i < request.getFiles().size(); i++) {
            formFiles.add(new FormFile(request.getFiles().get(i), request.getOrders().get(i), request.getTypes().get(i))); // 요청값 파일 리스트로 담기
        }

        log.info("■ 3. AWS S3 이미지 등록 비동기 처리 시작");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<CompletableFuture<S3File>> futures = formFiles.stream().map(awsS3FileService::uploadSingle).collect(Collectors.toList()); // 비동기 블록 방식으로 S3 파일 등록
        List<S3File> files = futures.stream().map(CompletableFuture::join).collect(Collectors.toList()); // 비동기 요청된 모든 S3 파일 등록이 완료되면 블록 해제
        stopWatch.stop();
        log.info("■ 4. AWS S3 이미지 등록 비동기 처리 종료 (걸린 시간: {}ms)", stopWatch.getTotalTimeMillis());

        log.info("■ 5. AWS S3 이미지 URL 업데이트 쿼리 실행");
        files.forEach(file -> {
            if (file.isUploadSuccess()) {
                formService.updateImage(fid, file);
            } else {
                fileUploadService.insertFailUploadFile(file, ResultCode.FAIL_UPLOAD_FILE);
            }
        }); // 등록된 S3 파일 폼 정보 업데이트 (로고 또는 문항 순번을 식별로 업데이트)

        log.info("■ 6. 이미지 리스트 등록 요청 응답 성공");
        FormResponse response = new SuccessResponse();

        return ResponseEntity.ok().body(response); // 파일 등록 성공 응답
    }

}
